package com.dosmartie.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnratedOrderResponse {
  Map<String, Boolean> unratedProduct;
}
