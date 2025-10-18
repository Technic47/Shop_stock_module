package ru.kuznetsov.shop.stock.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.shop.data.service.KafkaService;
import ru.kuznetsov.shop.data.service.StockService;
import ru.kuznetsov.shop.represent.dto.StockDto;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static ru.kuznetsov.shop.represent.common.KafkaConst.*;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;
    private final KafkaService kafkaService;

    @GetMapping("/{id}")
    public ResponseEntity<StockDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.findById(id));
    }

    @GetMapping()
    public ResponseEntity<List<StockDto>> getAll() {
        return ResponseEntity.ok(stockService.findAll());
    }

    @GetMapping("/{id}/store")
    public ResponseEntity<List<StockDto>> getAllByStoreId(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.findAllByStoreId(id));
    }

    @GetMapping("/{id}/product")
    public ResponseEntity<List<StockDto>> getAllByProductId(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.findAllByProductId(id));
    }

    @PostMapping
    public ResponseEntity<Boolean> create(@RequestBody StockDto storeDto) {
        return ResponseEntity.ok(kafkaService.sendMessageWithEntity(
                storeDto,
                STORE_SAVE_TOPIC,
                Collections.singletonMap(OPERATION_ID_HEADER, UUID.randomUUID().toString().getBytes())));
    }

    @PostMapping("/batch")
    public ResponseEntity<Collection<Boolean>> createBatch(@RequestBody Collection<StockDto> stockDtoCollection) {
        byte[] operationId = UUID.randomUUID().toString().getBytes();

        return ResponseEntity.ok(
                stockDtoCollection.stream()
                        .map(dto -> kafkaService.sendMessageWithEntity(dto,
                                STORE_SAVE_TOPIC,
                                Collections.singletonMap(OPERATION_ID_HEADER, operationId)))
                        .toList()
        );
    }

    @DeleteMapping("/{id}")
    public void deleteeStore(@PathVariable Long id) {
        stockService.deleteById(id);
    }
}
