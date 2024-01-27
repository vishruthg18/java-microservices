package com.vishruth.orderservice.service;

import com.vishruth.orderservice.DTO.InventoryResponse;
import com.vishruth.orderservice.DTO.OrderLineItemsDTO;
import com.vishruth.orderservice.DTO.OrderRequest;
import com.vishruth.orderservice.model.Order;
import com.vishruth.orderservice.model.OrderLineItems;
import com.vishruth.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {


    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest){
        Order order=new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems=orderRequest.getOrderLineItemsDTOList()
                .stream().map(this::mapToDTO).toList();
        order.setOrderLineItemsList(orderLineItems);

       List<String> skuCodes= order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();

        //Calling inventory service
        InventoryResponse[] inventoryResponses= webClient.get()
                .uri("http://localhost:8083/inventory",uriBuilder ->
                        uriBuilder.queryParam("skuCode",skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock= Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);

        if(allProductsInStock){
            orderRepository.save(order);
        }else {
            throw new IllegalArgumentException("Product not in stock, Please try Later");
        }

    }

    private OrderLineItems mapToDTO(OrderLineItemsDTO orderLineItemsDTO) {
        OrderLineItems orderLineItems=new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDTO.getPrice());
        orderLineItems.setQuantity(orderLineItemsDTO.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDTO.getSkuCode());
        return orderLineItems;
    }
}
