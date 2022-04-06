package com.gucardev.tapucase.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = {"com.gucardev.tapucase"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class Config {
}
