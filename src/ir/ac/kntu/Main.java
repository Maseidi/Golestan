package ir.ac.kntu;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to the system!");
        Scanner scan = new Scanner(System.in);
        Course.makeCourseExamples();
        CourseGroup.makeCourseGroupExamples();
        Student.makeStudentExamples();
        Master.makeMasterExamples();
        startingMenu(scan);
    }

    public static void startingMenu(Scanner scan) {
        Messages.firstMessage();
        int option = Messages.checkInputs(scan, 1, 5);
        switch(option) {
            case 1:
                Admin.submitAdmin(scan);
                break;
            case 2:
                Admin.adminLogIn(scan);
                break;
            case 3:
                Master.masterLogIn(scan);
                break;
            case 4:
                Student.studentLogIn(scan);
                break;
            default:
                scan.close();
                System.exit(1);
        }
    }

}