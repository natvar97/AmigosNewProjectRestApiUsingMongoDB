package com.indialone.amigosnewproject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@SpringBootApplication
public class AmigosNewProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmigosNewProjectApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(
            StudentRepository repository,
            MongoTemplate mongoTemplate
    ) {
        return args -> {
            Address address = new Address(
                    "India",
                    "Deesa",
                    "384315"
            );
            String email = "prajapatiurvashi1707@gmail.com";
            Student student = new Student(
                    "Uru",
                    "Prajapati",
                    email,
                    Gender.FEMALE,
                    address,
                    List.of("BSc Chemistry", "Computer Science "),
                    BigDecimal.TEN,
                    LocalDateTime.now()
            );

//            usingMongoTemplateAndQuery(repository , mongoTemplate , email , student);

            repository.findStudentByEmail(email)
                    .ifPresentOrElse(s -> {
                        System.out.println(s + " is already exists.");
                    }, () -> {
                        System.out.println("Inserting student : " + student);
                        repository.insert(student);
                    });

        };
    }

    private void usingMongoTemplateAndQuery(
            StudentRepository repository,
            MongoTemplate mongoTemplate,
            String email,
            Student student
    ) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));

        List<Student> students = mongoTemplate.find(query, Student.class);
        if (students.size() > 1)
            throw new IllegalStateException("found main students with same email" + email);

        if (students.isEmpty()) {
            System.out.println("Inserting student : " + student);
            repository.insert(student);
        } else {
            System.out.println(student + " is already exists.");
        }

    }

}

