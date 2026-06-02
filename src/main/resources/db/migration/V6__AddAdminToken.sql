INSERT INTO invite_tokens (token, role, used, expires_at, created_at)
VALUES (
           'ANALYST-1650E6D0',
           'ADMIN',
           FALSE,
           CURRENT_TIMESTAMP + INTERVAL '365 days',
           CURRENT_TIMESTAMP
       );