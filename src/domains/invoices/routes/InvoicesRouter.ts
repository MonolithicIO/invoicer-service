import { Router } from 'express';

const invoiceRouter = Router();

invoiceRouter.get('/invoices', (req, res, next) => {
    res.send('get invoices');
});

invoiceRouter.post('/invoices', (req, res, next) => {
    res.send('create invoice');
});

invoiceRouter.get('/invoices/:id', (req, res, next) => {
    res.send(`get invoice id by id ${req.params.id}`);
});

invoiceRouter.delete('/invoices:id', (req, res) => {
    res.send(`deleting invoice by id ${req.params.id}`);
});

export default invoiceRouter;