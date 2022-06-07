create TABLE if not exists users (
   id SERIAL PRIMARY KEY,
   password varchar,
   email varchar unique,
   name varchar
);