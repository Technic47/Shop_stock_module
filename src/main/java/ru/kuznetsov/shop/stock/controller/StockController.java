package ru.kuznetsov.shop.stock.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.shop.data.service.KafkaService;
import ru.kuznetsov.shop.data.service.StockService;
import ru.kuznetsov.shop.represent.dto.StockDto;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static ru.kuznetsov.shop.represent.common.KafkaConst.OPERATION_ID_HEADER;
import static ru.kuznetsov.shop.represent.common.KafkaConst.STOCK_SAVE_TOPIC;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;
    private final KafkaService kafkaService;

    Logger logger = LoggerFactory.getLogger(StockController.class);

    @GetMapping("/{id}")
    public ResponseEntity<StockDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.findById(id));
    }

    @GetMapping()
    public ResponseEntity<List<StockDto>> getAll(
            @RequestParam(value = "productId", required = false) Long productId,
            @RequestParam(value = "storeId", required = false) Long storeId,
            @RequestParam(value = "ownerId", required = false) UUID ownerId
    ) {
        return ResponseEntity.ok(stockService.findAllByOptionalParams(productId, storeId, ownerId));
    }

    @GetMapping("/reservation")
    public ResponseEntity<List<StockDto>> getAllByReservation(@RequestParam Long reservationOrderId) {
        return ResponseEntity.ok(stockService.findAllByReservationOrderId(reservationOrderId));
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody StockDto storeDto) {
        String uuidString = UUID.randomUUID().toString();

        sendMessageToKafka(storeDto, uuidString);

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

    @DeleteMapping("/{id}")
    public void deleteeStore(@PathVariable Long id) {
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
