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

import com.softwaremagico.tm.advisor.ui.main.DebugExceptionReporter;
import com.softwaremagico.tm.log.BasicLogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import java.lang.reflect.Method;

public final class AdvisorLog extends BasicLogger {
    private static final String LOG_TAG = "AdvisorLog";
    private static final String LOG_CLASS = "android.util.Log";

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvisorLog.class);

    /**
     * Events that have business meaning (i.e. creating category, deleting form,
     * ...). To follow user actions.
     *
     * @param className       the name of the class to log.
     * @param messageTemplate string with static text as template.
     * @param arguments       parameters to fill up the template
     */
    public static void info(String className, String messageTemplate, Object... arguments) {
        logToAndroidInfo(className, messageTemplate, arguments);
        info(LOGGER, className, messageTemplate, arguments);
    }

    public static void info(Class<?> clazz, String messageTemplate, Object... arguments) {
        info(clazz.getName(), messageTemplate, arguments);
    }

    /**
     * Shows not critical errors. I.e. Email address not found, permissions not
     * allowed for this user, ...
     *
     * @param className       the name of the class to log.
     * @param messageTemplate string with static text as template.
     * @param arguments       parameters to fill up the template
     */
    public static void warning(String className, String messageTemplate, Object... arguments) {
        logToAndroidWarn(className, messageTemplate, arguments);
        warning(LOGGER, className, messageTemplate, arguments);
    }

    public static void warning(Class<?> clazz, String messageTemplate, Object... arguments) {
        warning(clazz.getName(), messageTemplate, arguments);
    }

    /**
     * For following the trace of the execution. I.e. Knowing if the application
     * access to a method, opening database connection, etc.
     *
     * @param className       the name of the class to log.
     * @param messageTemplate string with static text as template.
     * @param arguments       parameters to fill up the template
     */
    public static void debug(String className, String messageTemplate, Object... arguments) {
        logToAndroidDebug(className, messageTemplate, arguments);
        debug(LOGGER, className, messageTemplate, arguments);
    }

    public static void debug(Class<?> clazz, String messageTemplate, Object... arguments) {
        debug(clazz.getName(), messageTemplate, arguments);
    }

    /**
     * To log any not expected error that can cause application malfunction.
     *
     * @param className       the name of the class to log.
     * @param messageTemplate string with static text as template.
     * @param arguments       parameters to fill up the template
     */
    public static void severe(String className, String messageTemplate, Object... arguments) {
        logToAndroidError(className, messageTemplate, arguments);
        severe(LOGGER, className, messageTemplate, arguments);
    }

    public static void severe(Class<?> clazz, String messageTemplate, Object... arguments) {
        severe(clazz.getName(), messageTemplate, arguments);
    }

    public static void errorMessage(Class<?> clazz, Throwable throwable) {
        errorMessage(clazz.getName(), throwable);
    }

    public static void errorMessage(String className, Throwable throwable) {
        logToAndroidThrowable(className, throwable);
        errorMessageNotification(LOGGER, className, throwable);
        DebugExceptionReporter.reportIfEnabled(className, throwable);
    }

    /**
     * To log java exceptions and log also the stack trace. If enabled, also can
     * send an email to the administrator to alert of the error.
     *
     * @param className       the name of the class to log.
     * @param messageTemplate string with static text as template.
     * @param arguments       parameters to fill up the template
     */
    public static void errorMessage(String className, String messageTemplate, Object... arguments) {
        logToAndroidError(className, messageTemplate, arguments);
        errorMessageNotification(LOGGER, className, messageTemplate, arguments);
    }

    public static void errorMessage(Object object, Throwable throwable) {
        errorMessage(object.getClass().getName(), throwable);
    }

    public static boolean isDebugEnabled() {
        return LOGGER.isDebugEnabled();
    }

    private static void logToAndroidInfo(String className, String messageTemplate, Object... arguments) {
        logToAndroid("i", className, formatMessage(className, messageTemplate, arguments), null);
    }

    private static void logToAndroidWarn(String className, String messageTemplate, Object... arguments) {
        logToAndroid("w", className, formatMessage(className, messageTemplate, arguments), null);
    }

    private static void logToAndroidDebug(String className, String messageTemplate, Object... arguments) {
        logToAndroid("d", className, formatMessage(className, messageTemplate, arguments), null);
    }

    private static void logToAndroidError(String className, String messageTemplate, Object... arguments) {
        logToAndroid("e", className, formatMessage(className, messageTemplate, arguments), null);
    }

    private static void logToAndroidThrowable(String className, Throwable throwable) {
        logToAndroid("e", className, "Exception on class " + className, throwable);
    }

    private static String formatMessage(String className, String messageTemplate, Object... arguments) {
        final String message = MessageFormatter.arrayFormat(messageTemplate, arguments).getMessage();
        return className + ": " + message;
    }

    private static void logToAndroid(String methodName, String className, String message, Throwable throwable) {
        try {
            final Class<?> logClass = Class.forName(LOG_CLASS);
            final String tag = buildTag(className);
            if (throwable == null) {
                final Method method = logClass.getMethod(methodName, String.class, String.class);
                method.invoke(null, tag, message);
            } else {
                final Method method = logClass.getMethod(methodName, String.class, String.class, Throwable.class);
                method.invoke(null, tag, message, throwable);
            }
        } catch (ReflectiveOperationException ignored) {
            // Android Log is not available in JVM unit tests.
        }
    }

    private static String buildTag(String className) {
        return LOG_TAG;
    }
}
