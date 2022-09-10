package com.customerservice.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "customer")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotBlank(message = "name is required")
	@Column(name = "name")
	private String name;

	@Column(name = "ssn")
	private String ssn;

	@Column(name = "phone")
	@Length(min = 10, max = 10, message = "Please enter 10 digits in phone number")
	private String phone;

	@Column(name = "email")
	@Email(message = "Please enter valid email")
	private String email;

	@Column(name = "birthDate")
	private String birthDate;




}
