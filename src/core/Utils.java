package core;

import java.io.InputStream;
import java.util.Scanner;

public class Utils {
	public static String loadResource(String fileName) throws Exception {
		String result;
		InputStream in = Class.forName(Utils.class.getName()).getResourceAsStream(fileName);
		
		try(Scanner scanner = new Scanner(in, "UTF-8")) {
			result = scanner.useDelimiter("\\A").next();
		}
		
		return result;
	}
}
