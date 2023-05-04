package ir.ac.kntu;

import java.util.*;

public class Master {

    private String name;

    private String familyName;

    private String masterId;

    private String password;

    private ArrayList<CourseGroup> courseGroups = new ArrayList<>();

    private static ArrayList<Master> masters = new ArrayList<>();

    public Master(String name, String familyName, String masterId, String password, ArrayList<CourseGroup> courseGroups) {
        setName(name);
        setFamilyName(familyName);
        setMasterId(masterId);
        setPassword(password);
        setCourseGroups(courseGroups);
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

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<CourseGroup> getCourseGroups() {
        return courseGroups;
    }

    public void setCourseGroups(ArrayList<CourseGroup> courseGroups) {
        this.courseGroups = courseGroups;
    }

    public static ArrayList<Master> getMasters() {
        return masters;
    }

    public static void setMasters(ArrayList<Master> masters) {
        Master.masters = masters;
    }

    public static void submitMaster(Scanner scan) {
        System.out.println("Enter master's name:");
        String name = scan.nextLine();
        while (!name.matches("[A-Z][a-z]*")) {
            System.out.println("Name shall be like \"Ali\". Please try again.");
            name = scan.nextLine();
        }
        System.out.println("Enter master's family name:");
        String familyName = scan.nextLine();
        while (!familyName.matches("[A-Z][a-z]*")) {
            System.out.println("Family name shall be like \"Karimi\". Please try again.");
            familyName = scan.nextLine();
        }
        System.out.println("Enter master's ID:");
        String masterId = scan.nextLine();
        while (!masterId.matches("\\d{10}")) {
            System.out.println("ID must be a 10 digits number. Please try again:");
            masterId = scan.nextLine();
        }
        for ( Master master : masters ) {
            if (master.getMasterId().equals(masterId)) {
                System.out.println("This ID is already defined! Please try again.");
                Messages.pressEnter();
                submitMaster(scan);
            }
        }

        System.out.println("Enter master's password:");
        String password = scan.nextLine();

        ArrayList<CourseGroup> courseGroups = new ArrayList<>();
        Master master = new Master(name, familyName, masterId, password, courseGroups);
        masters.add(master);
        System.out.println("Master submitted successfully!");
        Messages.pressEnter();
        Admin.adminMenu(scan);
    }

    public static void changeCourseGroupMasters(Scanner scan, CourseGroup courseGroup) {
        if (courseGroup.getMasters().size() == 0) {
            System.out.println("There are no masters that have this course group!");
            Messages.pressEnter();
            CourseGroup.editCourseGroupInfo(scan, courseGroup);
        }

        System.out.println("Choose which master you wish to remove from this course group:");
        for ( Master master : courseGroup.getMasters() ) {
            System.out.format("%d. name: %s, family name: %s, master ID: %s\n", courseGroup.getMasters().indexOf(master)+1,
                    master.getName(), master.getFamilyName(), master.masterId);
        }
        System.out.format("%d. Finish removing masters from this course group.\n", courseGroup.getMasters().size()+1);

        while (true) {
            int option = Messages.checkInputs(scan, 1, courseGroup.getMasters().size()+1);
            if (option == courseGroup.getMasters().size()+1) {
                System.out.println("Changes applied successfully!");
                Messages.pressEnter();
                CourseGroup.editCourseGroupInfo(scan, courseGroup);
            } else {
                courseGroup.getMasters().remove(option-1);
                System.out.println("Removed successfully!");
                Messages.pressEnter();
                changeCourseGroupMasters(scan, courseGroup);
            }
        }
    }

    public static void showAllMasters(Scanner scan) {
        for ( Master master : masters ) {
            System.out.format("name : %s, family name : %s, ID : %s, password : %s\n",
                    master.name, master.familyName, master.masterId, master.password);
            System.out.println("Course groups that this master already has:");
            for ( CourseGroup courseGroup : master.courseGroups ) {
                System.out.format("course number : %s, course group number : %s, capacity : %s, term : %s\n",
                        courseGroup.getCourseNumber(), courseGroup.getCourseGroupNumber(), courseGroup.getCapacity(),
                        courseGroup.getTermNumber());
            }
            System.out.println("******************************************************");
        }

        Messages.pressEnter();
        Admin.adminMenu(scan);

    }

    public static void chooseMaster(Scanner scan) {
        System.out.println("Enter the master ID to choose course groups for");
        String masterId = scan.nextLine();

        for ( Master master : masters ) {
            if (master.masterId.equals(masterId)) {
                setCoursesForMasters(scan, master);
            }
        }

        System.out.println("No masters match this ID! Please try again.");
        Messages.pressEnter();
        Admin.adminMenu(scan);
    }

    public static void setCoursesForMasters(Scanner scan, Master master) {
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

            System.out.format("%d. Finish setting the course groups for this master\n",
                    CourseGroup.getCourseGroups().size() + 1);
            int option = Messages.checkInputs(scan, 1, CourseGroup.getCourseGroups().size() + 1);
            if (option == CourseGroup.getCourseGroups().size()+1) {
                System.out.println("Course groups added successfully!");
                Messages.pressEnter();
                chooseMaster(scan);
            } else {
                CourseGroup courseGroup = CourseGroup.getCourseGroups().get(option-1);
                if (courseGroup.getMasters().size() == 1) {
                    System.out.println("Each course group can only have a master!");
                    Messages.pressEnter();
                }  else if (hasOverlap(master, courseGroup)) {
                    System.out.println("This course group overlaps another class of this master!");
                    Messages.pressEnter();
                }  else {
                    master.getCourseGroups().add(courseGroup);
                    courseGroup.getMasters().add(master);
                    System.out.println("The course group has been added to the master's course groups!");
                    Messages.pressEnter();
                    chooseMaster(scan);
                }
            }
        }

    }

