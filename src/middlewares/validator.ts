import { NextFunction, Request, Response } from 'express';
import { validationResult } from 'express-validator';
import InvoicerError from '../errors/InvoicerError'

export const validationMiddleware =  async (req: Request, res: Response, next: NextFunction) => {
        const result = validationResult(req);
        if (result.isEmpty()) {
            next();
        } else {
            res.status(400).json(new InvoicerError(result.array()[0].msg));
        }
    };