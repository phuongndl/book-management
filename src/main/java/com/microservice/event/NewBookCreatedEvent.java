package com.microservice.event;

import lombok.Data;

@Data
public class NewBookCreatedEvent {
	private long bookId;
	private String isbn;
}
