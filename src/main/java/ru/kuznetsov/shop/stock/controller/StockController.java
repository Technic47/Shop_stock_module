package ru.kuznetsov.shop.stock.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.shop.data.service.StockService;
import ru.kuznetsov.shop.represent.dto.StockDto;
import ru.kuznetsov.shop.stock.service.KafkaService;

import java.util.Collection;
import java.util.List;

import static ru.kuznetsov.shop.data.common.KafkaTopics.STOCK_SAVE_TOPIC;

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

    @GetMapping("/{id}/stock")
    public ResponseEntity<List<StockDto>> getAllByStoreId(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.findAllByStoreId(id));
    }

    @GetMapping("/{id}/product")
    public ResponseEntity<List<StockDto>> getAllByProductId(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.findAllByProductId(id));
    }

    @PostMapping
    public ResponseEntity<Boolean> create(@RequestBody StockDto storeDto) {
        return ResponseEntity.ok(kafkaService.sendSaveMessage(storeDto, STOCK_SAVE_TOPIC));
    }

    @PostMapping("/batch")
    public ResponseEntity<Collection<Boolean>> createBatch(@RequestBody Collection<StockDto> storeDto) {
        return ResponseEntity.ok(
                storeDto.stream()
                        .map(dto -> kafkaService.sendSaveMessage(dto, STOCK_SAVE_TOPIC))
                        .toList()
        );
    }

    @DeleteMapping("/{id}")
    public void deleteeStore(@PathVariable Long id) {
        stockService.deleteById(id);
    }
}
