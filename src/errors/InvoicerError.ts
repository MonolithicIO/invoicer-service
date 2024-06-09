export default class InvoicerError {
    message: string;
    timeStamp: string = new Date().toISOString();

    constructor(message: string) {
        this.message = message
    }
}