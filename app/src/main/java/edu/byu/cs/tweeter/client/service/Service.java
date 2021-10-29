package edu.byu.cs.tweeter.client.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Base Service class which creates a thread and executes the task
 */
public class Service {

    protected void runTask(Runnable taskType){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(taskType);
    }
}
