package com.eventostec.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.coupon.CouponRequestDTO;
import com.eventostec.api.service.CouponService;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {

  @Autowired
  private CouponService couponService;

  @GetMapping("/event/{eventId}")
  public ResponseEntity<List<Coupon>> getCouponList(@PathVariable UUID eventId) {
    List<Coupon> coupons = couponService.getCouponsByEvent(eventId);
    
    return ResponseEntity.ok(coupons);
  }

  @PostMapping("/event/{eventId}")
  public ResponseEntity<Coupon> addCouponsToEvent(@PathVariable UUID eventId, @RequestBody CouponRequestDTO body) {
    Coupon coupon = couponService.createCoupon(eventId, body);

    return ResponseEntity.ok(coupon);
  }

}
