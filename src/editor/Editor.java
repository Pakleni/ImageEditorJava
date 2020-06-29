package editor;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import org.json.simple.parser.ParseException;


@SuppressWarnings("serial")
class SelectionPanel extends Panel {
	
	Selection s;
	Editor e;
	
	Label name = new Label();
	Checkbox active = new Checkbox("active", true);
	
	Button del = new Button("delete");
	
	public SelectionPanel(Editor e, Selection s, int i) {
		super();
		this.e = e;
		this.s = s;
		
		name.setText("selection: " + i);
		
		active.setState(s.active);
		
		this.add(name);
		this.add(active);
		this.add(del);
		
		active.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange()==1) {
					s.active = true;
				}
				else {
					s.active = false;
				}
				e.img.repaint();
			}
		});
		
		del.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				e.img.selections.remove(s);
				e.s.remove(SelectionPanel.this);
				e.revalidate();
				e.img.repaint();
			}
			
		});
	}
	
}

@SuppressWarnings("serial")
class LayerPanel extends Panel {
	
	Layer l;
	Editor e;
	
	Label name = new Label();
	
	Checkbox active = new Checkbox("active", true);
	Checkbox visible = new Checkbox("visible", true);
	
	Button del = new Button("delete");
	TextField alpha = new TextField("100");
	
	public LayerPanel(Editor e, Layer l, int i) {
		super();
		this.e = e;
		this.l = l;
		
		name.setText("layer: " + i);
		
		alpha.setText(""+l.alpha);
		active.setState(l.active);
		visible.setState(l.visible);
		
		
		this.add(name);
		this.add(alpha);
		this.add(active);
		this.add(visible);
		this.add(del);
		
		active.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange()==1) {
					l.active = true;
				}
				else {
					l.active = false;
				}
			}
		});
		
		visible.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange()==1) {
					l.visible = true;
				}
				else {
					l.visible = false;
				}
				
				e.img.repaint();
			}
		});
		
		del.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				e.img.layers.remove(l);
				e.l.remove(LayerPanel.this);
				e.revalidate();
				e.img.repaint();
			}
			
		});
		
		alpha.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					int i = Integer.parseInt(alpha.getText());
					
					if( i <= 100 && i >= 0 ) {
						l.alpha = i;
					}
				}
				catch(NumberFormatException e) {
					alpha.setText("100");
					l.alpha = 100;
				}
				
				e.img.repaint();
			}
			
		});
	}
	
}

@SuppressWarnings("serial")
class InputDialog extends Dialog {

	
	TextField in = new TextField();
	
	Button perform = new Button("OK");
	
	ActionListener close = e-> {dispose();};
	
	
	public InputDialog(Frame e) {
		super(e, "Entry Dialog", true);
		
		setup();
	}
	
	public InputDialog(Dialog e) {
		super(e, "Entry Dialog", true);
		
		setup();
	}
	
	private void setup() {
		setSize(280,200);
		
		GridBagLayout lay = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		
		this.setLayout(lay);
			
		gbc.ipady = 20;
		gbc.ipadx = 100;
	     
		add(in, gbc);
		
		gbc.ipady = 20;
		gbc.ipadx = 50;
		
		add(perform, gbc);
		
		dodajOsluskivace();	
	}
	
	public void start(ActionListener action) {
		in.addActionListener(action);
		in.addActionListener(close);
		perform.addActionListener(action);
		perform.addActionListener(close);
		
		setVisible(true);
	}
	
	private void dodajOsluskivace() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
				{dispose();}
		});
	}
	
}

@SuppressWarnings("serial")
class FillDialog extends Dialog {

	
	TextField r = new TextField();
	TextField g = new TextField();
	TextField b = new TextField();
	TextField a = new TextField();
	
	Button perform = new Button("OK");
	
	ActionListener close;
	
	public FillDialog(Editor e) {
		super(e, "Fill Dialog", true);
		
		close = ac -> {
			
			String r = FillDialog.this.r.getText();
			String g = FillDialog.this.g.getText();
			String b = FillDialog.this.b.getText();
			String a = FillDialog.this.a.getText();
			
			try { new Fill(Integer.parseInt(r),Integer.parseInt(g),Integer.parseInt(b),Integer.parseInt(a)).run(e); }
			catch(NumberFormatException er) { FillDialog.this.dispose(); }
			
			dispose();
		};
		
		setup();
		
		setVisible(true);
	}
	
