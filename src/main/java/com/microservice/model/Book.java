package com.microservice.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import com.microservice.payload.BookDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "book")
@Data
public class Book extends Auditable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String name;

	private String description;

	private double price;
	
	public static Book fromDto(BookDto dto) {
		Book book = new Book();
		if (dto.getId() != 0) {
			book.setId(dto.getId());
		}
		book.setName(dto.getName());
		book.setDescription(dto.getDescription());
		book.setPrice(dto.getPrice());
		
		return book;
	}
	
	public static BookDto toDto(Book book) {
		BookDto dto = new BookDto();
		if (book.getId() != null) {
			dto.setId(book.getId());
		}
		dto.setName(book.getName());
		dto.setDescription(book.getDescription());
		dto.setPrice(book.getPrice());
		
		return dto;
	}
}
