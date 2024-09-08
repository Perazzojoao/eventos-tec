package com.eventostec.api.domain.coupon;

import java.util.UUID;

public record CouponResponseDTO(
    UUID id,
    String code,
    Integer discount,
    String validUntil) {

}
