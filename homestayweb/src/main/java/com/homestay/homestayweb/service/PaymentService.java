package com.homestay.homestayweb.service;

import com.homestay.homestayweb.dto.response.PaymentResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {
    PaymentResponse.VNPayResponse createVnPayPayment(HttpServletRequest request);
}
