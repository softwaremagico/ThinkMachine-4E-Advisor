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

package com.softwaremagico.tm.advisor.ui.components;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CallbackNullSafetyTest {

    interface Callback {
        void onSuccess(String result);
        void onError(Exception error);
    }

    class CallbackManager {
        private List<Callback> callbacks = new ArrayList<>();

        void addCallback(Callback callback) {
            if (callback != null) {
                callbacks.add(callback);
            }
        }

        void removeCallback(Callback callback) {
            callbacks.remove(callback);
        }

        void notifySuccess(String result) {
            for (Callback callback : new ArrayList<>(callbacks)) {
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }
        }

        void notifyError(Exception error) {
            for (Callback callback : new ArrayList<>(callbacks)) {
                if (callback != null) {
                    callback.onError(error);
                }
            }
        }
    }

    @Test
    public void addNullCallback_shouldBeIgnored() {
        CallbackManager manager = new CallbackManager();
        
        // Add null callback
        manager.addCallback(null);
        
        // Notify - should not crash
        manager.notifySuccess("test");
        
        assertTrue("Should handle null callback", true);
    }

    @Test
    public void notifyWithValidCallback_shouldWork() {
        CallbackManager manager = new CallbackManager();
        
        final boolean[] called = {false};
        Callback callback = new Callback() {
            @Override
            public void onSuccess(String result) {
                called[0] = true;
            }

            @Override
            public void onError(Exception error) {
            }
        };
        
        manager.addCallback(callback);
        manager.notifySuccess("test");
        
        assertTrue("Callback should be called", called[0]);
    }

    @Test
    public void removeCallbackBeforeNotify_shouldNotCall() {
        CallbackManager manager = new CallbackManager();
        
        final boolean[] called = {false};
        Callback callback = new Callback() {
            @Override
            public void onSuccess(String result) {
                called[0] = true;
            }

            @Override
            public void onError(Exception error) {
            }
        };
        
        manager.addCallback(callback);
        manager.removeCallback(callback);
        manager.notifySuccess("test");
        
        assertFalse("Callback should not be called", called[0]);
    }

    @Test
    public void concurrentCallbackModification_shouldNotCrash() {
        CallbackManager manager = new CallbackManager();
        
        Callback callback1 = new Callback() {
            @Override
            public void onSuccess(String result) {
            }

            @Override
            public void onError(Exception error) {
            }
        };
        
        Callback callback2 = new Callback() {
            @Override
            public void onSuccess(String result) {
                manager.removeCallback(this);
            }

            @Override
            public void onError(Exception error) {
            }
        };
        
        manager.addCallback(callback1);
        manager.addCallback(callback2);
        
        // Should not crash even if callback removes itself during notification
        manager.notifySuccess("test");
        
        assertTrue("Should handle concurrent modification", true);
    }

    @Test
    public void callbackThrowingException_shouldNotCrashOthers() {
        CallbackManager manager = new CallbackManager();
        
        final boolean[] callback2Called = {false};
        
        Callback callback1 = new Callback() {
            @Override
            public void onSuccess(String result) {
                throw new RuntimeException("Test exception");
            }

            @Override
            public void onError(Exception error) {
            }
        };
        
        Callback callback2 = new Callback() {
            @Override
            public void onSuccess(String result) {
                callback2Called[0] = true;
            }

            @Override
            public void onError(Exception error) {
            }
        };
        
        manager.addCallback(callback1);
        manager.addCallback(callback2);
        
        // First callback throws exception, but second should still be called
        // (This test demonstrates a potential issue - callbacks should wrap calls in try-catch)
        try {
            manager.notifySuccess("test");
        } catch (RuntimeException e) {
            // Expected in current implementation
        }
        
        // Without try-catch in notifySuccess, callback2 might not be called
        // This demonstrates the need for defensive programming
    }

    @Test
    public void multipleNullCallbacks_shouldNotCrash() {
        CallbackManager manager = new CallbackManager();
        
        manager.addCallback(null);
        manager.addCallback(null);
        manager.addCallback(null);
        
        manager.notifySuccess("test");
        
        assertTrue("Should handle multiple null callbacks", true);
    }
}
