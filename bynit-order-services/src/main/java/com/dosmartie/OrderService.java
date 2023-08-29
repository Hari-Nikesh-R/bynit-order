package com.dosmartie;

import com.dosmartie.response.BaseResponse;
import com.dosmartie.response.OrderResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    ResponseEntity<BaseResponse<OrderResponse>> getOrder(String param);

    ResponseEntity<BaseResponse<List<OrderResponse>>> getAllOrder();
}
