-- 记录省份信息
CREATE TABLE  province
(
	id				int(18)			NOT NULL	AUTO_INCREMENT,
	province_name			varchar(30)		NOT NULL 	COMMENT'省 名',
	province_code			int(18)	NOT NULL	COMMENT'省 代号',

	PRIMARY KEY (id)
)ENGINE=InnoDB;


-- 记录城市信息
CREATE TABLE  city
(
	id				int(18)			NOT NULL	AUTO_INCREMENT,
	city_name			varchar(30)		NOT NULL 	COMMENT'城市 名',
	city_code			varchar(128)	NOT NULL	COMMENT'城市 代号',
	province_id				int(18)			NOT NULL ,

	PRIMARY KEY (id)
)ENGINE=InnoDB;

-- 记录县信息
CREATE TABLE  county
(
	id				int(18)			NOT NULL	AUTO_INCREMENT,
	county_name			varchar(30)		NOT NULL 	COMMENT'县 名',
	weather_id			varchar(128)	NOT NULL	COMMENT'天气id',

     city_id				int(18)			NOT NULL,
	PRIMARY KEY (id)
)ENGINE=InnoDB;