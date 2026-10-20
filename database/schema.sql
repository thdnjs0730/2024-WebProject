-- Initialize a new database. User data and credentials are not included.
CREATE TABLE IF NOT EXISTS users (
 id VARCHAR(32) PRIMARY KEY, password VARCHAR(100) NOT NULL,
 email VARCHAR(254) NOT NULL, is_admin BOOLEAN NOT NULL DEFAULT FALSE, nickname VARCHAR(45) NOT NULL
);
CREATE TABLE IF NOT EXISTS adoptionposts (
 id INT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(255) NOT NULL, description TEXT NOT NULL,
 pet_type VARCHAR(50) NOT NULL, user_id VARCHAR(32) NOT NULL, photo VARCHAR(255),
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS messages (
 id INT AUTO_INCREMENT PRIMARY KEY, from_user_id VARCHAR(32) NOT NULL, to_user_id VARCHAR(32) NOT NULL,
 message TEXT NOT NULL, sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY (from_user_id) REFERENCES users(id) ON DELETE CASCADE,
 FOREIGN KEY (to_user_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE INDEX messages_recipient ON messages(to_user_id, sent_at);
CREATE INDEX messages_sender ON messages(from_user_id, sent_at);
