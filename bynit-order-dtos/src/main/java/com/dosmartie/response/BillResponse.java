package com.dosmartie.response;

import com.dosmartie.request.CustomerDetailRequest;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class BillResponse {
    private String orderId;
    private List<ProductResponse> availableProduct;
    private OrderStatus orderStatus;
    private double totalOrder;
    private boolean emailSent;
    private CustomerDetailRequest orderedCustomerDetail;
}
