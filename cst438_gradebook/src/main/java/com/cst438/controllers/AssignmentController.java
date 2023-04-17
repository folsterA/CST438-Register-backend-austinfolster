package com.cst438.controllers;

import java.sql.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentListDTO.AssignmentDTO;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
public class AssignmentController {
	
	@Autowired
	AssignmentRepository assignmentRepository;
	
	@Autowired
	CourseRepository courseRepository;
	
	@PostMapping("/assignment")
	@Transactional
	public AssignmentDTO newAssignment(@RequestBody AssignmentDTO dto) {
		String userEmail = "dwisneski@csumb.edu";
		// validate course and that the course instructor is the user
		Course c = courseRepository.findById(dto.courseId).orElse(null);
		if (c != null && c.getInstructor().equals(userEmail)) {
			// create and save new assignment
			// update and return dto with new assignment primary key
			Assignment a = new Assignment();
			a.setCourse(c);
			a.setName(dto.assignmentName);
			a.setDueDate(Date.valueOf(dto.dueDate));
			a.setNeedsGrading(1);
			a = assignmentRepository.save(a);
			dto.courseId=a.getId();
			return dto;
			
		} else {
			// invalid course
			throw new ResponseStatusException( 
                           HttpStatus.BAD_REQUEST, 
                          "Invalid course id.");
		}
	}
}
