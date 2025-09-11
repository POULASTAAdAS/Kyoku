CREATE DATABASE IF NOT EXISTS USER;

USE USER;

CREATE TABLE
    IF NOT EXISTS UserType (
        id INT PRIMARY KEY AUTO_INCREMENT,
        `type` VARCHAR(8) NOT NULL UNIQUE
    );

CREATE TABLE
    IF NOT EXISTS Country (
        id INT PRIMARY KEY AUTO_INCREMENT,
        country VARCHAR(40) NOT NULL,
        code VARCHAR(4) NOT NULL,
        UNIQUE KEY uq_country (country),
        UNIQUE KEY uq_code (code)
    );

CREATE TABLE
    IF NOT EXISTS `User` (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        user_type_id INT NOT NULL,
        email VARCHAR(320) NOT NULL,
        username VARCHAR(320) NOT NULL,
        display_name VARCHAR(320) NOT NULL,
        password_hash VARCHAR(700) NOT NULL,
        profile_pic VARCHAR(700) DEFAULT NULL,
        birth_date DATE DEFAULT NULL,
        country_id INT NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        -- 
        UNIQUE KEY uq_user_type_email (user_type_id, email),
        INDEX idx_email (email),
        INDEX idx_user_type_id (user_type_id),
        CONSTRAINT fk_user_user_type FOREIGN KEY (user_type_id) REFERENCES UserType (id) ON DELETE CASCADE,
        CONSTRAINT fk_user_country FOREIGN KEY (country_id) REFERENCES Country (id) ON DELETE CASCADE
    );

CREATE TABLE
    IF NOT EXISTS UserJWTToken (
        user_id BIGINT PRIMARY KEY,
        refresh_token VARCHAR(1000) NOT NULL,
        last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_user_token FOREIGN KEY (user_id) REFERENCES `User` (id) ON DELETE CASCADE
    );