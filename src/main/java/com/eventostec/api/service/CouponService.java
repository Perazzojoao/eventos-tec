package com.eventostec.api.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.coupon.CouponRequestDTO;
import com.eventostec.api.domain.coupon.CouponResponseDTO;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventDetailsDTO;
import com.eventostec.api.repositories.CouponRepository;
import com.eventostec.api.repositories.EventRepository;

@Service
public class CouponService {
  @Autowired
  private CouponRepository couponRepository;

  @Autowired
  private EventRepository eventRepository;

  public CouponResponseDTO createCoupon(UUID eventId, CouponRequestDTO couponData) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new RuntimeException("Event not found"));

    Coupon newCoupon = new Coupon();
    newCoupon.setCode(couponData.code());
    newCoupon.setDiscount(couponData.discount());
    newCoupon.setValid(new Date(couponData.valid()));
    newCoupon.setEvent(event);

    couponRepository.save(newCoupon);

    return new CouponResponseDTO(newCoupon.getId(), newCoupon.getCode(), newCoupon.getDiscount(), newCoupon.getValid().toString());
  }

  public List<Coupon> getCouponsByEvent(UUID eventId) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new RuntimeException("Event not found"));

    return couponRepository.findAll();
  }

  public List<Coupon> consultCoupons(UUID eventId, Date currentDate) {
    return couponRepository.findByEventIdAndValidAfter(eventId, currentDate);
  }
}
