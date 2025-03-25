package com.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

public class ReminderApp {
    public static void main(String[] args) {
        System.out.println("Starting Payment Reminder App");

        final Dotenv dotenv = Dotenv.load();
        final String recipientEmail = dotenv.get("RECIPIENT_EMAIL");
        // Get reminder days from environment variable with default fallback to 3
        int reminderDays;
        try {
            reminderDays = Integer.parseInt(dotenv.get("REMINDER_DAYS_AHEAD"));
        } catch (NumberFormatException e) {
            System.out.println("Warning: REMINDER_DAYS_AHEAD not set or invalid. Using default value of 3.");
            reminderDays = 3;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File("data.json");
            List<Payment> paymentList = Arrays.asList(mapper.readValue(file, Payment[].class));
            System.out.println("Loaded payments: " + paymentList);

            // Initialize Email Service
            EmailService emailService = new EmailService();

            // Process payments and send reminders based on due date
            sendDueDateReminders(paymentList, emailService, recipientEmail, reminderDays);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendDueDateReminders(List<Payment> payments, EmailService emailService,
            String recipientEmail, int reminderDays) {
        LocalDate today = LocalDate.now();
        YearMonth currentYearMonth = YearMonth.now();

        // Check each payment
        for (Payment payment : payments) {
            try {
                // Parse the due date
                int dayOfMonth = Integer.parseInt(payment.monthlyDueDate());

                // Calculate the due date for current month, handling months with fewer days
                LocalDate dueDate;

                if (dayOfMonth > currentYearMonth.lengthOfMonth()) {
                    // If the day doesn't exist in this month (e.g., 30th in Feb), use last day of
                    // month
                    dueDate = currentYearMonth.atEndOfMonth();
                } else {
                    dueDate = currentYearMonth.atDay(dayOfMonth);
                }

                // If due date has already passed this month, check next month's due date
                if (dueDate.isBefore(today)) {
                    YearMonth nextMonth = currentYearMonth.plusMonths(1);

                    // Again handle cases where the day might not exist in next month
                    if (dayOfMonth > nextMonth.lengthOfMonth()) {
                        dueDate = nextMonth.atEndOfMonth();
                    } else {
                        dueDate = nextMonth.atDay(dayOfMonth);
                    }
                }

                // Calculate days until due date
                long daysLeft = ChronoUnit.DAYS.between(today, dueDate);

                // Send reminder if due date is within the mentioned reminder days
                if (daysLeft >= 0 && daysLeft <= reminderDays) {
                    String subject = "Payment Reminder: " + payment.billingAccount() + " due in " + daysLeft
                            + " day(s)";

                    String content = "Hello, \n\nThis is a reminder that your payment for " + payment.billingAccount()
                            +
                            " is due on " + dueDate.toString() + " (" + daysLeft + " day(s) left).\n\n" +
                            "Please make your payment to avoid any late fees.\n\n" +
                            "Thank you,\nPayment Reminder System";

                    emailService.sendEmail(recipientEmail, subject, content);

                    System.out.println(
                            "Sent reminder for: " + payment.billingAccount() + " - Due in " + daysLeft + " days");
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid due date format for payment: " + payment.billingAccount());
            }
        }
    }
}