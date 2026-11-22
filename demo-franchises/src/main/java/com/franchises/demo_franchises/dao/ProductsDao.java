package com.franchises.demo_franchises.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.franchises.demo_franchises.entity.Products;

public interface ProductsDao extends JpaRepository<Products, Long>{

}
