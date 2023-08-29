package com.dosmartie;

import com.dosmartie.request.EmailRequest;

public interface MailService {
    String sendEmail(EmailRequest mailBody);
}
