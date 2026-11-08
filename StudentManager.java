import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class StudentManager {

    // The list that holds all Student objects
    private ArrayList<Student> students;

    // Constructor: creates an empty list
    public StudentManager() {
        students = new ArrayList<Student>();
    }

    // 1. Add a student (prevents duplicate IDs)
    public void addStudent(Student s) {
        if (findById(s.getId()) != null) {
            System.out.println("Error: Student ID " + s.getId() + " already exists.");
            return;
        }
        students.add(s);
        System.out.println("Student added successfully.");
    }

    // Returns the list of students (used by the GUI to fill the table)
    public ArrayList<Student> getStudents() {
        return students;
    }

    // 2. Find a student by ID (returns null if not found)
    public Student findById(String id) {
        for (Student s : students) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    // 3. Update a student's grade
    public void updateGrade(String id, double grade) {
        Student s = findById(id);
        if (s == null) {
            System.out.println("Error: Student with ID " + id + " not found.");
            return;
        }
        s.setGrade(grade);
        System.out.println("Grade updated successfully.");
    }

    // 4. Remove a student by ID
    public void removeById(String id) {
        Student s = findById(id);
        if (s == null) {
            System.out.println("Error: Student with ID " + id + " not found.");
            return;
        }
        students.remove(s);
        System.out.println("Student removed successfully.");
    }

    // 5. Display all students (handles empty list)
    public void displayAll() {
        if (students.isEmpty()) {
            System.out.println("The list is empty. No students to display.");
            return;
        }
        System.out.println("--- Student List ---");
        for (Student s : students) {
            System.out.println(s);
        }
        System.out.println("--------------------");
    }

    // 6. Sort by name, A to Z (anonymous Comparator, no lambdas)
    public void sortByName() {
        if (students.isEmpty()) {
            System.out.println("The list is empty. Nothing to sort.");
            return;
        }
        Collections.sort(students, new Comparator<Student>() {
            @Override
            public int compare(Student a, Student b) {
                return a.getName().compareTo(b.getName());
            }
        });
        System.out.println("Students sorted by name (A to Z).");
    }

    // 7. Sort by grade, highest to lowest (anonymous Comparator, no lambdas)
    public void sortByGrade() {
        if (students.isEmpty()) {
            System.out.println("The list is empty. Nothing to sort.");
            return;
        }
        Collections.sort(students, new Comparator<Student>() {
            @Override
            public int compare(Student a, Student b) {
                if (a.getGrade() > b.getGrade()) {
                    return -1;   // a comes first (higher grade)
                } else if (a.getGrade() < b.getGrade()) {
                    return 1;    // b comes first (higher grade)
                } else {
                    return 0;    // equal grades
                }
            }
        });
        System.out.println("Students sorted by grade (highest to lowest).");
    }

    // Main method with the Scanner menu
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentManager manager = new StudentManager();
        int choice = -1;

        while (choice != 0) {
            System.out.println();
            System.out.println("===== Student Manager Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. Find Student by ID");
            System.out.println("3. Update Student Grade");
            System.out.println("4. Remove Student by ID");
            System.out.println("5. Display All Students");
            System.out.println("6. Sort by Name");
            System.out.println("7. Sort by Grade");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            // Validate that the user entered a number
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // discard the bad input
                continue;
            }
            scanner.nextLine(); // consume the leftover newline

            switch (choice) {
                case 1:
                    System.out.print("Enter ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Grade: ");
                    double grade = scanner.nextDouble();
                    scanner.nextLine(); // consume the leftover newline
                    manager.addStudent(new Student(id, name, grade));
                    break;

                case 2:
                    System.out.print("Enter ID to find: ");
                    String findId = scanner.nextLine();
                    Student found = manager.findById(findId);
                    if (found == null) {
                        System.out.println("Error: Student with ID " + findId + " not found.");
                    } else {
                        System.out.println("Found: " + found);
                    }
                    break;

                case 3:
                    System.out.print("Enter ID: ");
                    String updId = scanner.nextLine();
                    System.out.print("Enter new Grade: ");
                    double newGrade = scanner.nextDouble();
                    scanner.nextLine(); // consume the leftover newline
                    manager.updateGrade(updId, newGrade);
                    break;

                case 4:
                    System.out.print("Enter ID to remove: ");
                    String delId = scanner.nextLine();
                    manager.removeById(delId);
                    break;

                case 5:
                    manager.displayAll();
                    break;

                case 6:
                    manager.sortByName();
                    manager.displayAll();
                    break;

                case 7:
                    manager.sortByGrade();
                    manager.displayAll();
                    break;

                case 0:
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }
}
