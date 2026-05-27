CREATE DATABASE IF NOT EXISTS fleet_db;
USE fleet_db;

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS departments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_names VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN','STANDARD_USER') NOT NULL,
    department_id BIGINT NOT NULL,
    FOREIGN KEY (department_id) REFERENCES departments(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS assets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    serial_number VARCHAR(255) NOT NULL UNIQUE,
    status ENUM('ASSIGNED','AVAILABLE','OUT_OF_SERVICE','UNDER_MAINTENANCE') NOT NULL,
    category_id BIGINT NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS packs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    status ENUM('ASSIGNED','AVAILABLE') NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS packs_assets (
    pack_id BIGINT NOT NULL,
    asset_id BIGINT NOT NULL,
    PRIMARY KEY (pack_id, asset_id),
    FOREIGN KEY (pack_id) REFERENCES packs(id),
    FOREIGN KEY (asset_id) REFERENCES assets(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS assignments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    start_date DATE NOT NULL,
    end_date DATE,
    user_id BIGINT NOT NULL,
    asset_id BIGINT,
    pack_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (asset_id) REFERENCES assets(id),
    FOREIGN KEY (pack_id) REFERENCES packs(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS breakdowns (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(500) NOT NULL,
    reported_at DATETIME(6) NOT NULL,
    asset_id BIGINT NOT NULL,
    reporter_id BIGINT NOT NULL,
    FOREIGN KEY (asset_id) REFERENCES assets(id),
    FOREIGN KEY (reporter_id) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS maintenances (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(500),
    start_date DATE NOT NULL,
    end_date DATE,
    status ENUM('FINISHED','IN_PROGRESS') NOT NULL,
    asset_id BIGINT NOT NULL,
    breakdown_id BIGINT,
    responsible_id BIGINT,
    FOREIGN KEY (asset_id) REFERENCES assets(id),
    FOREIGN KEY (breakdown_id) REFERENCES breakdowns(id),
    FOREIGN KEY (responsible_id) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reason TEXT NOT NULL,
    status ENUM('APPROVED','CANCELED','PENDING','REJECTED') NOT NULL,
    created_at DATETIME(6) NOT NULL,
    reject_reason VARCHAR(300),
    validated_at DATETIME(6),
    requester_id BIGINT NOT NULL,
    validator_id BIGINT,
    asset_id BIGINT,
    pack_id BIGINT,
    FOREIGN KEY (requester_id) REFERENCES users(id),
    FOREIGN KEY (validator_id) REFERENCES users(id),
    FOREIGN KEY (asset_id) REFERENCES assets(id),
    FOREIGN KEY (pack_id) REFERENCES packs(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS fleet_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event VARCHAR(255) NOT NULL,
    timestamp DATETIME(6) NOT NULL
) ENGINE=InnoDB;