-- -----------------------------------------------------
-- Base de Datos: pokemon_battles (batallas_pokemon)
-- -----------------------------------------------------
CREATE DATABASE IF NOT EXISTS `pokemon` 
DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;

USE `pokemon`;

-- -----------------------------------------------------
-- Tabla `partidas_guardadas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `partidas_guardadas` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre_guardado` VARCHAR(100) NOT NULL,
  `fecha` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `turno` BOOLEAN NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Tabla `batalla`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `batalla` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `partida_id` INT NOT NULL,
  `jugador` TINYINT(1) NOT NULL COMMENT '1 = Jugador, 0 = Rival',
  `nombre_pokemon` VARCHAR(50) NOT NULL,
  `vida` DOUBLE NOT NULL DEFAULT 1.0,
  PRIMARY KEY (`id`),
  INDEX `fk_batalla_partida_idx` (`partida_id` ASC),
  CONSTRAINT `fk_batalla_partida`
    FOREIGN KEY (`partida_id`)
    REFERENCES `partidas_guardadas` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Tabla `movimientos_pokemon`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `movimientos_pokemon` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `pokemon_id` INT NOT NULL,
  `nombre_movimiento` VARCHAR(50) NOT NULL,
  `pp_actual` INT NOT NULL,
  `pp_maximo` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_movimientos_pokemon_batalla_idx` (`pokemon_id` ASC),
  CONSTRAINT `fk_movimientos_pokemon_batalla`
    FOREIGN KEY (`pokemon_id`)
    REFERENCES `batalla` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB;
