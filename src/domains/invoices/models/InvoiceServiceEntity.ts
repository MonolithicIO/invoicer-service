import { Column, CreateDateColumn, Entity, ManyToOne, PrimaryGeneratedColumn, UpdateDateColumn } from 'typeorm';
import { InvoiceEntity } from './InvoiceEntity';
import { Expose } from 'class-transformer';

@Entity()
export class InvoiceServiceEntity {

    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    name: string;

    @Column()
    quantity: number;

    @Column()
    unitPrice: number;

    @Expose()
    get totalPrice(): number {
        return this.quantity * this.unitPrice;
    }

    @ManyToOne(() => InvoiceEntity, (invoice) => invoice.id)
    invoice: Promise<InvoiceServiceEntity>;

    @CreateDateColumn()
    createdAt: Date;

    @UpdateDateColumn()
    updatedAt: Date;

}