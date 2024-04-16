package game.files;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import game.figures.Figure;
import game.util.DateTimeUtil;
import game.util.ResourceUtil;

public class FileService {

	private static final Logger LOG = LoggerFactory.getLogger(FileService.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	private static final String CHESSBOARDS_DIR = "src/main/resources/saved-chessboards";
	private static final String BASE_FILE_NAME = "chessboard-";
	private static final String FILE_EXTENSION = ".chessboard";

	public void checkDirectory() {
		Path path = Paths.get(CHESSBOARDS_DIR);
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException ex) {
				LOG.error("Error occured during directory check", ex);
			}
		}
	}

	public List<String> getFileNames() {
		try {
			return Files.list(Paths.get(CHESSBOARDS_DIR))
								  .map(Path::getFileName)
								  .map(Path::toString)
								  .collect(Collectors.toList());
		} catch (IOException ex) {
			LOG.error("Error occured during searching for saves", ex);
			return Collections.emptyList();
		}
	}

	public void saveDataToFile(List<Figure> figures) {
		Path path = Paths.get(CHESSBOARDS_DIR, createFileName());

		try (OutputStream writer = ResourceUtil.openCompressedBase64OutputStream(path)) {
			MAPPER.writeValue(writer, figures);
		} catch (IOException ex) {
			LOG.error("Error occured during saving data to file", ex);
		}
	}

	public List<Figure> loadDataFromFile(String fileName) {
		Path path = Paths.get(CHESSBOARDS_DIR, fileName);

		try (InputStream reader = ResourceUtil.openDecompressedBase64InputStream(path)) {
			return MAPPER.readValue(reader, new TypeReference<List<Figure>>() {});
		} catch (IOException ex) {
			LOG.error("Error occured during reading data from file", ex);
			return Collections.emptyList();
		}
	}

	private String createFileName() {
		return BASE_FILE_NAME + DateTimeUtil.getActualDateTimeString() + FILE_EXTENSION;
	}
}
