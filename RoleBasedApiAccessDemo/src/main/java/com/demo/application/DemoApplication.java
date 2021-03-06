package com.demo.application;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class DemoApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		new DemoApplication().configure(new SpringApplicationBuilder(DemoApplication.class)).run(args);
	}
}