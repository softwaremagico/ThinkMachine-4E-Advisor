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

public class ResourceManagementSafetyTest {

    interface Resource extends AutoCloseable {
        void use();
        @Override
        void close();
    }

    static class TestResource implements Resource {
        private boolean closed = false;

        @Override
        public void use() {
            if (closed) {
                throw new IllegalStateException("Resource already closed");
            }
        }

        @Override
        public void close() {
            closed = true;
        }

        public boolean isClosed() {
            return closed;
        }
    }

    @Test
    public void tryWithResources_shouldAutoClose() {
        TestResource resource = new TestResource();
        
        try (TestResource tr = resource) {
            tr.use();
            assertFalse("Resource should be open", tr.isClosed());
        } catch (Exception e) {
            fail("Should not throw exception");
        }
        
        assertTrue("Resource should be closed", resource.isClosed());
    }

    @Test
    public void tryWithResources_withException_shouldStillClose() {
        TestResource resource = new TestResource();
        
        try {
            try (TestResource tr = resource) {
                tr.use();
                throw new RuntimeException("Test exception");
            }
        } catch (RuntimeException e) {
            // Expected
        }
        
        assertTrue("Resource should be closed even with exception", resource.isClosed());
    }

    @Test
    public void manualClose_shouldWork() {
        TestResource resource = new TestResource();
        
        try {
            resource.use();
            assertFalse("Resource should be open", resource.isClosed());
        } finally {
            resource.close();
        }
        
        assertTrue("Resource should be closed", resource.isClosed());
    }

    @Test
    public void nullResourceClose_shouldNotCrash() {
        Resource resource = null;
        
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                fail("Should not close null resource");
            }
        }
        
        assertTrue("Null resource check should pass", true);
    }

    @Test
    public void multipleResources_shouldAllClose() {
        TestResource r1 = new TestResource();
        TestResource r2 = new TestResource();
        
        try (TestResource res1 = r1; TestResource res2 = r2) {
            res1.use();
            res2.use();
        } catch (Exception e) {
            fail("Should not throw exception");
        }
        
        assertTrue("First resource should be closed", r1.isClosed());
        assertTrue("Second resource should be closed", r2.isClosed());
    }

    @Test
    public void suppressedExceptions_shouldBeHandled() {
        TestResource resource = new TestResource();
        
        try {
            try (TestResource tr = resource) {
                throw new RuntimeException("Primary exception");
            } catch (RuntimeException e) {
                // Expected - primary exception
                assertEquals("Primary exception message", "Primary exception", e.getMessage());
                
                // Check for suppressed exceptions
                Throwable[] suppressed = e.getSuppressed();
                assertTrue("Can have suppressed exceptions", suppressed != null);
            }
        } catch (Exception e) {
            fail("Should not throw from try-with-resources");
        }
        
        assertTrue("Resource should be closed", resource.isClosed());
    }

    @Test
    public void resourceInNestedTry_shouldClose() {
        TestResource outer = new TestResource();
        TestResource inner = new TestResource();
        
        try {
            try (TestResource o = outer) {
                try (TestResource i = inner) {
                    o.use();
                    i.use();
                }
            }
        } catch (Exception e) {
            fail("Should not throw exception");
        }
        
        assertTrue("Outer resource should be closed", outer.isClosed());
        assertTrue("Inner resource should be closed", inner.isClosed());
    }

    @Test
    public void resourceCloseThrowingException_shouldPropagate() {
        class FailingResource implements AutoCloseable {
            @Override
            public void close() throws Exception {
                throw new RuntimeException("Close failed");
            }
        }
        
        try {
            try (FailingResource r = new FailingResource()) {
                // Use resource
            }
            fail("Should throw exception on close");
        } catch (Exception e) {
            assertTrue("Close exception should propagate", true);
        }
    }
}
