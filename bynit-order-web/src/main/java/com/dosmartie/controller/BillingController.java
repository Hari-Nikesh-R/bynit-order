package com.dosmartie.controller;


import com.dosmartie.BillService;
import com.dosmartie.response.BaseResponse;
import com.dosmartie.response.BillResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.dosmartie.helper.Constants.EMAIL;
import static com.dosmartie.helper.Constants.REQUESTED_DATE;
import static com.dosmartie.helper.Urls.BILL;
import static com.dosmartie.helper.Urls.ORDER_ID_PARAM;

@RestController
@RequestMapping(value = BILL)
public class BillingController {
    @Autowired
    private BillService billService;
    @GetMapping(value = ORDER_ID_PARAM)
    public BaseResponse<BillResponse> getBill(@PathVariable("orderId") String orderId, @RequestHeader(EMAIL) String email, HttpServletResponse response) {
        return billService.generateBill(orderId, email, response);
    }
    @GetMapping
    public ResponseEntity<?> getDailyPurchaseBillPdf(@RequestParam(REQUESTED_DATE) String requestDate, HttpServletResponse response) {
        return billService.generateDailyPurchaseBillPdf(requestDate, response);
    }

}
