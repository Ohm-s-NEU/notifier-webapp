package com.advcloud.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;




public class EmailRequestDto {

    @Email(message = "Invalid Email address")
    private String email;
    @NotEmpty(message = "Email body cannot be Null")
    private String body;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
}
