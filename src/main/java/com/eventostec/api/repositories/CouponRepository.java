package com.eventostec.api.repositories;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eventostec.api.domain.coupon.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, UUID> {

  @Query("SELECT c FROM Coupon c WHERE c.event.id = :eventId AND c.valid >= :currentDate")
  public List<Coupon> findByEventIdAndValidAfter(UUID eventId, Date currentDate);

}
