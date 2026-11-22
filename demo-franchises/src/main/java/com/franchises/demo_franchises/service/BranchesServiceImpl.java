package com.franchises.demo_franchises.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.franchises.demo_franchises.dao.BranchesDao;
import com.franchises.demo_franchises.entity.Branches;

import jakarta.transaction.Transactional;

@Service
public class BranchesServiceImpl implements BranchesService {

	private final BranchesDao branchesDao;
	
	BranchesServiceImpl(BranchesDao branchesDao) {
		this.branchesDao = branchesDao;
	}
	
	@Override
	@Transactional
	public List<Branches> findAll() {
		return (List<Branches>) branchesDao.findAll();
	}

	@Override
	@Transactional
	public Branches save(Branches branches) {
		return branchesDao.save(branches);
	}

	@Override
	@Transactional
	public Branches findById(Long id) {
		return branchesDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Branches branches) {
		branchesDao.delete(branches);
		
	}

}
