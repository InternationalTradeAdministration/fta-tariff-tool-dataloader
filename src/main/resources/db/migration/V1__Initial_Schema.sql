CREATE TABLE COUNTRY (
    CODE VARCHAR(2) NOT NULL PRIMARY KEY,
    NAME VARCHAR(100) NOT NULL
);

INSERT INTO COUNTRY (CODE, NAME) VALUES ('KR', 'SOUTH KOREA');
INSERT INTO COUNTRY (CODE, NAME) VALUES ('AU', 'AUSTRALIA');

CREATE TABLE HS6 (
    CODE VARCHAR(6) NOT NULL PRIMARY KEY,
    DESCRIPTION VARCHAR(255) NULL
);

CREATE TABLE PRODUCT_TYPE (
    ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    LEGACY_ID BIGINT NOT NULL,
    DESCRIPTION VARCHAR(255)
);

CREATE TABLE RATE (
  ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  YEAR INT NOT NULL,
  VALUE FLOAT NULL,
  ALT VARCHAR(255) NULL
);

CREATE TABLE STAGING_BASKET (
    ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    LEGACY_ID BIGINT NOT NULL,
    DESCRIPTION VARCHAR(255)
);

CREATE TABLE TARIFF (
    ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    LEGACY_ID BIGINT NOT NULL,
    COUNTRY VARCHAR(2) NOT NULL,
    TARIFF_LINE VARCHAR(255) NULL,
    DESCRIPTION VARCHAR(255) NULL,
    SECTOR_CODE VARCHAR(255) NULL,
    BASE_RATE VARCHAR(255) NULL,
    BASE_RATE_ALT VARCHAR(255) NULL,
    FINAL_YEAR INT NULL,
    TARIFF_RATE_QUOTA INT NULL,
    TARIFF_RATE_QUOTA_NOTES LONGTEXT,
    TARIFF_ELIMINATED BIT NULL,
    PARTNER_NAME VARCHAR(255) NULL,
    REPORTER_NAME VARCHAR(255) NULL,
    PARTNER_START_YEAR INT NULL,
    REPORTER_START_YEAR INT NULL,
    PARTNER_AGREEMENT_NAME VARCHAR(255) NULL,
    REPORTER_AGREEMENT_NAME VARCHAR(255) NULL,
    QUOTA_NAME VARCHAR(255) NULL,
    RULE_TEXT LONGTEXT NULL,
    LINK_TEXT VARCHAR(255) NULL,
    LINK_URL VARCHAR(255) NULL,
    HS6_CODE VARCHAR(6) NOT NULL,
    STAGING_BASKET_ID BIGINT NOT NULL,
    PRODUCT_TYPE_ID BIGINT NOT NULL
);

CREATE TABLE TARIFF_RATES (
    TARIFF_ID BIGINT,
    RATES_ID BIGINT,
    PRIMARY KEY (TARIFF_ID, RATES_ID)
)
