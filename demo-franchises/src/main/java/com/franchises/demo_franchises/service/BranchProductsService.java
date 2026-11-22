package com.franchises.demo_franchises.service;

import java.util.List;

import com.franchises.demo_franchises.dao.BranchProductsDao.TopProductByBranchProjection;
import com.franchises.demo_franchises.entity.BranchProducts;

public interface BranchProductsService {

	public List<BranchProducts> findAll();
	public BranchProducts save(BranchProducts branchProducts);
	public BranchProducts findById(Long idBranch, Long idProduct);
	public void delete(BranchProducts branchProducts);
	public BranchProducts updateStock(Long idBranch, Long idProduct, Integer stock);
	public List<TopProductByBranchProjection> getTopStockProductsByFranchise(Long franchiseId);
}
