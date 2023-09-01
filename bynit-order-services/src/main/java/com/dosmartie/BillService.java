package com.dosmartie;

import com.dosmartie.response.BaseResponse;
import com.dosmartie.response.BillResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface BillService {
    BaseResponse<BillResponse> generateBill(String orderId, String email, HttpServletResponse response);
    ResponseEntity<?> generateDailyPurchaseBillPdf(String requestDate, HttpServletResponse response);
}
