create table analyst(
    analyst_id SERIAL PRIMARY KEY ,
    token_id INT NOT NULL UNIQUE ,
    login VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(500) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ANALYST',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (token_id) REFERENCES invite_tokens(token_id) ON DELETE RESTRICT
);