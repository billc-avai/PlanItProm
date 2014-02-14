package com.sevendesign.planitprom.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Enables logging to system log/sdcard file. */
public final class DebugLog {

	public static final String LOG_DIRECTORY = "debug_logs";
	public static final String LOG_EXTENSION = ".txt";

	private static final String LOG_FIELD_SEPARATOR = " | ";
	private static final String UNKNOWN_SIGNATURE = "[unknown]";
	private static final String DEFAULT_PACKAGE_NAME = "default";

	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(
			"[yyyy-MM-dd HH:mm:ss.SSS] ");

	private static final Pattern CLASS_NAME_PATTERN = Pattern
			.compile("([A-Z]*|(^[a-z]))[_\\da-z\\$]*");

	private static boolean enabled = true;
	private static boolean echoEnabled = true;

	private static Level savingLevel = Level.Verbose;
	private static Level echoLevel = Level.Verbose;

	private static String packageName = DEFAULT_PACKAGE_NAME;

	/** Logging level. */
	public static enum Level {
		Verbose("V"), Debug("D"), Information("I"), Warning("W"), Error("E");

		private final String label;

		private Level(String label) {
			this.label = label;
		}

		@Override
		public String toString() {
			return label;
		}
	}

	private DebugLog() {
	}

	public static synchronized void prepare(Context context) {
		if (context != null) {
			packageName = context.getPackageName();
		} else {
			packageName = DEFAULT_PACKAGE_NAME;
		}
	}

	/**
	 * Writes tag and message to the log file and also calls Log.d with given
	 * params.
	 */
	public static void d(String tag, String message) {
		logMessage(tag, message, Level.Debug);
	}

	/**
	 * Writes tag and message to the log file and also calls Log.w with given
	 * params.
	 */
	public static void w(String tag, String message) {
		logMessage(tag, message, Level.Warning);
	}

	/**
	 * Writes tag and message to the log file and also calls Log.e with given
	 * params.
	 */
	public static void e(String tag, String message) {
		logMessage(tag, message, Level.Error);
	}

	/**
	 * Writes tag and message to the log file and also calls Log.i with given
	 * params.
	 */
	public static void i(String tag, String message) {
		logMessage(tag, message, Level.Information);
	}

	/**
	 * Writes a message about exception to system log and to the file.
	 * @param tag
	 *            tag string
	 * @param where
	 *            a string describing where the exception happened
	 * @param e
	 *            an exception which class name and message will be saved in log
	 */
	public static void e(String tag, String where, Throwable e) {
		String message = new StringBuilder("Exception in ").append(where)
				.append(": ").append(e.getClass().getName())
				.append(LOG_FIELD_SEPARATOR).append(e.getMessage()).toString();

		e(tag, message);
	}

	public static void d(String message) {
		String tag = getCallerClassName();
		d(tag, message);
	}

	public static void i(String message) {
		String tag = getCallerClassName();
		i(tag, message);
	}

	public static void w(String message) {
		String tag = getCallerClassName();
		w(tag, message);
	}

	public static void e(String message) {
		String tag = getCallerClassName();
		e(tag, message);
	}

	public static void logException(Throwable e) {
		String tag = getCallerClassName();
		String where = getCallerMethodName();
		if (e != null) {
			e(tag, where, e);
		} else {
			e(tag, where, new Exception("Unknown exception"));
		}
	}

	public static void logMethod() {
		String tag = getCallerClassName();
		String where = getCallerMethodName();
		i(tag, where);
	}

	public static boolean isEnabled() {
		return enabled;
	}

	/** When not enabled, messages are not stored to the log file. */
	public static synchronized void setEnabled(boolean enabled) {
		DebugLog.enabled = enabled;
	}

	public static boolean isEchoEnabled() {
		return echoEnabled;
	}

	/** When echo is not enabled, messages are not printed to Android Log. */
	public static void setEchoEnabled(boolean echoEnabled) {
		DebugLog.echoEnabled = echoEnabled;
	}

	public static Level getLevel() {
		return savingLevel;
	}

	public static void setLevel(Level level) {
		if (level != null) {
			DebugLog.savingLevel = level;
		}
	}

	public static Level getEchoLevel() {
		return echoLevel;
	}

	public static void setEchoLevel(Level echoLevel) {
		if (echoLevel != null) {
			DebugLog.echoLevel = echoLevel;
		}
	}

