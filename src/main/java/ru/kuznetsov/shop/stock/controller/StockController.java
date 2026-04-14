package ru.kuznetsov.shop.stock.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.shop.data.service.StockService;
import ru.kuznetsov.shop.kafka.service.KafkaService;
import ru.kuznetsov.shop.represent.dto.StockDto;
import ru.kuznetsov.shop.stock.api.StockControllerApi;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static ru.kuznetsov.shop.represent.common.KafkaConst.OPERATION_ID_HEADER;
import static ru.kuznetsov.shop.represent.common.KafkaConst.STOCK_SAVE_TOPIC;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController implements StockControllerApi {

    private final StockService stockService;
    private final KafkaService kafkaService;

    Logger logger = LoggerFactory.getLogger(StockController.class);

    @GetMapping("/{id}")
    public ResponseEntity<StockDto> getById(@PathVariable Long id) {
        StockDto byId = stockService.findById(id);
        return byId == null ?
                ResponseEntity.status(NO_CONTENT).build()
                : ResponseEntity.ok(byId);
    }

    @GetMapping()
    public ResponseEntity<List<StockDto>> getAll(
            @RequestParam(value = "productId", required = false) Long productId,
            @RequestParam(value = "storeId", required = false) Long storeId,
            @RequestParam(value = "ownerId", required = false) UUID ownerId
    ) {
        List<StockDto> allByOptionalParams = stockService.findAllByOptionalParams(productId, storeId, ownerId);

        return allByOptionalParams.isEmpty() ?
                ResponseEntity.status(NO_CONTENT).build()
                : ResponseEntity.ok(allByOptionalParams);
    }

    @GetMapping("/reservation")
    public ResponseEntity<List<StockDto>> getAllByReservation(@RequestParam String reservationOrderId) {
        List<StockDto> allByReservationOrderId = stockService.findAllByReservationOrderId(Long.parseLong(reservationOrderId));

        return allByReservationOrderId.isEmpty() ?
                ResponseEntity.status(NO_CONTENT).build()
                : ResponseEntity.ok(allByReservationOrderId);
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody StockDto stockDto) {
        String uuidString = UUID.randomUUID().toString();

        sendMessageToKafka(stockDto, uuidString);

        return ResponseEntity.ok(uuidString);
    }

    @PostMapping("/batch")
    public ResponseEntity<String> createBatch(@RequestBody Collection<StockDto> stockDtoCollection) {
        String uuidString = UUID.randomUUID().toString();

        for (StockDto stockDto : stockDtoCollection) {
            sendMessageToKafka(stockDto, uuidString);
        }

        return ResponseEntity.ok(uuidString);
    }

    @PutMapping
    public ResponseEntity<StockDto> update(@RequestBody StockDto stockDto) {
        return ResponseEntity.ok(stockService.update(stockDto));
    }

    @DeleteMapping("/{id}")
    public void deleteStock(@PathVariable Long id) {
        stockService.deleteById(id);
    }

    private void sendMessageToKafka(StockDto stockDto, String uuidString) {
        boolean sendResult = kafkaService.sendMessageWithEntity(
                stockDto,
                STOCK_SAVE_TOPIC,
                Collections.singletonMap(OPERATION_ID_HEADER, uuidString.getBytes()));

        if (!sendResult) {
            logger.warn("Failed to send product to topic. Product: {} operation id {}", stockDto, uuidString);
        }
    }
}
