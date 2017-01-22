DROP TABLE IF EXISTS waiting_users, exams, subjects, user_permissions, permissions, users;

CREATE TABLE IF NOT EXISTS users (
  id            BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username      VARCHAR(32) NOT NULL,
  password      VARCHAR(128),
  email         VARCHAR(255) UNIQUE,
  register_date TIMESTAMP,
  last_modified TIMESTAMP,
  token         VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS subjects (
  id            BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
  android_id    BIGINT,
  user_id       BIGINT,
  title         VARCHAR(64) NOT NULL,
  color         VARCHAR(9),
  has_grade     BOOLEAN,
  last_modified TIMESTAMP,

  CONSTRAINT `subjects_to_users` FOREIGN KEY (`user_id`) REFERENCES users (`id`)
    ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS exams (
  id            BIGINT NOT NULL,
  android_id    BIGINT,
  date          TIMESTAMP,
  info          VARCHAR(255),
  grade         DOUBLE PRECISION,
  last_modified TIMESTAMP,
  subject_id    BIGINT,

  CONSTRAINT `exams_to_subjects` FOREIGN KEY (`subject_id`) REFERENCES subjects (`id`)
    ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS permissions (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL UNIQUE
);

INSERT IGNORE INTO permissions(name) VALUE ("ADMIN");
INSERT IGNORE INTO permissions(name) VALUE ("USER");

CREATE TABLE IF NOT EXISTS user_permissions (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  permission_id BIGINT NOT NULL,
  CONSTRAINT `user_permissions_to_users` FOREIGN KEY (`user_id`) REFERENCES users (`id`) ON DELETE CASCADE,
  CONSTRAINT `user_permissions_to_permissions` FOREIGN KEY (`permission_id`) REFERENCES permissions(`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS waiting_users (
  username      VARCHAR(32)  NOT NULL,
  register_date TIMESTAMP,
  email         VARCHAR(255) NOT NULL,
  register_key  VARCHAR(255),
  password      VARCHAR(128)
);
