package com.example.AgeoMeter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

@SpringBootApplication
public class AgeOMeterApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgeOMeterApplication.class, args);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your birthdate (MM/dd/yyyy): ");
        String userInput = scanner.next();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date birthDate = dateFormat.parse(userInput);
            Date currentDate = new Date();

            AgeInfo ageInfo = calculateAge(birthDate, currentDate);

            if (ageInfo != null) {
                System.out.println("Exact age: " + ageInfo.getYears() + " years, " + ageInfo.getMonths() + " months, "
                        + ageInfo.getDays() + " days.");

                System.out.println("\nAdditional Information:");
                System.out.println("Total number of hours: " + ageInfo.getTotalHours());
                System.out.println("Total number of days: " + ageInfo.getTotalDays());
                System.out.println("Total number of weeks: " + ageInfo.getTotalWeeks());
                System.out.println("Total number of months: " + ageInfo.getTotalMonths());
                System.out.println("Total number of years: " + ageInfo.getTotalYears());
            } else {
                System.out.println("Invalid birthdate. Please enter a valid date.");
            }

        } catch (ParseException e) {
            System.out.println("Invalid date format. Please enter the date in the format MM/dd/yyyy.");
        } finally {
            // Close the scanner
            scanner.close();
        }
    }

    private static AgeInfo calculateAge(Date birthDate, Date currentDate) {
        if (birthDate == null || currentDate == null || currentDate.before(birthDate)) {
            return null;
        }

        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(birthDate);

        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(currentDate);

        int years = currentCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);
        int months = currentCalendar.get(Calendar.MONTH) - birthCalendar.get(Calendar.MONTH);
        int days = currentCalendar.get(Calendar.DAY_OF_MONTH) - birthCalendar.get(Calendar.DAY_OF_MONTH);

        if (days < 0) {
            // Borrow a month
            int lastMonthMaxDay = birthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            days += lastMonthMaxDay;
            months--;
        }

        if (months < 0) {
            // Borrow a year
            months += 12;
            years--;
        }

        long totalDays = daysBetween(birthCalendar, currentCalendar);

        return new AgeInfo(years, months, days, totalDays);
    }

    private static long daysBetween(Calendar startDate, Calendar endDate) {
        long startMillis = startDate.getTimeInMillis();
        long endMillis = endDate.getTimeInMillis();
        return (endMillis - startMillis) / (24 * 60 * 60 * 1000);
    }

    private static class AgeInfo {
        private final int years;
        private final int months;
        private final int days;
        private final long totalDays;

        public AgeInfo(int years, int months, int days, long totalDays) {
            this.years = years;
            this.months = months;
            this.days = days;
            this.totalDays = totalDays;
        }

        public int getYears() {
            return years;
        }

        public int getMonths() {
            return months;
        }

        public int getDays() {
            return days;
        }

        public long getTotalDays() {
            return totalDays;
        }

        public long getTotalHours() {
            return totalDays * 24;
        }

        public long getTotalWeeks() {
            return totalDays / 7;
        }

        public long getTotalMonths() {
            int totalMonths = years * 12 + months;
            if (days > 0) {
                totalMonths++;
            }
            return totalMonths;
        }

        public long getTotalYears() {
            return totalDays / 365;
        }
    }
}
