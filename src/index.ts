import express, { Request, Response } from 'express';
import dotenv from 'dotenv';

dotenv.config();

const app = express();
const port = 3000;

app.use(express.json);

app.listen(port, () => {
  console.log(`server running on port ${port}`);
});
