/*
 *  Copyright (C) 2024 Softwaremagico
 *
 *  This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero  <softwaremagico@gmail.com> Valencia (Spain).
 *
 *  This program is free software; you can redistribute it and/or modify it under  the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this Program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.softwaremagico.tm.advisor.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public final class AdvisorLog {

    private static final Logger logger = LoggerFactory.getLogger(AdvisorLog.class);

    private AdvisorLog() {

    }

    /**
     * Events that have business meaning (i.e. creating category, deleting form, ...). To follow user actions.
     *
     * @param message
     */
    private static void info(String message) {
        logger.info(message);
    }

    /**
     * Events that have business meaning (i.e. creating category, deleting form, ...). To follow user actions.
     */
    public static void info(String className, String message) {
        info(className + ": " + message);
    }

    /**
     * Shows not critical errors. I.e. Email address not found, permissions not allowed for this user, ...
     *
     * @param message
     */
    private static void warning(String message) {
        logger.warn(message);
    }

    /**
     * Shows not critical errors. I.e. Email address not found, permissions not allowed for this user, ...
     *
     * @param message
     */
    public static void warning(String className, String message) {
        warning(className + ": " + message);
    }

    /**
     * For following the trace of the execution. I.e. Knowing if the application access to a method, opening database
     * connection, etc.
     *
     * @param message
     */
    private static void debug(String message) {
        if (isDebugEnabled()) {
            logger.debug(message);
        }
    }

    /**
     * For following the trace of the execution. I.e. Knowing if the application access to a method, opening database
     * connection, etc.
     */
    public static void debug(String className, String message) {
        debug(className + ": " + message);
    }

    /**
     * To log any not expected error that can cause application malfuncionality. I.e. couldn't open database connection,
     * etc..
     *
     * @param message
     */
    private static void severe(String message) {
        logger.error(message);
    }

    /**
     * To log any not expected error that can cause application malfuncionality.
     *
     * @param message
     */
    public static void severe(String className, String message) {
        severe(className + ": " + message);
    }

    /**
     * To log java exceptions and log also the stack trace.
     *
     * @param className
     * @param throwable
     */
    public static void errorMessage(String className, Throwable throwable) {
        final String error = getStackTrace(throwable);
        severe(className, error);
    }

    private static String getStackTrace(Throwable throwable) {
        final Writer writer = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(writer);
        try {
            throwable.printStackTrace(printWriter);
            try {
                return writer.toString();
            } finally {
                try {
                    writer.close();
                } catch (IOException e) {
                    AdvisorLog.errorMessage(AdvisorLog.class.getName(), e);
                }
            }
        } finally {
            printWriter.close();
        }
    }

    public static boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }
}
