package es.unizar.tmdad.tweelytics.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class FileHelper {

	/**
	 * Gets the content of the file
	 * @param file File path
	 * @return File content
	 * @throws IOException
	 */
	public static String readContent(String file) throws IOException{
		return FileHelper.readContent(file, Charset.defaultCharset());
	}
	
	/**
	 * Gets the content of the file
	 * @param file File path
	 * @return File content
	 * @throws IOException
	 */
	public static String readContent(String file, Charset charset) throws IOException {
		Resource resource = new ClassPathResource(file);
		InputStream in = resource.getInputStream();
		Scanner s = new Scanner(in);
		s.useDelimiter("\\A");
		String res = s.hasNext() ? s.next() : "";
		s.close();
		return res;
	}
	
	/**
	 * Gets the content of the file
	 * @param file File
	 * @return File content
	 * @throws IOException
	 */
	public static String readContent(File file) throws IOException {
		return FileHelper.readContent(file, Charset.defaultCharset());
	}
	
	/**
	 * Gets the content of the file
	 * @param file File
	 * @return File content
	 * @throws IOException
	 */
	public static String readContent(File file, Charset charset) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(file.getPath()));
		return new String(encoded, charset);
	}
}
