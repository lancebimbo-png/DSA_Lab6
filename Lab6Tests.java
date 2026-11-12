import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Lab6Tests {

    private static int total = 0;
    private static int passed = 0;
    private static int failed = 0;

    private static PrintStream out;
    private static PrintStream quiet;

    public static void main(String[] args) {
        out = System.out;
        quiet = new PrintStream(new ByteArrayOutputStream());
        System.setOut(quiet);

        studentClassTests();
        emptyManagerTests();
        addTests();
        findTests();
        updateTests();
        removeTests();
        displayTests();
        sortByNameTests();
        sortByGradeTests();
        sortByNameIgnoreCaseTests();
        readGradeTests();

        System.setOut(out);
        out.println();
        out.println("Total checks: " + total);
        out.println("Passed: " + passed);
        out.println("Failed: " + failed);
        if (failed == 0) {
            out.println("RESULT: PASS");
        } else {
            out.println("RESULT: FAIL");
        }
        System.exit(failed == 0 ? 0 : 1);
    }

    private static void check(boolean condition, String description) {
        total++;
        if (condition) {
            passed++;
            out.println("PASS: " + description);
        } else {
            failed++;
            out.println("FAIL: " + description);
        }
    }

    private static String captureDisplay(Runnable action) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer));
        action.run();
        System.setOut(quiet);
        return buffer.toString();
    }

    private static StudentManager populated() {
        StudentManager m = new StudentManager();
        m.addStudent(new Student("101", "Alice", 92));
        m.addStudent(new Student("102", "Bob", 88));
        m.addStudent(new Student("103", "Charlie", 88));
        return m;
    }

    private static void studentClassTests() {
        Student s = new Student("101", "Alice", 92);
        check("101".equals(s.getId()), "Student constructor stores ID");
        check("Alice".equals(s.getName()), "Student constructor stores name");
        check(s.getGrade() == 92, "Student constructor stores grade");

        s.setId("102");
        s.setName("Bob");
        s.setGrade(88);
        check("102".equals(s.getId()), "setId changes the ID");
        check("Bob".equals(s.getName()), "setName changes the name");
        check(s.getGrade() == 88, "setGrade changes the grade");

        String text = s.toString();
        check(text.contains("102") && text.contains("Bob") && text.contains("88"),
                "toString shows ID, name, and grade");
    }

    private static void emptyManagerTests() {
        StudentManager m = new StudentManager();
        check(m.getStudents() != null && m.getStudents().isEmpty(),
                "New manager starts with an empty list");
        check(m.findById("101") == null, "findById on empty list returns null");

        String display = captureDisplay(new Runnable() {
            @Override
            public void run() {
                m.displayAll();
            }
        });
        check(display.contains("empty"), "displayAll on empty list reports the empty message");

        m.sortByName();
        check(m.getStudents().isEmpty(), "sortByName on empty list does not crash");

        m.sortByGrade();
        check(m.getStudents().isEmpty(), "sortByGrade on empty list does not crash");
    }

    private static void addTests() {
        StudentManager m = new StudentManager();
        m.addStudent(new Student("101", "Alice", 92));
        check(m.getStudents().size() == 1, "addStudent adds one record");

        Student found = m.findById("101");
        check(found != null && "Alice".equals(found.getName()) && found.getGrade() == 92,
                "Added student is findable with the correct fields");

        m.addStudent(new Student("102", "Bob", 88));
        m.addStudent(new Student("103", "Charlie", 88));
        m.addStudent(new Student("104", "Diana", 95));
        check(m.getStudents().size() == 4, "ArrayList grows to hold 4 students");

        m.addStudent(new Student("101", "Eve", 70));
        check(m.getStudents().size() == 4, "Duplicate ID is rejected, list size unchanged");
        check("Alice".equals(m.findById("101").getName()),
                "Duplicate add does not overwrite the original record");
    }

    private static void findTests() {
        StudentManager m = populated();
        Student bob = m.findById("102");
        check(bob != null && "Bob".equals(bob.getName()) && bob.getGrade() == 88,
                "findById returns the existing student");
        check(m.findById("999") == null, "findById returns null for a missing ID");
        check(m.getStudents().size() == 3, "findById does not modify the list");
    }

    private static void updateTests() {
        StudentManager m = populated();
        m.updateGrade("102", 96);
        check(m.findById("102").getGrade() == 96, "updateGrade changes the grade of the existing ID");
        check("Bob".equals(m.findById("102").getName()), "updateGrade leaves the name unchanged");
        check(m.getStudents().size() == 3, "updateGrade does not change the list size");

        m.updateGrade("999", 50);
        check(m.getStudents().size() == 3, "updateGrade on a missing ID leaves the list unchanged");
        check(m.findById("101").getGrade() == 92,
                "updateGrade on a missing ID does not touch other students");
    }

    private static void removeTests() {
        StudentManager m = populated();
        m.removeById("101");
        check(m.findById("101") == null, "removeById removes the student with that ID");
        check(m.getStudents().size() == 2, "List shrinks after remove");
        check(m.findById("102") != null && m.findById("103") != null,
                "Other students remain after remove");

        m.removeById("999");
        check(m.getStudents().size() == 2, "removeById on a missing ID leaves the list unchanged");

        m.addStudent(new Student("105", "Frank", 80));
        check(m.findById("105") != null, "List can still grow after a remove");
    }

    private static void displayTests() {
        StudentManager m = populated();
        String display = captureDisplay(new Runnable() {
            @Override
            public void run() {
                m.displayAll();
            }
        });
        check(display.contains("--- Student List ---"), "displayAll prints the list header");
        check(display.contains("101") && display.contains("Alice"), "displayAll prints the first student");
        check(display.contains("103") && display.contains("Charlie"),
                "displayAll prints every student in the list");
    }

    private static void sortByNameTests() {
        StudentManager m = new StudentManager();
        m.addStudent(new Student("102", "Bob", 88));
        m.addStudent(new Student("101", "Alice", 92));
        m.addStudent(new Student("106", "Bob", 85));
        m.addStudent(new Student("103", "Charlie", 88));
        m.sortByName();

        check("Alice".equals(m.getStudents().get(0).getName()), "sortByName puts A names first");
        check("Charlie".equals(m.getStudents().get(3).getName()), "sortByName puts Z names last");
        check("102".equals(m.getStudents().get(1).getId())
                        && "106".equals(m.getStudents().get(2).getId()),
                "Equal names keep their original order (stable sort)");
        check(m.getStudents().get(0).getGrade() == 92,
                "sortByName reorders references only, grades unchanged");
    }

    private static void sortByGradeTests() {
        StudentManager m = new StudentManager();
        m.addStudent(new Student("102", "Bob", 88));
        m.addStudent(new Student("104", "Diana", 97));
        m.addStudent(new Student("103", "Charlie", 88));
        m.addStudent(new Student("106", "Bob", 85));
        m.sortByGrade();

        check("104".equals(m.getStudents().get(0).getId()) && m.getStudents().get(0).getGrade() == 97,
                "sortByGrade puts the highest grade first");
        check("106".equals(m.getStudents().get(3).getId()) && m.getStudents().get(3).getGrade() == 85,
                "sortByGrade puts the lowest grade last");
        check("102".equals(m.getStudents().get(1).getId())
                        && "103".equals(m.getStudents().get(2).getId()),
                "Equal grades keep their original order (stable sort)");
        check("Diana".equals(m.getStudents().get(0).getName()),
                "sortByGrade reorders references only, names unchanged");
    }

    private static void sortByNameIgnoreCaseTests() {
        StudentManager m = new StudentManager();
        m.addStudent(new Student("1", "zed", 80));
        m.addStudent(new Student("2", "Alice", 90));
        m.addStudent(new Student("3", "bob", 70));
        m.sortByName();
        check("Alice".equals(m.getStudents().get(0).getName())
                        && "bob".equals(m.getStudents().get(1).getName())
                        && "zed".equals(m.getStudents().get(2).getName()),
                "sortByName ignores upper/lower case (Alice, bob, zed)");
    }

    private static void readGradeTests() {
        Scanner good = new Scanner("85.5\n");
        check(StudentManager.readGrade(good, "") == 85.5, "readGrade accepts a valid number");
        Scanner bad = new Scanner("abc\n\n90\n");
        check(StudentManager.readGrade(bad, "") == 90.0,
                "readGrade rejects non-numeric and blank input, then accepts a number");
    }
}
