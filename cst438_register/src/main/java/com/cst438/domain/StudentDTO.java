package com.cst438.domain;

public class StudentDTO {
	
	public int student_id;
	public String name;
	public String email;
	public int statusCode;
	public String status;
	
	public StudentDTO() {
		this.student_id = 0;
		this.name = "";
		this.email = "";
		this.statusCode = 0;
		this.status = null;
	}
	
	public StudentDTO(int student_id, String name, String email, int statusCode, String status) {
		this.student_id = student_id;
		this.name = name;
		this.email = email;
		this.statusCode = statusCode;
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "EnrollmentDTO [student_id=" + this.student_id + ", name=" + this.name + ", email=" + this.email
				+ ", statusCose=" + this.statusCode + ", status=" + this.status + "]";
	}

}
