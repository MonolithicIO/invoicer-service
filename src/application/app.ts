import express from 'express';
import dotenv from 'dotenv';
import { AppDataSource } from './database/connection';
import invoicesRouter from '../domains/invoices/routes/InvoicesRouter';

dotenv.config();

AppDataSource.initialize()
    .then(() => {
        console.log('database started');
    })
    .catch((err) => {
        console.log(err);
    });

const app = express();

app.use(express.json());
app.use(invoicesRouter);

export default app;
