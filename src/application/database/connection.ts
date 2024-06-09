import 'reflect-metadata';
import { DataSource } from 'typeorm';
import 'dotenv/config';
import { InvoiceEntity } from '../../domains/invoices/entities/InvoiceEntity';
import { InvoicePaymentInfoEntity } from '../../domains/invoices/entities/InvoicePaymentInfoEntity';
import { InvoiceServiceEntity } from '../../domains/invoices/entities/InvoiceServiceEntity';

export const AppDataSource = new DataSource({
    type: 'postgres',
    host: 'localhost',
    port: 5432,
    username: process.env.DB_USERNAME,
    password: process.env.DB_PASSWORD,
    database: process.env.DB_NAME,
    synchronize: true,
    logging: true,
    subscribers: [],
    migrations: ['./migrations/*.ts'],
    entities: [InvoiceEntity, InvoicePaymentInfoEntity, InvoiceServiceEntity]
});
