package com.transaction.core.exception;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException(Long accoundId){
        super("No sufficient balance in account ::" + accoundId);
    }
}
