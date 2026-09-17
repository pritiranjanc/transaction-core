package com.transaction.core.exception;

public class InvalidTransactionException extends RuntimeException{

    public InvalidTransactionException(String msg){
        super(msg);
    }
}
