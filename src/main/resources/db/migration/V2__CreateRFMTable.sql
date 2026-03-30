CREATE TABLE segmentUsers(
    segment_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL ,
    segment VARCHAR(20) NOT NULL ,
    r_minutes DOUBLE PRECISION NOT NULL ,
    f BIGINT NOT NULL ,
    m DOUBLE PRECISION NOT NULL ,
    updated_at BIGINT NOT NULL ,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);