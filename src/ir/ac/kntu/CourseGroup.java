package ir.ac.kntu;

import java.util.*;

public class CourseGroup {

    private String courseGroupNumber;

    private String courseNumber;

    private ArrayList<Student> students = new ArrayList<>();

    private ArrayList<Time> times = new ArrayList<>();

    private String capacity;

    private ArrayList<Master> masters = new ArrayList<>();

    private String termNumber;

    private Course course;

    private String units;

    private static ArrayList<CourseGroup> courseGroups = new ArrayList<>();

    public CourseGroup(String courseGroupNumber, Course course, ArrayList<Student> students, ArrayList<Time> times,
                       String capacity, ArrayList<Master> masters, String termNumber) {
        setCourseGroupNumber(courseGroupNumber);
        setCourse(course);
        setCourseNumber(course.getCourseNumber());
        setStudents(students);
        setTimes(times);
        setCapacity(capacity);
        setMasters(masters);
        setTermNumber(termNumber);
        setUnits(course.getUnits());
    }

    public String getCourseGroupNumber() {
        return courseGroupNumber;
    }

    public void setCourseGroupNumber(String courseGroupNumber) {
        this.courseGroupNumber = courseGroupNumber;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public ArrayList<Time> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<Time> times) {
        this.times = times;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        if (Integer.parseInt(capacity) <= 0) {
            this.capacity = "0";
        } else if (Integer.parseInt(capacity) > 70) {
            this.capacity = "70";
        } else {
            this.capacity = capacity;
        }
    }

    public ArrayList<Master> getMasters() {
        return masters;
    }

    public void setMasters(ArrayList<Master> masters) {
        this.masters = masters;
    }

    public String getTermNumber() {
        return termNumber;
    }

    public void setTermNumber(String termNumber) {
        if (Integer.parseInt(termNumber) <= 0) {
            this.termNumber = "0";
        } else {
            this.termNumber = termNumber;
        }
    }

    public static ArrayList<CourseGroup> getCourseGroups() {
        return courseGroups;
    }

