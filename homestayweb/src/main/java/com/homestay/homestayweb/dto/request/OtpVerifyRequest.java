package com.homestay.homestayweb.dto.request;

import lombok.Data;

@Data
public class OtpVerifyRequest {
    private String email;
    private String otp;
}
