package com.homestay.homestayweb.service.implementation;

import com.homestay.homestayweb.config.VNPAYConfig;
import com.homestay.homestayweb.dto.response.PaymentResponse;
import com.homestay.homestayweb.service.PaymentService;
import com.homestay.homestayweb.utils.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final VNPAYConfig vnpayConfig;

    @Override
    public PaymentResponse.VNPayResponse createVnPayPayment(HttpServletRequest request) {
        Map<String, String> vnp_Params = new HashMap<>();

        int amount = Integer.parseInt(request.getParameter("amount")) * 100;
        String bankCode = request.getParameter("bankCode");
        String orderType = Optional.ofNullable(request.getParameter("ordertype")).orElse("other");
        String orderInfo = Optional.ofNullable(request.getParameter("orderInfo")).orElse("Thanh toan dich vu");
        String language = Optional.ofNullable(request.getParameter("language")).orElse("vn");
        String vnp_TxnRef = String.valueOf(new Random().nextInt(99999999));
        String vnp_IpAddr = VNPayUtil.getIpAddress(request);

        // Format thời gian theo Asia/Ho_Chi_Minh
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        Calendar calendar = Calendar.getInstance();

        String createDate = formatter.format(calendar.getTime());
        calendar.add(Calendar.MINUTE, 15); // Hết hạn sau 15 phút
        String expireDate = formatter.format(calendar.getTime());

        // Gán các tham số vnp_*
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnpayConfig.getVnp_TmnCode());
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_ReturnUrl", vnpayConfig.getVnp_ReturnUrl());
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_Locale", language);
        vnp_Params.put("vnp_CreateDate", createDate);
        vnp_Params.put("vnp_ExpireDate", expireDate);
        vnp_Params.put("vnp_SecureHashType", "SHA512"); // ✅ BẮT BUỘC có dòng này

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }

        // Sắp xếp và tạo chuỗi hashData (KHÔNG encode)
        SortedMap<String, String> sortedParams = new TreeMap<>(vnp_Params);
        StringBuilder hashData = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            if (hashData.length() > 0) {
                hashData.append("&");
            }
            hashData.append(entry.getKey()).append("=").append(entry.getValue());
        }

        // Tạo chữ ký SHA512
        String secureHash = VNPayUtil.hmacSHA512(vnpayConfig.getSecretKey(), hashData.toString());

        // Tạo query có encode
        StringBuilder query = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            if (query.length() > 0) {
                query.append("&");
            }
            query.append(URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII));
            query.append("=").append(URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII));
        }

        // Thêm secure hash
        query.append("&vnp_SecureHash=").append(secureHash);

        // In log để debug nếu cần
        System.out.println("====[VNPay HASH DATA]====");
        System.out.println(hashData.toString());
        System.out.println("====[VNPay QUERY URL]====");
        System.out.println(query.toString());

        String paymentUrl = vnpayConfig.getVnp_PayUrl() + "?" + query;

        return PaymentResponse.VNPayResponse.builder()
                .code("00")
                .message("Success")
                .paymentUrl(paymentUrl)
                .build();
    }
}
