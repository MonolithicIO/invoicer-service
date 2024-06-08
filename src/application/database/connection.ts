import { DataSource } from 'typeorm';

export const AppDataSource = new DataSource({
  type: 'postgres',
  host: 'localhost',
  port: 5432,
  username: 'invoicer',
  password: '123',
  database: 'invoicer-dev',
  synchronize: true,
  logging: true,
  subscribers: [],
  migrations: [],
  entities: [],
});
