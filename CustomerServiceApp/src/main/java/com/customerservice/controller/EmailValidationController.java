package com.customerservice.controller;

import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/v1/api")
public class EmailValidationController {

	@GetMapping("/emailvalidate")
	public boolean emailValidate() {
		return true;
	}

}
