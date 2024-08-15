create table if not exists t_intermediary(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name varchar not null,
    iban varchar not null,
    swift varchar(11) not null,
    bank_name varchar not null,
    bank_address varchar not null
)