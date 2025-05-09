package com.homestay.homestayweb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class PaymentResponse {
    @Builder
    @Data
    @AllArgsConstructor
    public static class VNPayResponse {
        public String code;
        public String message;
        public String paymentUrl;
    }
}
