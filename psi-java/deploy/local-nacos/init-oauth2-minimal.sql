CREATE TABLE IF NOT EXISTS `role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(64) DEFAULT NULL,
  `data` TEXT,
  `root` LONGTEXT,
  `auth` TEXT,
  `keyword` VARCHAR(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(64) DEFAULT NULL,
  `py` VARCHAR(64) DEFAULT NULL,
  `tel` VARCHAR(64) DEFAULT NULL,
  `frame` VARCHAR(64) DEFAULT NULL,
  `user` VARCHAR(64) DEFAULT NULL,
  `pwd` VARCHAR(255) DEFAULT NULL,
  `role` INT DEFAULT NULL,
  `img` VARCHAR(255) DEFAULT NULL,
  `token` VARCHAR(512) DEFAULT NULL,
  `expire` INT DEFAULT NULL,
  `data` TEXT,
  `more` TEXT,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_login` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `func_perm` (
  `id` VARCHAR(64) NOT NULL,
  `url` VARCHAR(255) DEFAULT NULL,
  `is_enable` CHAR(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `role` (`id`, `name`, `data`, `root`, `auth`, `keyword`)
VALUES (1, '超级管理员', NULL, '[{"fun":[{"id":"1"}]}]', NULL, 'SUPER_ADMIN')
ON DUPLICATE KEY UPDATE
  `name` = VALUES(`name`),
  `root` = VALUES(`root`),
  `keyword` = VALUES(`keyword`);

INSERT INTO `user` (`id`, `name`, `user`, `pwd`, `role`)
VALUES (1, '管理员', 'admin', '$2a$10$K7U.Xolbbz3fGsAzpIawmeQuTWt/W0TXA8DpugqRwWsE0PeRSi1Vu', 1)
ON DUPLICATE KEY UPDATE
  `name` = VALUES(`name`),
  `pwd` = VALUES(`pwd`),
  `role` = VALUES(`role`);

INSERT INTO `func_perm` (`id`, `url`, `is_enable`)
VALUES ('1', '/**', '1')
ON DUPLICATE KEY UPDATE
  `url` = VALUES(`url`),
  `is_enable` = VALUES(`is_enable`);
