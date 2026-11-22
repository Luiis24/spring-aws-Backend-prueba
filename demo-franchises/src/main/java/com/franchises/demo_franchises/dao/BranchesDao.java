package com.franchises.demo_franchises.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.franchises.demo_franchises.entity.Branches;

public interface BranchesDao extends JpaRepository<Branches, Long>{

}
