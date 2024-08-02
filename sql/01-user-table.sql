CREATE TABLE IF NOT EXISTS T_USER(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    email varchar(150) not null unique,
    password varchar(20) not null,
    verified boolean not null
);