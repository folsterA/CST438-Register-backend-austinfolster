package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

/*
 * This example shows how to use selenium testing using the web driver 
 * with Chrome browser.
 * 
 *  - Buttons, input, and anchor elements are located using XPATH expression.
 *  - onClick( ) method is used with buttons and anchor tags.
 *  - Input fields are located and sendKeys( ) method is used to enter test data.
 *  - Spring Boot JPA is used to initialize, verify and reset the database before
 *      and after testing.
 *      
 *    Make sure that TEST_COURSE_ID is a valid course for TEST_SEMESTER.
 *    
 *    URL is the server on which Node.js is running.
 */

@SpringBootTest
public class EndToEndStudentTest {

	public static final String CHROME_DRIVER_FILE_LOCATION = "C:\\Users\\Austin\\Documents\\Java Projects\\CST438-Register-backend-austinfolster\\cst438_register\\src\\test\\resources\\chromedriver.exe";

	public static final String URL = "http://localhost:3000";

	public static final String TEST_STUDENT_EMAIL = "test@csumb.edu";
	
	public static final String TEST_STUDENT_NAME = "Test Student";

	public static final int SLEEP_DURATION = 1000; // 1 second.

	/*
	 * When running in @SpringBootTest environment, database repositories can be used
	 * with the actual database.
	 */
	
	@Autowired
	EnrollmentRepository enrollmentRepository;

	@Autowired
	CourseRepository courseRepository;
	

	@Autowired
	StudentRepository studentRepository;
	

	/*
	 * Administrator adds student TEST_STUDENT_NAME and TEST_STUDENT_EMAIL to the database.
	 */
	
	@Test
	public void addStudentTest() throws Exception {

		/*
		 * if student already exists, then delete the student.
		 */
		
		Student x = null;
		do {
			x = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
			if (x != null)
				studentRepository.delete(x);
		} while (x != null);

		// set the driver location and start driver
		//@formatter:off
		// browser	property name 				Java Driver Class
		// edge 	webdriver.edge.driver 		EdgeDriver
		// FireFox 	webdriver.firefox.driver 	FirefoxDriver
		// IE 		webdriver.ie.driver 		InternetExplorerDriver
		//@formatter:on

		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		try {
			
			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);

			// perform the correct steps to get a student added
			
			// select the 'Add Student' button to bring up the pop-up and click
			driver.findElement(By.xpath("//button[@id='addStudent']")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// enter the student's name
			driver.findElement(By.xpath("//input[@id='student_name']")).sendKeys(TEST_STUDENT_NAME);
			Thread.sleep(SLEEP_DURATION);
			
			// enter the student's email
			driver.findElement(By.xpath("//input[@id='student_email']")).sendKeys(TEST_STUDENT_EMAIL);
			Thread.sleep(SLEEP_DURATION);
			
			// select the 'Add' button and click to confirm addition
			driver.findElement(By.xpath("//button[@id='Add']")).click();
			Thread.sleep(SLEEP_DURATION * 4);
			
			// verify that enrollment row has been inserted to database.
			Student e = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
			System.out.println(e.toString());
			assertNotNull(e, "Student not found in database.");

		} catch (Exception ex) {
			throw ex;
		} finally {
			// clean up database.
			Student e = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
			if (e != null)
				studentRepository.delete(e);

			driver.quit();
		}

	}
}
