import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Main university management system that handles course, student, and professor management.
 * Provides a command-line interface for various university operations.
 */
public class UniversityManagementSystem {
    private static List<Course> courses;
    private static List<Student> students;
    private static List<Professor> professors;

    /**
     * Checks if a string contains only digits.
     *
     * @param str the string to check
     * @return Boolean.TRUE if string contains only digits, Boolean.FALSE otherwise
     */
    private static Boolean isDigits(String str) {
        if (str.isEmpty()) {
            return Boolean.FALSE;
        }
        for (int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) > '9' || str.charAt(i) < '0') {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    /**
     * Finds a course by name.
     *
     * @param courseName the course name to search for
     * @return the Course object if found, null otherwise
     */
    private static Course getCourse(String courseName) {
        for (Course course : courses) {
            if (Objects.equals(course.getCourseName(), courseName)) {
                return course;
            }
        }
        return null;
    }

    /**
     * Finds a course by ID.
     *
     * @param courseId the course ID to search for
     * @return the Course object if found, null otherwise
     */
    private static Course getCourse(int courseId) {
        for (Course course : courses) {
            if (course.getCourseId() == courseId) {
                return course;
            }
        }
        return null;
    }

    /**
     * Finds a student by ID.
     *
     * @param studentId the student ID to search for
     * @return the Student object if found, null otherwise
     */
    private static Student getStudent(int studentId) {
        for (Student student : students) {
            if (student.getMemberId() == studentId) {
                return student;
            }
        }
        return null;
    }

    /**
     * Finds a professor by ID.
     *
     * @param professorId the professor ID to search for
     * @return the Professor object if found, null otherwise
     */
    private static Professor getProfessor(int professorId) {
        for (Professor professor : professors) {
            if (professor.getMemberId() == professorId) {
                return professor;
            }
        }
        return null;
    }

    /**
     * Converts a string to CourseLevel enum.
     *
     * @param courseLevel the string representation of course level
     * @return the corresponding CourseLevel
     * @throws RuntimeException if the string is not "bachelor" or "master"
     */
    private static CourseLevel toCourseLevelFromString(String courseLevel) {
        courseLevel = courseLevel.toLowerCase();
        if (Objects.equals(courseLevel, "bachelor")) {
            return CourseLevel.BACHELOR;
        }
        if (Objects.equals(courseLevel, "master")) {
            return CourseLevel.MASTER;
        }
        throw new RuntimeException("Wrong inputs");
    }

    /**
     * Initializes the system with sample data including courses, students, and professors.
     */
    public static void fillInitialData() {
        courses = new ArrayList<>();
        courses.add(new Course("java_beginner", CourseLevel.BACHELOR));
        courses.add(new Course("java_intermediate", CourseLevel.BACHELOR));
        courses.add(new Course("python_basics", CourseLevel.BACHELOR));
        courses.add(new Course("algorithms", CourseLevel.MASTER));
        courses.add(new Course("advanced_programming", CourseLevel.MASTER));
        courses.add(new Course("mathematical_analysis", CourseLevel.MASTER));
        courses.add(new Course("computer_vision", CourseLevel.MASTER));

        Student alice = new Student("alice");
        alice.enroll(getCourse("java_beginner"));
        alice.enroll(getCourse("java_intermediate"));
        alice.enroll(getCourse("python_basics"));

        Student bob = new Student("bob");
        bob.enroll(getCourse("java_beginner"));
        bob.enroll(getCourse("algorithms"));

        Student alex = new Student("alex");
        alex.enroll(getCourse("advanced_programming"));

        students = new ArrayList<>();
        students.add(alice);
        students.add(bob);
        students.add(alex);

        Professor ali = new Professor("ali");
        ali.teach(getCourse("java_beginner"));
        ali.teach(getCourse("java_intermediate"));

        Professor ahmed = new Professor("ahmed");
        ahmed.teach(getCourse("python_basics"));
        ahmed.teach(getCourse("advanced_programming"));

        Professor andrey = new Professor("andrey");
        andrey.teach(getCourse("mathematical_analysis"));

        professors = new ArrayList<>();
        professors.add(ali);
        professors.add(ahmed);
        professors.add(andrey);
    }

    /**
     * Main method that runs the university management system.
     * Provides a command-line interface for managing courses, students, and professors.
     *
     * Supported commands:
     * - course: Add a new course
     * - student: Add a new student
     * - professor: Add a new professor
     * - enroll: Enroll a student in a course
     * - drop: Drop a student from a course
     * - teach: Assign a professor to teach a course
     * - exempt: Remove a professor from teaching a course
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        fillInitialData();
        Scanner input = new Scanner(System.in);
        try {
            while (input.hasNextLine()) {
                String command = input.nextLine().toLowerCase();
                if (command.isEmpty()) {
                    break;
                }

                switch (command) {
                    case "course":
                        String courseName = input.nextLine().toLowerCase();
                        if (getCourse(courseName) != null) {
                            throw new RuntimeException("Course exists");
                        }
                        if (NameChecker.isCorrectCourseName(courseName) == Boolean.FALSE) {
                            throw new RuntimeException("Wrong inputs");
                        }

                        String courseLevel = input.nextLine().toLowerCase();
                        if ((!courseLevel.equals("bachelor") && !courseLevel.equals("master"))) {
                            throw new RuntimeException("Wrong inputs");
                        }

                        courses.add(new Course(courseName, toCourseLevelFromString(courseLevel)));
                        System.out.println("Added successfully");
                        break;
                    case "student":
                        String studentName = input.nextLine().toLowerCase();
                        if (NameChecker.isCorrectPeopleName(studentName) == Boolean.FALSE) {
                            throw new RuntimeException("Wrong inputs");
                        }

                        students.add(new Student(studentName));
                        System.out.println("Added successfully");
                        break;
                    case "professor":
                        String professorName = input.nextLine().toLowerCase();
                        if (NameChecker.isCorrectPeopleName(professorName) == Boolean.FALSE) {
                            throw new RuntimeException("Wrong inputs");
                        }

                        professors.add(new Professor(professorName));
                        System.out.println("Added successfully");
                        break;
                    case "enroll":
                        String studentToEnrollIdString = input.nextLine();
                        if (!isDigits(studentToEnrollIdString)) {
                            throw new RuntimeException("Wrong inputs");
                        }
                        int studentToEnrollId = Integer.parseInt(studentToEnrollIdString);
                        Student studentToEnroll = getStudent(studentToEnrollId);
                        if (studentToEnroll == null) {
                            throw new RuntimeException("Wrong inputs");
                        }

                        String courseToEnrollIdString = input.nextLine();
                        if (!isDigits(courseToEnrollIdString)) {
                            throw new RuntimeException("Wrong inputs");
                        }
                        int courseToEnrollId = Integer.parseInt(courseToEnrollIdString);
                        Course courseToEnroll = getCourse(courseToEnrollId);
                        if (courseToEnroll == null) {
                            throw new RuntimeException("Wrong inputs");
                        }

                        studentToEnroll.enroll(courseToEnroll);
                        System.out.println("Enrolled successfully");
                        break;
                    case "drop":
                        String studentToDropIdString = input.nextLine();
                        if (!isDigits(studentToDropIdString)) {
                            throw new RuntimeException("Wrong inputs");
                        }
                        int studentToDropId = Integer.parseInt(studentToDropIdString);
                        Student studentToDrop = getStudent(studentToDropId);
                        if (studentToDrop == null) {
                            throw new RuntimeException("Wrong inputs");
                        }

                        String courseToDropIdString = input.nextLine();
                        if (!isDigits(courseToDropIdString)) {
                            throw new RuntimeException("Wrong inputs");
                        }
                        int courseToDropId = Integer.parseInt(courseToDropIdString);
                        Course courseToDrop = getCourse(courseToDropId);
                        if (courseToDrop == null) {
                            throw new RuntimeException("Wrong inputs");
                        }

                        studentToDrop.drop(courseToDrop);
                        System.out.println("Dropped successfully");
                        break;
                    case "teach":
                        String professorToTeachIdString = input.nextLine();
                        if (!isDigits(professorToTeachIdString)) {
                            throw new RuntimeException("Wrong inputs");
                        }
                        int professorToTeachId = Integer.parseInt(professorToTeachIdString);
                        Professor professorToTeach = getProfessor(professorToTeachId);
                        if (professorToTeach == null) {
                            throw new RuntimeException("Wrong inputs");
                        }

                        String courseToTeachIdString = input.nextLine();
                        if (!isDigits(courseToTeachIdString)) {
                            throw new RuntimeException("Wrong inputs");
                        }
                        int courseToTeachId = Integer.parseInt(courseToTeachIdString);
                        Course courseToTeach = getCourse(courseToTeachId);
                        if (courseToTeach == null) {
                            throw new RuntimeException("Wrong inputs");
                        }

                        professorToTeach.teach(courseToTeach);
                        System.out.println("Professor is successfully assigned to teach this course");
                        break;
                    case "exempt":
                        String professorToExemptIdString = input.nextLine();
                        if (!isDigits(professorToExemptIdString)) {
                            throw new RuntimeException("Wrong inputs");
                        }
                        int professorToExemptId = Integer.parseInt(professorToExemptIdString);
                        Professor professorToExempt = getProfessor(professorToExemptId);
                        if (professorToExempt == null) {
                            throw new RuntimeException("Wrong inputs");
                        }

                        String courseToExemptIdString = input.nextLine();
                        if (!isDigits(courseToExemptIdString)) {
                            throw new RuntimeException("Wrong inputs");
                        }
                        int courseToExemptId = Integer.parseInt(courseToExemptIdString);
                        Course courseToExempt = getCourse(courseToExemptId);
                        if (courseToExempt == null) {
                            throw new RuntimeException("Wrong inputs");
                        }

                        professorToExempt.exempt(courseToExempt);
                        System.out.println("Professor is exempted");
                        break;
                    default:
                        throw new RuntimeException("Wrong inputs");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

/**
 * Utility class for validating university member and course names.
 * Provides static methods to check name correctness according to business rules.
 */
final class NameChecker {
    private NameChecker() { }

    /**
     * Validates if a person's name meets the requirements.
     *
     * @param name the name to validate
     * @return Boolean.TRUE if name is valid, Boolean.FALSE otherwise
     * @throws RuntimeException if name is a reserved keyword or empty
     */
    static Boolean isCorrectPeopleName(String name) {
        if (Objects.equals(name, "course")
                || Objects.equals(name, "student")
                || Objects.equals(name, "professor")
                || Objects.equals(name, "enroll")
                || Objects.equals(name, "drop")
                || Objects.equals(name, "exempt")
                || Objects.equals(name, "teach")
                || Objects.equals(name, "")) {
            throw new RuntimeException("Wrong inputs");
        }

        name = name.toLowerCase();
        for (char ch : name.toCharArray()) {
            if (ch < 'a' || ch > 'z') {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    /**
     * Validates if a course name meets the requirements.
     *
     * @param name the course name to validate
     * @return Boolean.TRUE if course name is valid, Boolean.FALSE otherwise
     * @throws RuntimeException if name is a reserved keyword or empty
     */
    static Boolean isCorrectCourseName(String name) {
        name = name.toLowerCase();
        if (Objects.equals(name, "course")
                || Objects.equals(name, "student")
                || Objects.equals(name, "professor")
                || Objects.equals(name, "enroll")
                || Objects.equals(name, "drop")
                || Objects.equals(name, "exempt")
                || Objects.equals(name, "teach")
                || Objects.equals(name, "")) {
            throw new RuntimeException("Wrong inputs");
        }
        char[] arr = name.toCharArray();
        for (int i = 0; i < arr.length; ++i) {
            if ((i == 0 || i == arr.length - 1) && (arr[i] == '_')) {
                return Boolean.FALSE;
            } else if ((arr[i] < 'a' || arr[i] > 'z') && (arr[i] != '_')) {
                return Boolean.FALSE;
            }
            if (i > 0 && arr[i] == '_' && (arr[i - 1] < 'a' || arr[i - 1] > 'z')) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }
}

/**
 * Abstract base class representing a member of the university.
 * Provides common functionality for all university members with automatic ID generation.
 */
abstract class UniversityMember {
    private static int numberOfMembers = 0;
    private int memberId;
    private String name;

    /**
     * Constructs a new UniversityMember with the given name.
     * Automatically generates a unique member ID.
     *
     * @param memberName the name of the university member
     */
    public UniversityMember(String memberName) {
        numberOfMembers += 1;
        memberId = numberOfMembers;
        name = memberName;
    }

    /**
     * Returns the unique member ID.
     *
     * @return the member ID
     */
    public int getMemberId() {
        return memberId;
    }

    /**
     * Returns the member's name.
     *
     * @return the member name
     */
    public String getName() {
        return name;
    }
}

/**
 * Enum representing the level of a course.
 * Courses can be either at bachelor's or master's level.
 */
enum CourseLevel {
    /** Bachelor's level course */
    BACHELOR,
    /** Master's level course */
    MASTER
}

/**
 * Interface for entities that can enroll in and drop courses.
 */
interface Enrollable {
    /**
     * Drops a course.
     *
     * @param course the course to drop
     * @return Boolean.TRUE if successful
     * @throws RuntimeException if student is not enrolled in the course
     */
    Boolean drop(Course course);

    /**
     * Enrolls in a course.
     *
     * @param course the course to enroll in
     * @return Boolean.TRUE if successful
     * @throws RuntimeException if course is full or student has reached maximum enrollment
     */
    Boolean enroll(Course course);
}

/**
 * Represents a university course with capacity limits and enrollment management.
 */
class Course {
    private static final int CAPACITY = 3;
    private static int numberOfCourses = 0;
    private int enrollCount = 0;
    private int courseId;
    private String courseName;
    private List<Student> enrolledStudents;
    private CourseLevel courseLevel;

    /**
     * Constructs a new Course with the given name and level.
     * Automatically generates a unique course ID.
     *
     * @param courseNameValue the name of the course
     * @param courseLevelValue the level of the course (BACHELOR or MASTER)
     */
    public Course(String courseNameValue, CourseLevel courseLevelValue) {
        numberOfCourses += 1;
        courseId = numberOfCourses;
        courseName = courseNameValue;
        courseLevel = courseLevelValue;
        enrolledStudents = new ArrayList<>();
    }

    /**
     * Returns the course ID.
     *
     * @return the course ID
     */
    public int getCourseId() {
        return courseId;
    }

    /**
     * Returns the course name.
     *
     * @return the course name
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Checks if the course has reached its capacity.
     *
     * @return Boolean.TRUE if course is full, false otherwise
     */
    public Boolean isFull() {
        if (enrollCount == CAPACITY) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * Adds a student to the course.
     *
     * @param newStudent the student to add
     * @throws RuntimeException if student is already enrolled, course is full,
     *         or student has reached maximum enrollment
     */
    public void addStudent(Student newStudent) {
        for (Student student : enrolledStudents) {
            if (student.equals(newStudent)) {
                throw new RuntimeException("Student is already enrolled in this course");
            }
        }
        if (newStudent.getCountOfEnrolledCourses() == Student.getMaxEnrollment()) {
            throw new RuntimeException("Maximum enrollment is reached for the student");
        }
        if (isFull()) {
            throw new RuntimeException("Course is full");
        }
        enrolledStudents.add(newStudent);
        enrollCount += 1;
    }

    /**
     * Removes a student from the course.
     *
     * @param oldStudent the student to remove
     * @throws RuntimeException if student is not enrolled in the course
     */
    public void removeStudent(Student oldStudent) {
        int idxOfCourseToDrop = -1;
        for (int i = 0; i < enrolledStudents.size(); ++i) {
            if (enrolledStudents.get(i).equals(oldStudent)) {
                idxOfCourseToDrop = i;
                break;
            }
        }
        if (idxOfCourseToDrop == -1) {
            throw new RuntimeException("Student is not enrolled in this course");
        }
        enrolledStudents.remove(idxOfCourseToDrop);
        enrollCount -= 1;
    }
}

/**
 * Represents a professor who can teach courses at the university.
 * Professors have a maximum teaching load limit.
 */
class Professor extends UniversityMember {
    private static final int MAX_LOAD = 2;
    private List<Course> assignedCourses;

    /**
     * Constructs a new Professor with the given name.
     *
     * @param memberName the professor's name
     */
    public Professor(String memberName) {
        super(memberName);
        assignedCourses = new ArrayList<>();
    }

    /**
     * Assigns the professor to teach a course.
     *
     * @param course the course to teach
     * @return Boolean.TRUE if successful
     * @throws RuntimeException if professor's load is complete or already teaching the course
     */
    public Boolean teach(Course course) {
        if (MAX_LOAD == assignedCourses.size()) {
            throw new RuntimeException("Professor's load is complete");
        }
        for (Course assignedCourse : assignedCourses) {
            if (assignedCourse.equals(course)) {
                throw new RuntimeException("Professor is already teaching this course");
            }
        }
        assignedCourses.add(course);
        return Boolean.TRUE;
    }

    /**
     * Removes the professor from teaching a course.
     *
     * @param course the course to stop teaching
     * @return Boolean.TRUE if successful
     * @throws RuntimeException if professor is not teaching the course
     */
    public Boolean exempt(Course course) {
        int idxOfCourseToExempt = -1;
        for (int i = 0; i < assignedCourses.size(); ++i) {
            if (assignedCourses.get(i).equals(course)) {
                idxOfCourseToExempt = i;
            }
        }
        if (idxOfCourseToExempt == -1) {
            throw new RuntimeException("Professor is not teaching this course");
        }
        assignedCourses.remove(idxOfCourseToExempt);
        return Boolean.TRUE;
    }
}

/**
 * Represents a student who can enroll in and drop courses.
 * Students have a maximum enrollment limit.
 */
class Student extends UniversityMember implements Enrollable {
    private static final int MAX_ENROLLMENT = 3;
    private List<Course> enrolledCourses;

    /**
     * Constructs a new Student with the given name.
     *
     * @param memberName the student's name
     */
    public Student(String memberName) {
        super(memberName);
        enrolledCourses = new ArrayList<>();
    }

    /**
     * Enrolls the student in a course.
     *
     * @param course the course to enroll in
     * @return Boolean.TRUE if successful
     * @throws RuntimeException if course is full, student has reached maximum enrollment,
     *         or student is already enrolled in the course
     */
    @Override
    public Boolean enroll(Course course) {
        course.addStudent(this);
        enrolledCourses.add(course);
        return Boolean.TRUE;
    }

    /**
     * Drops a course for the student.
     *
     * @param course the course to drop
     * @return Boolean.TRUE if successful
     * @throws RuntimeException if student is not enrolled in the course
     */
    @Override
    public Boolean drop(Course course) {
        int idxOfCourseToDrop = -1;
        for (int i = 0; i < enrolledCourses.size(); ++i) {
            if (enrolledCourses.get(i).equals(course)) {
                idxOfCourseToDrop = i;
            }
        }
        if (idxOfCourseToDrop == -1) {
            throw new RuntimeException("Student is not enrolled in this course");
        }
        enrolledCourses.remove(idxOfCourseToDrop);
        course.removeStudent(this);
        return Boolean.TRUE;
    }

    /**
     * Returns the number of courses the student is currently enrolled in.
     *
     * @return the count of enrolled courses
     */
    public int getCountOfEnrolledCourses() {
        return enrolledCourses.size();
    }

    /**
     * Returns the maximum number of courses a student can enroll in.
     *
     * @return the maximum enrollment limit
     */
    public static int getMaxEnrollment() {
        return MAX_ENROLLMENT;
    }
}
