CREATE TABLE IF NOT EXISTS t_beneficiary(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name varchar not null,
    iban varchar not null,
    swift varchar(11) not null,
    bank_name varchar not null,
    bank_address varchar not null,
    user_id uuid not null,
    is_deleted boolean default false,
        constraint fk_beneficiary_to_user
                foreign key (user_id)
                    references t_user(id)
                    on delete CASCADE
);