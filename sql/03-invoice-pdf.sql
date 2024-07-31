CREATE TYPE pdf_status as ENUM ('ok', 'pending', 'failed');

CREATE TABLE IF NOT EXISTS t_invoice_pdf(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    path varchar,
    status pdf_status not null,
    invoice_id uuid not null,
    created_at date default CURRENT_DATE,
    updated_at date default CURRENT_DATE,
    constraint fk_t_invoice_pdf
        foreign key (invoice_id)
            references t_invoice(id)
            ON DELETE CASCADE
);