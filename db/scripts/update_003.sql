create TABLE if not exists users (
   id SERIAL PRIMARY KEY,
   name varchar,
   email varchar unique
);