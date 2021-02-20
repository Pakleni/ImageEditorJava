package editor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class OperationCreator extends Dialog{

	CompositeOperation comp;
	
	ActionListener close;
	
	public OperationCreator(Editor e, String name) {
		super(e, "Operation Creator", true);
		
		if (e.img == null) dispose();
		
		close = a-> { 
			e.img.operations.add(comp);
			e.edit.add(new CompositeMenuItem(e, comp));
			dispose();
		};
		
		setup(name);
	}
	
	public OperationCreator(OperationCreator e, String name) {
		super(e, "Operation Creator", true);
		
		close = a-> {
			e.addOperation(comp);
			dispose();
		};
		
		setup(name);
	}
	
	private void setup(String name) {
		
		setSize(250,400);
		
		this.setResizable(false);
		
		comp = new CompositeOperation(name);
		
		addButtons();
		
		dodajOsluskivace();	
		
		setVisible(true);
	}
	
	private void addButtons() {
		
		Button perform = new Button("Finish");
		perform.addActionListener(close);
		
		Button add = new Button("Add");
		Button sub = new Button("Subtract");
		Button rsb = new Button("Reverse Subtract");
		Button mul = new Button("Multiply");
		Button div = new Button("Divide");
		Button rdv = new Button("Reverse Divide");
		Button pow = new Button("Power");
		Button log = new Button("Logarithm");
		Button abs = new Button("Absolute");
		Button min = new Button("Minimum");
		Button max = new Button("Maximum");
		Button inv = new Button("Invert");
		Button bnw = new Button("Black & White");
		Button gra = new Button("Grayscale");
		Button med = new Button("Median");
		Button fill = new Button("Fill");
		Button comp = new Button("Create Composite");
		
		abs.addActionListener(e->{this.addOperation(new Absolute());});
		inv.addActionListener(e->{this.addOperation(new Invert());});
		bnw.addActionListener(e->{this.addOperation(new BlackWhite());});
		gra.addActionListener(e->{this.addOperation(new Grayscale());});
		med.addActionListener(e->{this.addOperation(new Median());});
		
		add.addActionListener(e->{
			InputDialog d = new InputDialog(this);
			d.start(a->{
					String s = d.in.getText();
					try { OperationCreator.this.addOperation(new Add(Integer.parseInt(s))); }
					catch(NumberFormatException er) { d.dispose(); }
				
			});
		});
		
		sub.addActionListener(e->{
			InputDialog d = new InputDialog(this);
			d.start(a->{
					String s = d.in.getText();
					try { OperationCreator.this.addOperation(new Subtract(Integer.parseInt(s))); }
					catch(NumberFormatException er) { d.dispose(); }
				
			});
		});
		
		rsb.addActionListener(e->{
			InputDialog d = new InputDialog(this);
			d.start(a->{
					String s = d.in.getText();
					try { OperationCreator.this.addOperation(new ReverseSubtract(Integer.parseInt(s))); }
					catch(NumberFormatException er) { d.dispose(); }
				
			});
		});
		
		mul.addActionListener(e->{
			InputDialog d = new InputDialog(this);
			d.start(a->{
					String s = d.in.getText();
					try { OperationCreator.this.addOperation(new Multiply(Integer.parseInt(s))); }
					catch(NumberFormatException er) { d.dispose(); }
				
			});
		});
		
		div.addActionListener(e->{
			InputDialog d = new InputDialog(this);
			d.start(a->{
					String s = d.in.getText();
					try { OperationCreator.this.addOperation(new Divide(Integer.parseInt(s))); }
					catch(NumberFormatException er) { d.dispose(); }
				
			});
		});
		
		rdv.addActionListener(e->{
			InputDialog d = new InputDialog(this);
			d.start(a->{
					String s = d.in.getText();
					try { OperationCreator.this.addOperation(new ReverseDivide(Integer.parseInt(s))); }
					catch(NumberFormatException er) { d.dispose(); }
				
			});
		});
		
		min.addActionListener(e->{
			InputDialog d = new InputDialog(this);
			d.start(a->{
					String s = d.in.getText();
					try { OperationCreator.this.addOperation(new Minimum(Integer.parseInt(s))); }
					catch(NumberFormatException er) { d.dispose(); }
				
			});
		});
		
		max.addActionListener(e->{
			InputDialog d = new InputDialog(this);
			d.start(a->{
					String s = d.in.getText();
					try {OperationCreator.this.addOperation(new Maximum(Integer.parseInt(s)));}
					catch(NumberFormatException er) { d.dispose(); }
				
			});
		});
		
		pow.addActionListener(e->{
			InputDialog d = new InputDialog(this);
			d.start(a->{
					String s = d.in.getText();
					try { OperationCreator.this.addOperation(new Power(Double.parseDouble(s))); }
					catch(NumberFormatException er) { d.dispose(); }
				
			});
		});
		
		log.addActionListener(e->{
			InputDialog d = new InputDialog(this);
			d.start(a->{
					String s = d.in.getText();
					try { OperationCreator.this.addOperation(new Logarithm(Double.parseDouble(s))); }
					catch(NumberFormatException er) { d.dispose(); }
				
			});
		});
		
		fill.addActionListener(e->{
			new FillDialog(OperationCreator.this);
		});
		
		comp.addActionListener(e->{
			InputDialog d = new InputDialog(this);
			d.start(a->{
					String s = d.in.getText();
					new OperationCreator(OperationCreator.this, s);
			});
		});
		
		this.setLayout(new GridLayout(9,2));

		
		add(add);
		add(sub);
		add(rsb);
		add(mul);
		add(div);
		add(rdv);
		add(pow);
		add(log);
		add(abs);
		add(min);
		add(max);
		add(inv);
		add(bnw);
		add(gra);
		add(med);
		add(fill);
		add(comp);
		
		add(perform);
	}
	
	void addOperation(Operation op) {
		comp.add(op);
	}
	
	private void dodajOsluskivace() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
				{dispose();}
		});
	}
	
}

public class CompositeOperation extends Operation {

	public CompositeOperation(String name) {
		super(name);
	}
	
	public CompositeOperation(String name, JSONArray c) {
		super(name, c);
	}
	
	static CompositeOperation parse(String path) throws FileNotFoundException, IOException, ParseException {
		
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
		
		
		obj = par.parse(new FileReader(path), fac);
		
		LinkedHashMap jo = (LinkedHashMap) obj;
		
		return new CompositeOperation((String)jo.get("name"), (JSONArray)jo.get("c"));
	}
	
	void save(String path) throws IOException {
		
		File fun = new File(path);
		fun.createNewFile();
		FileWriter wr = new FileWriter(fun);
		wr.write(toString());
		wr.flush();
		wr.close();
		
	}
	
	public void add(Operation op) {
		this.c.add(op);
	}

}