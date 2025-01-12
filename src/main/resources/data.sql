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

INSERT INTO DISH (name, price, restaurant_id, actuality_date)
VALUES ('Burger', 9999, 1, DATE '2025-01-10'),
       ('Meatballs', 19900, 1, CURRENT_DATE),
       ('Pasta', 30050, 2, DATE '2025-01-10'),
       ('Pizza', 50000, 2, CURRENT_DATE);

INSERT INTO VOTE (restaurant_id, user_id, created_at)
VALUES (1, 2, DATE '2025-01-10'),
       (2, 2, CURRENT_DATE);