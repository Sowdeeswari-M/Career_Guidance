-- Career Guidance Platform Database Schema

-- User accounts table
CREATE TABLE IF NOT EXISTS user_accounts (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email_address VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    user_role VARCHAR(20) NOT NULL,
    is_account_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    phone_number VARCHAR(20),
    profile_bio TEXT,
    profile_image_url VARCHAR(255),
    registration_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login_time TIMESTAMP,

    -- Student specific fields
    current_education_level VARCHAR(50),
    field_of_study VARCHAR(100),
    career_interests TEXT,

    -- Mentor specific fields
    professional_title VARCHAR(100),
    company_name VARCHAR(100),
    years_of_experience INT,
    expertise_areas TEXT
);

CREATE INDEX idx_username ON user_accounts (username);
CREATE INDEX idx_email ON user_accounts (email_address);
CREATE INDEX idx_role ON user_accounts (user_role);
-- Skill assessments table
CREATE TABLE IF NOT EXISTS skill_assessments (
    assessment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    assessment_title VARCHAR(200) NOT NULL,
    assessment_description TEXT,
    skill_category VARCHAR(100) NOT NULL,
    duration_minutes INT NOT NULL,
    passing_score DECIMAL(5,2) NOT NULL,
    difficulty_level VARCHAR(20) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP
);

CREATE INDEX idx_category ON skill_assessments (skill_category);
CREATE INDEX idx_difficulty ON skill_assessments (difficulty_level);
CREATE INDEX idx_active ON skill_assessments (is_active);

-- Assessment questions table
CREATE TABLE IF NOT EXISTS assessment_questions (
    question_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_text TEXT NOT NULL,
    question_type VARCHAR(20) NOT NULL,
    points_value INT NOT NULL,
    question_order INT NOT NULL,
    assessment_id BIGINT NOT NULL,

    FOREIGN KEY (assessment_id) REFERENCES skill_assessments(assessment_id) ON DELETE CASCADE
);

CREATE INDEX idx_assessment ON assessment_questions (assessment_id);
CREATE INDEX idx_order ON assessment_questions (question_order);

-- Question options table
CREATE TABLE IF NOT EXISTS question_options (
    option_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    option_text TEXT NOT NULL,
    is_correct_answer BOOLEAN NOT NULL DEFAULT FALSE,
    option_order INT NOT NULL,
    question_id BIGINT NOT NULL,

    FOREIGN KEY (question_id) REFERENCES assessment_questions(question_id) ON DELETE CASCADE
);

CREATE INDEX idx_question ON question_options (question_id);

-- Assessment attempts table
CREATE TABLE IF NOT EXISTS assessment_attempts (
    attempt_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    assessment_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completion_time TIMESTAMP,
    achieved_score DECIMAL(5,2) NOT NULL DEFAULT 0.0,
    total_possible_score DECIMAL(5,2) NOT NULL,
    percentage_score DECIMAL(5,2) NOT NULL DEFAULT 0.0,
    attempt_status VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS',
    is_passed BOOLEAN NOT NULL DEFAULT FALSE,
    time_spent_minutes INT,
    feedback_comments TEXT,

    FOREIGN KEY (user_id) REFERENCES user_accounts(user_id) ON DELETE CASCADE,
    FOREIGN KEY (assessment_id) REFERENCES skill_assessments(assessment_id) ON DELETE CASCADE
);

CREATE INDEX idx_attempt_user ON assessment_attempts (user_id);
CREATE INDEX idx_attempt_assessment ON assessment_attempts (assessment_id);
CREATE INDEX idx_status ON assessment_attempts (attempt_status);

