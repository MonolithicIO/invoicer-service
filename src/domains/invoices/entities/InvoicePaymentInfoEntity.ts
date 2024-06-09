import { Column, CreateDateColumn, Entity, OneToOne, PrimaryGeneratedColumn, UpdateDateColumn } from 'typeorm';
import { InvoiceEntity } from './InvoiceEntity';
import { JoinColumn } from 'typeorm';

@Entity()
export class InvoicePaymentInfoEntity {

    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    beneficiaryName: string;

    @Column()
    beneficiaryIBAN: string;

    @Column()
    beneficiarySwift: string;

    @Column()
    beneficiaryBankName: string;

    @Column()
    beneficiaryBankAddress: string;

    @Column({ nullable: true })
    intermediaryAccountNumber: string;

    @Column({ nullable: true })
    intermediarySwift: string;

    @Column({ nullable: true })
    intermediaryBankName: string;

    @Column({ nullable: true })
    intermediaryBankAddress: string;

    @OneToOne(() => InvoiceEntity)
    @JoinColumn()
    invoice: Promise<InvoiceEntity>;

    @CreateDateColumn()
    createdAt: Date;

    @UpdateDateColumn()
    updatedAt: Date;

}