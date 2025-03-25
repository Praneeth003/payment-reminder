# Payment Reminder System

A simple Java application that sends email reminders for upcoming bill payments based on monthly due dates.

## Features

- Reads payment information from a JSON configuration file
- Sends email reminders for payments due within a configurable timeframe
- Automatically handles different month lengths for consistent reminders
- Can be run manually or scheduled via GitHub Actions

## Technologies Used

- Java 21
- Maven
- Jackson for JSON parsing
- Jakarta Mail API for email functionality
- dotenv-java for environment variable management
- GitHub Actions for scheduling

## Prerequisites

- Java 21 or higher
- Maven
- SMTP email account credentials
- Git (optional, for cloning the repository)

## Installation

1. Clone the repository:

```bash
git clone https://github.com/yourusername/payment-reminder.git
cd payment-reminder
```

2. Create a `.env` file in the project root with the following content:

```
EMAIL_USERNAME=your-email@example.com
EMAIL_PASSWORD=your-email-password
EMAIL_HOST=smtp.example.com
EMAIL_PORT=587
RECIPIENT_EMAIL=recipient@example.com
REMINDER_DAYS_AHEAD=3
```

3. Configure your payment data in `data.json`:

```json
[
  {
    "billingAccount": "Credit Card Name",
    "monthlyDueDate": "15"
  },
  {
    "billingAccount": "Another Account",
    "monthlyDueDate": "23"
  }
]
```

4. Build the project:

```bash
mvn clean package
```

## Configuration

### Environment Variables

| Variable            | Description                                                  |
| ------------------- | ------------------------------------------------------------ |
| EMAIL_USERNAME      | Your email address used to send reminders                    |
| EMAIL_PASSWORD      | Your email password or app password                          |
| EMAIL_HOST          | SMTP server host (e.g., smtp.gmail.com)                      |
| EMAIL_PORT          | SMTP server port (typically 587 for TLS)                     |
| RECIPIENT_EMAIL     | Email address to receive reminders                           |
| REMINDER_DAYS_AHEAD | Number of days before due date to send reminder (default: 3) |

### `data.json` Format

The `data.json` file must contain an array of payment objects with the following structure:

```json
[
  {
    "billingAccount": "Account Name",
    "monthlyDueDate": "DayOfMonth"
  }
]
```

- `billingAccount`: Name or description of the payment account
- `monthlyDueDate`: Day of month when payment is due (1-31)

## Usage

### Running Locally

Execute the application using:

```bash
java -jar target/reminder-app-1.0-SNAPSHOT.jar
```

### Setting up GitHub Actions

This repository includes a GitHub Actions workflow file to run the application automatically on a daily schedule.

To set up GitHub Actions:

1. Add the following secrets to your GitHub repository:

   - EMAIL_USERNAME
   - EMAIL_PASSWORD
   - EMAIL_HOST
   - EMAIL_PORT
   - RECIPIENT_EMAIL
   - REMINDER_DAYS_AHEAD

2. The workflow will run daily at 8:00 AM EST (13:00 UTC) by default. You can modify the schedule in `.github/workflows/daily-check.yml` if needed.

3. You can also manually trigger the workflow from the "Actions" tab in your GitHub repository.

## How It Works

The application:

1. Reads payment information from `data.json`
2. For each payment, calculates the next due date based on the current date
3. If a payment is due within the configured number of days (REMINDER_DAYS_AHEAD), sends an email reminder
4. Handles edge cases like months with fewer days

## License

This project is licensed under the GNU General Public License v3.0 - see the `LICENSE` file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
