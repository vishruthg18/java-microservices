package com.vishruth.orderservice.controller;

import com.vishruth.orderservice.DTO.OrderRequest;
import com.vishruth.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest)
    {
        orderService.placeOrder(orderRequest);
        return "Order Placed Successfully";
    }
}
