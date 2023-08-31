package com.dosmartie.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class RateRequest {
    @NotNull(message = "Rating must not be null")
    @Min(value = 0, message = "Min rating is 0")
    @Max(value = 10, message = "Max rating is 10")
    private Double rate;
    @NotNull(message = "itemSkus must not be null")
    private String itemSku;
    private String review;
}
