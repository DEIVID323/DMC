package com.example.DMC;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DmcApplication {

	public static void main(String[] args) {
		 SpringApplication.run(DmcApplication.class, args);  
		/* System.out.println(new BCryptPasswordEncoder().encode("12345")); */

       /*  BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "12345"; // la contraseña en texto plano
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
     */
	}

}
