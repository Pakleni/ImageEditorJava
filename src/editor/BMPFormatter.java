package editor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.json.simple.parser.ParseException;

public class BMPFormatter extends Formatter {

	public BMPFormatter() {
	}

	@Override
	public Image load(String path) throws IOException, ParseException {
		return new Image(loadLayer(path));
	}
	
	public Layer loadLayer(String path) throws IOException, ParseException {
		
		File bmpFile;
		
		if (path.equals("")) {
			bmpFile = new File("bin/Images/Shapes.bmp");
		}
		else{
			bmpFile = new File(path);
		}
		BufferedImage image = ImageIO.read(bmpFile);
		
		int height = image.getHeight();
		int width = image.getWidth();
		
		
		Layer layer = new Layer(width, height);
		
		for (int i =0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int rgb = image.getRGB(j, i);
				
				int a = (rgb >> 24) & 0xFF * 100 / 255;
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >>  8) & 0xFF;
				int b = (rgb      ) & 0xFF;
				
				layer.pixels.get(i).set(j, new Pixel(r, g, b, a));
			}
		}
	
		
		return layer;
	}

	@Override
	public void save(String path, Image img) throws IOException {
		
		BufferedImage image = new BufferedImage(
				img.width,
				img.height,
				BufferedImage.TYPE_INT_RGB);
		
		for (int i = 0; i < img.height; i++ ) {
			for (int j = 0; j < img.width; j++ ) {
				Pixel pix = img.get(i, j);
				int rgb = 0;
				
				rgb += (pix.a*255/100) << 24;
				rgb +=  pix.r << 16;
				rgb +=  pix.g << 8 ;
				rgb +=  pix.b      ;
				
				image.setRGB(j, i, rgb);
			}
		}
		
		ImageIO.write(image, "bmp" , new File(path));
		
	}

}
