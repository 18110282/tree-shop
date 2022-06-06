package com.treeshop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class TreeShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(TreeShopApplication.class, args);
        //See all bean
        //Arrays.stream(app.getBeanDefinitionNames()).forEach(System.out::println);
    }

//    @Bean
//    public CommandLineRunner commandLineRunner(){
//        return args -> {
//          System.out.println("Hello World");
//        };
//    }

}
