package com.microservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{

}
