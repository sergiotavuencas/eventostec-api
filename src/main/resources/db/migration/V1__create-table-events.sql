CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE events (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    imgUrl VARCHAR(100) NOT NULL,
    eventUrl VARCHAR(100) NOT NULL,
    remote BOOLEAN NOT NULL,
    date TIMESTAMP NOT NULL
);