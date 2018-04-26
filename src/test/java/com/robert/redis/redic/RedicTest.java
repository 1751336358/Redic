package com.robert.redis.redic;

import java.util.Arrays;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

@ContextConfiguration(locations = "/spring/application-context-redic-test.xml")
public class RedicTest extends AbstractTestNGSpringContextTests {
	@Test(groups = { "redic" })
	public void testRedic() {
		/*Redic redic = (Redic) applicationContext.getBean("redic");
		redic.set("name", "robert");
		System.out.println("Get value: " + redic.get("name"));*/
	}
	
	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/spring/application-context-redic-test.xml");
		Redic redic = (Redic) applicationContext.getBean("redic");

		redic.set("name", "robert");
	
		System.out.println("Get value: " + redic.get("name"));
	}
}
