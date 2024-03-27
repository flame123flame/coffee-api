package framework.utils;

import java.util.Random;
import java.util.UUID;

public class GenerateRandomString {

	public static String generate() {
		int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 10;
	    Random random = new Random();
	    String generatedCode = random.ints(leftLimit, rightLimit + 1)
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();	 
	    Long nanoTime = System.nanoTime();
	    generatedCode = nanoTime.toString().concat(generatedCode);
	    return generatedCode;
	}
	
	public static String generateUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	
}
