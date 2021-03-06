package com.pengurus.crm.entities;

import java.util.HashSet;
import java.util.Set;

import com.pengurus.crm.shared.dto.PersonalDataDTO;

public class PersonalData {

	private Long id;
	private String firstName;
	private String lastName;
	private String address;
	private Set<String> phoneNumber;
	private Set<String> email;

	public PersonalData() {
		super();
	}

	public PersonalData(PersonalDataDTO personalDataDTO) {
		this(personalDataDTO.getId(), personalDataDTO.getFirstName(),
				personalDataDTO.getLastName(), personalDataDTO.getAddress(),
				new HashSet<String>(personalDataDTO.getPhoneNumber()),
				new HashSet<String>(personalDataDTO.getEmail()));
	}

	public PersonalData(Long id, String firstName, String lastName, String address,
			Set<String> phoneNumber, Set<String> email) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.email = email;
	}
	public PersonalData(String firstName, String lastName, String address,
			Set<String> phoneNumber, Set<String> email) {
		this(null, firstName, lastName, address, phoneNumber, email);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Set<String> getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Set<String> phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Set<String> getEmail() {
		return email;
	}

	public void setEmail(Set<String> email) {
		this.email = email;
	}

	public PersonalDataDTO toDTO() {
		return new PersonalDataDTO(getId(), getFirstName(), getLastName(),
				getAddress(), new HashSet<String>(getPhoneNumber()),
				new HashSet<String>(getEmail()));
	}

}
