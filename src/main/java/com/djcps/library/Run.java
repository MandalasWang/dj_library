package com.djcps.library;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author djsxs
 *
 */
@SpringBootApplication
@MapperScan(value = "com.djcps.library.mapper")
@ComponentScan(basePackages="com.djcps.library")
@EnableAutoConfiguration
@EnableScheduling
@ServletComponentScan
public class Run {
	public static void main(String[] args) {
		SpringApplication.run(Run.class, args);
	}
}
