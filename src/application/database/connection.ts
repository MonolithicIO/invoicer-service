import 'reflect-metadata';
import { DataSource } from 'typeorm';
import 'dotenv/config';
import { InvoiceEntity } from '../../domains/invoices/models/InvoiceEntity';
import { InvoicePaymentInfoEntity } from '../../domains/invoices/models/InvoicePaymentInfoEntity';
import { InvoiceServiceEntity } from '../../domains/invoices/models/InvoiceServiceEntity';

export const AppDataSource = new DataSource({
    type: 'postgres',
    host: 'localhost',
    port: 5432,
    username: process.env.DB_USERNAME,
    password: process.env.DB_PASSWORD,
    database: process.env.DB_NAME,
    synchronize: false,
    logging: true,
    subscribers: [],
    migrations: [],
    entities: [InvoiceEntity, InvoicePaymentInfoEntity, InvoiceServiceEntity]
});
