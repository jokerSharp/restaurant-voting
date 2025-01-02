INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest'),
       ('JustAdmin', 'just@gmail.com', '{noop}admin');

INSERT INTO USER_ROLE (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2),
       ('ADMIN', 4);

INSERT INTO RESTAURANT (name)
VALUES ('Mirazur'),
       ('Geranium');

INSERT INTO DISH (name, price, restaurant_id)
VALUES ('Burger', 9999, 1),
       ('Meatballs', 19900, 1),
       ('Pasta', 30050, 2),
       ('Pizza', 50000, 2);