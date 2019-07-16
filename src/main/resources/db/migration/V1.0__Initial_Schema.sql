create table country (
    id bigint not null auto_increment primary key,
    code varchar(25) not null,
    name varchar(100) not null,
    endpointme_freshen_url varchar(1000) null
);

insert into country (code, name, endpointme_freshen_url) values ('AU', 'Australia','https://api.govwizely.com/v1/australia_tariff_rates/freshen.json?api_key=1cCV-1ddALkUhGpWa2L-aYit');
insert into country (code, name, endpointme_freshen_url) values ('BH', 'Bahrain', 'https://api.govwizely.com/v1/bahrain_tariff_rates/freshen.json?api_key=1cCV-1ddALkUhGpWa2L-aYit');
insert into country (code, name, endpointme_freshen_url) values ('CA', 'Canada USMCA','https://api.govwizely.com/v1/canada_tariff_rates/freshen.json?api_key=1cCV-1ddALkUhGpWa2L-aYit');
insert into country (code, name) values ('CL', 'Chile');
insert into country (code, name) values ('CO', 'Colombia');
insert into country (code, name) values ('CR', 'Costa Rica');
insert into country (code, name) values ('DO', 'Dominican Republic');
insert into country (code, name) values ('SV', 'El Salvador');
insert into country (code, name) values ('GT', 'Guatemala');
insert into country (code, name) values ('HN', 'Honduras');
insert into country (code, name) values ('MX', 'Mexico USMCA');
insert into country (code, name) values ('MA', 'Morocco');
insert into country (code, name) values ('NI', 'Nicaragua');
insert into country (code, name) values ('OM', 'Oman');
insert into country (code, name) values ('PA', 'Panama');
insert into country (code, name) values ('PE', 'Peru');
insert into country (code, name, endpointme_freshen_url) values ('KR', 'South Korea','https://api.govwizely.com/v1/south_korea_tariff_rates/freshen.json?api_key=1cCV-1ddALkUhGpWa2L-aYit');
insert into country (code, name) values ('SG', 'Singapore');

create table hs6 (
    id bigint not null auto_increment primary key,
    code varchar(6) not null,
    description varchar(255) null
);

create table product_type (
    id bigint not null auto_increment primary key,
    description varchar(255)
);

create table staging_basket (
    id bigint not null auto_increment primary key,
    description varchar(255)
);

create table tariff (
    id bigint not null auto_increment primary key,
    legacy_id bigint not null,
    tariff_line varchar(255) null,
    description varchar(3000) null,
    sector_code varchar(255) null,
    base_rate varchar(255) null,
    final_year int null,
    tariff_rate_quota int null,
    tariff_rate_quota_note varchar(500),
    tariff_eliminated bit null,
    partner_name varchar(255) null,
    reporter_name varchar(255) null,
    partner_start_year int null,
    reporter_start_year int null,
    quota_name varchar(255) null,
    rule_text varchar(5000) null,
    hs6_id bigint not null,
    country_id bigint not null,
    staging_basket_id bigint not null,
    product_type_id bigint not null
);

create table rate (
  id bigint not null auto_increment primary key,
  tariff_id bigint,
  year int not null,
  value varchar(255) not null,
  unique(tariff_id, year),
  foreign key (tariff_id) references tariff(id) on delete cascade
 );

create table link (
  id bigint not null auto_increment primary key,
  tariff_id bigint,
  link_url varchar(255) not null,
  link_text varchar(255),
  unique(tariff_id, link_url),
  foreign key (tariff_id) references tariff(id) on delete cascade
);
