## [TopJava](https://javaops.ru/view/topjava) graduation project

REST API service with JDK 21, Spring Boot, Data JPA, Web, Security, Lombok, H2, Caffeine Cache, Swagger/OpenAPI 3.0

Requirements for a voting service:

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote for a restaurant they want to have lunch at today
* Only one vote counted per user
* If user votes again the same day:
    - If it is before 11:00 we assume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Run: `mvn spring-boot:run` in the root directory

[REST API documentation](http://localhost:8080/)

Credentials:
```
Admin: admin@gmail.com / admin
User:  user@yandex.ru / password
Guest: guest@gmail.com / guest
```