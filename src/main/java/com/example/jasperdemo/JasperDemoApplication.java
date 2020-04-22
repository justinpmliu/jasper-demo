package com.example.jasperdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.jasperdemo.mapper")
@SpringBootApplication
public class JasperDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(JasperDemoApplication.class, args);
	}

}
