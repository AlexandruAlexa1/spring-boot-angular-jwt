package com.aa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aa.domain.Role;
import com.aa.repository.RoleRepository;

@Service
public class RoleService {

	@Autowired
	private RoleRepository repo;
	
	public List<Role> findAll() {
		return repo.findAll();
	}
}
