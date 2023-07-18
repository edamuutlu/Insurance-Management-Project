CREATE TABLE `car` (
  `id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int DEFAULT NULL,
  `plate1` int NOT NULL,
  `plate2` varchar(3) DEFAULT NULL,
  `plate3` int NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `purpose` varchar(255) DEFAULT NULL,
  `brand` varchar(255) DEFAULT NULL,
  `fuel_type` varchar(255) DEFAULT NULL,
  `engine_size` int NOT NULL,
  `seat_capacity` int DEFAULT NULL,
  `offer` int DEFAULT NULL,
  `result` varchar(255) DEFAULT NULL,
  `start_date` varchar(255) DEFAULT NULL,
  `end_date` varchar(255) DEFAULT NULL,
  `period` int NOT NULL,
  `days_diff` int DEFAULT NULL,
  `refund` int DEFAULT NULL,
  `status` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=187 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `customer` (
  `customer_id` int NOT NULL AUTO_INCREMENT,
  `tc` varchar(11) DEFAULT NULL,
  `birth` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `province` varchar(255) DEFAULT NULL,
  `district` varchar(255) DEFAULT NULL,
  `status` int NOT NULL,
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci