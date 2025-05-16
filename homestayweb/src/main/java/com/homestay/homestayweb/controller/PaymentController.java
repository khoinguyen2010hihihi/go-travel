package com.homestay.homestayweb.controller;

import com.homestay.homestayweb.dto.response.PaymentResponse;
import com.homestay.homestayweb.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-vnpay-url/{bookingId}")
    public ResponseEntity<?> createPayment(@PathVariable Long bookingId, HttpServletRequest request) {
        String paymentUrl = paymentService.createVNPayPaymentUrl(bookingId, request);
        return ResponseEntity.ok(new PaymentResponse("success", "Redirect to VNPay", paymentUrl));
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<?> vnPayReturn(HttpServletRequest request) {
        Map<String, String> vnpParams = new HashMap<>();
        Map<String, String[]> parameterMap = request.getParameterMap();

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            if (values != null && values.length > 0) {
                vnpParams.put(key, values[0]);
            }
        }

        String result = paymentService.handleVNPayReturn(vnpParams);
        return ResponseEntity.ok(new PaymentResponse("success", result, null));
    }

}
