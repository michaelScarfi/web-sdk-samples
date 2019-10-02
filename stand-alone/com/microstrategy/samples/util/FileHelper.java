import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.microstrategy.web.beans.MarkupOutput;

public class FileHelper {
	/*
	 * Helper function to save an in-memory object to file
	 */
	public static void saveByteArrayToFile(byte[] byteArray, String targetFilePath) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(targetFilePath);
			bos = new BufferedOutputStream(fos);
			bos.write(byteArray);

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// Fetch contents of MarkupOutput object and save to file
	public static void saveMarkupOutputToFile(MarkupOutput markupOutput, String targetFilePath) {
		boolean isBinary = markupOutput.getBinaryMode();
		if (isBinary) {
			// get binary data
			byte[] bytes = markupOutput.getBinaryContent();
			saveByteArrayToFile(bytes, targetFilePath);
			return;
		}

		// get string contents of MarkupOutput
		String results = markupOutput.getCopyAsString();
		byte[] stringBytes = results.getBytes();

		saveByteArrayToFile(stringBytes, targetFilePath);
	}

	public static byte[] readFiletoByteArray(String filePath) throws IOException {
		Path fileLocation = Paths.get(filePath);
		byte[] data = Files.readAllBytes(fileLocation);

		return data;
	}
}
