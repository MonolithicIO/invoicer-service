CREATE TABLE IF NOT EXISTS t_invoice (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    external_id varchar unique not null,
    sender_company_name varchar not null,
    sender_company_address varchar not null,
    recipient_company_name varchar not null,
    recipient_company_address varchar not null,
    issue_date date not null,
    due_date date not null,
    beneficiary_name varchar not null,
    beneficiary_iban varchar not null,
    beneficiary_swift varchar(11) not null,
    beneficiary_bank_name varchar not null,
    beneficiary_bank_address varchar not null,
    intermediary_iban varchar,
    intermediary_swift varchar(11),
    intermediary_bank_name varchar,
    intermediary_bank_address varchar
);
