CREATE TABLE IF NOT EXISTS t_invoice_activity(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    description varchar not null,
    quantity integer not null,
    unit_price bigint not null,
    invoice_id uuid not null,
    created_at date default CURRENT_DATE,
    updated_at date default CURRENT_DATE,
    constraint fk_invoice
        foreign key (invoice_id)
            references t_invoice(id)
            ON DELETE CASCADE
);