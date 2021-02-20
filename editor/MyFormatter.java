package editor;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.json.simple.JSONArray; 
import org.json.simple.JSONValue;
import org.json.simple.parser.*;

public class MyFormatter extends Formatter {

	@Override
	public void save(String path, Image img) throws IOException {
		
		if (img == null) return;
		
		LinkedHashMap obj = new LinkedHashMap();
		
		obj.put("width", img.width);
		obj.put("height", img.height);
		
		JSONArray layers = new JSONArray();
		JSONArray functions = new JSONArray();
		JSONArray selections = new JSONArray();
		
		
		
		for (Layer l: img.layers) {
			LinkedHashMap layer = new LinkedHashMap();
			JSONArray pixels = new JSONArray();
			
			
			for (Vector<Pixel> arr: l.pixels) {
				for (Pixel pix: arr) {
					JSONArray pixel = new JSONArray();
					
					pixel.add(pix.r);
					pixel.add(pix.g);
					pixel.add(pix.b);
					pixel.add(pix.a);
					
					pixels.add(pixel);
				}
			}
			
			layer.put("pixels", pixels);
			
			layer.put("alpha", l.alpha);
			layer.put("active", l.active);
			layer.put("visible", l.visible);
			
			layers.add(layer);
		}
		
		Integer i = 0;
		for (Selection l: img.selections) {
			LinkedHashMap selection = new LinkedHashMap();
			JSONArray rects = new JSONArray();
			
			
			
			
			LinkedHashMap rect = new LinkedHashMap();
			
			
			rect.put("w", l.width);
			rect.put("h", l.height);
			rect.put("x", l.x);
			
			//HERE
			rect.put("y", l.y + l.height);
			
			rects.add(rect);
			selection.put("name", i.toString());
			selection.put("rects", rects);
			selection.put("active", l.active);
			
			selections.add(selection);
			
			i++;
		}
		
		for (CompositeOperation comp: img.operations) {
			functions.add(comp);
		}
		
		
		obj.put("layers", layers);
		obj.put("functions", functions);
		obj.put("selections", selections);
		
		
		File file;
		
		if (path.equals("")) {
			file = new File("./Images/Saves/temp.json");
		}
		else {
			file = new File(path);
		}
		
		file.createNewFile();
		
		FileWriter wr = new FileWriter(file);
		
		wr.write(JSONValue.toJSONString(obj));
		wr.flush();
		wr.close();
	}

	@Override
	public Image load(String path) throws ParseException, IOException{
		Object obj;
		
		JSONParser par = new JSONParser();
		
		ContainerFactory fac = new ContainerFactory() {

			@Override
			public List creatArrayContainer() {
				return new JSONArray();
			}

			@Override
			public Map createObjectContainer() {
				return new LinkedHashMap();
			}
		};
		
		if (path.equals("")) {
			obj = par.parse(new FileReader("./Images/Saves/Shapes.json"), fac);
		}
		else {
			obj = par.parse(new FileReader(path), fac);
		}
		
		LinkedHashMap jo = (LinkedHashMap) obj;
		
		
		Long width = (long)jo.get("width");
		Long height = (long)jo.get("height");
		
		JSONArray layers = (JSONArray)jo.get("layers");
		JSONArray functions = (JSONArray)jo.get("functions");
		JSONArray selections = (JSONArray)jo.get("selections");
		
		Image img = new Image(width.intValue(), height.intValue());
		
		for (Object curr: layers) {
			
			LinkedHashMap layer = (LinkedHashMap) curr;
			
			JSONArray pixels = (JSONArray) layer.get("pixels");
			
			Vector<Vector<Pixel>> pix = new Vector<Vector<Pixel>>();

			
			for (int i = 0; i < height; i++) {
				
				pix.add(new Vector<Pixel>());
				
				for (int j = 0; j < width; j++) {
					int r,g,b,a;
					
					JSONArray arr = (JSONArray) pixels.get(i*width.intValue() + j);
					
					r = ((Long)arr.get(0)).intValue();
					g = ((Long)arr.get(1)).intValue();
					b = ((Long)arr.get(2)).intValue();
					a = ((Long)arr.get(3)).intValue();
					
					Pixel pixel = new Pixel(r,g,b,a);
					
					pix.get(i).add(pixel);
				}
			}
			
			Layer l = new Layer(pix);
			l.active = (boolean) layer.get("active");
			l.visible = (boolean) layer.get("visible");
			l.alpha = ((Long) layer.get("alpha")).intValue();
			
			img.layers.add(l);
		}
		
		
		
		for (Object curr: functions) {
			
			LinkedHashMap fun = (LinkedHashMap) curr;
			
			String name = (String) fun.get("name");
			JSONArray c = (JSONArray) fun.get("c");
			
			CompositeOperation comp = new CompositeOperation(name, c);
			
			img.operations.add(comp);
		}
		
		
		for (Object curr: selections) {
			
			LinkedHashMap selection = (LinkedHashMap) curr;
			
			String name = (String) selection.get("name");
			JSONArray rects = (JSONArray) selection.get("rects");
			
			for (Object i: rects) {
				
				LinkedHashMap rect = (LinkedHashMap) i;
				
				int w, h, x, y;
				
				w = ((Long) rect.get("w")).intValue();
				h = ((Long) rect.get("h")).intValue();
				x = ((Long) rect.get("x")).intValue();
				y = ((Long) rect.get("y")).intValue();
				
				
				//HERE
				Selection s = new Selection(w, h, x, y - h);
				s.active = (boolean) selection.get("active");
				img.selections.add(s);
			}
		}
		
		
		
		return img;
		
	}
	
	public static void main(String[] args) {
		 MyFormatter form = new MyFormatter();
		 try {
			Image img = form.load("");
			 
			form.save("./Images/Saves/img.json", img);
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	 }
	 
}
