package dn.cfind;

import java.io.*;

public class Debug {
	public static final boolean DEBUG_MODE = true;
	
	private static final OutputStream dummyOut = new ByteArrayOutputStream();
	private static final PrintStream dummyPrint = new PrintStream(dummyOut);

	private static final OutputStream dummyErr = new ByteArrayOutputStream();
	private static final PrintStream dummyPrintErr = new PrintStream(dummyErr);
	
	public static final PrintStream out;
	public static final PrintStream err;
	
	private Debug() {
		
	}
	
	static {
		if(DEBUG_MODE) {
			out = System.out;
			err = System.err;
		}
		else {
			out = dummyPrint;
			err = dummyPrintErr;
		}
	}
}
