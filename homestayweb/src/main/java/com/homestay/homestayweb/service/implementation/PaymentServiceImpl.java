package com.homestay.homestayweb.service.implementation;

import com.homestay.homestayweb.entity.Booking;
import com.homestay.homestayweb.entity.Payment;
import com.homestay.homestayweb.repository.BookingRepository;
import com.homestay.homestayweb.repository.PaymentRepository;
import com.homestay.homestayweb.service.PaymentService;
import com.homestay.homestayweb.utils.VnPayUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

    @Value("${vnpay.tmnCode}")
    private String vnp_TmnCode;

    @Value("${vnpay.hashSecret}")
    private String vnp_HashSecret;

    @Value("${vnpay.payUrl}")
    private String vnp_Url;

    @Value("${vnpay.returnUrl}")
    private String vnp_ReturnUrl;

    @Override
    public String createVNPayPaymentUrl(Long bookingId, HttpServletRequest request) {
        try {
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));

            // Chuẩn bị thông tin giao dịch
            String vnp_TxnRef = String.valueOf(System.currentTimeMillis()); // Mã đơn hàng
            String vnp_OrderInfo = "Thanh toan don dat phong #" + bookingId;
            String orderType = "other";
            String amount = String.valueOf((long)(booking.getTotalPrice() * 100)); // Nhân 100 để ra đơn vị VND

            String vnp_IpAddr = request.getRemoteAddr();
            String vnp_CreateDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", amount);
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
            vnp_Params.put("vnp_OrderType", orderType);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            return VnPayUtil.getPaymentUrl(vnp_Params, vnp_HashSecret, vnp_Url);

        } catch (Exception e) {
            throw new RuntimeException("Tạo URL thanh toán thất bại: " + e.getMessage());
        }
    }

    @Override
    public String handleVNPayReturn(Map<String, String> vnpParams) {
        String responseCode = vnpParams.get("vnp_ResponseCode");
        String txnRef = vnpParams.get("vnp_TxnRef");
        String bookingIdRaw = vnpParams.get("vnp_OrderInfo").replaceAll("[^0-9]", "");

        if ("00".equals(responseCode)) {
            Long bookingId = Long.parseLong(bookingIdRaw);

            // Cập nhật trạng thái thanh toán
            Payment payment = new Payment();
            payment.setBookingId(bookingId);
            payment.setAmount(Double.parseDouble(vnpParams.get("vnp_Amount")) / 100);
            payment.setPaymentMethod("VNPay");
            payment.setPaymentStatus("Completed");
            payment.setCreatedAt(new Date());
            payment.setUserId(1L); // TODO: lấy userId thực tế từ session/token

            paymentRepository.save(payment);

            return "Thanh toán thành công cho đơn #" + bookingId;
        } else {
            return "Thanh toán thất bại hoặc bị hủy";
        }
    }
}