    public static boolean hasOverlap(Master master, CourseGroup courseGroup) {
        for ( CourseGroup newCourseGroup : master.getCourseGroups() ) {
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

    public static void masterLogIn(Scanner scan) {
        System.out.println("Enter your ID");
        String username = scan.nextLine();
        System.out.println("Enter your password");
        String password = scan.nextLine();

        for ( Master master : masters ) {
            if ( master.masterId.equals(username) && master.password.equals(password) ) {
                System.out.println("Log in successful!");
                Messages.pressEnter();
                masterMenu(scan, master);
            } else if (master.masterId.equals(username) && !master.password.equals(password)) {
                System.out.println("The password is wrong! Please try again.");
                Messages.pressEnter();
                Main.startingMenu(scan);
            }
        }

        System.out.println("This ID does not exist! Please try again.");
        Messages.pressEnter();
        Main.startingMenu(scan);
    }

    public static void masterMenu(Scanner scan, Master master) {
        Messages.masterAndStudentMenuMessages();
        int option = Messages.checkInputs(scan, 1, 2);
        switch (option) {
            case 1:
                checkAllCourseGroups(scan, master);
                break;
            default:
                Main.startingMenu(scan);
        }
    }

    public static void checkAllCourseGroups(Scanner scan, Master master) {
        for ( CourseGroup courseGroup : master.courseGroups ) {
            System.out.format("course number : %s, course group number : %s, capacity : %s, term : %s, units : %s\n",
                    courseGroup.getCourseNumber(), courseGroup.getCourseGroupNumber(), courseGroup.getCapacity(),
                    courseGroup.getTermNumber(), courseGroup.getUnits());
            System.out.println("Sessions in a week:");
            for ( Time time : courseGroup.getTimes() ) {
                System.out.println(time.showTime());
            }
        }

        Messages.pressEnter();
        masterMenu(scan, master);

    }

    public static void makeMasterExamples() {
        ArrayList<CourseGroup> courseGroups1 = new ArrayList<>();
        courseGroups1.add(CourseGroup.getCourseGroups().get(0));
        Master master1 = new Master("Reza", "Karimi", "1234545320", "password1", courseGroups1);
        ArrayList<Master> masters1 = new ArrayList<>();
        masters1.add(master1);
        CourseGroup.getCourseGroups().get(0).setMasters(masters1);
        masters.add(master1);

        ArrayList<CourseGroup> courseGroups2 = new ArrayList<>();
        courseGroups2.add(CourseGroup.getCourseGroups().get(1));
        Master master2 = new Master("Ali", "Taghavi", "1224555319", "password2", courseGroups2);
        ArrayList<Master> masters2 = new ArrayList<>();
        masters2.add(master2);
        CourseGroup.getCourseGroups().get(1).setMasters(masters2);
        masters.add(master2);
    }

}
