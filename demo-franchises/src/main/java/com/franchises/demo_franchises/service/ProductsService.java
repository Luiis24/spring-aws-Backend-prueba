package com.franchises.demo_franchises.service;

import java.util.List;

import com.franchises.demo_franchises.entity.Products;

public interface ProductsService {
	
	public List<Products> findAll();
	public Products save(Products products);
	public Products findById(Long id);
	public void delete (Products products);
	
}
