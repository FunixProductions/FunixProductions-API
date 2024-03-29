create table funixproductions_billing
(
    id                          bigint generated by default as identity primary key,
    created_at                  timestamp not null,
    updated_at                  timestamp,
    uuid                        varchar(255) not null constraint UK_billing_public_id unique,
    percentage_discount         double precision,
    price_ht                    double precision not null,
    price_tax                   double precision not null,
    price_ttc                   double precision not null,
    vat_information             varchar(255),
    billing_description         varchar(300)     not null,
    billed_entity_address       varchar(255),
    billed_entity_city          varchar(255),
    billed_entity_email         varchar(255)     not null,
    billed_entity_funix_prod_id varchar(255),
    billed_entity_name          varchar(255)     not null,
    billed_entity_phone         varchar(255),
    billed_entity_siret         varchar(255),
    billed_entity_tva_number    varchar(255),
    billed_entity_website       varchar(255),
    billed_entity_zip_code      varchar(255),
    invoice_file_path           varchar(255),
    payment_origin              varchar(255)     not null,
    payment_type                varchar(255)     not null
);
