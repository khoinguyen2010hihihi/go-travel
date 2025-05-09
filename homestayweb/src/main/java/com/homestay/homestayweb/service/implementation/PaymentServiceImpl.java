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
        String bankCode = request.getParameter("bankcode");
        String orderType = request.getParameter("ordertype");
        String orderInfo = request.getParameter("orderInfo");
        String language = request.getParameter("language");
        String vnp_TxnRef = String.valueOf(new Random().nextInt(99999999));
        String vnp_IpAddr = VNPayUtil.getIpAddress(request);

        // Thêm giá trị mặc định nếu orderType hoặc orderInfo null/trống
        if (orderType == null || orderType.isEmpty()) {
            orderType = "other"; // Giá trị mặc định
        }

        if (orderInfo == null || orderInfo.isEmpty()) {
            orderInfo = "Thanh toan dich vu"; // Giá trị mặc định
        }

        // Các tham số bắt buộc
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnpayConfig.getVnp_TmnCode());
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfo); // Đã được xử lý giá trị mặc định
        vnp_Params.put("vnp_OrderType", orderType); // Đã được xử lý giá trị mặc định
        vnp_Params.put("vnp_ReturnUrl", vnpayConfig.getVnp_ReturnUrl());
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_Locale", (language == null || language.isEmpty()) ? "vn" : language);

        // Xử lý thời gian
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("Etc/GMT+7"));
        Calendar calendar = Calendar.getInstance();

        vnp_Params.put("vnp_CreateDate", formatter.format(calendar.getTime()));
        calendar.add(Calendar.MINUTE, 30);
        vnp_Params.put("vnp_ExpireDate", formatter.format(calendar.getTime()));

        // Thêm bankCode nếu có
        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }

        // Sắp xếp params và tạo chuỗi hash
        SortedMap<String, String> sortedParams = new TreeMap<>(vnp_Params);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            String value = entry.getValue();
            if (value != null && !value.isEmpty()) {
                String encodedKey = URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII);
                String encodedValue = URLEncoder.encode(value, StandardCharsets.US_ASCII);

                if (hashData.length() > 0) {
                    hashData.append("&");
                    query.append("&");
                }

                hashData.append(entry.getKey()).append("=").append(value);
                query.append(encodedKey).append("=").append(encodedValue);
            }
        }

        // Tạo chữ ký
        String secureHash = VNPayUtil.hmacSHA512(vnpayConfig.getSecretKey(), hashData.toString());
        query.append("&vnp_SecureHash=").append(secureHash);

        String paymentUrl = vnpayConfig.getVnp_PayUrl() + "?" + query;

        return PaymentResponse.VNPayResponse.builder()
                .code("00")
                .message("Success")
                .paymentUrl(paymentUrl)
                .build();
    }
}