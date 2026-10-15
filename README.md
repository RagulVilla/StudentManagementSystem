# Student Management System

A console-based Student Management System developed using Java. This project allows users to manage student records through a simple menu-driven interface.

## Features

- Add new student records
- View all students
- Search students by ID
- Update student details
- Delete student records with confirmation
- Validate empty inputs
- Prevent duplicate student IDs
- Save and load student records using file handling
- Preserve student records after restarting the application

## Technologies Used

- Java
- Gradle
- IntelliJ IDEA
- File Handling
- Git and GitHub

## Project Structure

```text
StudentManagementSystem/
├── src/
│   └── main/
│       └── java/
│           ├── Main.java
│           └── Student.java
├── students.txt
├── build.gradle.kts
├── gradlew
├── gradlew.bat
├── gradle/
└── README.md
```

## How to Run

1. Clone or download this repository.
2. Open the project in IntelliJ IDEA.
3. Make sure a compatible JDK is configured.
4. Run `Main.java`.
5. Follow the menu displayed in the console.

## Application Menu

1. Add Student
2. View All Students
3. Search Student
4. Update Student
5. Delete Student
6. Exit

## Data Storage

Student records are stored in a local text file named `students.txt`. The application loads existing records when it starts and saves changes when student records are added, updated, or deleted.

## Learning Outcomes

- Java classes and objects
- Constructors, getters, and setters
- ArrayList and collections
- Loops and conditional statements
- Methods and input validation
- Exception handling
- File reading and writing
- Gradle project structure
- Git and GitHub basics

## Future Improvements

- Add a graphical user interface
- Integrate a database such as MySQL
- Add sorting and filtering
- Add automated unit tests

## Author

Ragul SV
