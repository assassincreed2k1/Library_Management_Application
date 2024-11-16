package com.library.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Service class that manages background threads using an ExecutorService.
 * It allows tasks to be submitted for execution and provides methods
 * to control the execution state of those tasks.
 */
public class BackgroundService {

    private ExecutorService executor;

    /**
     * Constructor initializes the executor with a fixed thread pool size.
     * The size of the pool is the larger of 6 or the number of available processors.
     */
    public BackgroundService() {
        executor = Executors.newFixedThreadPool(Math.max(6, Runtime.getRuntime().availableProcessors()));
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    /**
     * Starts a new background thread to execute the given task.
     * 
     * @param task The task to be executed in a separate thread.
     */
    public void startNewThread(Runnable task) {
        executor.submit(task);
    }

    /**
     * Checks if any threads are currently running.
     * 
     * @return true if threads are still running, false otherwise.
     */
    public boolean isThreadRunning() {
        return !executor.isTerminated();
    }

    /**
     * Stops all threads immediately by shutting down the executor.
     * Any tasks that are still running will be interrupted.
     */
    public void stopAllThreads() {
        executor.shutdownNow();
    }

    /**
     * Waits for all submitted tasks to finish executing.
     * This method will block the calling thread until all tasks have completed.
     * 
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    public void waitForThreads() throws InterruptedException {
        while (!executor.isTerminated()) {
            Thread.sleep(100);
        }
    }

    /**
     * Gracefully shuts down the executor, waiting for the currently executing tasks
     * to finish before shutting down.
     */
    public void shutdown() {
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
    }
}
