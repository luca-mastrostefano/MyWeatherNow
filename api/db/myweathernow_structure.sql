-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Gen 05, 2015 alle 19:08
-- Versione del server: 5.6.17-log
-- PHP Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `myweathernow`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `forecasts`
--

CREATE TABLE IF NOT EXISTS `forecasts` (
  `id`          INT(11)     NOT NULL AUTO_INCREMENT,
  `city`        VARCHAR(32) NOT NULL,
  `date`        TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `temperature` FLOAT DEFAULT NULL,
  `humidity`    FLOAT DEFAULT NULL,
  `wind`        FLOAT DEFAULT NULL,
  `wind_dir`    FLOAT DEFAULT NULL,
  `rain`        FLOAT DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1
  AUTO_INCREMENT =3;

-- --------------------------------------------------------

--
-- Struttura della tabella `ratings`
--

CREATE TABLE IF NOT EXISTS `ratings` (
  `id_user`     INT(11) NOT NULL,
  `id_forecast` INT(11) NOT NULL,
  `user_rating` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id_user`, `id_forecast`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id`   INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1
  AUTO_INCREMENT =11;

/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
