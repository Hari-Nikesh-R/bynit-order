package com.dosmartie;


import com.dosmartie.response.ProductResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public interface OrderHistoryRepository extends MongoRepository<OrderHistory, String> {
    Optional<OrderHistory> findByEmail(String email);
    Optional<OrderHistory> findByOrderId(String orderId);
    List<OrderHistory> findAllByCreatedDate(Date requestDate);
}
