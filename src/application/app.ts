import express from 'express';
import dotenv from 'dotenv';
import { AppDataSource } from './database/connection';

dotenv.config();

AppDataSource.initialize()
  .then(() => {
    console.log('database started');
  })
  .catch((err) => {
    console.log(err);
  });

const app = express();
app.use(express.json);

export default app;
