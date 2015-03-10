SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `GigaByte` ;
CREATE SCHEMA IF NOT EXISTS `GigaByte` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;
USE `GigaByte` ;

-- -----------------------------------------------------
-- Table `GigaByte`.`account`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GigaByte`.`account` ;

CREATE TABLE IF NOT EXISTS `GigaByte`.`account` (
  `account_id` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(45) NULL,
  `password` TEXT NULL,
  `fname` VARCHAR(45) NULL,
  `lname` VARCHAR(45) NULL,
  `phone` VARCHAR(45) NULL,
  `address` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  `role` VARCHAR(45) NULL,
  `date_registration` DATE NULL,
  PRIMARY KEY (`account_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GigaByte`.`category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GigaByte`.`category` ;

CREATE TABLE IF NOT EXISTS `GigaByte`.`category` (
  `category_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `parent_id` INT NULL,
  PRIMARY KEY (`category_id`),
  INDEX `parent_id_idx` (`parent_id` ASC),
  CONSTRAINT `category_parent_id`
    FOREIGN KEY (`parent_id`)
    REFERENCES `GigaByte`.`category` (`category_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GigaByte`.`images`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GigaByte`.`images` ;

CREATE TABLE IF NOT EXISTS `GigaByte`.`images` (
  `images_id` INT NOT NULL AUTO_INCREMENT,
  `product_id` INT NULL,
  `path` VARCHAR(300) NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`images_id`),
  INDEX `fk_product_id_images_idx` (`product_id` ASC),
  CONSTRAINT `fk_product_id_images`
    FOREIGN KEY (`product_id`)
    REFERENCES `GigaByte`.`product` (`product_id`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GigaByte`.`product`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GigaByte`.`product` ;

CREATE TABLE IF NOT EXISTS `GigaByte`.`product` (
  `product_id` INT NOT NULL AUTO_INCREMENT,
  `category_id` INT NULL,
  `image_id` INT NULL DEFAULT 1,
  `name` VARCHAR(45) NULL,
  `count` INT NULL,
  `price` VARCHAR(45) NULL,
  `description` TEXT NULL,
  `adding_date` DATE NULL,
  PRIMARY KEY (`product_id`),
  INDEX `category_id_idx` (`category_id` ASC),
  INDEX `oriduct_image_id_idx` (`image_id` ASC),
  CONSTRAINT `product_category_id`
    FOREIGN KEY (`category_id`)
    REFERENCES `GigaByte`.`category` (`category_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `oriduct_image_id`
    FOREIGN KEY (`image_id`)
    REFERENCES `GigaByte`.`images` (`images_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GigaByte`.`comments`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GigaByte`.`comments` ;

CREATE TABLE IF NOT EXISTS `GigaByte`.`comments` (
  `comments_id` INT NOT NULL AUTO_INCREMENT,
  `product_id` INT NOT NULL,
  `account_id` INT NOT NULL,
  `description` TEXT NOT NULL,
  `add_time` DATETIME NULL,
  PRIMARY KEY (`comments_id`),
  INDEX `product_id_idx` (`product_id` ASC),
  INDEX `account_id_idx` (`account_id` ASC),
  CONSTRAINT `product_id`
    FOREIGN KEY (`product_id`)
    REFERENCES `GigaByte`.`product` (`product_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `account_id`
    FOREIGN KEY (`account_id`)
    REFERENCES `GigaByte`.`account` (`account_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GigaByte`.`cart`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GigaByte`.`cart` ;

CREATE TABLE IF NOT EXISTS `GigaByte`.`cart` (
  `account_id` INT NOT NULL,
  PRIMARY KEY (`account_id`),
  CONSTRAINT `fk_cart_account_id`
    FOREIGN KEY (`account_id`)
    REFERENCES `GigaByte`.`account` (`account_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GigaByte`.`orders`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GigaByte`.`orders` ;

CREATE TABLE IF NOT EXISTS `GigaByte`.`orders` (
  `order_id` INT NOT NULL AUTO_INCREMENT,
  `account_id` INT NULL,
  `status` VARCHAR(45) NULL,
  `date_ordering` DATE NULL,
  PRIMARY KEY (`order_id`),
  INDEX `account_id_idx` (`account_id` ASC),
  CONSTRAINT `order_account_id`
    FOREIGN KEY (`account_id`)
    REFERENCES `GigaByte`.`account` (`account_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GigaByte`.`item_orders`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GigaByte`.`item_orders` ;

CREATE TABLE IF NOT EXISTS `GigaByte`.`item_orders` (
  `item_orders_id` INT NOT NULL AUTO_INCREMENT,
  `order_id` INT NULL,
  `product_id` INT NULL,
  `count` INT NULL,
  PRIMARY KEY (`item_orders_id`),
  INDEX `order_id_idx` (`order_id` ASC),
  INDEX `item_product_id_idx` (`product_id` ASC),
  CONSTRAINT `fk_item_order_id`
    FOREIGN KEY (`order_id`)
    REFERENCES `GigaByte`.`orders` (`order_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_item_product_id`
    FOREIGN KEY (`product_id`)
    REFERENCES `GigaByte`.`product` (`product_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GigaByte`.`features`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GigaByte`.`features` ;

CREATE TABLE IF NOT EXISTS `GigaByte`.`features` (
  `features_id` INT NOT NULL AUTO_INCREMENT,
  `product_id` INT NULL,
  `name` VARCHAR(45) NULL,
  `value` VARCHAR(45) NULL,
  PRIMARY KEY (`features_id`),
  INDEX `fk_features_product_id_idx` (`product_id` ASC),
  CONSTRAINT `fk_features_product_id`
    FOREIGN KEY (`product_id`)
    REFERENCES `GigaByte`.`product` (`product_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GigaByte`.`item_cart`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GigaByte`.`item_cart` ;

CREATE TABLE IF NOT EXISTS `GigaByte`.`item_cart` (
  `item_cart_id` INT NOT NULL AUTO_INCREMENT,
  `product_id` INT NULL,
  `cart_id` INT NULL,
  `count` INT NULL,
  PRIMARY KEY (`item_cart_id`),
  INDEX `fk_item_cart_product_id_idx` (`product_id` ASC),
  INDEX `fk_cart_id_idx` (`cart_id` ASC),
  CONSTRAINT `fk_item_cart_product_id`
    FOREIGN KEY (`product_id`)
    REFERENCES `GigaByte`.`product` (`product_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_cart_id`
    FOREIGN KEY (`cart_id`)
    REFERENCES `GigaByte`.`cart` (`account_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `GigaByte` ;

-- -----------------------------------------------------
-- function register_user
-- -----------------------------------------------------

USE `GigaByte`;
DROP function IF EXISTS `GigaByte`.`register_user`;

DELIMITER $$
USE `GigaByte`$$

CREATE FUNCTION register_user(login VARCHAR(30),  pass TEXT,  fname VARCHAR(30),  lname VARCHAR(30),
					 phone VARCHAR(30),  address VARCHAR(30),  email VARCHAR(30),  role VARCHAR(10) )
 RETURNS INT
BEGIN
	declare id INT default -1;
	INSERT INTO account(login,password,fname,lname,phone,address,email,role,date_registration)VALUES(login,pass,fname,lname,phone,address,email,role,NOW());
	select distinct last_insert_id() INTO id from account;

	INSERT INTO cart VALUES(id);
	RETURN id;

END;
$$

DELIMITER ;

delimiter $$
drop trigger if exists check_login$$
create trigger check_login before insert on account
for each row 
begin
	if (select count(*) from account where login = new.login) != 0 then
		signal sqlstate '45000' set MESSAGE_TEXT = 'User already exists';
    else
		set new.password = CONCAT(md5(new.password),md5(new.login));
    end if;
end;
$$
delimiter ;

delimiter ||
drop trigger if exists update_main_image||
create trigger update_main_image before delete on images
for each row 
begin
	declare id INT;
	declare pid INT; 
	SET id = OLD.images_id;
	
	SET pid = ( select distinct p.product_id from product p INNER JOIN images i ON p.image_id = i.images_id where p.image_id = id);

	if (select count(*) from product p INNER JOIN images i ON p.image_id = i.images_id  where p.image_id = id) != 0 then 
	update product set image_id = 1 where product_id = pid;
    end if;

end;
||
delimiter ;

insert into images(product_id, path, name) values(null,'http://gigabyteshop.hol.es/','no_image.gif');
select register_user('1',  '1',  '1',  '1','1','1', '1',  'admin');

insert into category(name,parent_id) values('Hardware', null);
insert into category(name,parent_id) values('Software', null);
insert into category(name,parent_id) values('Phone', null);
insert into category(name,parent_id) values('HDD', 1);
insert into category(name,parent_id) values('SDD', 1);
insert into category(name,parent_id) values('IDE', 2);
insert into category(name,parent_id) values('OS', 2);
insert into category(name,parent_id) values('Samsung', 3);
insert into category(name,parent_id) values('Samsung s4', 8);
insert into category(name,parent_id) values('Samsung s5', 8);
insert into category(name,parent_id) values('FLY', 3);
insert into category(name,parent_id) values('iPhone', 3);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

