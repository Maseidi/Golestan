package ir.ac.kntu;

import java.util.*;

public class Student {

    private String name;

    private String familyName;

    private String studentId;

    private String units;

    private String password;

    private HashMap<Course, String> passedCourses = new HashMap<>();

    private ArrayList<CourseGroup> courseGroups = new ArrayList<>();

    private static ArrayList<Student> students = new ArrayList<>();

    public Student(String name, String familyName, String studentId, String units, HashMap<Course, String> passedCourses,
                   ArrayList<CourseGroup> courseGroups, String password) {
        setName(name);
        setFamilyName(familyName);
        setStudentId(studentId);
        setUnits(units);
        setPassedCourses(passedCourses);
        setCourseGroups(courseGroups);
        setPassword(password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        if ( Integer.parseInt(units) >= 20 ) {
            this.units = "20";
        } else if ( Integer.parseInt(units) <= 0 ) {
            this.units = "0";
        } else {
            this.units = units;
        }
    }

    public HashMap<Course, String> getPassedCourses() {
        return passedCourses;
    }

    public void setPassedCourses(HashMap<Course, String> passedCourses) {
        this.passedCourses = passedCourses;
    }

    public ArrayList<CourseGroup> getCourseGroups() {
        return courseGroups;
    }

    public void setCourseGroups(ArrayList<CourseGroup> courseGroups) {
        this.courseGroups = courseGroups;
    }

    public static ArrayList<Student> getStudents() {
        return students;
    }

    public static void setStudents(ArrayList<Student> students) {
        Student.students = students;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static void submitStudent(Scanner scan) {
        System.out.println("Enter the student's name:");
        String name = scan.nextLine();
        while (!name.matches("[A-Z][a-z]*")) {
            System.out.println("Name shall be like \"Ali\". Please try again.");
            name = scan.nextLine();
        }
        System.out.println("Enter the student's family name:");
        String familyName = scan.nextLine();
        while (!familyName.matches("[A-Z][a-z]*")) {
            System.out.println("Family name shall be like \"Karimi\". Please try again.");
            familyName = scan.nextLine();
        }
        System.out.println("Enter the student's ID:");
        String studentId = scan.nextLine();
        while (!studentId.matches("\\d{10}")) {
            System.out.println("ID must be a 10 digits number. Please try again:");
            studentId = scan.nextLine();
        }
        for ( Student student : students ) {
            if (student.studentId.equals(studentId)) {
                System.out.println("This ID is already defined! Please try again.");
                Messages.pressEnter();
                submitStudent(scan);
            }
        }

        System.out.println("Enter the student's term units: ");
        String units = scan.nextLine();

        System.out.println("Enter the student's password:");
        String password = scan.nextLine();

        HashMap<Course, String > passedCourses = makePassedCourses(scan);

        makeNeededCoursesAsPassed(passedCourses, scan);
        System.out.println("Passed courses submitted successfully!");
        Messages.pressEnter();

        ArrayList<CourseGroup> courseGroups = new ArrayList<>();

        Student student = new Student(name, familyName, studentId, units, passedCourses, courseGroups, password);
        students.add(student);
        System.out.println("Student submitted successfully!");
        Messages.pressEnter();
        Admin.adminMenu(scan);
    }

    public static HashMap<Course, String> makePassedCourses(Scanner scan) {
        HashMap<Course, String > hashMap = new HashMap<>();
        if (Course.getCourses().size() == 0) {
            System.out.println("There aren't any courses available to add as passed courses!");
            Messages.pressEnter();
            return hashMap;
        }

        while( true ) {
            for ( Course course : Course.getCourses() ) {
                System.out.format("%d. name: %s, course number : %s\n", Course.getCourses().indexOf(course) + 1,
                        course.getName(), course.getCourseNumber());
            }

            System.out.format("%d. Finish setting the passed course' list\n", Course.getCourses().size()+1);
            System.out.println("Choose the course number to add as a passed course:");

            int option = Messages.checkInputs(scan, 1, Course.getCourses().size() + 1);
            if (option == Course.getCourses().size() + 1) {
                break;
            } else {
                System.out.println("Now enter the student's mark at this course:");
                int mark = Messages.checkInputs(scan, 10, 20);
                hashMap.put(Course.getCourses().get(option - 1), mark+"");
                System.out.println("Added successfully!");
                Messages.pressEnter();
            }
        }

        return hashMap;
    }

    public static void makeNeededCoursesAsPassed(HashMap<Course, String> hashMap, Scanner scan) {
        for ( Course course1 : hashMap.keySet() ) {
            for ( Course course2 : course1.getNeededCourses() ) {
                if (!hashMap.containsKey(course2)) {
                    System.out.format("course name : %s, course number : %s, course units : %s\n", course2.getName(),
                            course2.getCourseNumber(), course2.getUnits());
                    System.out.format("This course is a needed course for \"%s\" course!", course1.getName());
                    System.out.println(" Enter the needed course's mark:");
                    int mark = Messages.checkInputs(scan, 10, 20);
                    hashMap.put(course2, mark+"");
                    System.out.println("Added successfully!");
                }
            }
        }
    }


    public static void changeCourseGroupStudents(Scanner scan, CourseGroup courseGroup) {
        if (courseGroup.getStudents().size() == 0) {
            System.out.println("There are no students that have this course group!");
            Messages.pressEnter();
            CourseGroup.editCourseGroupInfo(scan, courseGroup);
        }

        System.out.println("Choose which student you wish to remove from this course group:");
        for ( Student student : courseGroup.getStudents() ) {
            System.out.format("%d. name: %s, family name: %s, master ID: %s", courseGroup.getStudents().indexOf(student)+1,
                    student.getName(), student.getFamilyName());
        }
        System.out.format("%d. Finish removing students from this course group.", courseGroup.getStudents().size()+1);

        while (true) {
            int option = Messages.checkInputs(scan, 1, courseGroup.getStudents().size()+1);
            if (option == courseGroup.getStudents().size()+1) {
                System.out.println("Changes applied successfully!");
                Messages.pressEnter();
                CourseGroup.editCourseGroupInfo(scan, courseGroup);
            } else {
                courseGroup.getStudents().remove(option-1);
                System.out.println("Removed successfully!");
                Messages.pressEnter();
                changeCourseGroupStudents(scan, courseGroup);
            }
        }
    }

    public static void showAllStudents(Scanner scan) {
        for ( Student student : students) {
            System.out.format("name: %s, family name : %s, ID : %s, units : %s, password : %s\n",
                    student.name, student.familyName, student.studentId, student.units, student.password);
            System.out.println("Course groups that this student already has:");
            for (CourseGroup courseGroup : student.courseGroups) {
                System.out.format("course number : %s, course group number : %s, capacity : %s, term : %s\n",
                        courseGroup.getCourseNumber(), courseGroup.getCourseGroupNumber(), courseGroup.getCapacity(),
                        courseGroup.getTermNumber());
            }
            System.out.println("---------------------------------------------------------");
            System.out.println("Courses that this student passed:");
            for ( Course course : Course.getCourses()) {
                if (student.passedCourses.containsKey(course)) {
                    System.out.format("course name : %s, course number : %s, mark : %s\n",
                            course.getName(), course.getCourseNumber(), student.passedCourses.get(course));
                }
            }
            System.out.println("******************************************************\n");
        }

        Messages.pressEnter();
        Admin.adminMenu(scan);

    }

    public static void chooseStudent(Scanner scan) {
        System.out.println("Enter the student ID to choose course groups for:");
        String studentId = scan.nextLine();

        for ( Student student : students ) {
            if (student.studentId.equals(studentId)) {
                setCourseGroupsForStudent(scan, student);
            }
        }

        System.out.println("No students match this ID! Please try again.");
        Messages.pressEnter();
        Admin.adminMenu(scan);

    }

    public static void setCourseGroupsForStudent(Scanner scan, Student student) {
        if (CourseGroup.getCourseGroups().size() == 0) {
            System.out.println("There are no course groups to choose from.");
            Messages.pressEnter();
            Admin.adminMenu(scan);
        }
        while (true) {
            System.out.println("Select the course group from the list below:");
            for (CourseGroup courseGroup : CourseGroup.getCourseGroups()) {
                System.out.format("%d. course number : %s, course group number : %s, capacity : %s, term : %s\n",
                        CourseGroup.getCourseGroups().indexOf(courseGroup) + 1, courseGroup.getCourseNumber(),
                        courseGroup.getCourseGroupNumber(), courseGroup.getCapacity(), courseGroup.getTermNumber());
            }
            System.out.format("%d. Finish setting the course groups for this student\n",
                    CourseGroup.getCourseGroups().size() + 1);
            int option = Messages.checkInputs(scan, 1, CourseGroup.getCourseGroups().size() + 1);
            if (option == CourseGroup.getCourseGroups().size()+1) {
                System.out.println("Course groups added successfully!");
                Messages.pressEnter();
                chooseStudent(scan);
            } else {
                CourseGroup courseGroup = CourseGroup.getCourseGroups().get(option-1);
                if (courseGroup.getCapacity().equals("0")) {
                    System.out.println("This course group is full!");
                    Messages.pressEnter();
                } else if (Integer.parseInt(student.units) < Integer.parseInt(courseGroup.getUnits())) {
                    System.out.println("This student doesn't have enough units to have this course group!");
                    Messages.pressEnter();
                } else if (hasOverlap(student, courseGroup)) {
                    System.out.println("This course group overlaps another class of this student!");
                    Messages.pressEnter();
                } else if (isAlreadyInTheList(student, courseGroup)) {
                    System.out.println("This course is already in the student's course group list!");
                    Messages.pressEnter();
                } else if (hasPassed(student, courseGroup)) {
                    System.out.println("This course has been passed by this student!");
                    Messages.pressEnter();
                } else {
                    student.getCourseGroups().add(courseGroup);
                    student.units = ""+(Integer.parseInt(student.units)-Integer.parseInt(courseGroup.getUnits()));
                    courseGroup.getStudents().add(student);
                    courseGroup.setCapacity(Integer.parseInt(courseGroup.getCapacity())-1+"");
                    System.out.println("The course group has been added to the student's course groups!");
                    Messages.pressEnter();
                    chooseStudent(scan);
                }
            }
        }
    }

    public static boolean hasOverlap(Student student, CourseGroup courseGroup) {
        for ( CourseGroup newCourseGroup : student.getCourseGroups() ) {
            for (Time time1 : newCourseGroup.getTimes()) {
                for (Time time2 : courseGroup.getTimes()) {
                    if (time1.isOverlap(time2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isAlreadyInTheList(Student student, CourseGroup courseGroup) {
        for ( CourseGroup newCourseGroup : student.courseGroups ) {
            if (newCourseGroup.getCourseNumber().equals(courseGroup.getCourseNumber())) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasPassed(Student student, CourseGroup courseGroup) {
        for ( Course course : student.getPassedCourses().keySet() ) {
            if ( course.getCourseNumber().equals(courseGroup.getCourseNumber())) {
                return true;
            }
        }
        return false;
    }

    public static void studentLogIn(Scanner scan) {
        System.out.println("Enter your ID");
        String username = scan.nextLine();
        System.out.println("Enter your password");
        String password = scan.nextLine();

        for ( Student student : students ) {
            if ( student.studentId.equals(username) && student.password.equals(password) ) {
                System.out.println("Log in successful!");
                Messages.pressEnter();
                studentMenu(scan, student);
            } else if (student.studentId.equals(username) && !student.password.equals(password)) {
                System.out.println("The password is wrong! Please try again.");
                Messages.pressEnter();
                Main.startingMenu(scan);
            }
        }

        System.out.println("This ID does not exist! Please try again.");
        Messages.pressEnter();
        Main.startingMenu(scan);
    }

    public static void studentMenu(Scanner scan, Student student) {
        Messages.masterAndStudentMenuMessages();
        int option = Messages.checkInputs(scan, 1, 2);
        switch (option) {
            case 1:
                checkAllCourseGroups(scan, student);
                break;
            default:
                Main.startingMenu(scan);
        }
    }

    public static void checkAllCourseGroups(Scanner scan, Student student) {
        for ( CourseGroup courseGroup : student.courseGroups ) {
            System.out.format("course number : %s, course group number : %s, capacity : %s, term : %s, units : %s\n",
                    courseGroup.getCourseNumber(), courseGroup.getCourseGroupNumber(), courseGroup.getCapacity(),
                    courseGroup.getTermNumber(), courseGroup.getUnits());
            System.out.println("Sessions in a week:");
            for ( Time time : courseGroup.getTimes() ) {
                System.out.println(time.showTime());
            }
        }

        Messages.pressEnter();
        studentMenu(scan, student);

    }

    public static void makeStudentExamples() {
        HashMap<Course, String> passedCourses1 = new HashMap<>();
        passedCourses1.put(Course.getCourses().get(0), "17");
        ArrayList<CourseGroup> courseGroups1 = new ArrayList<>();
        Student student1 = new Student("Mohammad", "Razavi", "4233894522", "20",
                passedCourses1, courseGroups1, "password1");

        students.add(student1);

        HashMap<Course, String> passedCourses2 = new HashMap<>();
        passedCourses2.put(Course.getCourses().get(0), "13");
        ArrayList<CourseGroup> courseGroups2 = new ArrayList<>();
        courseGroups2.add(CourseGroup.getCourseGroups().get(1));
        Student student2 = new Student("Mahdi", "Alavi", "4223904517", "14",
                passedCourses2, courseGroups2, "password2");

        students.add(student2);
    }

}