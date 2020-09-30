ALTER TABLE animals DROP COLUMN last_fed;

CREATE TABLE animal_types
( id INT PRIMARY KEY AUTO_INCREMENT
, name VARCHAR(25) UNIQUE NOT NULL
, required_space INT NOT NULL
);

INSERT INTO animal_types (name, required_space)
VALUES
    ("tiger", 10),
    ("elephant", 20);

ALTER TABLE animals
ADD FOREIGN KEY (type)
REFERENCES animal_types (id);

UPDATE properties
SET value = '3'
WHERE name = "version";
