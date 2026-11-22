package com.franchises.demo_franchises.entity;

import java.io.Serializable;
import java.util.Objects;

public class BranchProductsId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idBranch;
	private Long idProduct;

	public BranchProductsId() {}

	public BranchProductsId(Long idBranch, Long idProduct) {
		this.idBranch = idBranch;
		this.idProduct = idProduct;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BranchProductsId that)) return false;
		return Objects.equals(idBranch, that.idBranch) && Objects.equals(idProduct, that.idProduct);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idBranch, idProduct);
	}
}
