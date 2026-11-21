package com.franchises.demo_franchises.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.franchises.demo_franchises.dao.FranchiseDao;
import com.franchises.demo_franchises.entity.Franchise;

import jakarta.transaction.Transactional;

@Service
public class FranchiseServiceImpl implements FranchiseService {

	private final FranchiseDao franchiseDao;

	FranchiseServiceImpl(FranchiseDao franchiseDao) {
		this.franchiseDao = franchiseDao;
	}
	
	@Override
	@Transactional
	public List<Franchise> findAll() {
		return (List<Franchise>) franchiseDao.findAll();
	}

	@Override
	@Transactional
	public Franchise save(Franchise franchise) {
		return franchiseDao.save(franchise);
	}

	@Override
	@Transactional
	public Franchise findById(Long id) {
		return franchiseDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Franchise franchise) {
		franchiseDao.delete(franchise);		
	}

}
