# MySchedule 

**MySchedule** - is a web application designed for teachers and students to create, manage, and optimize their schedules efficiently. It allows users to organize classes, set time slots, and customize their weekly plans with ease. The platform provides an intuitive interface, making it simple to add, edit, and rearrange schedules based on personal or institutional needs. With features like real-time updates, notifications, and flexible customization, **MySchedule** helps improve time management and ensures a smooth scheduling experience for both educators and learners.

---

## 📋 Key Features

- **Sсhedule Creation**: creating schedules for teachers and students
- **Session Scheduling**: Ensure schedules are created without overlapping time slots
- **User Management**: Ban/Unban users or change their roles with admin credentials
- **Personal portfolio**: pages, where you can find my contacts and CV 
- **CRUD for classrooms**: creating, updating, deleting and retreaving classrooms with stuff credentials
- **CRUD for groups**: creating, updating, deleting and retreaving groups with stuff credentials
- **CRUD for courses**: creating, updating, deleting and retreaving courses with stuff credentials

--- 

## ⚙️ Technologies

- **Programming language**: Java 11
- **Framework**: Spring Boot, Spring Core, Spring Data JPA, Spring Web
- **Authorization**: Spring Security, HttpSession
- **Database**: PostgeSQL
- **Migrations**: FlyWay
- **Testing**: JUnit, Mockito, Parametrized tests, Testcontainers, JMeter, Docker
- **Test covarage**: JaCoCo
- **Logging**: SLF4J
- **Deployment**: AWS, RDS, EC2, ElasticBean stalk

---

## 🚀 Getting Started  

**URL:** http://myschedule-env.eba-jxsimkic.eu-north-1.elasticbeanstalk.com 

---

## 📂 Project Structure

- **config/**: Contains configuration classes, such as beans, CORS settings, or application profiles.  
- **controller/**: Handles HTTP requests and returns responses.
- **converter/**: Contains classes responsible for transforming data between different representations, such as entity-to-DTO and DTO-to-entity conversions.
- **dto/**: Contains classes used for transferring data between layers.
- **editor/**: Provides custom property editors for handling specific data types in request parameters or form submissions.
- **entity/**: Defines entities mapped to database tables.
- **exception/**: Defines custom exceptions and exception handling logic for the application.
- **handler/**: Implements centralized mechanisms for processing exceptions, events, or request handling, such as global exception handlers.
- **repository/**: Provides database access methods.
- **service/**: Contains business logic and service layer code.
- **resources/**: Stores configuration files, templates, and migration scripts.
- **test/**: Contains test classes for unit and integration testing.  
- **pom.xml**: Lists dependencies, plugins, and build configuration.

--- 

## 🛠 Contributing

If you want to contribute to this project, feel free to fork the repository, create a feature branch, and submit a pull request.

---

## 📜 License

This project is licensed under the [LICENSE](./LICENSE). You are free to use, modify, and distribute this software as described in the license.
