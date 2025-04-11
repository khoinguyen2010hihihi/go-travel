package com.homestay.homestayweb.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponse {
    private Long roomId;
    private Long homestayId;
    private String roomType;
    private Double price;
    private Boolean availability;
    private String features;
}
