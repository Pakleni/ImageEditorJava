package editor;

import java.util.Vector;

public class Layer {
	
	int width, height;
	Vector<Vector<Pixel>> pixels;
	
	boolean active = true;
	boolean visible = true;
	
	int alpha = 100;
	
	public Layer(Vector<Vector<Pixel>> pixels) {
		super();
		
		height = pixels.size();
		if (height > 0) {
			width = pixels.get(0).size();
		}
		else {
			width = 0;
		}
		
		this.pixels = pixels;
	}

	public Layer(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		
		pixels = new Vector<Vector<Pixel>>();
		
		for (int i = 0; i < height; i++) {
			
			Vector<Pixel> px = new Vector<Pixel>();
			
			
			for (int j = 0; j < width; j++) {
				
				px.add(new Pixel());
				
			}
			
			
			pixels.add(px);
		}
	}
	
	public void resize(int width, int height) {
		
		for (int i = this.height; i < height; i++) {
			Vector<Pixel> curr = new Vector<Pixel>();
			
			for (int j = 0; j < width; j++) {
				curr.add(new Pixel(0,0,0,0));
			}
			
			pixels.add(curr);
		}
		
		for (int i = 0; i < this.height; i++) {
			for (int j = this.width; j < width; j++) {
				pixels.get(i).add(new Pixel(0,0,0,0));
			}
		}
		
		this.height = height;
		this.width = width;
		
	}
	
}
