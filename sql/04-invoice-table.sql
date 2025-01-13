CREATE TABLE IF NOT EXISTS t_invoice (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    external_id varchar unique not null,
    sender_company_name varchar not null,
    sender_company_address varchar not null,
    recipient_company_name varchar not null,
    recipient_company_address varchar not null,
    issue_date date not null,
    due_date date not null,
    created_at TIMESTAMP DEFAULT CURRENT_DATE,
    updated_at TIMESTAMP DEFAULT CURRENT_DATE,
    user_id uuid not null,
    beneficiary_id uuid not null,
    intermediary_id uuid,
    is_deleted boolean default false,
    constraint fk_invoice_to_user
        foreign key (user_id)
            references t_user(id)
            on delete CASCADE,
    constraint fk_invoice_to_beneficiary
        foreign key (beneficiary_id)
            references t_beneficiary(id),
    constraint fk_invoice_to_intermediary
            foreign key (intermediary_id)
                references t_intermediary(id)
);