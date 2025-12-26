-- MySQL Database Setup for Career Guidance Platform

-- Create database
CREATE DATABASE IF NOT EXISTS career_guidance_db;
USE career_guidance_db;

-- Create user (optional, if you want a dedicated user)
-- CREATE USER 'career_user'@'localhost' IDENTIFIED BY 'career_password';
-- GRANT ALL PRIVILEGES ON career_guidance_db.* TO 'career_user'@'localhost';
-- FLUSH PRIVILEGES;

-- The tables will be created automatically by Spring Boot using schema.sql