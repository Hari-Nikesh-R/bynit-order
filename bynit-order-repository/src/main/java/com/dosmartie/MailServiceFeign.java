package com.dosmartie;

import com.dosmartie.request.EmailRequest;
import com.dosmartie.response.BaseResponse;
import feign.Headers;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;

public interface MailServiceFeign {
    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @RequestLine("POST /mail/send")
    BaseResponse<Object> sendMail(@RequestBody EmailRequest emailRequest);
}
