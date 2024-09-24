CREATE TABLE IF NOT EXISTS T_USER(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    email varchar(150) not null unique,
    password varchar(60) not null,
    verified boolean not null,
    created_at DATE DEFAULT CURRENT_DATE,
    updated_at DATE DEFAULT CURRENT_DATE
);