package com.dosmartie;

import com.dosmartie.request.RateRequest;
import com.dosmartie.response.BaseResponse;
import com.dosmartie.response.OrderResponse;
import com.dosmartie.response.ProductResponse;
import com.dosmartie.response.UnratedOrderResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface OrderService {
    ResponseEntity<BaseResponse<List<OrderResponse>>> getOrder(String param, String authId);
    ResponseEntity<BaseResponse<OrderResponse>> getOrderByOrderId(String orderId, String authId, String email);

    ResponseEntity<BaseResponse<List<OrderResponse>>> getAllOrder(String authId);
    Map<String, ProductResponse> getAllUnratedProducts(String orderId, List<RateRequest> requests, String email);
}