	private static void logMessage(String tag, String message, Level level) {
		if (echoEnabled && level.ordinal() >= echoLevel.ordinal()) {
			echoMessage(tag, message, level);
		}
		if (enabled && level.ordinal() >= savingLevel.ordinal()) {
			logToFile(tag, message, level);
		}
	}

	private static void echoMessage(String tag, String message, Level level) {
		switch (level) {
		case Debug:
			Log.d(tag, message);
			break;

		case Error:
			Log.e(tag, message);
			break;

		case Warning:
			Log.w(tag, message);
			break;

		case Information:
			Log.i(tag, message);
			break;

		case Verbose:
			Log.v(tag, message);
			break;
		}
	}

	private static synchronized void logToFile(String tag, String message,
			Level level) {
		Writer writer = getLogWriter();

		if (writer != null) {
			try {
				writer.write(buildLogMessage(tag, message, level));
			} catch (IOException e) {
				// can't do anything about this exception
			} finally {
				closeSafely(writer);
			}
		}
	}

	private static void closeSafely(Closeable closeable) {
		try {
			closeable.close();
		} catch (IOException e) {
			// ignoring
		}
	}

	private static String buildLogMessage(String tag, String message,
			Level level) {
		String editedTag = (tag != null) ? tag.replace('|', '/') : "";
		return new StringBuilder().append(TIME_FORMAT.format(new Date()))
				.append(level).append(LOG_FIELD_SEPARATOR).append(editedTag)
				.append(LOG_FIELD_SEPARATOR).append(message).append("\r\n")
				.toString();
	}

	private static Writer getLogWriter() {
		FileWriter writer = null;

		File externalStorage = Environment.getExternalStorageDirectory();

		prepareLogsFolder(externalStorage);

		File logfile = new File(externalStorage, getLogFilename());

		try {
			writer = new FileWriter(logfile, true);
		} catch (IOException e) {
			// ignoring this
		}

		return writer;
	}

	private static void prepareLogsFolder(File externalStorage) {
		File logDir = new File(externalStorage, LOG_DIRECTORY);
		if (!logDir.exists()) {
			logDir.mkdir();
		}
	}

	private static String getLogFilename() {
		return new StringBuilder(LOG_DIRECTORY).append(File.separatorChar)
				.append(packageName).append(LOG_EXTENSION).toString();
	}

	private static String tokenizeClassName(String className) {
		List<String> parts = new ArrayList<String>();
		String result;

		try {
			Matcher matcher = CLASS_NAME_PATTERN.matcher(className);

			while (matcher.find()) {
				String part = className.substring(matcher.start(),
						matcher.end());
				if (!TextUtils.isEmpty(part.trim())) {
					parts.add(part.toUpperCase());
				}
			}

			result = parts.isEmpty() ? className : TextUtils.join("_", parts);
		} catch (Exception e) {
			result = className;
		}

		return result;
	}

	private static String getCallerClassName() {
		String className;
		final int CALLER_STACK_INDEX = 4;

		try {
			StackTraceElement[] callStack = Thread.currentThread()
					.getStackTrace();
			if (callStack.length > CALLER_STACK_INDEX) {
				String fullClassName = callStack[CALLER_STACK_INDEX]
						.getClassName();
				int lastPoint = fullClassName.lastIndexOf(".");
				if (lastPoint >= 0 && (lastPoint + 1) < fullClassName.length()) {
					className = fullClassName.substring(lastPoint + 1);
				} else {
					className = UNKNOWN_SIGNATURE;
				}
			} else {
				className = UNKNOWN_SIGNATURE;
			}
		} catch (Exception e) {
			className = UNKNOWN_SIGNATURE;
		}

		return tokenizeClassName(className);
	}

	private static String getCallerMethodName() {
		String methodName;
		final int CALLER_STACK_INDEX = 4;

		try {
			StackTraceElement[] callStack = Thread.currentThread()
					.getStackTrace();
			if (callStack.length > CALLER_STACK_INDEX) {
				methodName = callStack[CALLER_STACK_INDEX].getMethodName();
			} else {
				methodName = UNKNOWN_SIGNATURE;
			}
		} catch (Exception e) {
			methodName = UNKNOWN_SIGNATURE;
		}

		return methodName;
	}
}
