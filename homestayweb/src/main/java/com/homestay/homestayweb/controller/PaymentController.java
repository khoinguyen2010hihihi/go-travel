package com.homestay.homestayweb.controller;

import com.homestay.homestayweb.service.PaymentService;
import com.homestay.homestayweb.dto.response.ResponseObject;
import com.homestay.homestayweb.dto.response.PaymentResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/vnpay")
    public ResponseObject<PaymentResponse.VNPayResponse> createVnPay(HttpServletRequest req,
                                                                     @RequestParam long amount,
                                                                     @RequestParam(required=false) String bankCode) {
        // amount: số VNĐ, ví dụ 500000
        // bankCode: optional
        // Chuyển amount, bankCode vào HttpServletRequest để service dùng
        req.setAttribute("amount", String.valueOf(amount));
        if (bankCode != null) req.setAttribute("bankCode", bankCode);
        PaymentResponse.VNPayResponse resp = paymentService.createVnPayPayment(req);
        return new ResponseObject<>(HttpStatus.OK, "Success", resp);
    }

    @GetMapping("/vnpay-callback")
    public ResponseObject<PaymentResponse.VNPayResponse> callback(HttpServletRequest req) {
        String code = req.getParameter("vnp_ResponseCode");
        if ("00".equals(code)) {
            return new ResponseObject<>(HttpStatus.OK, "Thanh toán thành công",
                    new PaymentResponse.VNPayResponse("00","Success",""));
        } else {
            return new ResponseObject<>(HttpStatus.BAD_REQUEST, "Thanh toán thất bại", null);
        }
    }
}

