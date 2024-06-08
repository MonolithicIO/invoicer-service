import {
    Entity,
    Column,
    PrimaryGeneratedColumn,
    OneToMany,
    OneToOne,
    CreateDateColumn,
    UpdateDateColumn
} from 'typeorm';
import { InvoiceServiceEntity } from './InvoiceServiceEntity';
import { InvoicePaymentInfoEntity } from './InvoicePaymentInfoEntity';

@Entity()
export class InvoiceEntity {

    @PrimaryGeneratedColumn('uuid')
    id: string;

    @Column()
    senderCompanyName: string;

    @Column()
    senderCompanyAddress: string;

    @Column()
    issueDate: Date;

    @Column()
    dueDate: Date;

    @Column()
    invoiceNumber: string;

    @Column()
    recipientCompanyName: string;

    @Column()
    recipientCompanyAddress: string;

    @OneToMany(() => InvoiceServiceEntity, (service) => service.invoice)
    services: InvoiceServiceEntity[];

    @OneToOne(() => InvoicePaymentInfoEntity)
    paymentInfo: InvoicePaymentInfoEntity;

    @CreateDateColumn()
    createdAt: Date;

    @UpdateDateColumn()
    updatedAt: Date;


}