package ru.kuznetsov.shop.stock.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.shop.data.dto.StockDto;
import ru.kuznetsov.shop.data.service.StockService;
import ru.kuznetsov.shop.data.service.StoreService;

import java.util.List;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final StoreService storeService;
    private final StockService stockService;

    @GetMapping("/{id}")
    public ResponseEntity<StockDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.findById(id));
    }

    @GetMapping()
    public ResponseEntity<List<StockDto>> getAll() {
        return ResponseEntity.ok(stockService.findAll());
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<List<StockDto>> getAllStockByStoreId(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.findAllByStoreId(id));
    }

    @GetMapping("/{id}/product")
    public ResponseEntity<List<StockDto>> getAllStockByProductId(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.findAllByProductId(id));
    }

    @PostMapping
    public ResponseEntity<StockDto> createStore(@RequestBody StockDto storeDto) {
        return ResponseEntity.ok(stockService.add(storeDto));
    }

    @DeleteMapping("/{id}")
    public void deleteeStore(@PathVariable Long id) {
        stockService.deleteById(id);
    }
}
