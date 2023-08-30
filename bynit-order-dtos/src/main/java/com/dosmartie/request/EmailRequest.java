package com.dosmartie.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailRequest {
    private String recipient;
    private String subject;
    private String name;
    private byte[] pdfData;
}
