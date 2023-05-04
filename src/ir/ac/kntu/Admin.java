package ir.ac.kntu;

import java.util.*;

public class Admin {

    private static ArrayList<Admin> admins = new ArrayList<>();

    private String userName;

    private String password;

    public Admin(String userName, String password) {
        setUserName(userName);
        setPassword(password);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static void submitAdmin(Scanner scan) {
        System.out.println("Enter the admin username:");
        String username = scan.nextLine();
        for ( Admin admin : admins ) {
            if (admin.userName.equals(username)) {
                System.out.println("This username is already taken! Please try again");
                submitAdmin(scan);
            }
        }

        System.out.println("Enter the admin password");
        String password = scan.nextLine();

        Admin admin = new Admin(username, password);
        admins.add(admin);

        System.out.println("Admin submitted successfully!");
        Messages.pressEnter();
        Main.startingMenu(scan);
    }

    public static void adminLogIn(Scanner scan) {
        System.out.println("Enter your username");
        String username = scan.nextLine();
        System.out.println("Enter your password");
        String password = scan.nextLine();

        for ( Admin admin : admins ) {
            if ( admin.getUserName().equals(username) && admin.getPassword().equals(password) ) {
                System.out.println("Log in successful!");
                Messages.pressEnter();
                adminMenu(scan);
            } else if (admin.getUserName().equals(username) && !admin.getPassword().equals(password)) {
                System.out.println("The password is wrong! Please try again.");
                Messages.pressEnter();
                Main.startingMenu(scan);
            }
        }

        System.out.println("This username does not exist! Please try again.");
        Messages.pressEnter();
        Main.startingMenu(scan);

    }

    public static void adminMenu(Scanner scan) {
        Messages.adminMenuMessages();
        int option = Messages.checkInputs(scan, 1, 13);
        switch (option) {
            case 1:
                Student.submitStudent(scan);
                break;
            case 2:
                Master.submitMaster(scan);
                break;
            case 3:
                Course.submitCourse(scan);
                break;
            case 4:
                CourseGroup.submitCourseGroup(scan);
                break;
            case 5:
                CourseGroup.chooseCourseGroupToEdit(1,scan);
                break;
            case 6:
                CourseGroup.chooseCourseGroupToEdit(2,scan);
                break;
            case 7:
                Student.chooseStudent(scan);
                break;
            case 8:
                Master.chooseMaster(scan);
                break;
            case 9:
                Student.showAllStudents(scan);
                break;
            case 10:
                Course.showAllCourses(scan);
                break;
            case 11:
                Master.showAllMasters(scan);
                break;
            case 12:
                CourseGroup.showAllCourseGroups(scan);
                break;
            default:
                Main.startingMenu(scan);
        }
    }

}