# Enterprise Employee Management Platform

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-green)
![MySQL](https://img.shields.io/badge/MySQL-9-blue)
![Security](https://img.shields.io/badge/Spring_Security-6-success)
![License](https://img.shields.io/badge/License-MIT-yellow)

A modern Enterprise Employee Management Platform built using Spring Boot, Spring Security, JWT Authentication, Thymeleaf, MySQL, Flyway, and Role-Based Access Control (RBAC).

---

# Overview

The Enterprise Employee Management Platform is a full-stack web application designed to streamline employee administration within an organization.

The platform provides secure authentication, employee profile management, department management, leave request processing, task assignment, and audit logging while following enterprise-grade architecture and security practices.

---

# Features

## Authentication & Authorization

* Secure Login System
* JWT Authentication
* Spring Security 6
* BCrypt Password Encryption
* Role-Based Access Control (ADMIN, MANAGER, EMPLOYEE)

## Employee Management

* Create Employees
* Update Employee Information
* View Employee Profiles
* Employee Directory

## Department Management

* Create Departments
* Manage Departments
* Employee Department Assignment

## Leave Management

* Leave Requests
* Leave Approval Workflow
* Leave Status Tracking

## Task Management

* Task Creation
* Task Assignment
* Task Tracking
* Due Date Monitoring

## Audit Logging

* User Activity Tracking
* CRUD Operation Monitoring
* Login Activity Recording
* System Audit Trail

---

# Technology Stack

| Layer              | Technology                  |
| ------------------ | --------------------------- |
| Language           | Java 21                     |
| Backend            | Spring Boot 3.5             |
| Security           | Spring Security 6           |
| Authentication     | JWT                         |
| Frontend           | Thymeleaf                   |
| UI Framework       | Bootstrap 5                 |
| Database           | MySQL                       |
| ORM                | Spring Data JPA / Hibernate |
| Database Migration | Flyway                      |
| Build Tool         | Maven                       |
| API Documentation  | Swagger OpenAPI             |

---

# Screenshots

## Login Page

![Login](screenshots/login.png)

## Dashboard

![Dashboard](screenshots/dashboard.png)

## Employees

![Employees](screenshots/employees.png)

## Departments

![Departments](screenshots/departments.png)

## Leave Management

![Leaves](screenshots/leaves.png)

## Task Management

![Tasks](screenshots/tasks.png)

## Audit Logs

![Audit Logs](screenshots/audit.png)

---

# System Architecture

![Architecture](screenshots/architecture.png)

---

# Database ER Diagram

![ER Diagram](screenshots/er-diagram.png)

---

# API Documentation

![Swagger UI](screenshots/swagger.png)

---

# Project Structure

```text
src
├── main
│   ├── java
│   │   └── com.enterprise.empmgmt
│   │       ├── config
│   │       ├── controller
│   │       ├── service
│   │       ├── repository
│   │       ├── mapper
│   │       ├── security
│   │       ├── domain
│   │       ├── dto
│   │       └── exception
│   │
│   └── resources
│       ├── templates
│       ├── static
│       ├── db
│       │   └── migration
│       └── application.yml
│
└── test
```

---

# Database Design

The system consists of the following entities:

* Users
* Roles
* User Roles
* Employee Profiles
* Departments
* Leave Requests
* Tasks
* Audit Logs

These entities are connected using relational database design principles and managed through JPA/Hibernate.

---

# Security Features

* JWT Token Authentication
* Spring Security Filter Chain
* Password Encryption using BCrypt
* Role-Based Access Control (RBAC)
* Endpoint Protection
* Authentication & Authorization Layers

---

# Admin Dashboard Modules

### Dashboard

* Employee Statistics
* Department Overview
* Leave Summary
* Task Summary

### Employees

* Employee Listing
* Employee Information

### Departments

* Department Management

### Leaves

* Leave Request Tracking

### Tasks

* Task Assignment & Monitoring

### Audit

* System Activity Monitoring

---

# Getting Started

## Clone Repository

```bash
git clone https://github.com/YOUR_USERNAME/employee-management-platform.git
```

## Navigate to Project

```bash
cd employee-management-platform
```

## Configure Database

Create MySQL database:

```sql
CREATE DATABASE emp_mgmt;
```

Update database credentials inside:

```text
src/main/resources/application.yml
```

## Run Application

```bash
mvn spring-boot:run
```

Application URL:

```text
http://localhost:8081
```

Admin Login:

```text
Email: admin@enterprise.com
Password: ChangeMe1!
```

---

# Future Enhancements

* Attendance Management
* Payroll System
* Email Notifications
* Employee Performance Reviews
* Analytics Dashboard
* AI-Powered HR Insights
* Multi-Tenant Architecture
* Cloud Deployment

---

# Resume Project Summary

Developed a full-stack Enterprise Employee Management Platform using Java 21, Spring Boot 3, Spring Security, JWT Authentication, Thymeleaf, MySQL, Flyway, and Role-Based Access Control (RBAC). Implemented employee lifecycle management, department administration, leave processing, task tracking, audit logging, REST APIs, and secure authentication following enterprise-grade architecture and security practices.

---

# Author

Karan

B.Tech Electronics & Telecommunication Engineering

2026 Graduate

---

© 2026 Enterprise Employee Management Platform
