docker run -d -p 5432:5432 --name my-postgres -e POSTGRES_PASSWORD=mysecretpassword postgres

docker exec -it my-postgres bash

CREATE DATABASE mytestdb;
\c mytestdb

CREATE TABLE batch(name character(255) PRIMARY KEY, schema jsonb not null);

INSERT INTO batch VALUES ('user-bnp', '{"name": "string", "email": "email", "finished": "boolean"}');

INSERT INTO batch VALUES ('user-zephyr', '{"id": "long", "name": "string", "email": "email", "dateNaissance": "date"}');

INSERT INTO batch VALUES ('test', '{"id": "int", "nom": "string", "prenom": "String"}');