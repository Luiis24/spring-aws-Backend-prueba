package com.franchises.demo_franchises.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.franchises.demo_franchises.entity.Franchise;
import com.franchises.demo_franchises.service.FranchiseService;

@RestController
@RequestMapping("/franchises")
public class FranchiseController {

	private final FranchiseService franchiseService;

	FranchiseController(FranchiseService franchiseService) {
		this.franchiseService = franchiseService;
	}

	@GetMapping
	public ResponseEntity<Object> getAll() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Franchise> list = franchiseService.findAll();
			return new ResponseEntity<Object>(list, HttpStatus.OK);
		} catch (Exception e) {
			map.put("message", e.getMessage());
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Object> getById(@PathVariable Long id) {
		try {
			Franchise data = franchiseService.findById(id);
			return new ResponseEntity<Object>(data, HttpStatus.OK);
		} catch (Exception e) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("message", e.getMessage());
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping
	public ResponseEntity<Object> save(@RequestBody Franchise franchise) {
		Map<String, Object> map = new HashMap<>();
		try {
			Franchise data = franchiseService.save(franchise);
			return new ResponseEntity<Object>(data, HttpStatus.CREATED);
		} catch (Exception e) {
			map.put("message", e.getMessage());
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody Franchise franchise) {
		Map<String, Object> map = new HashMap<>();
		try {
			Franchise existing = franchiseService.findById(id);
			if (existing == null) {
				map.put("message", "Franchise not found");
				return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
			}
			franchise.setId(id);
			Franchise updated = franchiseService.save(franchise);
			return new ResponseEntity<>(updated, HttpStatus.OK);
		} catch (Exception e) {
			map.put("message", e.getMessage());
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Object> delete(@PathVariable Long id) {
		Map<String, Object> map = new HashMap<>();
		try {
			Franchise data = franchiseService.findById(id);
			franchiseService.delete(data);
			map.put("message", "Franchise deleted successfully");
			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception e) {
			map.put("message", e.getMessage());
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
