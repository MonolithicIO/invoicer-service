import { AppDataSource } from '../../../application/database/connection';
import { InvoiceEntity } from '../entities/InvoiceEntity';

class InvoiceRepository {

    datasource = AppDataSource;

    create(): string {
        const repository = this.datasource.getRepository(InvoiceEntity);
        const newInvoice = repository.create();
        2;
    }
}