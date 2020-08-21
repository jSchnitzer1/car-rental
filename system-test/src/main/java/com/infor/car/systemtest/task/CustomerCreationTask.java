package com.infor.car.systemtest.task;

import com.infor.car.systemtest.service.AppConstants;
import com.infor.car.systemtest.service.CustomerService;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public final class CustomerCreationTask implements Runnable {

    private CustomerService service;
    private int taskId;

    @Override
    public void run() {
        service.create(AppConstants.getCustomersToBeCreated().get(taskId))
                .doOnError(e -> AppConstants.logError(e, "creating customer"))
                .subscribe(c -> {
                    AppConstants.displayObject("Create", c);
                });
        AppConstants.simulateDelayedProcessing(2000);
    }
}