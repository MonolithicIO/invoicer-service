import { Router, Request, Response, NextFunction } from 'express';
import { checkSchema } from 'express-validator';
import { createInvoiceValidator } from '../validator/CreateInvoiceValidator';
import { validationMiddleware } from '../../../middlewares/validator';

const invoiceRouter = Router();

invoiceRouter.get('/invoices', (req, res, next) => {
    res.send('get invoices');
});

invoiceRouter.post(
    '/invoices',
    checkSchema(createInvoiceValidator),
    validationMiddleware,
    async (req: Request, res: Response, next: NextFunction) => {
        res.send('create invoice validated');
    });

invoiceRouter.get('/invoices/:id', (req, res, next) => {
    res.send(`get invoice id by id ${req.params.id}`);
});

invoiceRouter.delete('/invoices:id', (req, res) => {
    res.send(`deleting invoice by id ${req.params.id}`);
});

export default invoiceRouter;