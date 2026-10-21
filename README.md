# Expenses-Fashion-Tracker

A Java Spring Boot web application designed to help fashion designers track business income, expenses, assets, customers, orders, and reports to understand whether their business is making a profit or loss.

## Technologies Used

- Java
- Spring Boot
- MySQL
- Maven
- HTML and CSS
- Thymeleaf

## Main Features

- Manage business assets.
- Register and manage customer records.
- Record income and expenses.
- Manage business orders and locations.
- Generate business reports.

## Database Setup

The application uses a MySQL database named `expensetracker_db`.

Configure your local database connection in `src/main/resources/application.properties` before running the application. Do not publish real customer data, database passwords, or other secrets.

The original database dump containing customer information and transaction records is intentionally excluded from this repository.

## Running the Application

1. Install a compatible Java Development Kit and MySQL.
2. Create the `expensetracker_db` database in MySQL.
3. Configure the database connection in `application.properties`.
4. Open the project in a compatible Java IDE or run it with the Maven wrapper.

## Project Purpose

This project demonstrates the use of Java Spring Boot and MySQL to build a business expense and financial tracking application.
