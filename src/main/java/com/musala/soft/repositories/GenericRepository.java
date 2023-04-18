package com.musala.soft.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface GenericRepository <T,IDT> extends PagingAndSortingRepository<T,IDT> {
}