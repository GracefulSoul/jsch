package gracefulsoul.logger;

import com.jcraft.jsch.Logger;

public class JschLogger implements Logger {
	
	private static int DEFAULT_LOGGING_LEVEL;
	
	/**
	 * Default logging is info.
	 */
	public JschLogger() {
		DEFAULT_LOGGING_LEVEL = Logger.INFO;
	}
	
	/**
	 * Using LoggerLevel's ordinal number.
	 * 
	 * @see LoggerLevel
	 * @param level
	 */
	public JschLogger(int level) {
		DEFAULT_LOGGING_LEVEL = level;
	}
	
	@Override
	public boolean isEnabled(int level) {
		return level == DEFAULT_LOGGING_LEVEL;
	}

	@Override
	public void log(int level, String message) {
		System.out.printf("%s - %s%n", LoggerLevel.values()[level].name(), message);
		
	}

}
