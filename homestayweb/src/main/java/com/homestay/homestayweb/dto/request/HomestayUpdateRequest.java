package com.homestay.homestayweb.dto.request;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HomestayUpdateRequest {
    private String name;
    private String street;
    private String ward;
    private String district;
    private String description;
    private BigDecimal surfRating;
    private String contactInfo;
}
