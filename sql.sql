CREATE TABLE client (
  email_address varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  phone_number int NOT NULL,
  surname varchar(255) DEFAULT NULL,
  users_id bigint NOT NULL
) ;
INSERT INTO client (email_address, name, phone_number, surname, users_id) VALUES
('adam.malysz@gamil.com', 'Adam', 111222334, 'Malysz', 1);
CREATE TABLE court (
  id SERIAL NOT NULL ,
  name varchar(255) DEFAULT NULL,
  status bit(1) DEFAULT NULL,
  type varchar(255) DEFAULT NULL
) ;
INSERT INTO court (id, name, status, type) VALUES
(1, 'court_1', b'1', 'ziemny'),
(2, 'court_2', b'1', 'ziemny'),
(3, 'court_3', b'1', 'ziemny'),
(4, 'court_4', b'1', 'ziemny');
CREATE TABLE price_list (
  id bigint NOT NULL,
  price float DEFAULT NULL,
  time varchar(255) DEFAULT NULL,
  type varchar(255) DEFAULT NULL
) ;
INSERT INTO price_list (id, price, time, type) VALUES
(1, 40, '6.00 - 14.00', 'From Monday To Friday [1h]'),
(2, 50, '14.00 - 23.00', 'From Monday To Friday [1h]'),
(3, 45, '6.00 - 23.00', 'From Saturday To Sunday [1h]'),
(4, 30, '23.00 - 01.00', 'Night Gaming [1h]'),
(5, 30, '5 or more hours Tennis Pass', 'From Monday To Friday [1h]'),
(6, 40, '5 or more hours Tennis Pass', 'From Saturday To Sunday [1h]'),
(7, 5, 'The Duration Of The Game', 'Rocket Rental'),
(8, 2, 'The Duration Of The Game', 'Balls Rental'),
(9, 2, 'The Duration Of The Game', 'Shoes Rental (1 pairs)');
CREATE TABLE reservation (
  id SERIAL NOT NULL,
  date_of_reservation date DEFAULT NULL,
  final_payment_date date DEFAULT NULL,
  final_price float NOT NULL,
  status_of_reservation varchar DEFAULT NULL,
  status_paying varchar DEFAULT NULL,
  type_of_paying varchar DEFAULT NULL
) ;
INSERT INTO reservation (id, date_of_reservation, final_payment_date, final_price, status_of_reservation, status_paying, type_of_paying) VALUES
(28, '2020-09-03', '2020-09-03', 114, 'Reserved', 'Paid', 'online'),
(33, '2020-09-04', '2020-09-04', 40, 'Reserved', 'Paid', 'online'),
(34, '2020-09-04', '2020-09-04', 95, 'Reserved', 'Paid', 'online'),
(35, '2020-09-04', '2020-09-04', 155, 'Reserved', 'To Pay', 'online'),
(36, '2020-09-04', '2020-09-04', 179, 'Reserved', 'To Pay', 'online'),
(37, '2020-09-04', '2020-09-06', 54, 'Reserved', 'To Pay', 'offline'),
(38, '2020-09-04', '2020-09-04', 127, 'Reserved', 'Paid', 'online');
CREATE TABLE reservation_services(
  id SERIAL NOT NULL,
  reservation_id bigint DEFAULT NULL,
  services_id bigint DEFAULT NULL
) ;
INSERT INTO reservation_services (id, reservation_id, services_id) VALUES
(800, 35, 800),
(801, 35, 801),
(802, 35, 802),
(808, 36, 808),
(809, 36, 809),
(810, 36, 810),
(811, 36, 811),
(812, 37, 812),
(817, 38, 817),
(818, 38, 818),
(819, 38, 819);
CREATE TABLE services (
  id SERIAL NOT NULL,
  date date DEFAULT NULL,
  if_balls bit(1) DEFAULT NULL,
  if_rocket bit(1) DEFAULT NULL,
  if_shoes bit(1) DEFAULT NULL,
  number_of_hours float NOT NULL,
  price float NOT NULL,
  time time DEFAULT NULL,
  unit_cost float NOT NULL,
  court_id bigint DEFAULT NULL
) ;
INSERT INTO services (id, date, if_balls, if_rocket, if_shoes, number_of_hours, price, time, unit_cost, court_id) VALUES
(800, '2020-09-04', b'0', b'0', b'0', 1.5, 60, '07:00:00', 40, 1),
(801, '2020-09-04', b'0', b'0', b'0', 1, 40, '08:00:00', 40, 3),
(802, '2020-09-07', b'0', b'1', b'0', 1, 55, '18:00:00', 50, 3),
(808, '2020-09-04', b'0', b'0', b'1', 1, 52, '14:00:00', 50, 2),
(809, '2020-09-04', b'0', b'1', b'0', 1.5, 65, '10:30:00', 40, 4),
(810, '2020-09-10', b'1', b'0', b'0', 1, 42, '06:30:00', 40, 2),
(811, '2020-09-10', b'0', b'0', b'0', 0.5, 20, '08:30:00', 40, 4),
(812, '2020-09-06', b'1', b'1', b'1', 1, 54, '12:00:00', 45, 1),
(817, '2020-09-04', b'0', b'0', b'0', 1, 40, '09:30:00', 40, 1),
(818, '2020-09-04', b'0', b'1', b'1', 1.5, 67, '06:30:00', 40, 2),
(819, '2020-09-04', b'0', b'0', b'0', 0.5, 20, '06:00:00', 40, 4);
CREATE TABLE users (
  id SERIAL NOT NULL,
  password varchar(255) DEFAULT NULL,
  role varchar(255) DEFAULT NULL,
  username varchar(255) DEFAULT NULL
) ;
INSERT INTO users (id, password, role, username) VALUES
(1, '$2a$10$0FYEcgpnYsUioWmOWaaXhewHvX43b.SbyAE9Vqv7yIVmo.0KozWtq', 'ROLE_USER', 'user');
CREATE TABLE user_reservation (
  id SERIAL NOT NULL,
  reservation_id bigint DEFAULT NULL,
  users_id bigint DEFAULT NULL
);
INSERT INTO user_reservation (id, reservation_id, users_id) VALUES
(31, 35, 1),
(32, 36, 1),
(33, 37, 1),
(34, 38, 1);
ALTER TABLE client
  ADD PRIMARY KEY (users_id);
