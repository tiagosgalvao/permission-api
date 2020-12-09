# Permission Reactive RESTful API

Me, Tiago Galvao hereby declare: that this code is authentic created by my own authorship, utilizing the listed technologies below. 
It might or not have also been resulted from for some proof of concept (Poc), personal purposes or even selection process, although 
the company(ies) involved won't be disclosed. There is a chance that during the development process some references, tutorials, 
videos from the web or books supported me in order to apply best practices resulting in a good application. 

## Requirements
For building and running the application you need:

- [Spring Boot](http://projects.spring.io/spring-boot)
- [JDK 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Gradle](https://gradle.org)

## Introduction
This guide walks you through the process of creating an application that accesses non relational database through Reactive RESTful API calls.

## About the application
Check file task-description-compact.pdf.

## Programming Languages
This project is authored in Java 11.

## Dependencies
* 	[Git](https://git-scm.com/) - Free and Open-Source distributed version control system
* 	[Lombok](https://projectlombok.org/) - Never write another getter or equals method again, with one annotation your class has a fully featured builder, 
Automate your logging variables, and much more
* 	[Spring Boot](https://spring.io/projects/spring-boot) - Framework to ease the bootstrapping and development of new Spring Applications

## Running the application locally
There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the 
`com.galvao.permission.PermissionApplication` class from your IDE.

Alternatively you can use the [Spring Boot Gradle plugin](https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/html/) like so:

```shell
./gradlew bootRun
```

Enabling the profile "LOCAL" will display two extra controllers in swagger to help playing with the API.
```
-Dspring.profiles.active=LOCAL
```

* compiles Java classes to the /target directory
* copies all resources to the /target directory
* starts an embedded Apache Tomcat server

## Folder structure + important files

```bash
.
├── README.md
├── build.gradle
├── settings.gradle
└── src
    ├── main
    │   ├── java                                # service
    │   └── resources
    │       ├── application.yml                 # Common application configuration runnning using docker configs
    └── test
        ├── java                                # Sample Testcases
```

## Database Migration
This application is running using an embed mongoDB.

MongoDB [reference](https://www.mongodb.com/)

## Testing the application
There is a documentation swagger page with all the possible endpoint calls for the API.

http://localhost:8080/api-docs/

## Integration tests
The application has a very high test coverage - 100%, that guarantees that all the scenarios are covered. (Controller + Service - layers)