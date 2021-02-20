package editor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.parser.ParseException;

public class PAMFormatter extends Formatter {

	@Override
	public Image load(String path) throws IOException, ParseException {
		return new Image(loadLayer(path));
	}

	@Override
	public void save(String path, Image img) throws IOException {
		
		String n = "\n";
		StringBuffer out = new StringBuffer();
		
		out.append("P7" + n + 
				"WIDTH " + img.width + n +
				"HEIGHT " + img.height + n +
				"DEPTH 4" + n +
				"MAXVAL 255" + n +
				"TUPLTYPE RGB_ALPHA" + n +
				"ENDHDR" + n);
		
		byte pixs[] = new byte[4*img.width*img.height];
		
		for (int i = 0; i < img.height; ++i) {
			for (int j = 0; j < img.width; ++j) {
				Pixel p = img.get(i, j);
				
				pixs[(i*img.width + j) * 4    ] = (byte)(p.r & 0xFF);
				pixs[(i*img.width + j) * 4 + 1] = (byte)(p.g & 0xFF);
				pixs[(i*img.width + j) * 4 + 2] = (byte)(p.b & 0xFF);
				pixs[(i*img.width + j) * 4 + 3] = (byte)((p.a * 255 / 100) & 0xFF);
			}
		}
		
		File file;
		
		if (path.equals("")) {
			file = new File("./Images/temp.pam");
		}
		else {
			file = new File(path);
		}
		
		file.createNewFile();
		
		FileWriter wr = new FileWriter(file);
		
		wr.write(out.toString());
		wr.flush();
		wr.close();
		
		FileOutputStream fos = new FileOutputStream(file, true);
				
		fos.write(pixs);
		
		fos.close();

	}

	public Layer loadLayer(String path) throws IOException, ParseException {
		
		byte[] pamFile;
		
		if (path.equals("")) {
			pamFile = Files.readAllBytes(Paths.get("./Images/sample.pam"));
		}
		else{
			pamFile = Files.readAllBytes(Paths.get(path));
		}
		
		String in = new String(pamFile);
		
		String reg = "P7\nWIDTH ([0-9]*)\nHEIGHT ([0-9]*)\nDEPTH (?:[0-9]*)\nMAXVAL (?:[0-9]*)\nTUPLTYPE (.*)\nENDHDR\n(.*)";
		
		Pattern pattern = Pattern.compile(reg);
		
		Matcher matcher = pattern.matcher(in);
		
		Layer layer;
		
		if (matcher.find()) {
			int width = Integer.parseInt(matcher.group(1));
			int height = Integer.parseInt(matcher.group(2));
			
			Vector<Vector<Pixel>> pixels = new Vector<Vector<Pixel>>();
			
			String type = matcher.group(3);
			byte[] pix = matcher.group(4).getBytes();
			
			if (type == "BLACKANDWHITE") {
				for (int i = 0; i < height; ++i) {
					Vector<Pixel> row = new Vector<Pixel>();
					for (int j = 0; j < width; ++j) {
						int r, g, b, a = 100;
						
						r = pix[(i*width + j) * 4    ] << 8;
						g = pix[(i*width + j) * 4    ] << 8;
						b = pix[(i*width + j) * 4    ] << 8;
						
						row.add(new Pixel(r,g,b,a));						
					}
					pixels.add(row);
				}
			}
			else if (type == "GRAYSCALE") {
				for (int i = 0; i < height; ++i) {
					Vector<Pixel> row = new Vector<Pixel>();
					for (int j = 0; j < width; ++j) {
						int r, g, b, a = 100;
						
						r = pix[(i*width + j) * 1    ] & 0xFF;
						g = pix[(i*width + j) * 1    ] & 0xFF;
						b = pix[(i*width + j) * 1    ] & 0xFF;
						
						row.add(new Pixel(r,g,b,a));						
					}
					pixels.add(row);
				}
			}
			else if (type == "RGB") {
				for (int i = 0; i < height; ++i) {
					Vector<Pixel> row = new Vector<Pixel>();
					for (int j = 0; j < width; ++j) {
						int r, g, b, a = 100;
						
						r = pix[(i*width + j) * 3    ] & 0xFF;
						g = pix[(i*width + j) * 3 + 1] & 0xFF;
						b = pix[(i*width + j) * 3 + 2] & 0xFF;
						
						row.add(new Pixel(r,g,b,a));						
					}
					pixels.add(row);
				}
			}
			else if (type == "BLACKANDWHITE_ALPHA") {
				for (int i = 0; i < height; ++i) {
					Vector<Pixel> row = new Vector<Pixel>();
					for (int j = 0; j < width; ++j) {
						int r, g, b, a;
						
						r = pix[(i*width + j) * 2    ] << 8;
						g = pix[(i*width + j) * 2    ] << 8;
						b = pix[(i*width + j) * 2    ] << 8;
						a = pix[(i*width + j) * 2 + 1] & 0xFF;
						
						row.add(new Pixel(r,g,b,a*100/255));						
					}
					pixels.add(row);
				}
			}
			else if (type == "GRAYSCALE_ALPHA") {
				for (int i = 0; i < height; ++i) {
					Vector<Pixel> row = new Vector<Pixel>();
					for (int j = 0; j < width; ++j) {
						int r, g, b, a;
						
						r = pix[(i*width + j) * 2    ] & 0xFF;
						g = pix[(i*width + j) * 2    ] & 0xFF;
						b = pix[(i*width + j) * 2    ] & 0xFF;
						a = pix[(i*width + j) * 2 + 1] & 0xFF;
						
						row.add(new Pixel(r,g,b,a*100/255));						
					}
					pixels.add(row);
				}
			}
			else if (type.equals("RGB_ALPHA")) {
				for (int i = 0; i < height; ++i) {
					Vector<Pixel> row = new Vector<Pixel>();
					for (int j = 0; j < width; ++j) {
						int r, g, b, a;
						
						r = pix[(i*width + j) * 4    ] & 0xFF;
						g = pix[(i*width + j) * 4 + 1] & 0xFF;
						b = pix[(i*width + j) * 4 + 2] & 0xFF;
						a = pix[(i*width + j) * 4 + 3] & 0xFF;
						
						row.add(new Pixel(r,g,b,a*100/255));						
					}
					pixels.add(row);
				}
			}
			
			layer = new Layer(pixels);
        }
		else {
			System.out.println(in);
			throw new ParseException(ParseException.ERROR_UNEXPECTED_TOKEN);
		}
		return layer;
	}
	
	public static void main(String[] args) {
		try {
			Image img = new PAMFormatter().load("");
			
			new PAMFormatter().save("", img);
			
			new PAMFormatter().load("./Images/temp.pam");
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
}
