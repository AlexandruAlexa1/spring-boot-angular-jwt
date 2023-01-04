package com.aa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "addresses")
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 20, nullable = false)
	@NotBlank(message = "City is required")
	@Length(min = 2, max = 20, message = "City mush have 5-20 characters")
	private String city;
	
	@Column(length = 20, nullable = false)
	@NotBlank(message = "State is required")
	@Length(min = 2, max = 20, message = "State mush have 5-20 characters")
	private String state;
	
	@Column(length = 20, nullable = false)
	@NotBlank(message = "Country is required")
	@Length(min = 2, max = 20, message = "Country mush have 5-20 characters")
	private String country;
	
	@Column(length = 20, nullable = false)
	@NotBlank(message = "Postal code is required")
	@Length(min = 2, max = 20, message = "PostalCode mush have 5-20 characters")
	private String postalCode;
	
	@Column(length = 15, nullable = false)
	@NotBlank(message = "Phone number is required")
	@Length(min = 10, max = 15, message = "Phone number mush have 10-15 characters")
	private String phoneNumber;
	
	public Address() {}

	public Address(String city, String state, String country, String postalCode, String phoneNumber) {
		this.city = city;
		this.state = state;
		this.country = country;
		this.postalCode = postalCode;
		this.phoneNumber = phoneNumber;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "Address [id=" + id + ", city=" + city + ", state=" + state + ", country=" + country + ", postalCode="
				+ postalCode + ", phoneNumber=" + phoneNumber + "]";
	}
	
}
