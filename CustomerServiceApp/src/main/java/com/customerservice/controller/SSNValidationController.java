package com.customerservice.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/v1/api")
public class SSNValidationController {

	@GetMapping("/ssnvalidate")
	public boolean ssnValidate() {
		return true;
	}

}
