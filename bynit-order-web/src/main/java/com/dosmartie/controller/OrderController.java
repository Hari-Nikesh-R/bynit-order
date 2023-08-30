package com.dosmartie.controller;


import com.dosmartie.OrderService;
import com.dosmartie.response.BaseResponse;
import com.dosmartie.response.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.dosmartie.helper.Constants.PARAM;
import static com.dosmartie.helper.Urls.*;


@RestController
@RequestMapping(value = ORDER)
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<BaseResponse<OrderResponse>> getProductByEmail(@RequestParam(PARAM) String param) {
        return orderService.getOrder(param);
    }

    @GetMapping(value = ALL)
    public ResponseEntity<BaseResponse<List<OrderResponse>>> getAllOrder() {
        return orderService.getAllOrder();
    }

}
