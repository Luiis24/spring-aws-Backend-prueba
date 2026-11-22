package com.franchises.demo_franchises.service;

import java.util.List;

import com.franchises.demo_franchises.entity.Branches;

public interface BranchesService {

	public List<Branches> findAll();
	public Branches save(Branches branches);
	public Branches findById(Long id);
	public void delete (Branches branches);
}
