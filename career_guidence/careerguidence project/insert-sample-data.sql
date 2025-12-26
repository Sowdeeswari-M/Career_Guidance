-- Insert sample data into MySQL database
USE career_guidance_db;

-- Insert sample users with BCrypt encoded passwords (password = "password")
INSERT INTO user_accounts (username, email_address, password_hash, full_name, user_role, is_account_active, is_email_verified, registration_date, current_education_level, field_of_study, career_interests) VALUES
('student1', 'student@demo.com', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqyc5rhjkv8A.xNuGYvOzne', 'John Student', 'STUDENT', 1, 1, NOW(), 'Bachelor''s Degree', 'Computer Science', 'Software Development, Data Science');

INSERT INTO user_accounts (username, email_address, password_hash, full_name, user_role, is_account_active, is_email_verified, registration_date, professional_title, company_name, years_of_experience, expertise_areas) VALUES
('mentor1', 'mentor@demo.com', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqyc5rhjkv8A.xNuGYvOzne', 'Sarah Mentor', 'MENTOR', 1, 1, NOW(), 'Senior Software Engineer', 'Tech Corp', 8, 'Java, Spring Boot, Career Development');

INSERT INTO user_accounts (username, email_address, password_hash, full_name, user_role, is_account_active, is_email_verified, registration_date) VALUES
('admin1', 'admin@demo.com', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqyc5rhjkv8A.xNuGYvOzne', 'Admin User', 'ADMIN', 1, 1, NOW());

-- Insert sample assessments
INSERT INTO skill_assessments (assessment_title, assessment_description, skill_category, duration_minutes, passing_score, difficulty_level, is_active, created_date) VALUES
('Java Programming Basics', 'Test your fundamental Java programming knowledge', 'Programming', 30, 70.0, 'BEGINNER', 1, NOW()),
('Data Analysis Fundamentals', 'Assess your data analysis and statistics skills', 'Data Science', 45, 75.0, 'INTERMEDIATE', 1, NOW()),
('Business Communication', 'Evaluate your business communication abilities', 'Business', 25, 65.0, 'BEGINNER', 1, NOW());

-- Insert sample questions for Java assessment
INSERT INTO assessment_questions (question_text, question_type, points_value, question_order, assessment_id) VALUES
('What is the correct way to declare a variable in Java?', 'MULTIPLE_CHOICE', 10, 1, 1),
('Java is platform independent. True or False?', 'TRUE_FALSE', 5, 2, 1),
('Explain the concept of inheritance in Java.', 'SHORT_ANSWER', 15, 3, 1);

-- Insert options for multiple choice question
INSERT INTO question_options (option_text, is_correct_answer, option_order, question_id) VALUES
('int x = 5;', 1, 1, 1),
('integer x = 5;', 0, 2, 1),
('var x = 5;', 0, 3, 1),
('x = 5;', 0, 4, 1);

-- Insert options for true/false question
INSERT INTO question_options (option_text, is_correct_answer, option_order, question_id) VALUES
('True', 1, 1, 2),
('False', 0, 2, 2);