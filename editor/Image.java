package editor;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class Image extends Canvas {
	
	int width, height;
	Vector<Layer> layers = new Vector<Layer>();
	Vector<Selection> selections = new Vector<Selection>();
	Vector<CompositeOperation> operations = new Vector<CompositeOperation>();
	
	public Image(int width, int height) {
		this();
		this.width = width;
		this.height = height;
	}
	
	public Image(Layer layer) {
		this();
		this.width = layer.width;
		this.height = layer.height;
		
		layers.add(layer);
	}
	
	private Image() {
		super();
	}

	BufferedImage bf;
	
	@Override
	public void update(Graphics g) {
		paint(g);
	}
	
	@Override
	public void paint(Graphics g) {
		
		if (getWidth() <= 0 || getHeight() <= 0) return;
		
		bf = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		Graphics gb = bf.getGraphics();
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Pixel pix = get(i, j);
				
				int r = 255, gr = 255, b = 255;
				
				float srcA = (float)pix.a / 100;
				
				r = Math.round(pix.r * srcA + r*(1-srcA));
				gr = Math.round(pix.g * srcA + gr*(1-srcA));
				b = Math.round(pix.b * srcA + b*(1-srcA));
				
				int rgb = 0;
				rgb += r  << 16;
				rgb += gr << 8 ;
				rgb += b       ;
								
				bf.setRGB(j, i, rgb);
			}
		}
		
		Graphics2D gi = (Graphics2D) gb;
		
		float[] dash1 = { 2f, 0f, 2f };
		BasicStroke bs1 = new BasicStroke(1, 
		        BasicStroke.CAP_BUTT, 
		        BasicStroke.JOIN_ROUND, 
		        1.0f, 
		        dash1,
		        2f);
		
		gi.setStroke(bs1);
		gi.setColor(Color.MAGENTA);
		
		for(Selection s: selections) {
			
			if(!s.active) continue;
			
			int xa = s.x;
			int ya = s.y;
			
			int xb = s.x + s.width;
			int yb = s.y + s.height;
			
			gi.drawLine(xa, ya, xb, ya);
			gi.drawLine(xa, ya, xa, yb);
			
			gi.drawLine(xb, ya, xb, yb);
			gi.drawLine(xa, yb, xb, yb);
		}
		
		float w = (float) getWidth()/ width;
		float h = (float) getHeight()/ height;
		AffineTransform at = AffineTransform.getScaleInstance(w, h);
		((Graphics2D)g).drawRenderedImage(bf, at);
		
	}
	
	public Pixel get(int h, int w){
		double r = 0, g = 0, b = 0, a = 0;

		for (Layer l : layers) {
			if (!l.visible) continue;

			Pixel pix = l.pixels.get(h).get(w);
			double srcA = pix.a / 100.0 * l.alpha / 100;
			double d = a * (1 - srcA);

			a = srcA + d;
			if (a != 0) {
				r = (pix.r * srcA + r * d) / a;
				g = (pix.g * srcA + g * d) / a;
				b = (pix.b * srcA + b * d) / a;
			}
		}
		return new Pixel(
				(int)Math.round(r),
				(int)Math.round(g),
				(int)Math.round(b),
				(int)Math.round(a * 100)
		);
	}
	
	public void addLayer(Layer newL) {
		layers.add(newL);
		
		width = Math.max(width, newL.width);
		height = Math.max(height, newL.height);
		
		for (Layer l: layers) {
			l.resize(width, height);
		}
	}
}
