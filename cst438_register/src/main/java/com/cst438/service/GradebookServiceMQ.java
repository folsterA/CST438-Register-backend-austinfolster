package com.cst438.service;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.cst438.domain.CourseDTOG;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;


public class GradebookServiceMQ extends GradebookService {
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	@Autowired
	Queue gradebookQueue;
	
	
	public GradebookServiceMQ() {
		System.out.println("MQ grade book service");
	}
	
	// send message to grade book service about new student enrollment in course
	@Override
	public void enrollStudent(String student_email, String student_name, int course_id) {
		 
		// TODO 
		// create EnrollmentDTO and send to gradebookQueue
		EnrollmentDTO enrollment = new EnrollmentDTO();
		enrollment.studentEmail = student_email;
		enrollment.studentName = student_name;
		enrollment.course_id = course_id;
		
		System.out.println("Message send to gradbook service for student "+ student_email +" " + course_id);  
		rabbitTemplate.convertAndSend(gradebookQueue.getName(), enrollment);
		System.out.println("Message sent.");
		
	}
	
	@RabbitListener(queues = "registration-queue")
	public void receive(CourseDTOG courseDTOG) {
		System.out.println("Receive enrollment :" + courseDTOG);

		//TODO 
		// for each student grade in courseDTOG,  find the student enrollment entity, update the grade and save back to enrollmentRepository.		
		for (CourseDTOG.GradeDTO grade : courseDTOG.grades) {
			Enrollment enrollment = enrollmentRepository.findByEmailAndCourseId(grade.student_email, courseDTOG.course_id);
			enrollment.setCourseGrade(grade.grade);
			enrollmentRepository.save(enrollment);
		}
	}

}
