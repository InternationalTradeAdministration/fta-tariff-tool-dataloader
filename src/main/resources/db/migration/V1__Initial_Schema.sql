create table country (
    id bigint not null auto_increment primary key,
    code varchar(25) not null,
    name varchar(100) not null
);

insert into country (code, name) values ('KR', 'South Korea');
insert into country (code, name) values ('AU', 'Australia');
insert into country (code, name) values ('BH', 'Bahrain');
insert into country (code, name) values ('CA-USMCA', 'Canada - USMCA');

create table hs6 (
    code varchar(6) not null primary key,
    description varchar(255) null
);

create table product_type (
    id bigint not null auto_increment primary key,
    legacy_id bigint not null,
    description varchar(255)
);

create table rate (
  id bigint not null auto_increment primary key,
  year int not null,
  value varchar(255) not null
);

create table staging_basket (
    id bigint not null auto_increment primary key,
    legacy_id bigint not null,
    description varchar(255)
);

create table tariff (
    id bigint not null auto_increment primary key,
    legacy_id bigint not null,
    tariff_line varchar(255) null,
    description longtext null,
    sector_code varchar(255) null,
    base_rate varchar(255) null,
    base_rate_alt varchar(255) null,
    final_year int null,
    tariff_rate_quota int null,
    tariff_rate_quota_notes longtext,
    tariff_eliminated bit null,
    partner_name varchar(255) null,
    reporter_name varchar(255) null,
    partner_start_year int null,
    reporter_start_year int null,
    partner_agreement_name varchar(255) null,
    reporter_agreement_name varchar(255) null,
    quota_name varchar(255) null,
    rule_text longtext null,
    link_text varchar(255) null,
    link_url varchar(255) null,
    hs6_code varchar(6) not null,
    country_id bigint not null,
    staging_basket_id bigint not null,
    product_type_id bigint not null
);

create table tariff_rates (
    tariff_id bigint,
    rates_id bigint,
    primary key (tariff_id, rates_id)
);
