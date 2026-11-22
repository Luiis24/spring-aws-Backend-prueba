package com.franchises.demo_franchises.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.franchises.demo_franchises.dao.BranchProductsDao.TopProductByBranchProjection;
import com.franchises.demo_franchises.entity.BranchProducts;
import com.franchises.demo_franchises.service.BranchProductsService;

@RestController
@RequestMapping("/branch-products")
public class BranchProductsController {

	private final BranchProductsService branchProductsService;

	BranchProductsController(BranchProductsService branchProductsService) {
		this.branchProductsService = branchProductsService;
	}

	@PostMapping
	public ResponseEntity<Object> addProductToBranch(@RequestBody BranchProducts branchProducts) {
		Map<String, Object> map = new HashMap<>();
		try {
			BranchProducts existing = branchProductsService.findById(
					branchProducts.getIdBranch(), branchProducts.getIdProduct());
			if (existing != null) {
				map.put("message", "Product already exists in this branch");
				return new ResponseEntity<>(map, HttpStatus.CONFLICT);
			}
			BranchProducts data = branchProductsService.save(branchProducts);
			return new ResponseEntity<>(data, HttpStatus.CREATED);
		} catch (Exception e) {
			map.put("message", e.getMessage());
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/branch/{idBranch}/product/{idProduct}")
	public ResponseEntity<Object> removeProductFromBranch(
			@PathVariable Long idBranch,
			@PathVariable Long idProduct) {
		Map<String, Object> map = new HashMap<>();
		try {
			BranchProducts existing = branchProductsService.findById(idBranch, idProduct);
			if (existing == null) {
				map.put("message", "Branch product not found");
				return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
			}
			branchProductsService.delete(existing);
			map.put("message", "Product removed from branch successfully");
			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception e) {
			map.put("message", e.getMessage());
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PatchMapping("/branch/{idBranch}/product/{idProduct}/stock")
	public ResponseEntity<Object> updateStock(
			@PathVariable Long idBranch,
			@PathVariable Long idProduct,
			@RequestBody Map<String, Integer> body) {
		Map<String, Object> map = new HashMap<>();
		try {
			Integer newStock = body.get("stock");
			if (newStock == null || newStock < 0) {
				map.put("message", "Invalid stock value");
				return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
			}
			BranchProducts updated = branchProductsService.updateStock(idBranch, idProduct, newStock);
			if (updated == null) {
				map.put("message", "Branch product not found");
				return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(updated, HttpStatus.OK);
		} catch (Exception e) {
			map.put("message", e.getMessage());
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/franchise/{franchiseId}/top-stock")
	public ResponseEntity<Object> getTopStockByFranchise(@PathVariable Long franchiseId) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<TopProductByBranchProjection> result =
					branchProductsService.getTopStockProductsByFranchise(franchiseId);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			map.put("message", e.getMessage());
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
