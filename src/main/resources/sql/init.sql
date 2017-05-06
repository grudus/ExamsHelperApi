USE examshelper_test;
DROP TABLE IF EXISTS exams, subjects, user_roles, roles, users;

CREATE TABLE IF NOT EXISTS users (
  id            BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username      VARCHAR(32) NOT NULL,
  password      VARCHAR(128) DEFAULT NULL,
  email         VARCHAR(255) UNIQUE,
  register_date DATETIME DEFAULT NOW(),
  last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  token         VARCHAR(255) DEFAULT NULL,
  state         VARCHAR(255) DEFAULT 'ENABLED'
);

CREATE TABLE IF NOT EXISTS subjects (
  id            BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
  android_id    BIGINT DEFAULT NULL,
  user_id       BIGINT NOT NULL,
  label         VARCHAR(64) NOT NULL,
  color         VARCHAR(9) DEFAULT '#000000',
  last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  UNIQUE INDEX (user_id, label),
  UNIQUE INDEX (user_id, android_id),
  CONSTRAINT `subjects_to_users` FOREIGN KEY (`user_id`) REFERENCES users (`id`)
    ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS exams (
  id            BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  android_id    BIGINT DEFAULT NULL,
  date          DATETIME DEFAULT NOW(),
  info          VARCHAR(255) DEFAULT NULL,
  grade         DOUBLE PRECISION DEFAULT -1.0,
  last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  subject_id    BIGINT NOT NULL,

  UNIQUE INDEX (android_id, subject_id),
  CONSTRAINT `exams_to_subjects` FOREIGN KEY (`subject_id`) REFERENCES subjects (`id`)
    ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS roles (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL UNIQUE
);

INSERT IGNORE INTO roles (name) VALUE ('ADMIN');
INSERT IGNORE INTO roles (name) VALUE ('USER');

CREATE TABLE IF NOT EXISTS user_roles (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  UNIQUE INDEX (user_id, role_id),
  CONSTRAINT `user_roles_to_users` FOREIGN KEY (`user_id`) REFERENCES users (`id`) ON DELETE CASCADE,
  CONSTRAINT `user_roles_to_roles` FOREIGN KEY (`role_id`) REFERENCES roles(`id`) ON DELETE CASCADE
);
