-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: May 24, 2015 at 04:50 PM
-- Server version: 5.6.17
-- PHP Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `my_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `gas_stations`
--

CREATE TABLE IF NOT EXISTS `gas_stations` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `GAS_REVENUE` double NOT NULL DEFAULT '0',
  `CLEAN_REVENUE` double NOT NULL DEFAULT '0',
  `CARS_WASHED` int(11) NOT NULL DEFAULT '0',
  `CARS_CLEANED` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `pumps`
--

CREATE TABLE IF NOT EXISTS `pumps` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STATION_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `STATION_ID` (`STATION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `service_types`
--

CREATE TABLE IF NOT EXISTS `service_types` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `unique_NAME` (`NAME`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `service_types`
--

INSERT INTO `service_types` (`ID`, `NAME`) VALUES
(1, 'cleaning'),
(0, 'fuel');

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE IF NOT EXISTS `transactions` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `AMOUNT` double NOT NULL,
  `TIME_STAMP` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `SERVICE_TYPE` int(11) DEFAULT NULL,
  `PUMP` int(11) DEFAULT NULL,
  `STATION_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `PUMP` (`PUMP`),
  KEY `TYPE` (`SERVICE_TYPE`),
  KEY `STATION_ID` (`STATION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `pumps`
--
ALTER TABLE `pumps`
  ADD CONSTRAINT `pumps_ibfk_1` FOREIGN KEY (`STATION_ID`) REFERENCES `gas_stations` (`ID`);

--
-- Constraints for table `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`PUMP`) REFERENCES `pumps` (`ID`),
  ADD CONSTRAINT `transactions_ibfk_2` FOREIGN KEY (`SERVICE_TYPE`) REFERENCES `service_types` (`ID`),
  ADD CONSTRAINT `transactions_ibfk_3` FOREIGN KEY (`STATION_ID`) REFERENCES `gas_stations` (`ID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
