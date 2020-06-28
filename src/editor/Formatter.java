package editor;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.parser.ParseException;

public abstract class Formatter {
	
	abstract public Image load(String path) throws IOException, ParseException;

	abstract public void save(String path, Image img) throws IOException;
}
