package com.franchises.demo_franchises.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.franchises.demo_franchises.entity.BranchProducts;
import com.franchises.demo_franchises.entity.BranchProductsId;

public interface BranchProductsDao extends JpaRepository<BranchProducts, BranchProductsId> {

	public interface TopProductByBranchProjection {
		Long getBranchId();
		String getBranchName();
		Long getProductId();
		String getProductName();
		Integer getStock();
	}

	@Query(value = """
			SELECT s.id AS branchId,
			       s.nombre AS branchName,
			       p.id AS productId,
			       p.nombre AS productName,
			       sp.stock AS stock
			FROM sucursal_productos sp
			INNER JOIN sucursales s ON s.id = sp.sucursal_id
			INNER JOIN productos p ON p.id = sp.producto_id
			WHERE s.franquicia_id = :franchiseId
			  AND sp.stock = (
			      SELECT MAX(sp2.stock)
			      FROM sucursal_productos sp2
			      WHERE sp2.sucursal_id = sp.sucursal_id
			  )
			""", nativeQuery = true)
	List<TopProductByBranchProjection> findTopStockProductByFranchise(@Param("franchiseId") Long franchiseId);
}
