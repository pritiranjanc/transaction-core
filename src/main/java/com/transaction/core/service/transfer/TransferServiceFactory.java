package com.transaction.core.service.transfer;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TransferServiceFactory {

    private final Map<String, TransferService> services;

    public TransferServiceFactory(List<TransferService> services) {
        this.services = services.stream()
                .collect(Collectors.toMap(TransferService::supportedTransferType,
                        Function.identity()));
    }

    public TransferService getTransferService(String transferType){
        return services.get(transferType);
    }

}
