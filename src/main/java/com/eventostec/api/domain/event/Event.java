package com.eventostec.api.domain.event;

import java.util.Date;
import java.util.UUID;

import com.eventostec.api.domain.address.Address;
import com.eventostec.api.domain.coupon.Coupon;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {
  @Id
  @GeneratedValue
  private UUID id;
  private String title;
  private String description;
  private String imageUrl;
  private String eventUrl;
  private Boolean remote;
  private Date date;

  @OneToOne(mappedBy = "event", cascade = CascadeType.ALL)
  private Address address;
}
