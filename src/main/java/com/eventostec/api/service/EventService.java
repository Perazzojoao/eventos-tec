package com.eventostec.api.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.eventostec.api.domain.address.Address;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventRequestDTO;
import com.eventostec.api.domain.event.EventResponseDTO;
import com.eventostec.api.repositories.EventRepository;

@Service
public class EventService {

	@Value("${aws.bucket.name}")
	private String bucketName;

	@Autowired
	private AmazonS3 s3Client;

	@Autowired
	private EventRepository repository;

	@Autowired
	private AddressService addressService;

	public EventResponseDTO createEvent(EventRequestDTO data) {
		String imgUrl = null;

		if (data.image() != null) {
			imgUrl = this.uploadImg(data.image());
		}

		Event newEvent = new Event();
		newEvent.setTitle(data.title());
		newEvent.setDescription(data.description());
		newEvent.setEventUrl(data.eventUrl());
		newEvent.setDate(new Date(data.date()));
		newEvent.setImageUrl(imgUrl);
		newEvent.setRemote(data.remote());

		this.repository.save(newEvent);

		Address address = new Address();
		if (!data.remote()) {
			address = this.addressService.createAddress(data, newEvent);
		}
		return new EventResponseDTO(
				newEvent.getId(),
				newEvent.getTitle(),
				newEvent.getDescription(),
				newEvent.getDate(),
				address.getCity(),
				address.getUf(),
				newEvent.getRemote(),
				newEvent.getEventUrl(),
				newEvent.getImageUrl());
	}

	public List<EventResponseDTO> getUpcomingEvents(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Event> eventsPage = this.repository.findUpcomingEvents(new Date(), pageable);

		return eventsPage.map(event -> new EventResponseDTO(
				event.getId(),
				event.getTitle(),
				event.getDescription(),
				event.getDate(),
				event.getAddress() != null ? event.getAddress().getCity() : "",
				event.getAddress() != null ? event.getAddress().getUf() : "",
				event.getRemote(),
				event.getEventUrl(),
				event.getImageUrl()))
				.stream().toList();
	}

	public List<EventResponseDTO> getFilteredEvents(int page, int size, String title, String city, String uf,
			Date startDate, Date endDate) {

		title = title != null ? title : "";
		city = city != null ? city : "";
		uf = uf != null ? uf : "";
		startDate = startDate != null ? startDate : new Date(0);
		endDate = endDate != null ? endDate : new Date();

		Pageable pageable = PageRequest.of(page, size);
		Page<Event> eventsPage = this.repository.findFilteredEvents(title, city, uf, startDate, endDate,
				pageable);

		return eventsPage.map(event -> new EventResponseDTO(
				event.getId(),
				event.getTitle(),
				event.getDescription(),
				event.getDate(),
				event.getAddress() != null ? event.getAddress().getCity() : "",
				event.getAddress() != null ? event.getAddress().getUf() : "",
				event.getRemote(),
				event.getEventUrl(),
				event.getImageUrl()))
				.stream().toList();
	}

	private String uploadImg(MultipartFile multipartFile) {
		String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

		try {
			File file = this.convertMultipartFile(multipartFile);
			s3Client.putObject(bucketName, fileName, file);
			file.delete();
			return s3Client.getUrl(bucketName, fileName).toString();
		} catch (Exception e) {
			System.out.println("Erro ao subir arquivo: " + e.getMessage());
			return "";
		}
	}

	private File convertMultipartFile(MultipartFile multipartFile) throws IOException {
		File convFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(multipartFile.getBytes());
		fos.close();
		return convFile;
	}
}
