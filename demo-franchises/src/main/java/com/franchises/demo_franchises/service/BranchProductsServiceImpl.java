package com.franchises.demo_franchises.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.franchises.demo_franchises.dao.BranchProductsDao;
import com.franchises.demo_franchises.dao.BranchProductsDao.TopProductByBranchProjection;
import com.franchises.demo_franchises.entity.BranchProducts;
import com.franchises.demo_franchises.entity.BranchProductsId;

import jakarta.transaction.Transactional;

@Service
public class BranchProductsServiceImpl implements BranchProductsService {

	private final BranchProductsDao branchProductsDao;

	BranchProductsServiceImpl(BranchProductsDao branchProductsDao) {
		this.branchProductsDao = branchProductsDao;
	}

	@Override
	@Transactional
	public List<BranchProducts> findAll() {
		return branchProductsDao.findAll();
	}

	@Override
	@Transactional
	public BranchProducts save(BranchProducts branchProducts) {
		return branchProductsDao.save(branchProducts);
	}

	@Override
	@Transactional
	public BranchProducts findById(Long idBranch, Long idProduct) {
		return branchProductsDao.findById(new BranchProductsId(idBranch, idProduct)).orElse(null);
	}

	@Override
	@Transactional
	public void delete(BranchProducts branchProducts) {
		branchProductsDao.delete(branchProducts);
	}

	@Override
	@Transactional
	public BranchProducts updateStock(Long idBranch, Long idProduct, Integer stock) {
		BranchProducts existing = findById(idBranch, idProduct);
		if (existing == null) {
			return null;
		}
		existing.setStock(stock);
		return branchProductsDao.save(existing);
	}

	@Override
	@Transactional
	public List<TopProductByBranchProjection> getTopStockProductsByFranchise(Long franchiseId) {
		return branchProductsDao.findTopStockProductByFranchise(franchiseId);
	}
}