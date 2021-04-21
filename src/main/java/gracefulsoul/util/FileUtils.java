package gracefulsoul.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
	
	private static final int BUFFER_SIZE = 1024;

	private FileUtils() {
		// Utility class.
	}
	
	/**
	 * If the directory does not exist, create it.
	 * 
	 * @param dirPath
	 */
	public static void mkdirIfNotExists(String dirPath) {
		File folder = new File(dirPath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
	}
	
	/**
	 * Copy file.
	 * 
	 * @param inputStream
	 * @param outputStream
	 * @throws IOException
	 */
	public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
		byte[] byteArr = new byte[BUFFER_SIZE];
		int byteRead;
		while ((byteRead = inputStream.read(byteArr)) != -1) {
			outputStream.write(byteArr, 0, byteRead);
		}
	}
	
}
