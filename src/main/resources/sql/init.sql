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

  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES users (`id`)
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

  CONSTRAINT `subject_id` FOREIGN KEY (`subject_id`) REFERENCES subjects (`id`)
    ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS waiting_users (
  username      VARCHAR(32)  NOT NULL,
  register_date TIMESTAMP,
  email         VARCHAR(255) NOT NULL,
  register_key  VARCHAR(255),
  password      VARCHAR(128)
);