package editor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public abstract class Operation {

	public Operation(String name) {
		
		this.name = name;
		c = new JSONArray();
	}
	
	public Operation(String name, JSONArray c) {
		
		this.name = name;
		this.c = c;
	}
	
	String name;
	JSONArray c;
	
	@Override
	public String toString() {
		
		LinkedHashMap obj = new LinkedHashMap();
		
		obj.put("name", name);
		obj.put("c", c);
		
		return JSONValue.toJSONString(obj);
	}
	
	public void run(Editor e) {
		
		try {
			if (e.img == null) return;
			
			
			
			MyFormatter form = new MyFormatter();
			form.save("./temp/temp.json",e.img);
			
			
			File fun = new File("./temp/temp.fun");
			fun.createNewFile();
			FileWriter wr = new FileWriter(fun);
			wr.write(toString());
			wr.flush();
			wr.close();
			
			File error = new File("./temp/err.txt");
			error.createNewFile();
			
			
			Process process =
					new ProcessBuilder(
							"./ImageEditor/ImageEditor.exe",
							"./temp/temp.json",
							"./temp/temp.fun")
					.redirectError(error)
					.start();
			
			
			process.waitFor();
			
			
			e.onLoad(form.load("./temp/temp.json"));
			
			
			File file = new File("./temp/temp.json");
			file.delete();
			
//			file = new File("./temp/temp.fun");
//			file.delete();
			
			
		} catch (IOException er) {
			// TODO Auto-generated catch block
			er.printStackTrace();
		} catch (InterruptedException er) {
			// TODO Auto-generated catch block
			er.printStackTrace();
		} catch (ParseException er) {
			// TODO Auto-generated catch block
			er.printStackTrace();
		}
	}
	
	
}
