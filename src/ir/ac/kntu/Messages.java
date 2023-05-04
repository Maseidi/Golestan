package ir.ac.kntu;

import java.util.Scanner;

public class Messages {

    public static void firstMessage() {
        System.out.println("Please choose your option:");
        System.out.println("1-Submit a new admin");
        System.out.println("2-Enter as admin");
        System.out.println("3-Enter as a master");
        System.out.println("4-Enter as a student");
        System.out.println("5-Exit system");
    }

    public static int checkInputs(Scanner scan, int start, int end) {
        String input = scan.nextLine();
        while (Integer.parseInt(input) > end || Integer.parseInt(input) < start) {
            System.out.println("Invalid input! Please try again");
            input = scan.nextLine();
        }
        return Integer.parseInt(input);
    }

    public static void adminMenuMessages() {
        System.out.println("Choose your option:");
        System.out.println("1-Submit a new student");
        System.out.println("2-Submit a new master");
        System.out.println("3-Submit a new course");
        System.out.println("4-Submit a new course group");
        System.out.println("5-Change a course group's information");
        System.out.println("6-Delete a course group");
        System.out.println("7-Setting courses for students");
        System.out.println("8-Setting courses for masters");
        System.out.println("9-Show all students' list");
        System.out.println("10-Show all courses' list");
        System.out.println("11-Show all masters' list");
        System.out.println("12-Show all course groups' list");
        System.out.println("13-Back");
    }

    public static void pressEnter() {
        System.out.println("Press enter to continue...");
        try {
            System.in.read();
        } catch ( Exception ex ) {}
    }

    public static void editCourseGroupMenu() {
        System.out.println("1-Course");
        System.out.println("2-Course group number");
        System.out.println("3-Course group capacity");
        System.out.println("4-Course group term number");
        System.out.println("5-Course group masters' list");
        System.out.println("6-Course group students' list");
        System.out.println("7-Course group sessions");
        System.out.println("8-Exit");
        System.out.print("NOTE: You can add students and masters to this course group in the admin menu options, ");
        System.out.println("right now you can delete a specific master or student from this course group.");
    }

    public static void masterAndStudentMenuMessages() {
        System.out.println("Choose your option:");
        System.out.println("1-Check all of your course groups");
        System.out.println("2-Exit");
    }

}