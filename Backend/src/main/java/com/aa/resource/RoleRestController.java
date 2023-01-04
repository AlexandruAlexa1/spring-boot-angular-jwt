package com.aa.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aa.domain.Role;
import com.aa.service.RoleService;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleRestController {

	@Autowired
	private RoleService service;
	
	@GetMapping
	public List<Role> listAll() {
		return service.findAll();
	}
	
}
