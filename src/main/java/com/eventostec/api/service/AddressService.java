package com.eventostec.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventostec.api.domain.address.Address;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventRequestDTO;
import com.eventostec.api.repositories.AddressRepository;

@Service
public class AddressService {
  @Autowired
  private AddressRepository addressRepository;

  public Address createAddress(EventRequestDTO data, Event event) {
    Address address = new Address();
    address.setCity(data.city());
    address.setUf(data.state());
    address.setEvent(event);

    return addressRepository.save(address);
  }
}
