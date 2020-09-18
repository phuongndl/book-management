package com.microservice.service.impl;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.service.GeneralService;

import java.util.List;
import java.util.Optional;

public abstract class GeneralServiceImpl<T> implements GeneralService<T> {

    protected JpaRepository<T, Long> repository;

    public GeneralServiceImpl(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    public T save(T t) {
        return repository.save(t);
    }

    @Override
    public void delete(T t) {
        repository.delete(t);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<T> findById(long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return repository.findAll();
    }
}
