package com.homestay.homestayweb.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

public interface PaymentService {
    String createVNPayPaymentUrl(Long bookingId, HttpServletRequest request) throws Exception;
    String handleVNPayReturn(Map<String, String> vnpParams);
}