	public FillDialog(OperationCreator op) {
		super(op, "Fill Dialog");
		
		close = ac -> {
			
			String r = FillDialog.this.r.getText();
			String g = FillDialog.this.g.getText();
			String b = FillDialog.this.b.getText();
			String a = FillDialog.this.a.getText();
			
			try { op.addOperation(new Fill(Integer.parseInt(r),Integer.parseInt(g),Integer.parseInt(b),Integer.parseInt(a))); }
			catch(NumberFormatException er) { FillDialog.this.dispose(); }
			
			dispose();
		};
		
		setup();
		
		setVisible(true);
	}
	
	private void setup() {
		
		setSize(280,200);
		
		this.setLayout(new GridLayout(5, 1));
		
		Panel r_p = new Panel(new GridLayout(1, 2, 10, 10));
		r_p.add(new Label("R"));
		r_p.add(r);
		Panel g_p = new Panel(new GridLayout(1, 2, 10, 10));
		g_p.add(new Label("G"));
		g_p.add(g);
		Panel b_p = new Panel(new GridLayout(1, 2, 10, 10));
		b_p.add(new Label("B"));
		b_p.add(b);
		Panel a_p = new Panel(new GridLayout(1, 2, 10, 10));
		a_p.add(new Label("A"));
		a_p.add(a);
		
		add(r_p);
		add(g_p);
		add(b_p);
		add(a_p);
		
		add(perform);
		
		dodajOsluskivace();
	}
	
	private void dodajOsluskivace() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
				{dispose();}
		});
		
		perform.addActionListener(close);
	}
	
}

@SuppressWarnings("serial")
class LayerDialog extends Dialog {
	
	public LayerDialog(Editor e) {
		super(e, "Fill Dialog", true);
		
		if (e.img != null) {			
			setup(e);
		}
		else {
			setupNull(e);
		}
		setVisible(true);
	}
	
	private void setupNull(Editor e) {
		setSize(100,200);
		
		this.setLayout(new GridLayout(3, 1));
		
		TextField w = new TextField("width");
		TextField h = new TextField("height");
		
		Button emp = new Button("Create");
		
		emp.addActionListener(ac->{
			int width  = Integer.parseInt(w.getText());
			int height = Integer.parseInt(h.getText());
			
			e.onLoad(new Image(new Layer(width, height)));

			dispose();
		});
		
		add(w);
		add(h);
		add(emp);
		
		dodajOsluskivace();
	}

	private void setup(Editor e) {
		
		setSize(100,200);
		
		this.setLayout(new GridLayout(3, 1));
		
		Button emp = new Button("Empty");
		Button bmp = new Button("BMP");
		Button pam = new Button("PAM");
		
		emp.addActionListener(ac->{
			Layer newL = new Layer(e.img.width, e.img.height);
			
			e.l.add(new LayerPanel(e, newL, e.img.layers.size()));
			
			e.img.addLayer(newL);
			
			e.refresh();
			dispose();
		});
		
		bmp.addActionListener(ac->{
			InputDialog d = new InputDialog(this);
			
			d.start(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent a) {	
					String s = d.in.getText();
					
					try {
						Layer newL = new BMPFormatter().loadLayer(s);
						e.l.add(new LayerPanel(e, newL, e.img.layers.size()));
						
						e.img.addLayer(newL);
						
						e.refresh();
						dispose();
						
					}  catch (IOException e1) {
						new ErrorMenu(LayerDialog.this, "FILE NOT FOUND");
					} catch (ParseException e1) {
						new ErrorMenu(LayerDialog.this, "INVALID FILE");
					}
				}
			});
		});
		
		pam.addActionListener(ac->{
			InputDialog d = new InputDialog(this);
			
			d.start(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent a) {	
					String s = d.in.getText();
					
					try {
						Layer newL = new PAMFormatter().loadLayer(s);
						e.l.add(new LayerPanel(e, newL, e.img.layers.size()));
						
						e.img.addLayer(newL);
						
						e.refresh();
						dispose();
						
					} catch (IOException e1) {
						new ErrorMenu(LayerDialog.this, "FILE NOT FOUND");
					} catch (ParseException e1) {
						new ErrorMenu(LayerDialog.this, "INVALID FILE");
					}
				}
			});

		});
		
		add(emp);
		add(bmp);
		add(pam);
		
		dodajOsluskivace();
	}
	
	private void dodajOsluskivace() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
				{dispose();}
		});
	}
	
}

