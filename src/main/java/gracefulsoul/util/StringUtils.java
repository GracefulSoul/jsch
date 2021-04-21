package gracefulsoul.util;

public class StringUtils {

	private StringUtils() {
		// Utility class.
	}
	
	/**
	 * String is null or empty("").
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		return str == null || str.length() == 0;
	}
	
	/**
	 * String is not null and not empty("").
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}
	
}
