package com.example.demo;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan
//@ComponentScan(basePackages = "com.example.demo.dao")
//@EnableJpaRepositories(basePackages = "com.example.demo.dao")
public class DemoScan {
}
