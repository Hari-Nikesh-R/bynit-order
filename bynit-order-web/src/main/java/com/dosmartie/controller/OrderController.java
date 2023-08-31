package com.dosmartie.controller;


import com.dosmartie.OrderService;
import com.dosmartie.request.RateRequest;
import com.dosmartie.response.BaseResponse;
import com.dosmartie.response.OrderResponse;
import com.dosmartie.response.ProductResponse;
import com.dosmartie.response.UnratedOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.dosmartie.helper.Constants.AUTH_ID;
import static com.dosmartie.helper.Constants.PARAM;
import static com.dosmartie.helper.Urls.*;


@RestController
@RequestMapping(value = ORDER)
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<BaseResponse<OrderResponse>> getProductByEmail(@RequestParam(PARAM) String param, @RequestHeader(AUTH_ID) String authId) {
        return orderService.getOrder(param, authId);
    }

    @GetMapping(value = ALL)
    public ResponseEntity<BaseResponse<List<OrderResponse>>> getAllOrder(@RequestHeader(AUTH_ID) String authId) {
        return orderService.getAllOrder(authId);
    }

    @PutMapping(value = "/unrated")
    public Map<String, ProductResponse> getUnratedProducts(@RequestParam("orderId") String orderId, @RequestBody List<RateRequest> requests) {
        return orderService.getAllUnratedProducts(orderId, requests);
    }

}
