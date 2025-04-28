package jpabook.jpashop;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;

@SpringBootApplication
public class JpashopApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpashopApplication.class, args);
    }
}