    public void setCourseGroups(ArrayList<CourseGroup> courseGroups) {
        this.courseGroups = courseGroups;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public static void submitCourseGroup(Scanner scan) {
        System.out.println("Select the course:");
        Course course = selectCourse(scan);
        System.out.println("Enter the course group's number:");
        String courseGroupNumber = scan.nextLine();
        if (repeatedCourse(course.getCourseNumber(), courseGroupNumber)) {
            System.out.println("This course group is already defined! Please try again.");
            Messages.pressEnter();
            submitCourseGroup(scan);
        }

        ArrayList<Student> students = new ArrayList<>();
        ArrayList<Master> masters = new ArrayList<>();
        ArrayList<Time> times = makeTimes(scan);

        System.out.println("Enter the course group capacity: ");
        String capacity = scan.nextLine();

        System.out.println("Enter the term number: ");
        String termNumber = scan.nextLine();

        CourseGroup courseGroup = new CourseGroup(courseGroupNumber, course, students, times, capacity, masters,
                termNumber);
        courseGroups.add(courseGroup);
        System.out.println("Course group submitted successfully!");
        Messages.pressEnter();
        Admin.adminMenu(scan);
    }

    public static Course selectCourse(Scanner scan) {
        if (Course.getCourses().size() == 0) {
            System.out.println("There are no courses to choose from! You can submit courses from the admin menu.");
            Messages.pressEnter();
            Admin.adminMenu(scan);
        }

        for (Course course : Course.getCourses()) {
            System.out.format("%d. %s\n", Course.getCourses().indexOf((Object) course) + 1, course.toString());
        }

        System.out.println("Choose the course from the list above:");
        int option = Integer.parseInt(scan.nextLine());
        return Course.getCourses().get(option - 1);
    }

    public static boolean repeatedCourse(String courseNumber, String courseGroupNumber) {
        for ( CourseGroup courseGroup : courseGroups ) {
            if (courseGroup.getCourseNumber().equals(courseNumber)
                    && courseGroup.getCourseGroupNumber().equals(courseGroupNumber)) {
                return true;
            }
        }

        return false;
    }

    public static ArrayList<Time> makeTimes(Scanner scan) {
        ArrayList<Time> times = new ArrayList<>();
        System.out.println("Enter the number of sessions:");
        int sessions = Integer.parseInt(scan.nextLine());

        for ( int i = 0; i < sessions; i++ ) {
            System.out.println("Enter the time parameters for session " + ( i + 1 ) + " of week");
            Messages.pressEnter();
            Time time = Time.makeTime(scan);
            if (makesOverlap(times, time)) {
                System.out.println("This time overlaps another session of this course group!");
                i--;
            } else {
                times.add(time);
            }
        }

        return times;
    }

    public static boolean makesOverlap(ArrayList<Time> times, Time time) {
        for ( Time variable : times ) {
            if (variable.isOverlap(time)) {
                return true;
            }
        }
        return false;
    }

    public static void chooseCourseGroupToEdit(int number, Scanner scan) {
        if (courseGroups.size() == 0) {
            System.out.println("There are no course groups to choose from!");
            Messages.pressEnter();
            Admin.adminMenu(scan);
        }
        System.out.println("Choose the course group:");
        for ( CourseGroup courseGroup : courseGroups ) {
            System.out.format("%d. course name: %s, course number: %s, course group's number: %s\n",
                    courseGroups.indexOf((Object) courseGroup) + 1, courseGroup.course.getName(),
                    courseGroup.courseNumber, courseGroup.courseGroupNumber );
        }
        System.out.format("%d. Finish editing the courses\n", courseGroups.size()+1);
        int option = Messages.checkInputs(scan, 1, courseGroups.size()+1);
        if (option == courseGroups.size()+1) {
            System.out.println("Changes applied successfully!");
            Messages.pressEnter();
            Admin.adminMenu(scan);
        } else {
            if (number == 1) {
                editCourseGroupInfo(scan, courseGroups.get(option - 1));
            } else {
                deleteCourseGroup(scan, courseGroups.get(option - 1));
            }
        }
    }

    public static void editCourseGroupInfo(Scanner scan, CourseGroup courseGroup) {
        System.out.println("Select the factor you wish to apply changes:");
        Messages.editCourseGroupMenu();
        int option = Messages.checkInputs(scan, 1, 8);
        switch (option) {
            case 1:
                changeCourseNumber(scan, courseGroup);
                break;
            case 2:
                changeCourseGroupNumber(scan, courseGroup);
                break;
            case 3:
                changeCourseGroupCapacity(scan, courseGroup);
                break;
            case 4:
                changeCourseGroupTermNumber(scan, courseGroup);
                break;
            case 5:
                Master.changeCourseGroupMasters(scan, courseGroup);
                break;
            case 6:
                Student.changeCourseGroupStudents(scan, courseGroup);
                break;
            case 7:
                changeCourseGroupSessions(scan, courseGroup);
                break;
            default:
                System.out.println("Changes applied successfully!");
                Messages.pressEnter();
                chooseCourseGroupToEdit(1, scan);

        }
    }

    public static void changeCourseNumber(Scanner scan, CourseGroup courseGroup) {
        System.out.println("Select the course you wish to put instead of the current course:");
        Course course = selectCourse(scan);
        if (repeatedCourse(course.getCourseNumber(), courseGroup.courseGroupNumber)) {
            System.out.println("This course group is already defined! Please try again.");
            Messages.pressEnter();
            changeCourseNumber(scan, courseGroup);
        }

        courseGroup.setCourse(course);
        courseGroup.setCourseNumber(course.getCourseNumber());
        courseGroup.setUnits(course.getUnits());
        System.out.println("Course and course number changed successfully!");
        Messages.pressEnter();
        editCourseGroupInfo(scan, courseGroup);
    }

    public static void changeCourseGroupNumber(Scanner scan, CourseGroup courseGroup) {
        System.out.println("Enter the new course group number:");
        String courseGroupNumber = scan.nextLine();
        if (repeatedCourse(courseGroup.courseNumber, courseGroupNumber)) {
            System.out.println("This course group is already defined! Please try again.");
            Messages.pressEnter();
            changeCourseGroupNumber(scan, courseGroup);
        }

        courseGroup.courseGroupNumber = courseGroupNumber;
        System.out.println("Course group number changed successfully!");
        Messages.pressEnter();
        editCourseGroupInfo(scan, courseGroup);
    }

    public static void changeCourseGroupCapacity(Scanner scan, CourseGroup courseGroup) {
        System.out.println("Enter the new course group capacity:");
        String capacity = scan.nextLine();
        courseGroup.capacity = capacity;
        System.out.println("Course group capacity changed successfully!");
        Messages.pressEnter();
        editCourseGroupInfo(scan, courseGroup);
    }

    public static void changeCourseGroupTermNumber(Scanner scan, CourseGroup courseGroup) {
        System.out.println("Enter the new course group term number:");
        String termNumber = scan.nextLine();
        courseGroup.termNumber = termNumber;
        System.out.println("Course group term number changed successfully!");
        Messages.pressEnter();
        editCourseGroupInfo(scan, courseGroup);
    }

    public static void changeCourseGroupSessions(Scanner scan, CourseGroup courseGroup) {
        System.out.println("Enter the new sessions timetable: ");
        ArrayList<Time> times = makeTimes(scan);
        courseGroup.times = times;
        System.out.println("Course group session times changed successfully!");
        Messages.pressEnter();
        editCourseGroupInfo(scan, courseGroup);
    }

    public static void deleteCourseGroup(Scanner scan, CourseGroup courseGroup) {
        for ( CourseGroup newCourseGroup : courseGroups) {
            if (newCourseGroup.equals(courseGroup)) {
                courseGroups.remove(newCourseGroup);
                deleteCourseGroupForMasters(newCourseGroup);
                deleteCourseGroupForStudents(newCourseGroup);
                break;
            }
        }
        System.out.println("Course group deleted successfully!");
        Messages.pressEnter();
        Admin.adminMenu(scan);
    }

    public static void deleteCourseGroupForMasters(CourseGroup courseGroup) {
        for ( Master master : Master.getMasters() ) {
            for (CourseGroup newCourseGroup : master.getCourseGroups()) {
                if (newCourseGroup.equals(courseGroup)) {
                    master.getCourseGroups().remove(courseGroup);
                    break;
                }
            }
        }
    }

    public static void deleteCourseGroupForStudents(CourseGroup courseGroup) {
        for ( Student student : Student.getStudents() ) {
            for (CourseGroup newCourseGroup : student.getCourseGroups()) {
                if (newCourseGroup.equals(courseGroup)) {
                    student.getCourseGroups().remove(courseGroup);
                    break;
                }
            }
        }
    }

    public static void showAllCourseGroups(Scanner scan) {
        for (CourseGroup courseGroup : courseGroups) {
            System.out.format("course number : %s, course name : %s, course group number : %s, capacity : %s" +
                            ", term : %s, units : %s\n", courseGroup.courseNumber, courseGroup.getCourse().getName(),
                    courseGroup.courseGroupNumber, courseGroup.capacity, courseGroup.termNumber, courseGroup.units);

            if (courseGroup.getMasters().size() != 0) {
                System.out.format("master ID : %s, master name : %s, master family name : %s\n",
                        courseGroup.getMasters().get(0).getMasterId(), courseGroup.getMasters().get(0).getName(),
                        courseGroup.getMasters().get(0).getFamilyName());
            }

            System.out.println("Sessions in a week:");
            for ( Time time : courseGroup.times ) {
                System.out.println(time.showTime());
            }
            System.out.println("**********************************************");
        }

        Messages.pressEnter();
        Admin.adminMenu(scan);

    }

    public static void makeCourseGroupExamples() {
        ArrayList<Student> students = new ArrayList<>();
        ArrayList<Master> masters = new ArrayList<>();
        ArrayList<Time> times1 = new ArrayList<>();
        Time time1 = new Time("1", "7", "30");
        Time time2 = new Time("3", "12", "45");
        Time time3 = new Time("5", "15", "00");
        times1.add(time1);
        times1.add(time2);
        times1.add(time3);
        CourseGroup courseGroup1 = new CourseGroup("1000", Course.getCourses().get(0),students, times1,
                "40", masters, "1");
        courseGroups.add(courseGroup1);

        ArrayList<Time> times2 = new ArrayList<>();
        Time time4 = new Time("1", "10", "40");
        Time time5 = new Time("3", "13", "20");
        Time time6 = new Time("5", "17", "00");
        times2.add(time4);
        times2.add(time5);
        times2.add(time6);
        CourseGroup courseGroup2 = new CourseGroup("2000", Course.getCourses().get(1),students, times2,
                "25", masters, "1");
        courseGroups.add(courseGroup2);

        ArrayList<Time> times3 = new ArrayList<>();
        Time time7 = new Time("1", "15", "00");
        Time time8 = new Time("5", "8", "00");
        times3.add(time7);
        times3.add(time8);
        CourseGroup courseGroup3 = new CourseGroup("3000", Course.getCourses().get(2),students, times3,
                "70", masters, "1");
        courseGroups.add(courseGroup3);

    }

}