package com.hackathon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AssignmentDecoderApplication {
    public static void main(String[] args) {
        SpringApplication.run(AssignmentDecoderApplication.class, args);
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   📚 Assignment Decoder with IBM Bob   ║");
        System.out.println("║   http://localhost:8080                ║");
        System.out.println("╚════════════════════════════════════════╝");
    }
}