drop database if exists betwebapplication;
create database betwebapplication;


-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 12 Sty 2020, 00:18
-- Wersja serwera: 10.1.37-MariaDB
-- Wersja PHP: 7.3.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `betwebapplication`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `client`
--

CREATE TABLE `client` (
  `bet_account_balance` float NOT NULL,
  `date_of_birth` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `nr_apartment` int(11) NOT NULL,
  `nr_of_bank_account` varchar(255) DEFAULT NULL,
  `place_od_residence` varchar(255) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Zrzut danych tabeli `client`
--

INSERT INTO `client` (`bet_account_balance`, `date_of_birth`, `email`, `name`, `nr_apartment`, `nr_of_bank_account`, `place_od_residence`, `street`, `surname`, `user_id`) VALUES
(1000, '1979-12-29', 'malysz@gmail.com', 'Adam', 99, '1234', 'Wisla', 'Krakowska', 'Malysz', 1),
(1000, '1985-04-03', 'mkubica@gmail.com', 'Robert', 24, '5678', 'Krakow', 'Warszzawska', 'Kubica', 2);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `coupon`
--

CREATE TABLE `coupon` (
  `id` bigint(20) NOT NULL,
  `amount` float NOT NULL,
  `date` datetime(6) DEFAULT NULL,
  `possible_win` float NOT NULL,
  `rate` float NOT NULL,
  `state` varchar(255) DEFAULT NULL,
  `id_user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Zrzut danych tabeli `coupon`
--

INSERT INTO `coupon` (`id`, `amount`, `date`, `possible_win`, `rate`, `state`, `id_user_id`) VALUES
(1, 100, '2020-01-12 00:11:36.000000', 598, 5.98, 'WIN', 1),
(2, 10, '2020-01-12 00:14:49.000000', 22.6, 2.26, 'FAIL', 1);

--
-- Wyzwalacze `coupon`
--
DELIMITER $$
CREATE TRIGGER `editAmount` BEFORE UPDATE ON `coupon` FOR EACH ROW BEGIN
if(old.amount != new.amount) then
	SET new.possible_win=(new.amount*new.rate);
END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `editStateOfCoupon` AFTER UPDATE ON `coupon` FOR EACH ROW BEGIN
Set @id=old.id_user_id;
SET @win=old.possible_win;
SET @amount=old.amount;
	if (new.state="WIN") THEN
		UPDATE client c SET c.bet_account_balance=(c.bet_account_balance+@win)
    	where c.user_id = @id;
    END IF;
if (old.date != new.date) then
	UPDATE client c SET c.bet_account_balance=(c.bet_account_balance-@amount)
    where c.user_id = @id;
END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `coupon_course`
--

CREATE TABLE `coupon_course` (
  `id` bigint(20) NOT NULL,
  `coupon_id` bigint(20) DEFAULT NULL,
  `courses_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Zrzut danych tabeli `coupon_course`
--

INSERT INTO `coupon_course` (`id`, `coupon_id`, `courses_id`) VALUES
(6, 1, 19),
(8, 1, 35),
(10, 2, 23),
(12, 1, 27),
(13, 2, 36),
(14, 2, 26);

--
-- Wyzwalacze `coupon_course`
--
DELIMITER $$
CREATE TRIGGER `addCourseToCoupon` AFTER INSERT ON `coupon_course` FOR EACH ROW BEGIN
set @kurs=(SELECT ROUND(exp(sum(log(coalesce(k.value,1)))),2) FROM coupon_course cc LEFT JOIN coupon c on cc.coupon_id=C.id LEFT JOIN courses k on cc.courses_id=k.id WHERE c.id=new.coupon_id);
UPDATE coupon c SET rate=@kurs, c.possible_win=(c.amount*@kurs) WHERE c.id=new.coupon_id;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `deleteCourseFromCoupon` AFTER DELETE ON `coupon_course` FOR EACH ROW BEGIN
set @kurs=(SELECT ROUND(exp(sum(log(coalesce(k.value,1)))),2) FROM coupon_course cc LEFT JOIN coupon c on cc.coupon_id=C.id LEFT JOIN courses k on cc.courses_id=k.id WHERE c.id=old.coupon_id);
if @kurs is not null THEN
UPDATE coupon c SET rate=@kurs, c.possible_win=(c.amount*@kurs) WHERE c.id=old.coupon_id;
ELSE
UPDATE coupon c SET rate=0, c.possible_win=0 WHERE c.id=old.coupon_id;
END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `courses`
--

CREATE TABLE `courses` (
  `id` bigint(20) NOT NULL,
  `state` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `value` float NOT NULL,
  `match_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Zrzut danych tabeli `courses`
--

INSERT INTO `courses` (`id`, `state`, `type`, `value`, `match_id`) VALUES
(1, 'N/A', '1', 1.7, 1),
(2, 'N/A', '12', 1.35, 1),
(3, 'N/A', 'X', 1.8, 1),
(4, 'N/A', 'X1', 1.3, 1),
(5, 'N/A', '2', 1.6, 1),
(6, 'N/A', 'X2', 1.4, 1),
(7, 'N/A', 'X', 1.8, 2),
(8, 'N/A', '2', 2.2, 2),
(9, 'N/A', '12', 1.4, 2),
(10, 'N/A', 'X2', 1.9, 2),
(11, 'N/A', 'X1', 1.1, 2),
(12, 'N/A', '1', 1.5, 2),
(13, 'N/A', '2', 1.8, 3),
(14, 'N/A', '1', 1.7, 3),
(15, 'N/A', 'X1', 1.4, 3),
(16, 'N/A', '12', 1.3, 3),
(17, 'N/A', 'X2', 1.5, 3),
(18, 'N/A', 'X', 1.6, 3),
(19, 'true', '1', 1.7, 4),
(20, 'false', '2', 2, 4),
(21, 'false', 'X', 1.8, 4),
(22, 'true', 'X1', 1.4, 4),
(23, 'true', '12', 1.45, 4),
(24, 'false', 'X2', 1.5, 4),
(25, 'true', '12', 1.6, 5),
(26, 'false', 'X1', 1.3, 5),
(27, 'true', '2', 2.2, 5),
(28, 'true', 'X2', 1.8, 5),
(29, 'false', 'X', 1.7, 5),
(30, 'false', '1', 1.5, 5),
(31, 'false', 'X', 1.8, 6),
(32, 'false', '2', 2.4, 6),
(33, 'true', '12', 1.5, 6),
(34, 'false', 'X2', 1.8, 6),
(35, 'true', '1', 1.6, 6),
(36, 'true', 'X1', 1.2, 6),
(37, 'N/A', '1', 1.3, 7),
(38, 'N/A', 'X2', 1.7, 7),
(39, 'N/A', '2', 2.8, 7),
(40, 'N/A', '12', 1.5, 7),
(41, 'N/A', 'X', 1.9, 7),
(42, 'N/A', 'X1', 1.1, 7),
(43, 'N/A', '2', 1.8, 8),
(44, 'N/A', 'X2', 1.6, 8),
(45, 'N/A', 'X', 1.8, 8),
(46, 'N/A', '1', 1.4, 8),
(47, 'N/A', '12', 1.5, 8),
(48, 'N/A', 'X1', 1.2, 8),
(49, 'N/A', '1', 1.7, 9),
(50, 'N/A', 'X', 1.8, 9),
(51, 'N/A', 'X1', 1.3, 9),
(52, 'N/A', '12', 1.4, 9),
(53, 'N/A', '2', 1.9, 9),
(54, 'N/A', 'X2', 1.5, 9),
(55, 'N/A', '1', 1.7, 10),
(56, 'N/A', '12', 1.35, 10),
(57, 'N/A', 'X', 1.8, 10),
(58, 'N/A', 'X1', 1.3, 10),
(59, 'N/A', 'X2', 1.2, 10),
(60, 'N/A', '2', 1.9, 10),
(61, 'N/A', 'X', 1.8, 11),
(62, 'N/A', 'X1', 1.4, 11),
(63, 'N/A', '2', 1.6, 11),
(64, 'N/A', '12', 1.4, 11),
(65, 'N/A', 'X2', 1.3, 11),
(66, 'N/A', '1', 1.5, 11),
(67, 'N/A', '2', 1.8, 12),
(68, 'N/A', 'X1', 1.3, 12),
(69, 'N/A', '12', 1.4, 12),
(70, 'N/A', '1', 1.4, 12),
(71, 'N/A', 'X', 1.7, 12),
(72, 'N/A', 'X2', 1.5, 12),
(73, 'N/A', '1', 1.3, 13),
(74, 'N/A', '12', 1.6, 13),
(75, 'N/A', '2', 1.9, 13),
(76, 'N/A', 'X1', 1.1, 13),
(77, 'N/A', 'X2', 1.5, 13),
(78, 'N/A', 'X', 1.6, 13),
(79, 'N/A', '1', 1.7, 14),
(80, 'N/A', '12', 1.38, 14),
(81, 'N/A', 'X', 1.8, 14),
(82, 'N/A', 'X1', 1.4, 14),
(83, 'N/A', '2', 1.6, 14),
(84, 'N/A', 'X2', 1.3, 14),
(85, 'N/A', 'X2', 1.45, 15),
(86, 'N/A', '12', 1.65, 15),
(87, 'N/A', '1', 1.4, 15),
(88, 'N/A', '2', 1.9, 15),
(89, 'N/A', 'X', 1.7, 15),
(90, 'N/A', 'X1', 1.2, 15),
(91, 'N/A', '1', 1.7, 16),
(92, 'N/A', '12', 1.35, 16),
(93, 'N/A', 'X', 1.8, 16),
(94, 'N/A', 'X1', 1.3, 16),
(95, 'N/A', 'X2', 1.45, 16),
(96, 'N/A', '2', 1.7, 16),
(97, 'N/A', '2', 2.5, 17),
(98, 'N/A', '12', 1.4, 17),
(99, 'N/A', '1', 1.4, 17),
(100, 'N/A', 'X', 1.9, 17),
(101, 'N/A', 'X2', 2, 17),
(102, 'N/A', 'X1', 1.2, 17),
(103, 'N/A', '1', 1.3, 18),
(104, 'N/A', 'X1', 1.15, 18),
(105, 'N/A', '12', 1.4, 18),
(106, 'N/A', '2', 3.3, 18),
(107, 'N/A', 'X', 1.4, 18),
(108, 'N/A', 'X2', 2.3, 18),
(109, 'N/A', '2', 1.8, 19),
(110, 'N/A', 'X1', 1.3, 19),
(111, 'N/A', '12', 1.45, 19),
(112, 'N/A', 'X', 1.7, 19),
(113, 'N/A', 'X2', 1.5, 19),
(114, 'N/A', '1', 1.6, 19),
(115, 'N/A', '1', 1.3, 20),
(116, 'N/A', 'X', 1.8, 20),
(117, 'N/A', '2', 2.4, 20),
(118, 'N/A', '12', 1.7, 20),
(119, 'N/A', 'X2', 1.5, 20),
(120, 'N/A', 'X1', 1.2, 20),
(121, 'N/A', 'X', 1.8, 21),
(122, 'N/A', 'X2', 1.7, 21),
(123, 'N/A', '12', 1.64, 21),
(124, 'N/A', '1', 2.3, 21),
(125, 'N/A', '2', 1.9, 21),
(126, 'N/A', 'X1', 1.5, 21);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `matches`
--

CREATE TABLE `matches` (
  `id` bigint(20) NOT NULL,
  `date` date DEFAULT NULL,
  `league` varchar(255) DEFAULT NULL,
  `score` varchar(255) DEFAULT NULL,
  `team1` varchar(255) DEFAULT NULL,
  `team2` varchar(255) DEFAULT NULL,
  `time` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Zrzut danych tabeli `matches`
--

INSERT INTO `matches` (`id`, `date`, `league`, `score`, `team1`, `team2`, `time`) VALUES
(1, '2020-01-17', 'ANG1', 'N/A', 'Arsenal', 'Chelsea', '19:30:00'),
(2, '2020-01-16', 'ANG1', 'N/A', 'Liverpool', 'Man Utd', '18:00:00'),
(3, '2020-01-18', 'ANG1', 'N/A', 'Tottenham', 'Man City', '17:45:00'),
(4, '2020-01-09', 'ANG1', '1', 'Leicester', 'Everton', '18:30:00'),
(5, '2020-01-02', 'ESP1', '2', 'Barcelona', 'Atletico', '19:00:00'),
(6, '2020-01-04', 'ESP1', '1', 'Real', 'Valencia', '20:00:00'),
(7, '2020-01-20', 'ESP1', 'N/A', 'Barcelona', 'Valencia', '19:45:00'),
(8, '2020-01-21', 'ESP1', 'N/A', 'Real', 'Atletico', '20:30:00'),
(9, '2020-01-22', 'ITA1', 'N/A', 'Inter', 'Juventus', '19:00:00'),
(10, '2020-01-20', 'ITA1', 'N/A', 'Milan', 'Napoli', '11:45:00'),
(11, '2020-01-09', 'ITA1', 'N/A', 'Roma', 'Lazio', '11:45:00'),
(12, '2020-02-14', 'CL', 'N/A', 'Real', 'Man City', '11:45:00'),
(13, '2020-02-14', 'CL', 'N/A', 'Barcelona', 'Napoli', '19:45:00'),
(14, '2020-02-14', 'CL', 'N/A', 'Tottenham', 'Bayern', '19:45:00'),
(15, '2020-01-31', 'GER1', 'N/A', 'Bayern', 'Borrusia', '20:00:00'),
(16, '2020-02-01', 'GER1', 'N/A', 'Schalke', 'Bayer', '19:30:00'),
(17, '2020-01-19', 'FRA1', 'N/A', 'PSQ', 'AS Monaco', '16:00:00'),
(18, '2020-02-14', 'POL1', 'N/A', 'Wis?a', 'Legia', '19:45:00'),
(19, '2020-02-15', 'POL1', 'N/A', 'Lechia', 'Piast', '17:00:00'),
(20, '2020-02-13', 'POL1', 'N/A', 'Lech', 'Jagielonia', '19:30:00'),
(21, '2020-02-15', 'POL1', 'N/A', 'Cracovia', 'Górnik', '19:45:00');

--
-- Wyzwalacze `matches`
--
DELIMITER $$
CREATE TRIGGER `editScoreOfMatch` BEFORE UPDATE ON `matches` FOR EACH ROW BEGIN
SET @id = new.id;
if(old.score != new.score) then
	if(new.score = "1") then
        		UPDATE courses c SET c.state="true" where c.match_id = @id
        		and (c.type="1" or c.type="X1" or c.type="12");

		 UPDATE courses c SET c.state="false" where c.match_id = @id
       		 and (c.type="2" or c.type="X" or c.type="X2");

		UPDATE coupon c SET c.state="FAIL" where c.state="N/A" and id in
		(select * from
			( SELECT c.id FROM coupon c
			left join coupon_course cc on c.id=cc.coupon_id
			left join courses co on co.id=cc.courses_id
			where co.match_id = @id and (co.type="2" or co.type="X" or co.type="X2") )
		Q);
	END IF;
	IF(new.score = "2") then
        		UPDATE courses c SET c.state="true" where c.match_id = @id
        		and (c.type="2" or c.type="X2" or c.type="12");

		UPDATE courses c SET c.state="false" where c.match_id = @id
        		and (c.type="1" or c.type="X" or c.type="X1");

		UPDATE coupon c SET c.state="FAIL" where c.state="N/A" and id in
		(select * from
			( SELECT c.id FROM coupon c
			left join coupon_course cc on c.id=cc.coupon_id
			left join courses co on co.id=cc.courses_id
			where co.match_id = @id and (co.type="1" or co.type="X" or co.type="X1") )
		Q);
    	END IF;
    	IF(new.score = "X") then
       		UPDATE courses c SET c.state="true" where c.match_id = @id
        		and (c.type="X" or c.type="X1" or c.type="X2");

		UPDATE courses c SET c.state="false" where c.match_id = @id
        		and (c.type="1" or c.type="2" or c.type="12");

		UPDATE coupon c SET c.state="FAIL" where c.state="N/A" and id in
		(select * from
			( SELECT c.id FROM coupon c
			left join coupon_course cc on c.id=cc.coupon_id
			left join courses co on co.id=cc.courses_id
			where co.match_id = @id and (co.type="1" or co.type="2" or co.type="12") )
		Q);
	END IF;
    	IF(new.score = "N/A") then
		UPDATE courses c SET c.state="N/A" where c.match_id = @id;
	END IF;

	UPDATE coupon c SET c.state="WIN"
	where c.state="N/A" and id in
        	(select * from
            		(SELECT if(XX.x=YY.y, id, 0) id from
            			(SELECT c.id id, count(c.id) x FROM coupon c
           			left join coupon_course cc on c.id=cc.coupon_id
            			left join courses co on co.id=cc.courses_id GROUP BY c.id) XX
           		natural join
            			(SELECT c.id id, count(co.state) y FROM coupon c
           			left join coupon_course cc on c.id=cc.coupon_id
            			left join courses co on co.id=cc.courses_id
            			where co.state="true" GROUP BY c.id) YY)
        	Q);
END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Zrzut danych tabeli `user`
--

INSERT INTO `user` (`id`, `password`, `role`, `username`) VALUES
(1, '$2a$10$.MlkMWfKClgkpk5VZDHnBO8QUfYVBkHzz/nVNgy7SvwY2pugM.X3e', 'ROLE_USER', 'User1'),
(2, '$2a$10$71DQxLl5CIgel0y4CmkeLOoAxIEZWgO4LXLYBkw/qXt0zwe1EksXu', 'ROLE_USER', 'User2'),
(3, '$2a$10$c1u4cZG.B8vkVgS9FaQu7uQ5NTWYjErAdlP9G3QH5u5bR6kAEbpr6', 'ROLE_ADMIN', 'Admin');

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `client`
--
ALTER TABLE `client`
  ADD PRIMARY KEY (`user_id`);

--
-- Indeksy dla tabeli `coupon`
--
ALTER TABLE `coupon`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKi6llxq1kyyp7ycju5rp4gt4xs` (`id_user_id`);

--
-- Indeksy dla tabeli `coupon_course`
--
ALTER TABLE `coupon_course`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKcpq9e856c2ecal5cynr7flt1p` (`coupon_id`),
  ADD KEY `FKmj6u8mawxlmf5kg8dpqk3urdo` (`courses_id`);

--
-- Indeksy dla tabeli `courses`
--
ALTER TABLE `courses`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKerevis3ft9fwiwvvqxxdd0cuw` (`match_id`);

--
-- Indeksy dla tabeli `matches`
--
ALTER TABLE `matches`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT dla tabeli `coupon`
--
ALTER TABLE `coupon`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT dla tabeli `coupon_course`
--
ALTER TABLE `coupon_course`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT dla tabeli `courses`
--
ALTER TABLE `courses`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=127;

--
-- AUTO_INCREMENT dla tabeli `matches`
--
ALTER TABLE `matches`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT dla tabeli `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `client`
--
ALTER TABLE `client`
  ADD CONSTRAINT `FKk1fi84oi1yyuswr40h38kjy1s` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Ograniczenia dla tabeli `coupon`
--
ALTER TABLE `coupon`
  ADD CONSTRAINT `FKi6llxq1kyyp7ycju5rp4gt4xs` FOREIGN KEY (`id_user_id`) REFERENCES `user` (`id`);

--
-- Ograniczenia dla tabeli `coupon_course`
--
ALTER TABLE `coupon_course`
  ADD CONSTRAINT `FKcpq9e856c2ecal5cynr7flt1p` FOREIGN KEY (`coupon_id`) REFERENCES `coupon` (`id`),
  ADD CONSTRAINT `FKmj6u8mawxlmf5kg8dpqk3urdo` FOREIGN KEY (`courses_id`) REFERENCES `courses` (`id`);

--
-- Ograniczenia dla tabeli `courses`
--
ALTER TABLE `courses`
  ADD CONSTRAINT `FKerevis3ft9fwiwvvqxxdd0cuw` FOREIGN KEY (`match_id`) REFERENCES `matches` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
