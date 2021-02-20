package editor;


class Add extends Operation {
	
	public Add(int c) {
		super("add");
		this.c.add(c);
	}
};

class Subtract extends Operation {
	
	public Subtract(int c) {
		super("sub");
		this.c.add(c);
	}
	
};

class ReverseSubtract extends Operation {
	public ReverseSubtract(int c) {
		super("rsb");
		this.c.add(c);
	}
	
};

class Multiply extends Operation {
	public Multiply(int c) {
		super("mul");
		this.c.add(c);
		
	}
};

class Divide extends Operation {
	public Divide(int c) {
		super("div");
		this.c.add(c);
	}
	
};

class ReverseDivide extends Operation {
	public ReverseDivide(int c) {
		super("rdv");
		

		this.c.add(c);
	}
};

class Power extends Operation {
	public Power(double c) {
		super("pow");

		this.c.add(c);
	}
	
};

class Logarithm extends Operation {
	public Logarithm(double c) {
		super("log");
		

		this.c.add(c);
	}
};

class Absolute extends Operation {

	public Absolute() {
		super("abs");
	}
};

class Minimum extends Operation {
	public Minimum(int c) {
		super("min");
		

		this.c.add(c);
	}
	
};

class Maximum extends Operation {
	public Maximum(int c) {
		super("max");
		

		this.c.add(c);
	}
};

class Invert extends Operation {

	public Invert() {
		super("inv");
	}
	
};

class BlackWhite extends Operation {

	public BlackWhite() {
		super("b&w");
	}
	
};

class Grayscale extends Operation {
	
	public Grayscale() {
		super("gra");
	}
};

class Median extends Operation {
	
	public Median() {
		super("med");
	}
};

class Fill extends Operation {
	public Fill(int r, int g, int b, int a) {
		super("fill");
		c.add(r);
		c.add(g);
		c.add(b);
		c.add(a);
	}
};
