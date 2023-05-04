package ir.ac.kntu;

import java.util.*;

public class Course {

    private String courseNumber;

    private String name;

    private String units;

    private ArrayList<Course> neededCourses= new ArrayList<>();

    private static ArrayList<Course> courses = new ArrayList<>();

    public Course(String courseNumber, String name, String units,  ArrayList<Course> neededCourses) {
        setCourseNumber(courseNumber);
        setName(name);
        setUnits(units);
        setNeededCourses(neededCourses);
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public ArrayList<Course> getNeededCourses() {
        return neededCourses;
    }

    public void setNeededCourses(ArrayList<Course> neededCourses) {
        this.neededCourses = neededCourses;
    }

    public static ArrayList<Course> getCourses() {
        return courses;
    }

    public static void setCourses(ArrayList<Course> courses) {
        Course.courses = courses;
    }

    @Override
    public String toString() {
        return "Course name: "+name+", Course number: "+courseNumber+", Course units: "+units;
    }

    public boolean checkNeededCourse(Course course) {
        if (neededCourses.contains(course)) {
            return true;
        } else {
            return false;
        }
    }

    public static void submitCourse(Scanner scan) {
        System.out.println("Enter the course number:");
        String courseNumber = scan.nextLine();
        System.out.println("Enter the course name: ");
        String courseName = scan.nextLine();

        for ( Course course : courses ) {
            if (course.courseNumber.equals(courseNumber)) {
                System.out.println("This course number is already taken! Please try again.");
                Messages.pressEnter();
                submitCourse(scan);
            } else if (course.name.equals(courseName)) {
                System.out.println("This name is already in use! Please try again.");
                Messages.pressEnter();
                submitCourse(scan);
            }
        }

        System.out.println("Enter the units value of this course:");
        String units = scan.nextLine();

        ArrayList<Course> neededCourses = new ArrayList<>();

        makeNeededCourses(scan, neededCourses);
        neededCourses = addAllNeededCourses(neededCourses);

        Course course = new Course(courseNumber, courseName, units, neededCourses);
        courses.add(course);
        System.out.println("The course submitted successfully!");
        Messages.pressEnter();
        Admin.adminMenu(scan);
    }

    public static void makeNeededCourses(Scanner scan, ArrayList<Course> neededCourses) {
        for ( Course course : courses ) {
            System.out.format("%d. %s\n", courses.indexOf((Object) course) + 1, course.toString());
        }

        if (courses.size() >= 1) {
            System.out.format("%d. Finish setting the needed courses' list\n", courses.size()+1);
            System.out.println("Choose the needed course from the lit above:");
            while (true) {
                int option = Messages.checkInputs(scan, 1, courses.size() + 1 );
                if (option == courses.size()+1) {
                    System.out.println("Needed courses list submitted successfully!");
                    Messages.pressEnter();
                    break;
                } else if (option >= 1 && option < courses.size()+1 && !neededCourses.contains(courses.get(option-1))) {
                    System.out.println("Added successfully!");
                    neededCourses.add(courses.get(option-1));
                } else {
                    System.out.println("Wrong input!");
                }
            }
        }
    }

    public static ArrayList<Course> addAllNeededCourses(ArrayList<Course> neededCourses) {
        ArrayList<Course> result = new ArrayList<>();
        for ( Course course : neededCourses ) {
            result.add(course);
        }

        for ( Course course1 : neededCourses ) {
            for ( Course course2 : course1.neededCourses ) {
                if ( !result.contains(course2) ) {
                    result.add(course2);
                }
            }
        }

        return result;
    }

    public static void showAllCourses(Scanner scan) {
        for ( Course course1 : courses ) {
            System.out.format("course name: %s, course number : %s, course units : %s\n",
                    course1.name, course1.courseNumber, course1.units);
            if (course1.getNeededCourses().size() != 0) {
                System.out.println("Needed courses for this course:");
                for (Course course2 : course1.neededCourses) {
                    System.out.format("course name: %s, course number : %s\n", course2.name, course2.courseNumber);
                }
            }
            System.out.println("******************************************************");
        }

        Messages.pressEnter();
        Admin.adminMenu(scan);
    }

    public static void makeCourseExamples() {
        ArrayList<Course> neededCourses1 = new ArrayList<>();
        Course course1 = new Course("1", "Fundamentals of Programming", "3", neededCourses1);
        courses.add(course1);
        ArrayList<Course> neededCourses2 = new ArrayList<>();
        neededCourses2.add(course1);
        Course course2 = new Course("2", "Advanced Programming", "3", neededCourses2);
        courses.add(course2);
        ArrayList<Course> neededCourses3 = new ArrayList<>();
        Course course3 = new Course("3", "Physics", "3", neededCourses3);
        courses.add(course3);
    }

}