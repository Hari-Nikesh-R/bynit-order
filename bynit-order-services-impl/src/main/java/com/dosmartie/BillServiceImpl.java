package com.dosmartie;

import com.dosmartie.helper.PdfUtils;
import com.dosmartie.helper.ResponseMessage;
import com.dosmartie.response.BaseResponse;
import com.dosmartie.response.BillResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BillServiceImpl implements BillService {

    private static final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kolkata"));
    @Autowired
    private OrderHistoryRepository orderHistoryRepository;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private ResponseMessage<BillResponse> responseMessage;

    @Override
    public BaseResponse<BillResponse> generateBill(String orderId, String email, HttpServletResponse response) {
        try {
            Optional<OrderHistory> optionalOrderHistory = retrieveOrderByOrderId(orderId, email);
            return optionalOrderHistory.map(orderHistory -> {
                String fileName = calendar.get(Calendar.DATE) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.YEAR);
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName + "-bill.pdf");
                List<BillResponse> billResponses = new ArrayList<>();
                billResponses.add(mapper.convertValue(orderHistory, BillResponse.class));
                PdfUtils.generatePdfAndSave(billResponses, fileName);
                return responseMessage.setSuccessResponse("Bill Generated", mapper.convertValue(optionalOrderHistory.get(), BillResponse.class));
            }).orElseGet(() -> responseMessage.setFailureResponse("No Order found"));
        } catch (Exception exception) {
            return responseMessage.setFailureResponse("Bill cannot be generated", exception);
        }
    }

    @Override
    public ResponseEntity<?> generateDailyPurchaseBillPdf(String requestDate, HttpServletResponse response) {
        try {
            List<OrderHistory> orderHistories = orderHistoryRepository.findAllByCreatedDate(parseStringToDate(requestDate));
            if (orderHistories.isEmpty()) {
                return ResponseEntity.ok(responseMessage.setFailureResponse("List is empty"));
            } else {
                String fileName = calendar.get(Calendar.DATE) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.YEAR);
                List<BillResponse> billResponses = mapper.convertValue(orderHistories, new TypeReference<>() {
                });
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName + "-bill.pdf");
               // boolean generated = PdfUtils.generatePdfAndSave(billResponses, fileName, requestDate);
//                todo:  for dynamically receive and download in front end as ByteResourceArray
                byte[] generated = PdfUtils.generatePurchaseHistoryPDF(billResponses, true);
                if (generated.length == 0) {
                    return ResponseEntity.ok(responseMessage.setFailureResponse("Pdf not generated"));
                }
                else {
                    ByteArrayResource resource = new ByteArrayResource(generated);
                   return ResponseEntity.ok()
                            .contentLength(generated.length)
                            .contentType(MediaType.APPLICATION_PDF)
                            .body(resource);
                }
                }
        } catch (Exception exception) {
            return ResponseEntity.ok(responseMessage.setFailureResponse("Pdf bill cannot be generated", exception));
        }
    }
    private synchronized Optional<OrderHistory> retrieveOrderByOrderId(String orderId, String email) {
        return orderHistoryRepository.findByOrderIdAndEmail(orderId, email);
    }
    private Date parseStringToDate(String requestDate) throws ParseException {
        return new SimpleDateFormat("dd-MM-yyyy").parse(requestDate);
    }
}
