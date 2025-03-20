CREATE TABLE IF NOT EXISTS users (
   id SERIAL PRIMARY KEY,
   name TEXT not null,
   email TEXT unique not null,
   password TEXT not null
);