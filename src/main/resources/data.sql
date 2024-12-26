INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (name)
VALUES ('Mirazur'),
       ('Geranium'),
       ('Noma');

INSERT INTO DISH (name, price, restaurant_id)
VALUES ('Burger', 9999, 1),
       ('Meatballs', 19900, 2),
       ('Pasta', 30050, 3),
       ('Pizza', 50000, 3);