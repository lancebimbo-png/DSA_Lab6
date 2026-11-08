public class Student {

    // Fields (instance variables)
    private String id;
    private String name;
    private double grade;

    // Constructor: creates a new Student object
    public Student(String id, String name, double grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getGrade() {
        return grade;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    // toString(): returns a readable string when a Student is printed
    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Grade: " + grade;
    }
}
