# Career Guidance and Skill Development Platform

## Overview
Interactive web platform that helps students identify suitable career paths, assess their skills, and access personalized learning resources. The system connects students with mentors and tracks skill progression for better career readiness.

## Features
- **User Management**: Role-based access for students, mentors, and administrators
- **Skill Assessment**: Interactive quizzes to evaluate career-oriented skills
- **Career Recommendations**: AI-powered suggestions based on assessment results
- **Mentorship Matching**: Connect students with industry professionals
- **Progress Tracking**: Monitor learning milestones and achievements

## Technology Stack
- **Backend**: Java 17, Spring Boot 3.2, Spring Security, Spring Data JPA
- **Database**: MySQL 8.0
- **Frontend**: Thymeleaf, Bootstrap 5, JavaScript
- **Build Tool**: Gradle
- **Authentication**: JWT tokens for API, session-based for web
- **Documentation**: Swagger/OpenAPI 3

## Quick Start

### Prerequisites
- Java 17+
- MySQL 8.0+
- Gradle 7.0+

### Setup
1. Clone the repository
2. Configure database in `application.properties`
3. Run `./gradlew bootRun`
4. Access at `http://localhost:8080/career-guidance`

### Demo Accounts
- Student: `student@demo.com` / `password`
- Mentor: `mentor@demo.com` / `password`
- Admin: `admin@demo.com` / `password`

## API Documentation
Access Swagger UI at: `http://localhost:8080/career-guidance/swagger-ui.html`

## Project Structure
```
src/main/java/com/campusconnect/facility/
├── controller/     # REST controllers
├── service/        # Business logic
├── repository/     # Data access layer
├── model/          # JPA entities
├── dto/            # Data transfer objects
├── config/         # Configuration classes
└── util/           # Utility classes
```

## Key Endpoints
- `/api/auth/*` - Authentication
- `/api/assessment/*` - Skill assessments
- `/api/recommendation/*` - Career recommendations
- `/api/mentorship/*` - Mentorship management
- `/api/progress/*` - Progress tracking

## Database Schema
- `user_accounts` - User information
- `skill_assessments` - Assessment definitions
- `assessment_attempts` - User attempts
- `career_recommendations` - Generated recommendations
- `mentorship_requests` - Mentorship connections
- `progress_records` - Achievement tracking

## Security
- JWT authentication for API endpoints
- CSRF protection for web forms
- BCrypt password hashing
- Role-based authorization

## Testing
Run tests: `./gradlew test`
- Unit tests for services
- Integration tests for controllers
- H2 in-memory database for testing