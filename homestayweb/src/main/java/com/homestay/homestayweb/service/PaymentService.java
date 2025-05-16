package com.homestay.homestayweb.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface PaymentService {
    String createVNPayPaymentUrl(Long bookingId, HttpServletRequest request);
    String handleVNPayReturn(Map<String, String> vnpParams);
}
