package com.contactmanager.bean;

/**
 * @className CS 6301-022 User Interface Design
 * @author Ankit Tolia (abt140130), Abhishek Bansal (axb146030)
 * @email-id abt140130@utdallas.edu,axb146030@utdallas.edu 
 * @version 1.1
 * @started date 1th Nov 2014
 * 
 * Java Class that performs the retrieval and saving of contact data which includes first and last name, phone and email.
 * Purpose : Class Assignment
 * @created by Ankit Tolia
 */

public class ContactListBean {

	private String firstName;
	private String lastName;
	private String phone;
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
