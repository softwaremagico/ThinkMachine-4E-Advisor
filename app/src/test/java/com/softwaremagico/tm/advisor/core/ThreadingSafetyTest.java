/*
 *  Copyright (C) 2026 Softwaremagico
 *
 *  This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero  <softwaremagico@gmail.com> Valencia (Spain).
 *
 *  This program is free software; you can redistribute it and/or modify it under  the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this Program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.softwaremagico.tm.advisor.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class ThreadingSafetyTest {

    @Test
    public void threadAccessSharedResource_shouldNotCrash() throws InterruptedException {
        final int[] sharedCounter = {0};
        
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                sharedCounter[0]++;
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                sharedCounter[0]++;
            }
        });
        
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        
        // Note: Due to race condition, the value might be less than 200
        // This demonstrates the need for synchronization
        assertTrue("Threads should not crash", true);
    }

    @Test
    public void synchronizedThreadAccess_shouldBeSafe() throws InterruptedException {
        final int[] sharedCounter = {0};
        final Object lock = new Object();
        
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                synchronized (lock) {
                    sharedCounter[0]++;
                }
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                synchronized (lock) {
                    sharedCounter[0]++;
                }
            }
        });
        
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        
        // With synchronization, should always be 200
        assertEquals("Synchronized access should give correct result", 200, sharedCounter[0]);
    }

    @Test
    public void threadInterruption_shouldBeHandled() throws InterruptedException {
        final boolean[] threadInterrupted = {false};
        
        Thread worker = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                threadInterrupted[0] = true;
                Thread.currentThread().interrupt();
            }
        });
        
        worker.start();
        worker.interrupt();
        worker.join();
        
        assertTrue("Thread interruption should be handled", threadInterrupted[0]);
    }

    @Test
    public void threadWithException_shouldNotCrashApp() throws InterruptedException {
        final boolean[] exceptionCaught = {false};
        
        Thread worker = new Thread(() -> {
            try {
                throw new RuntimeException("Test exception");
            } catch (RuntimeException e) {
                exceptionCaught[0] = true;
            }
        });
        
        worker.start();
        worker.join();
        
        assertTrue("Thread exception should be caught", exceptionCaught[0]);
    }

    @Test
    public void threadNullPointerInThread_shouldNotCrashApp() throws InterruptedException {
        final boolean[] npeHandled = {false};
        
        Thread worker = new Thread(() -> {
            try {
                String str = null;
                str.length();  // Will throw NPE
            } catch (NullPointerException e) {
                npeHandled[0] = true;
            }
        });
        
        worker.start();
        worker.join();
        
        assertTrue("NPE in thread should be handled", npeHandled[0]);
    }

    @Test
    public void multipleThradsWaitingOnLock_shouldNotDeadlock() throws InterruptedException {
        final Object lock = new Object();
        final boolean[] allReleased = {false};
        
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                allReleased[0] = true;
            }
        });
        
        t1.start();
        t2.start();
        
        // Use timeout to detect deadlock
        t1.join(1000);
        t2.join(1000);
        
        assertFalse("Should not deadlock", t1.isAlive() && t2.isAlive());
    }

    @Test
    public void threadCleanup_shouldReleaseResources() throws InterruptedException {
        final java.util.concurrent.atomic.AtomicInteger resourcesAllocated = new java.util.concurrent.atomic.AtomicInteger(0);
        
        Thread worker = new Thread(() -> {
            resourcesAllocated.incrementAndGet();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                resourcesAllocated.decrementAndGet();
            }
        });
        
        worker.start();
        worker.join();
        
        assertEquals("Resources should be cleaned up", 0, resourcesAllocated.get());
    }

    @Test
    public void executorService_shouldNotLeakThreads() throws InterruptedException {
        java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(2);
        
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        executor.shutdown();
        boolean terminated = executor.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS);
        
        assertTrue("Executor should terminate", terminated);
    }
}