@SuppressWarnings("serial")
class CompositeMenuItem extends MenuItem {
	CompositeMenuItem(Editor e, CompositeOperation op){
		super(op.name);
		
		this.addActionListener(a->{
			op.run(e);
		});
	}
}

@SuppressWarnings("serial")
class CompositeSaveMenu extends Dialog {
	CompositeSaveMenu(Editor e){
		super(e, "Save Menu", true);
		
		if (e.img == null || e.img.operations.size() == 0) dispose();
		
		this.setLayout(new GridLayout(e.img.operations.size(), 1));
		
		for (CompositeOperation op: e.img.operations) {
			Button butt = new Button(op.name);
			
			butt.addActionListener(a -> {
				InputDialog d = new InputDialog(this);
				d.start(ac->{
					String s = d.in.getText();
					
					try {
						op.save(s);
					} catch (IOException e1) {
						new ErrorMenu(CompositeSaveMenu.this, "INVALID PATH");
					}
					
				});
			});
			
			add(butt);
		}
		
		dodajOsluskivace();
		
		setVisible(true);
	}
	
	private void dodajOsluskivace() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
				{dispose();}
		});
	}
}

@SuppressWarnings("serial")
class ErrorMenu extends Dialog {
	ErrorMenu(Window w, String s){
		super(w, "Save Menu");
		
		this.setSize(400, 200);
		
		GridBagLayout lay = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		
		this.setLayout(lay);
		
		add(new Label(s), gbc);
		
		dodajOsluskivace();
		setVisible(true);
	}
	
	private void dodajOsluskivace() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
				{dispose();}
		});
	}
}


@SuppressWarnings("serial")
public class Editor extends Frame {
	
	Image img = null;
	
	static final Color color0 = new Color(0xFFFFFF);
	static final Color color1 = new Color(0xFFCCBC);
	static final Color color2 = new Color(0xD3E3FC);
	static final Color color3 = new Color(0x00887A);
	static final Color color4 = new Color(0x77A6F7);
	
	Editor(){
		super("Editor");
		
		this.setSize(1280, 720);
		add(right(), BorderLayout.EAST);
		
		
		dodajOsluskivace();
        dodajMeni();
        
        this.setBackground(color0);
		setVisible (true);
	}
	
	Panel l = new Panel(new GridLayout(10,3));
	Panel s = new Panel(new GridLayout(10,3));
	
	private Panel right() {
		
		Panel p = new Panel();
		p.setLayout(new GridLayout(2,1));
		
		initLayerMenu();
		initSelMenu();

		
		p.add(l);
		p.add(s);
		
		return p;
	}

	private void addLayer() {
		new LayerDialog(this);
	}
	