ALTER TABLE court
  ADD PRIMARY KEY (id);
ALTER TABLE price_list
  ADD PRIMARY KEY (id);
ALTER TABLE reservation
  ADD PRIMARY KEY (id);
ALTER TABLE reservation_services
  ADD PRIMARY KEY (id);
--ALTER TABLE reservation_services
--  ADD KEY FK_reservation_id (reservation_id);
--ALTER TABLE reservation_services
--  ADD KEY FK_services_id (services_id);
ALTER TABLE services
  ADD PRIMARY KEY (id);
--ALTER TABLE services
--  ADD KEY FK_court_id (court_id);
ALTER TABLE users
  ADD PRIMARY KEY (id);
ALTER TABLE user_reservation
  ADD PRIMARY KEY (id);
--ALTER TABLE user_reservation
--  ADD KEY FK_reservation_id_2 (reservation_id);
--ALTER TABLE user_reservation
--  ADD KEY FK_user_id_2 (user_id);
ALTER TABLE client
  ADD CONSTRAINT FK_users_id FOREIGN KEY (users_id) REFERENCES users (id);
ALTER TABLE reservation_services
  ADD CONSTRAINT FK_reservation_id FOREIGN KEY (reservation_id) REFERENCES reservation (id);
ALTER TABLE reservation_services
  ADD CONSTRAINT FK_services_id FOREIGN KEY (services_id) REFERENCES services (id);
ALTER TABLE services
  ADD CONSTRAINT FK_court_id FOREIGN KEY (court_id) REFERENCES court (id);
ALTER TABLE user_reservation
  ADD CONSTRAINT FK_reservation_id_2 FOREIGN KEY (reservation_id) REFERENCES reservation (id);
ALTER TABLE user_reservation
  ADD CONSTRAINT FK_users_id_2 FOREIGN KEY (users_id) REFERENCES users (id);
--COMMIT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

