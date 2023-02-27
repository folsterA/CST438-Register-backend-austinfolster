package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cst438.controller.StudentController;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@ContextConfiguration(classes = { StudentController.class })
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
public class JunitTestStudent {

	static final String URL = "http://localhost:8080";
	public static final String TEST_STUDENT_EMAIL = "test@csumb.edu";
	public static final String TEST_STUDENT_NAME  = "test";

	@MockBean
	StudentRepository studentRepository;

	@Autowired
	private MockMvc mvc;

	@Test
	public void addStudent() throws Exception {
		MockHttpServletResponse response;
		
		// add test student
		response = mvc.perform(
				MockMvcRequestBuilders
					.post("/addStudent?studentName=" + TEST_STUDENT_NAME + ",studentEmail=" + TEST_STUDENT_EMAIL)
					.accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
	
		// verify response
		assertEquals(200, response.getStatus());
	}

	@Test
	public void putHold() throws Exception {
		MockHttpServletResponse response;
		
		Student testStudent = new Student();
		testStudent.setName(TEST_STUDENT_NAME);
		testStudent.setEmail(TEST_STUDENT_EMAIL);
		testStudent.setStatus(null);
		testStudent.setStatusCode(0);
		
		given(studentRepository.findById(0)).willReturn(Optional.of(testStudent));
		
		// add test student
		response = mvc.perform(
				MockMvcRequestBuilders
					.post("/putHold?studentId=0")
					.accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
	
		// verify response
		assertEquals(200, response.getStatus());
		
		//verify id isn't 0
		Student result = fromJsonString(response.getContentAsString(), Student.class);
		assertNotEquals(0, result.getStatusCode());
	}

	@Test
	public void releaseHold() throws Exception {
		MockHttpServletResponse response;
		
		Student testStudent = new Student();
		testStudent.setName(TEST_STUDENT_NAME);
		testStudent.setEmail(TEST_STUDENT_EMAIL);
		testStudent.setStatus(null);
		testStudent.setStatusCode(0);
		
		given(studentRepository.findById(0)).willReturn(Optional.of(testStudent));
		
		// add test student
		response = mvc.perform(
				MockMvcRequestBuilders
					.post("/releaseHold?studentId=0")
					.accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
	
		// verify response
		assertEquals(200, response.getStatus());
		
		//verify id isn't 0
		Student result = fromJsonString(response.getContentAsString(), Student.class);
		assertEquals(0, result.getStatusCode());
	}
	
	private static <T> T  fromJsonString(String str, Class<T> valueType ) {
		try {
			return new ObjectMapper().readValue(str, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
