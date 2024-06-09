import { Request } from "express";
import {SwiftRegex} from '../../../utils/regex'

export const createInvoiceValidator = {
    senderCompanyName: {
        errorMessage: 'Missing field senderCompany name',
        trim: true,
        notEmpty: true
    },
    senderCompanyAddress: {
        errorMessage: 'Missing field senderCompanyAddress',
        trim: true,
        notEmpty: true
    },
    issueDate: {
        errorMessage: 'Invalid issue date',
        notEmpty: true,
        trim: true,
        isDate: true,
        toDate: true
    },
    dueDate: {
        errorMessage: 'Invalid due date',
        notEmpty: true,
        trim: true,
        isDate: true
    },
    invoiceNumber: {
        errorMessage: 'Missing field invoice number',
        trim: true,
        notEmpty: true
    },
    'paymentInfo.recipientCompanyName': {
        errorMessage: 'Missing field recipient company name',
        trim: true,
        notEmpty: true
    },
    'paymentInfo.recipientCompanyAddress': {
        errorMessage: 'Missing field recipient company address',
        trim: true,
        notEmpty: true
    },
    'paymentInfo.beneficiaryName': {
        errorMessage: 'Missing field paymentInfo:beneficiaryName',
        trim: true,
        notEmpty: true
    },
    'paymentInfo.beneficiaryIBAN': {
        errorMessage: 'Missing field paymentInfo:beneficiaryIban',
        trim: true,
        notEmpty: true
    },
    'paymentInfo.beneficiarySwift': {
        errorMessage: 'Invalid field paymentInfo.beneficiarySwift',
        trim: true,
        notEmpty: true,
        matches: {
            options: SwiftRegex
        }
    },
    'paymentInfo.beneficiaryBankName': {
        errorMessage: 'Missing field paymentInfo:beneficiaryBankName',
        trim: true,
        notEmpty: true
    },
    'paymentInfo.beneficiaryBankAddress': {
        errorMessage: 'Missing field paymentInfo:beneficiaryBankAddress',
        trim: true,
        notEmpty: true
    },
    'paymentInfo.intermediaryAccountNumber': {
        errorMessage: 'Missing field paymentInfo:intermediaryAccountNumber',
        trim: true,
        notEmpty: true,
    },
    'paymentInfo.intermediarySwift': {
        errorMessage: 'Missing field paymentInfo:intermediarySwiftCode',
        trim: true,
        notEmpty: true,
        matches: {
            options: SwiftRegex
        }
    },
    'paymentInfo.intermediaryBankName': {
        errorMessage: 'Missing field paymentInfo:intermediaryBankName',
        trim: true,
        notEmpty: true,
        matches: {
            options: SwiftRegex
        }
    },
    'paymentInfo.intermediaryBankAddress': {
        errorMessage: 'Missing field paymentInfo:intermediaryBankAddress',
        trim: true,
        notEmpty: true,
        matches: {
            options: SwiftRegex
        }
    },
};