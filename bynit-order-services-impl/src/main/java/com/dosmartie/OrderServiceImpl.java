package com.dosmartie;


import com.dosmartie.helper.PdfUtils;
import com.dosmartie.helper.ResponseMessage;
import com.dosmartie.request.EmailRequest;
import com.dosmartie.request.OrderRequest;
import com.dosmartie.request.RateRequest;
import com.dosmartie.response.*;
import com.dosmartie.utils.EncryptionUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private static final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kolkata"));
    private final OrderHistoryRepository orderHistoryRepository;

    private final MailService mailService;

    private final ObjectMapper mapper;

    private final ResponseMessage<OrderResponse> responseMessage;

    private final ResponseMessage<List<OrderResponse>> responseMessageList;

    private final EncryptionUtils encryptionUtils;

    public OrderServiceImpl(OrderHistoryRepository orderHistoryRepository, MailService mailService, ObjectMapper mapper, ResponseMessage<OrderResponse> responseMessage, ResponseMessage<List<OrderResponse>> responseMessageList, EncryptionUtils encryptionUtils) {
        this.orderHistoryRepository = orderHistoryRepository;
        this.mailService = mailService;
        this.mapper = mapper;
        this.responseMessage = responseMessage;
        this.responseMessageList = responseMessageList;
        this.encryptionUtils = encryptionUtils;
    }


    @KafkaListener(topics = "mytopic", groupId = "mygroup")
    public void createOrder(String orderRequest) {
        try {
            OrderHistory orderHistory = new OrderHistory();
            OrderRequest orderReq = mapper.readValue(orderRequest, OrderRequest.class);
            BeanUtils.copyProperties(orderReq, orderHistory);
            orderHistory.setCreatedDate(formatDate(new Date()));
            orderHistoryRepository.save(orderHistory);
            List<BillResponse> billResponseList = new ArrayList<>();
            billResponseList.add(mapper.convertValue(orderHistory, BillResponse.class));
            EmailRequest emailRequest = constructEmailRequest(PdfUtils.generatePurchaseHistoryPDF(billResponseList, false), orderHistory.getEmail(), orderHistory.getOrderedCustomerDetail().getName());
            //mailService.sendEmail(emailRequest);
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error(exception.fillInStackTrace().getLocalizedMessage());
        }
    }

    @Override
    public ResponseEntity<BaseResponse<OrderResponse>> getOrder(String param, String authId) {
        try {
            if (encryptionUtils.decryptAuthIdAndValidateRequest(authId)) {
                Optional<OrderHistory> optionalOrderHistory = getOrderHistory(param);
                return optionalOrderHistory.map(orderHistory -> {
                            OrderResponse orderResponse = new OrderResponse();
                            BeanUtils.copyProperties(orderHistory, orderResponse);
                            return ResponseEntity.ok(responseMessage.setSuccessResponse("Fetched result", orderResponse));
                        })
                        .orElseGet(() -> ResponseEntity.ok(responseMessage.setFailureResponse("NO ORDER FOUND")));
            }
            else {
                return ResponseEntity.ok(responseMessage.setUnauthorizedResponse());
            }
        } catch (Exception exception) {
            log.error(exception.fillInStackTrace().getLocalizedMessage());
            return ResponseEntity.ok(responseMessage.setFailureResponse("NO ORDER FOUND", exception));
        }
    }

    @Override
    public ResponseEntity<BaseResponse<List<OrderResponse>>> getAllOrder(String authId) {
        try {
            if (encryptionUtils.decryptAuthIdAndValidateRequest(authId)) {
                return ResponseEntity.ok(responseMessageList.setSuccessResponse("Fetched results", mapper.convertValue(orderHistoryRepository.findAll(), new TypeReference<>() {
                })));
            }
            else {
                return ResponseEntity.ok(responseMessageList.setUnauthorizedResponse());
            }
        } catch (Exception exception) {
            return ResponseEntity.ok(responseMessageList.setFailureResponse("Unable to fetch result", exception));
        }
    }

    @Override
    public synchronized Map<String, ProductResponse> getAllUnratedProducts(String orderId, List<RateRequest> requests) {
        Map<String, ProductResponse> unratedProduct = new HashMap<>();
        return orderHistoryRepository.findByOrderId(orderId).map(orderHistory -> {
            orderHistory.getAvailableProduct().forEach(productResponse -> {
                if (!productResponse.isRated()) {
                    unratedProduct.put(productResponse.getSku(), productResponse);
                    if (requests.size() > 0) {
                        requests.forEach(rateRequest -> {
                            if (productResponse.getSku().equals(rateRequest.getItemSku())) {
                                productResponse.setRated(true);
                                productResponse.setRatingBasedOnOrder(rateRequest.getRate());
                                productResponse.setReviews(new HashMap<>(){{put(orderHistory.getOrderedCustomerDetail().getName(), rateRequest.getReview());}});
                            }
                        });
                    }
                }
            });
            if (requests.size() > 0) {
                orderHistory.setAvailableProduct(orderHistory.getAvailableProduct());
                orderHistoryRepository.save(orderHistory);
            }
            return unratedProduct;
        }).orElseGet(() -> unratedProduct);
    }


    private synchronized Optional<OrderHistory> getOrderHistory(String param) {
        if (param.matches("[a-z0-9]+@[a-z]+\\.[a-z]{2,3}")) {
            return orderHistoryRepository.findByEmail(param);
        } else {
            return orderHistoryRepository.findByOrderId(param);
        }
    }


    private Date formatDate(Date date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = formatter.format(date);
        return new SimpleDateFormat("dd-MM-yyyy").parse(strDate);
    }

    private String currentDateAsString() {
        return calendar.get(Calendar.DATE) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.YEAR);
    }

    private EmailRequest constructEmailRequest(byte[] byteArrayResource, String email, String name) {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setName(name);
        emailRequest.setRecipient(email);
        emailRequest.setSubject("Invoice for Purchase");
        emailRequest.setPdfData(byteArrayResource);
        return emailRequest;
    }
}
