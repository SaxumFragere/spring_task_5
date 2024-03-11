package org.example;

import org.example.repos.TppRefAccountTypeRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Starter {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Starter.class);
        TppRefAccountTypeRepo tppRefAccountTypeRepo = ctx.getBean(TppRefAccountTypeRepo.class);
        System.out.println(tppRefAccountTypeRepo.findById(3L));
      //  tppRefAccountTypeRepo.findAll().forEach(System.out::println);
    }
}
