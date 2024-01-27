package com.vishruth.inventoryservice.controller;

import com.vishruth.inventoryservice.dto.InventoryResponse;
import com.vishruth.inventoryservice.service.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@AllArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    @GetMapping("/{sku-code}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStockSingle(@PathVariable("sku-code" ) String skuCodeSingle){
        return inventoryService.isInStockSingle(skuCodeSingle);

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode){
        return inventoryService.isInStock(skuCode);
    }
}
