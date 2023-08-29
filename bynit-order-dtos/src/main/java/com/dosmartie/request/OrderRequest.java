package com.dosmartie.request;


import com.dosmartie.response.OrderStatus;
import com.dosmartie.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private @Valid CustomerDetailRequest orderedCustomerDetail;
    private @Valid List<ProductResponse> availableProduct;
    private OrderStatus orderStatus;
    private String orderId;
    private String email;
    private double totalOrder;
}
