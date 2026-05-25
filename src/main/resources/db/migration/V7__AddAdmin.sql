INSERT INTO analyst (token_id, login, password, role, created_at)
VALUES (
           (SELECT token_id FROM invite_tokens WHERE token = 'ADMIN-1650E6D0'),
           'admin',
           '$2a$04$5D9XyLRN5gLPzGrZ8qKpUOJH5q3QxHtLkMvNwRsTqWeRtYuIoPk6K',
           'ADMIN',
           CURRENT_TIMESTAMP
       );

UPDATE invite_tokens
SET used = true
WHERE token = 'ADMIN-1650E6D0';