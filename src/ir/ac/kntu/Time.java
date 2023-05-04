package ir.ac.kntu;

import java.util.Scanner;

public class Time {

    private String dayOfWeek;

    private String hour;

    private String minute;

    public Time(String dayOfWeek, String hour, String minute) {
        setDayOfWeek(dayOfWeek);
        setHour(hour);
        setMinute(minute);
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        switch (dayOfWeek) {
            case "2":
                this.dayOfWeek = "Sunday";
                break;
            case "3":
                this.dayOfWeek = "Monday";
                break;
            case "4":
                this.dayOfWeek = "Tuesday";
                break;
            case "5":
                this.dayOfWeek = "Wednesday";
                break;
            default :
                this.dayOfWeek = "Saturday";
        }
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        if ( Integer.parseInt(hour) >= 15 ) {
            this.hour = "15";
        } else if ( Integer.parseInt(hour) <= 7 ) {
            this.hour = "7";
        } else {
            this.hour = hour;
        }
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {

        int minuteToInt = Integer.parseInt(minute);
        int hourToInt = Integer.parseInt(hour);

        if ( minuteToInt <= 7  ) {
            this.minute = "00";
        } else if (minuteToInt > 52) {
            this.minute = "00";
            this.hour = (hourToInt + 1)+"";
        } else if ( minuteToInt > 7 && minuteToInt <= 22 ) {
            this.minute = "15";
        } else if ( minuteToInt > 22 && minuteToInt <= 37 ) {
            this.minute = "30";
        } else if ( minuteToInt > 37 && minuteToInt <= 52 ) {
            this.minute = "45";
        }
    }

    public String startTime() {
        return hour+":"+minute;
    }

    public String endTime() {

        int hourToInt = Integer.parseInt(hour);

        switch (minute) {
            case "00":
                return ( hourToInt + 1 ) + ":30";
            case "15":
                return ( hourToInt + 1 ) + ":45";
            case "30":
                return ( hourToInt + 2 ) + ":00";
            default :
                return ( hourToInt + 2 ) + ":15";
        }
    }

    public String showTime() {
        return dayOfWeek+"s    "+startTime()+" - "+endTime();
    }

    public static Time makeTime(Scanner scan) {
        System.out.println("Enter the day of week:\n1-Saturday\n2-Sunday\n3-Monday\n4-Tuesday\n5-Wednesday");
        String dayOfWeek = scan.nextLine();
        System.out.println("Enter the start time hour:");
        String startTimeHour = scan.nextLine();
        System.out.println("Enter the start time minute:");
        String startTimeMinute= scan.nextLine();
        return new Time(dayOfWeek, startTimeHour, startTimeMinute);
    }

    public boolean isOverlap(Time time) {
        if ( !dayOfWeek.equals(time.dayOfWeek) ) {
            return false;
        }

        int startHour1 = Integer.parseInt(hour);
        int startMinute1 = Integer.parseInt(minute);
        int endHour1 = Integer.parseInt(endTime().substring(0, endTime().indexOf(":")));
        int endMinute1 = Integer.parseInt(endTime().substring(endTime().indexOf(":")+1));
        int startHour2 = Integer.parseInt(time.hour);
        int startMinute2 = Integer.parseInt(time.minute);
        int endHour2 = Integer.parseInt(time.endTime().substring(0, time.endTime().indexOf(":")));
        int endMinute2 = Integer.parseInt(time.endTime().substring(time.endTime().indexOf(":")+1));

        int value1 = startHour1*1000+startMinute1;
        int value2 = endHour1*1000+endMinute1;
        int value3 = startHour2*1000+startMinute2;
        int value4 = endHour2*1000+endMinute2;

        if ( (value3 >= value1 && value3 < value2) || (value1 >= value3 && value1 < value4)) {
            return true;
        }

        return false;

    }
}