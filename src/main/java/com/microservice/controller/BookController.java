package com.microservice.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.client.OrderClient;
import com.microservice.exceptions.NotFoundException;
import com.microservice.model.Book;
import com.microservice.payload.BookDto;
import com.microservice.service.BookService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class BookController {
	
	private final BookService bookService;
	
	private final OrderClient orderClient;

	@GetMapping("/all")
	public List<BookDto> list() {
		return bookService.findAll()
				.stream()
				.map(Book::toDto)
				.map(dto -> {
					dto.setSold(orderClient.countBookSold(dto.getId()));
					
					return dto;
				})
				.collect(Collectors.toList());
	}

	@GetMapping("/detail/{id}")
	public BookDto getBookDetail(@PathVariable("id") long id) {
		Book book = bookService.findById(id)
				.orElseThrow(() -> new NotFoundException("Book is not found"));
		BookDto bookDto = Book.toDto(book);
		bookDto.setSold(orderClient.countBookSold(id));
		return bookDto;
	}
	
	@PostMapping("/detail")
	public BookDto save(@RequestBody BookDto bookDto) {
		Book book = bookService.findById(bookDto.getId())
				.orElse(new Book());
		book.setId(bookDto.getId() == 0 ? null : book.getId());
		book.setName(bookDto.getName());
		book.setDescription(bookDto.getDescription());
		book.setPrice(bookDto.getPrice());
		book = bookService.save(book);
		return Book.toDto(book);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/detail/{id}")
	public void delete(@PathVariable("id") long id) {
		Book book = bookService.findById(id)
		.orElseThrow(() -> new NotFoundException("Book is not found"));
		bookService.delete(book);
	}
	
}
