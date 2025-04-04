package com.homestay.homestayweb.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class HomestayCreationRequest {
    private String name;
    private String street;
    private String ward;
    private String district;
    private String description;
    private BigDecimal surfRating;
    private String contactInfo;
}
