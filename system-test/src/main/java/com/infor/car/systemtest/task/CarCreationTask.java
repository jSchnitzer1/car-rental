package com.infor.car.systemtest.task;

import com.infor.car.systemtest.service.AppConstants;
import com.infor.car.systemtest.service.CarService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CarCreationTask implements Runnable {
    private CarService service;
    private int taskId;

    @Override
    public void run() {
        service.create(AppConstants.getCarsToBeRegistered().get(taskId))
                .doOnError(e -> AppConstants.logError(e, "creating car"))
                .subscribe(c -> {
                    AppConstants.displayObject("Created", c);
                });
        AppConstants.simulateDelayedProcessing(2000);
    }
}
