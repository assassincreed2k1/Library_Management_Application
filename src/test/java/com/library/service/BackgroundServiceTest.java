package com.library.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class BackgroundServiceTest {

    private BackgroundService backgroundService;

    @BeforeEach
    void setUp() {
        backgroundService = new BackgroundService();
    }

    @AfterEach
    void tearDown() {
        backgroundService.shutdown();
    }

    @Test
    void testStartNewThread() throws InterruptedException {
        AtomicBoolean taskExecuted = new AtomicBoolean(false);

        Runnable task = () -> taskExecuted.set(true);
        backgroundService.startNewThread(task);

        // Wait a short time to allow task to execute
        Thread.sleep(200);

        assertTrue(taskExecuted.get(), "Task should have been executed.");
    }

    @Test
    void testIsThreadRunning() {
        assertFalse(backgroundService.isThreadRunning(), "No threads should be running initially.");

        Runnable longTask = () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        };

        backgroundService.startNewThread(longTask);

        assertTrue(backgroundService.isThreadRunning(), "Thread should be running.");
    }

    @Test
    void testStopAllThreads() {
        Runnable longTask = () -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {
            }
        };

        backgroundService.startNewThread(longTask);
        backgroundService.stopAllThreads();

        assertTrue(backgroundService.getExecutor().isShutdown(), "Executor should be shutdown.");
    }

    @Test
    void testWaitForThreads() throws InterruptedException {
        Runnable shortTask = () -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }
        };

        backgroundService.startNewThread(shortTask);
        backgroundService.waitForThreads();

        assertTrue(backgroundService.getExecutor().isTerminated(), "All tasks should have completed.");
    }

    @Test
    void testShutdown() {
        backgroundService.shutdown();

        assertTrue(backgroundService.getExecutor().isShutdown(), "Executor should be shutdown.");
    }
}