-- Student answers table
CREATE TABLE IF NOT EXISTS student_answers (
    answer_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    attempt_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    selected_option_id BIGINT,
    text_answer TEXT,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,
    points_earned DECIMAL(5,2) NOT NULL DEFAULT 0.0,
    answered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (attempt_id) REFERENCES assessment_attempts(attempt_id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES assessment_questions(question_id) ON DELETE CASCADE,
    FOREIGN KEY (selected_option_id) REFERENCES question_options(option_id) ON DELETE SET NULL
);

CREATE INDEX idx_answer_attempt ON student_answers (attempt_id);
CREATE INDEX idx_answer_question ON student_answers (question_id);

-- Career recommendations table
CREATE TABLE IF NOT EXISTS career_recommendations (
    recommendation_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    career_path VARCHAR(200) NOT NULL,
    career_description TEXT,
    match_percentage DECIMAL(5,2) NOT NULL,
    required_skills TEXT,
    recommended_courses TEXT,
    industry_outlook TEXT,
    salary_range VARCHAR(100),
    recommendation_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    generated_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated_date TIMESTAMP,
    recommendation_reason TEXT,

    FOREIGN KEY (user_id) REFERENCES user_accounts(user_id) ON DELETE CASCADE
);

CREATE INDEX idx_rec_user ON career_recommendations (user_id);
CREATE INDEX IF NOT EXISTS idx_status ON career_recommendations (recommendation_status);
CREATE INDEX idx_career_path ON career_recommendations (career_path);

-- Mentorship requests table
CREATE TABLE IF NOT EXISTS mentorship_requests (
    request_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    mentor_id BIGINT,
    request_message TEXT NOT NULL,
    mentorship_goals TEXT,
    preferred_meeting_schedule TEXT,
    request_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    request_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    response_date TIMESTAMP,
    mentor_response TEXT,
    mentorship_start_date TIMESTAMP,
    mentorship_end_date TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    FOREIGN KEY (student_id) REFERENCES user_accounts(user_id) ON DELETE CASCADE,
    FOREIGN KEY (mentor_id) REFERENCES user_accounts(user_id) ON DELETE SET NULL
);

CREATE INDEX idx_student ON mentorship_requests (student_id);
CREATE INDEX idx_mentor ON mentorship_requests (mentor_id);
CREATE INDEX IF NOT EXISTS idx_status ON mentorship_requests (request_status);

-- Mentorship sessions table
CREATE TABLE IF NOT EXISTS mentorship_sessions (
    session_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_id BIGINT NOT NULL,
    session_title VARCHAR(200) NOT NULL,
    session_description TEXT,
    scheduled_date_time TIMESTAMP NOT NULL,
    duration_minutes INT,
    session_status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    session_notes TEXT,
    student_feedback TEXT,
    mentor_feedback TEXT,
    actual_start_time TIMESTAMP,
    actual_end_time TIMESTAMP,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (request_id) REFERENCES mentorship_requests(request_id) ON DELETE CASCADE
);

CREATE INDEX idx_request ON mentorship_sessions (request_id);
CREATE INDEX idx_scheduled_time ON mentorship_sessions (scheduled_date_time);
CREATE INDEX IF NOT EXISTS idx_status ON mentorship_sessions (session_status);

-- Progress records table
CREATE TABLE IF NOT EXISTS progress_records (
    progress_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    achievement_type VARCHAR(100) NOT NULL,
    achievement_title VARCHAR(200) NOT NULL,
    achievement_description TEXT,
    achieved_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    points_earned INT NOT NULL DEFAULT 0,
    achievement_category VARCHAR(30) NOT NULL,
    skills_improved TEXT,
    certificate_url VARCHAR(255),
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    verification_date TIMESTAMP,
    verification_notes TEXT,

    FOREIGN KEY (user_id) REFERENCES user_accounts(user_id) ON DELETE CASCADE
);

CREATE INDEX idx_progress_user ON progress_records (user_id);
CREATE INDEX idx_achievement_category ON progress_records (achievement_category);
CREATE INDEX idx_verified ON progress_records (is_verified);
CREATE INDEX idx_achieved_date ON progress_records (achieved_date);
