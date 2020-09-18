package com.microservice.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.microservice.exceptions.NotFoundException;
import com.microservice.model.Book;
import com.microservice.repository.BookRepository;
import com.microservice.service.BookService;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository repository;

	@PreAuthorize("hasPermission('book', 'view')")
	@Override
	public Optional<Book> findById(long id) {
		return repository.findById(id);
	}

	@PreAuthorize("hasPermission('book', 'view')")
	@Override
	public List<Book> findAll() {
		return repository.findAll();
	}

	@PreAuthorize("hasPermission('book', 'create')")
	@Override
	public Book save(Book t) {
		return repository.save(t);
	}

	@PreAuthorize("hasPermission('book', 'update')")
	@Override
	public Book update(Book t) {
		findById(t.getId())
				.orElseThrow(() -> new NotFoundException("Not found request book"));
		return repository.save(t);
	}

	@PreAuthorize("hasPermission('book', 'delete')")
	@Override
	public void delete(Book t) {
		Book b = findById(t.getId())
				.orElseThrow(() -> new NotFoundException("Not found request book"));
		repository.delete(b);
	}

}
