
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final ArrayList<Student> students = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    // Records are stored in this file in the project directory.
    private static final Path DATA_FILE =
            Paths.get("students.txt");

    public static void main(String[] args) {

        loadStudents();

        boolean running = true;

        while (running) {

            System.out.println("\n===== STUDENT MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {

                case "1":
                    addStudent();
                    break;

                case "2":
                    viewStudents();
                    break;

                case "3":
                    searchStudent();
                    break;

                case "4":
                    updateStudent();
                    break;

                case "5":
                    deleteStudent();
                    break;

                case "6":
                    running = false;
                    System.out.println("Application closed.");
                    break;

                default:
                    System.out.println("Invalid choice. Enter 1 to 6.");
            }
        }

        scanner.close();
    }

    // Add a new student
    private static void addStudent() {

        System.out.print("Enter student ID: ");
        String id = scanner.nextLine().trim();

        if (id.isEmpty()) {
            System.out.println("Student ID cannot be empty.");
            return;
        }

        if (findStudent(id) != null) {
            System.out.println("Student ID already exists.");
            return;
        }

        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Student name cannot be empty.");
            return;
        }

        System.out.print("Enter course: ");
        String course = scanner.nextLine().trim();

        if (course.isEmpty()) {
            System.out.println("Course cannot be empty.");
            return;
        }

        Student student = new Student(id, name, course);
        students.add(student);

        if (saveStudents()) {
            System.out.println("Student added successfully.");
        } else {
            students.remove(student);
            System.out.println("Could not save the student.");
        }
    }

    // Display all students
    private static void viewStudents() {

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.println("\n----- STUDENT RECORDS -----");

        for (Student student : students) {
            System.out.println(student);
        }

        System.out.println("Total students: " + students.size());
    }

    // Search for a student using their ID
    private static void searchStudent() {

        System.out.print("Enter student ID to search: ");
        String id = scanner.nextLine().trim();

        if (id.isEmpty()) {
            System.out.println("Student ID cannot be empty.");
            return;
        }

        Student student = findStudent(id);

        if (student != null) {
            System.out.println("\nStudent found:");
            System.out.println(student);
        } else {
            System.out.println("Student not found.");
        }
    }

    // Update a student's name and course
    private static void updateStudent() {

        System.out.print("Enter student ID to update: ");
        String id = scanner.nextLine().trim();

        if (id.isEmpty()) {
            System.out.println("Student ID cannot be empty.");
            return;
        }

        Student student = findStudent(id);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("Current details:");
        System.out.println(student);

        System.out.print("Enter new name: ");
        String newName = scanner.nextLine().trim();

        if (newName.isEmpty()) {
            System.out.println("Student name cannot be empty.");
            return;
        }

        System.out.print("Enter new course: ");
        String newCourse = scanner.nextLine().trim();

        if (newCourse.isEmpty()) {
            System.out.println("Course cannot be empty.");
            return;
        }

        String oldName = student.getName();
        String oldCourse = student.getCourse();

        student.setName(newName);
        student.setCourse(newCourse);

        if (saveStudents()) {
            System.out.println("Student updated successfully.");
        } else {
            student.setName(oldName);
            student.setCourse(oldCourse);
            System.out.println("Could not save changes. Update cancelled.");
        }
    }

    // Delete a student using their ID
    private static void deleteStudent() {

        System.out.print("Enter student ID to delete: ");
        String id = scanner.nextLine().trim();

        if (id.isEmpty()) {
            System.out.println("Student ID cannot be empty.");
            return;
        }

        Student student = findStudent(id);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("Student to delete:");
        System.out.println(student);
        System.out.print("Confirm deletion? (yes/no): ");

        String confirmation = scanner.nextLine().trim();

        if (!confirmation.equalsIgnoreCase("yes")) {
            System.out.println("Deletion cancelled.");
            return;
        }

        students.remove(student);

        if (saveStudents()) {
            System.out.println("Student deleted successfully.");
        } else {
            students.add(student);
            System.out.println("Could not save changes. Deletion cancelled.");
        }
    }

    // Find a student by ID; IDs are case-insensitive
    private static Student findStudent(String id) {

        for (Student student : students) {
            if (student.getId().equalsIgnoreCase(id)) {
                return student;
            }
        }

        return null;
    }

    // Save all student records to the text file
    private static boolean saveStudents() {

        try (BufferedWriter writer = Files.newBufferedWriter(
                DATA_FILE,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE)) {

            for (Student student : students) {

                // Encode fields so separators/newlines in text
                // do not corrupt the file format.
                String line = encode(student.getId()) + "|"
                        + encode(student.getName()) + "|"
                        + encode(student.getCourse());

                writer.write(line);
                writer.newLine();
            }

            return true;

        } catch (IOException e) {
            System.out.println("File save error: " + e.getMessage());
            return false;
        }
    }

    // Load student records when the program starts
    private static void loadStudents() {

        if (!Files.exists(DATA_FILE)) {
            System.out.println("No saved records yet. Starting fresh.");
            return;
        }

        List<Student> loadedStudents = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(
                DATA_FILE, StandardCharsets.UTF_8)) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("\\|", -1);

                if (parts.length != 3) {
                    System.out.println("Skipping invalid record in file.");
                    continue;
                }

                String id = decode(parts[0]);
                String name = decode(parts[1]);
                String course = decode(parts[2]);

                if (id.isEmpty() || name.isEmpty() || course.isEmpty()) {
                    System.out.println("Skipping incomplete student record.");
                    continue;
                }

                boolean duplicate = false;

                for (Student existing : loadedStudents) {
                    if (existing.getId().equalsIgnoreCase(id)) {
                        duplicate = true;
                        break;
                    }
                }

                if (!duplicate) {
                    loadedStudents.add(new Student(id, name, course));
                }
            }

            students.clear();
            students.addAll(loadedStudents);

            System.out.println("Loaded " + students.size()
                    + " student record(s).");

        } catch (IOException e) {
            System.out.println("File load error: " + e.getMessage());
            System.out.println("The application will continue with records in memory.");
        }
    }

    // Encode special characters for safe storage in a delimited text file
    private static String encode(String value) {
        return value.replace("%", "%25")
                .replace("|", "%7C")
                .replace("\r", "%0D")
                .replace("\n", "%0A");
    }

    // Decode stored text back into its original form
    private static String decode(String value) {
        return value.replace("%0A", "\n")
                .replace("%0D", "\r")
                .replace("%7C", "|")
                .replace("%25", "%");
    }
}
