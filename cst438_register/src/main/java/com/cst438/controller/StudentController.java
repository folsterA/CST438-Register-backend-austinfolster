package com.cst438.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://registerf-cst438.herokuapp.com/"})
public class StudentController {
	
	@Autowired
	StudentRepository studentRepository;
	
	// add a student to the system
	@PostMapping("addStudent")
	public Student AddStudent( String studentName, String studentEmail) {
		// check for student email
		Student existingStudent = studentRepository.findByEmail(studentEmail);
		if (existingStudent != null) {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student with email already exists.  " + studentEmail);
		}
		
		// add new student to the system
		Student student = new Student();
		student.setName(studentName);
		student.setEmail(studentEmail);
		
		studentRepository.save(student);
		return student;
	}
	
	// student registration hold
	@PostMapping("putHold")
	public Student PutHold( int studentId) {
		Student student = studentRepository.findById(studentId).orElse(null);
		
		if (student != null) {
			student.setStatusCode(1);
			student.setStatus("Held from registration by Administrator.");
			
			studentRepository.save(student);
			return student;
		} else {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "StudentId invalid.  " + studentId);			
		}
	}
	
	// release hold on student registration
	@PostMapping("releaseHold")
	public Student ReleaseHold( int studentId) {
		Student student = studentRepository.findById(studentId).orElse(null);
		
		if (student != null) {
			student.setStatusCode(0);
			student.setStatus("Not held from registration.");
			
			studentRepository.save(student);
			return student;
		} else {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "StudentId invalid.  " + studentId);			
		}
	}
}