	private void initLayerMenu() {
		Panel p = new Panel(new GridLayout(1,2));
		l.setBackground(color1);
		
		
		Button addL = new Button("+");

		addL.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addLayer();
			}
			
		});
		p.add(new Label("layers:"));
		p.add(addL);
		
		l.add(p);
		
	}
	
	private void initSelMenu() {
		s.add(new Label("selections:"));
		s.setBackground(color2);
	}
	
	private void dodajOsluskivace() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				exitSave();
				dispose();
			}
		});
	}
	
	private void dodajMeni() {
		MenuBar bar = new MenuBar();
		
		addLoad(bar);
		addSave(bar);
		addEdit(bar);
		
		this.setMenuBar(bar);
	}
	
	private void addLoad(MenuBar bar) {
		Menu load = new Menu("Load");
		bar.add(load);
		
		MenuItem BMP = new MenuItem("BMP");
		MenuItem PAM = new MenuItem("PAM");
		MenuItem JSON = new MenuItem("JSON");
		MenuItem Composite = new MenuItem("Composite");
		
		BMP.addActionListener(e->{loadBMP();});
		PAM.addActionListener(e->{loadPAM();});
		JSON.addActionListener(e->{loadJSON();});
		Composite.addActionListener(e->{loadComposite();});
		
		load.add(BMP);
		load.add(PAM);
		load.add(JSON);
		load.add(Composite);
	}
	
	private void loadComposite() {
		if (img == null) return;
		InputDialog d = new InputDialog(this);
		d.start(a->{
				String s = d.in.getText();
				try {
					CompositeOperation op = CompositeOperation.parse(s); 
					img.operations.add(op);
					edit.add(new CompositeMenuItem(Editor.this, op ));
				} catch (IOException e1) {
					new ErrorMenu(this, "FILE NOT FOUND");
				} catch (ParseException e1) {
					new ErrorMenu(this, "INVALID FILE");
				}
			
		});
	}
	
	private void addSave(MenuBar bar) {
		Menu save = new Menu("Save");
		bar.add(save);
		
		MenuItem BMP = new MenuItem("BMP");
		MenuItem PAM = new MenuItem("PAM");
		MenuItem JSON = new MenuItem("JSON");
		MenuItem Composite = new MenuItem("Composite");
		
		BMP.addActionListener(e->{saveBMP();});
		PAM.addActionListener(e->{savePAM();});
		JSON.addActionListener(e->{saveJSON();});
		Composite.addActionListener(e->{ new CompositeSaveMenu(this); });
		
		save.add(BMP);
		save.add(PAM);
		save.add(JSON);
		save.add(Composite);
	}

	Menu edit = new Menu("Edit");
	
	private void addEdit(MenuBar bar) {
		bar.add(edit);
		
		MenuItem add = new MenuItem("Add");
		MenuItem sub = new MenuItem("Subtract");
		MenuItem rsb = new MenuItem("Reverse Subtract");
		MenuItem mul = new MenuItem("Multiply");
		MenuItem div = new MenuItem("Divide");
		MenuItem rdv = new MenuItem("Reverse Divide");
		MenuItem pow = new MenuItem("Power");
		MenuItem log = new MenuItem("Logarithm");
		MenuItem abs = new MenuItem("Absolute");
		MenuItem min = new MenuItem("Minimum");
		MenuItem max = new MenuItem("Maximum");
		MenuItem inv = new MenuItem("Invert");
		MenuItem bnw = new MenuItem("Black & White");
		MenuItem gra = new MenuItem("Grayscale");
		MenuItem med = new MenuItem("Median");
		MenuItem fill = new MenuItem("Fill");
		
		MenuItem comp = new MenuItem("Create Composite");
		
		abs.addActionListener(e->{new Absolute().run(this);});
		inv.addActionListener(e->{new Invert().run(this);});
		bnw.addActionListener(e->{new BlackWhite().run(this);});
		gra.addActionListener(e->{new Grayscale().run(this);});
		med.addActionListener(e->{new Median().run(this);});
		
		add.addActionListener(e->{
			InputDialog d = new InputDialog(this);
			d.start(a->{
					String s = d.in.getText();
					try { new Add(Integer.parseInt(s)).run(Editor.this); }
					catch(NumberFormatException er) { d.dispose(); }
				
			});
		});
		
		sub.addActionListener(e->{
			InputDialog d = new InputDialog(this);
			d.start(a->{
					String s = d.in.getText();
					try { new Subtract(Integer.parseInt(s)).run(Editor.this); }
					catch(NumberFormatException er) { d.dispose(); }
				
			});
		});
		
		rsb.addActionListener(e->{
			InputDialog d = new InputDialog(this);
			d.start(a->{
					String s = d.in.getText();
					try { new ReverseSubtract(Integer.parseInt(s)).run(Editor.this); }
					catch(NumberFormatException er) { d.dispose(); }
				
			});
		});
		
		mul.addActionListener(e->{
			InputDialog d = new InputDialog(this);
			d.start(a->{
					String s = d.in.getText();
					try { new Multiply(Integer.parseInt(s)).run(Editor.this); }
					catch(NumberFormatException er) { d.dispose(); }
				
			});
		});
		
		div.addActionListener(e->{
			InputDialog d = new InputDialog(this);
			d.start(a->{
					String s = d.in.getText();
					try { new Divide(Integer.parseInt(s)).run(Editor.this); }
					catch(NumberFormatException er) { d.dispose(); }
				
			});
		});
		
		rdv.addActionListener(e->{
			InputDialog d = new InputDialog(this);
			d.start(a->{
					String s = d.in.getText();
					try { new ReverseDivide(Integer.parseInt(s)).run(Editor.this); }
					catch(NumberFormatException er) { d.dispose(); }
				
			});
		});
		
		min.addActionListener(e->{
			InputDialog d = new InputDialog(this);
			d.start(a->{
					String s = d.in.getText();
					try { new Minimum(Integer.parseInt(s)).run(Editor.this); }
					catch(NumberFormatException er) { d.dispose(); }
				
			});
		});
		
		max.addActionListener(e->{
			InputDialog d = new InputDialog(this);
			d.start(a->{
					String s = d.in.getText();
					try { new Maximum(Integer.parseInt(s)).run(Editor.this); }
					catch(NumberFormatException er) { d.dispose(); }
				
			});
		});
		
		pow.addActionListener(e->{
			InputDialog d = new InputDialog(this);
			d.start(a->{
					String s = d.in.getText();
					try { new Power(Double.parseDouble(s)).run(Editor.this); }
					catch(NumberFormatException er) { d.dispose(); }
				
			});
		});
		
		log.addActionListener(e->{
			InputDialog d = new InputDialog(this);
			d.start(a->{
					String s = d.in.getText();
					try { new Logarithm(Double.parseDouble(s)).run(Editor.this); }
					catch(NumberFormatException er) { d.dispose(); }
				
			});
		});
		
		fill.addActionListener(e->{
			new FillDialog(Editor.this);
		});
		
		comp.addActionListener(e->{
			InputDialog d = new InputDialog(this);
			d.start(a->{
					String s = d.in.getText();
					new OperationCreator(Editor.this, s);
			});
		});
		
		
		edit.add(add);
		edit.add(sub);
		edit.add(rsb);
		edit.add(mul);
		edit.add(div);
		edit.add(rdv);
		edit.add(pow);
		edit.add(log);
		edit.add(abs);
		edit.add(min);
		edit.add(max);
		edit.add(inv);
		edit.add(bnw);
		edit.add(gra);
		edit.add(med);
		edit.add(fill);
		edit.add(comp);
		
	}
	
	private void loadJSON(){
		
		InputDialog d = new InputDialog(this);
				
		d.start(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent a) {
				
				
				String s = d.in.getText();
				
				try {
					onLoad(new MyFormatter().load(s));
				}  catch (IOException e1) {
					new ErrorMenu(Editor.this, "FILE NOT FOUND");
				} catch (ParseException e1) {
					new ErrorMenu(Editor.this, "INVALID FILE");
				}
				
			}
			
		});

	}
	
	private void loadBMP(){
		
		InputDialog d = new InputDialog(this);
				
		d.start(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {	
				String s = d.in.getText();
				
				try {
					onLoad(new BMPFormatter().load(s));
				}  catch (IOException e1) {
					new ErrorMenu(Editor.this, "FILE NOT FOUND");
				} catch (ParseException e1) {
					new ErrorMenu(Editor.this, "INVALID FILE");
				}
			}
		});

	}
	
	private void loadPAM(){
		
		InputDialog d = new InputDialog(this);
				
		d.start(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {	
				String s = d.in.getText();
				
				try {
					onLoad(new PAMFormatter().load(s));
				}  catch (IOException e1) {
					new ErrorMenu(Editor.this, "FILE NOT FOUND");
				} catch (ParseException e1) {
					new ErrorMenu(Editor.this, "INVALID FILE");
				}
			}
		});

	}
	
	private void exitSave() {
		InputDialog d = new InputDialog(this);
		
		d.start(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent a) {
				
				
				String s = d.in.getText();
				
				try {
					new MyFormatter().save(s,img);
				} catch (IOException e1) {
					new ErrorMenu(Editor.this, "INVALID PATH");
				}
				
				Editor.this.dispose();
			}
			
		});
	}
	
	private void saveJSON() {
		
		InputDialog d = new InputDialog(this);
				
		d.start(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent a) {
				
				
				String s = d.in.getText();
				
				try {
					new MyFormatter().save(s,img);
				}  catch (IOException e1) {
					new ErrorMenu(Editor.this, "INVALID PATH");
				}
				
			}
			
		});

	}
	
	private void saveBMP() {
		
		InputDialog d = new InputDialog(this);
				
		d.start(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				String s = d.in.getText();
				
				try {
					new BMPFormatter().save(s,img);
				} catch (IOException e1) {
					new ErrorMenu(Editor.this, "INVALID PATH");
				}
			}
		});

	}
	
	private void savePAM() {
		InputDialog d = new InputDialog(this);
		
		d.start(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				String s = d.in.getText();
				
				try {
					new PAMFormatter().save(s,img);
				} catch (IOException e1) {
					new ErrorMenu(Editor.this, "INVALID PATH");
				}
			}
		});

	}

	void onLoad(Image new_img) {
		if (img != null) {
			this.remove(img);
			for (int i = 1; i < s.getComponentCount();) {
				s.remove(i);
			}
			for (int i = 1; i < l.getComponentCount();) {
				l.remove(i);
			}
			
			for (int i = 17; i < edit.getItemCount();) {
				edit.remove(i);
			}
		}
		
		img = new_img;
		
		img.addMouseListener(mouse);
		img.addMouseMotionListener(mouse);
		
		add(img, BorderLayout.CENTER);
		
		int i = 0;
		for (Selection sel: img.selections) {
			s.add(new SelectionPanel(this, sel, i));
			i++;
		}
		i = 0;
		for (Layer lay: img.layers) {
			l.add(new LayerPanel(this, lay, i));
			i++;
		}
		for (CompositeOperation op: img.operations) {
			edit.add(new CompositeMenuItem(this, op));
		}
		
		refresh();
	}
	
	void refresh() {
		
		revalidate();
		repaint();
	}

	boolean down = false;
	int xa;
	int ya;
	int xb;
	int yb;

	@Override
	public void update(Graphics g) {
		paint(g);
	}
	
	@Override
	public void paint(Graphics g) {
		
		if (img != null) {			
			img.paint(img.getGraphics());
		}
		
		if (down) {
			
			Graphics2D gi = (Graphics2D) img.getGraphics();
			
			float[] dash1 = { 2f, 0f, 2f };
			BasicStroke bs1 = new BasicStroke(1, 
			        BasicStroke.CAP_BUTT, 
			        BasicStroke.JOIN_ROUND, 
			        1.0f, 
			        dash1,
			        2f);
			
			gi.setStroke(bs1);
			gi.setColor(Color.MAGENTA);
			
			gi.drawLine(xa, ya, xb, ya);
			gi.drawLine(xa, ya, xa, yb);
			
			gi.drawLine(xb, ya, xb, yb);
			gi.drawLine(xa, yb, xb, yb);
			
		}
		
	}

	MouseAdapter mouse = new MouseAdapter() {
		

		@Override
		public void mousePressed(MouseEvent arg0) {
			if (arg0.getButton() != MouseEvent.BUTTON1) return;
			
			down = true;
			
			xa = Math.round(Math.round((float)arg0.getX() / ((float)img.getWidth()/img.width)) * ((float)img.getWidth()/img.width));
			ya =  Math.round(Math.round((float)arg0.getY() / ((float)img.getHeight()/img.height)) * ((float)img.getHeight()/img.height));
			xb = xa;
			yb = ya;
		}
		
		@Override
		public void mouseDragged(MouseEvent arg0) {
			int xn, yn;
			xn = Math.round(Math.round((float)arg0.getX() / ((float)img.getWidth()/img.width)) * ((float)img.getWidth()/img.width));
			yn = Math.round(Math.round((float)arg0.getY() / ((float)img.getHeight()/img.height)) * ((float)img.getHeight()/img.height));
			
			if (Math.abs(xb - xn) >= img.getWidth()/img.width || Math.abs(yb - yn) >= img.getHeight()/img.height) {				
				xb = xn;
				yb = yn;
				repaint();
			}
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			if (arg0.getButton() != MouseEvent.BUTTON1) return;
		
			if (!down) return;
			
			down = false;
			
			int xn, yn;
			xn = Math.round((float)arg0.getX() / ((float)img.getWidth()/img.width));
			yn = Math.round((float)arg0.getY() / ((float)img.getHeight()/img.height));
			
			int x,y,width, height;
			
			xa = Math.round(xa/((float)img.getWidth()/img.width));
			ya = Math.round(ya/((float)img.getHeight()/img.height));
			
			x = Math.min(xa, xn);
			y = Math.min(ya, yn);
			
			width = Math.abs(xa - xn);
			height = Math.abs(ya - yn);
			
			if (width <= 0 || height <= 0) return;
			
			Selection sel = new Selection(width, height, x, y);
			
			s.add(new SelectionPanel(Editor.this, sel, img.selections.size()));
			
			img.selections.add(sel);
			
			refresh();
		}
	};
	
	public static void main (String [] args)
    {
        new Editor ();
    }
}
