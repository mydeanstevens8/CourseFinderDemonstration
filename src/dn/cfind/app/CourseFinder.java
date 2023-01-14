package dn.cfind.app;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;

import javax.swing.*;

import dn.cfind.model.*;

public class CourseFinder {
	public static final String APP_DEFAULT_MEMORY_LOCATION = "cfind_memory";
	
	private JFrame uiFrame;
	private FinderPanel fPanel;
	private FinderSystem engine;
	
	private static Thread EventDispatchThread;
	
	private static CourseFinder instance = new CourseFinder();
	
	// Private constructor - no instantiation but ourselves.
	private CourseFinder() {
		engine = new FinderSystem();
		load();
	}
	
	public static CourseFinder getInstance() {
		return instance;
	}
	
	// Start!
	public static void main(String[] args) {
		CourseFinder f = getInstance();
		
		if(GraphicsEnvironment.isHeadless()) {
			// If we are here then a graphical UI is not supported.
		}
		else {
			try {
				EventQueue.invokeAndWait(new Runnable() {
					public void run() {
						f.UILaunch();
						EventDispatchThread = Thread.currentThread();
					}
				});
				
				EventDispatchThread.join();
				
				
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void UILaunch() {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			// Don't worry if we can't set it.
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// Again, don't worry.
			e.printStackTrace();
		}
		
		JFrame.setDefaultLookAndFeelDecorated(false);
		
		uiFrame = new JFrame("Course Finder");
		
		// Add in the components needed.
		fPanel = new FinderPanel();
		fPanel.loadData(engine);
		
		uiFrame.getContentPane().add(fPanel);

		uiFrame.setMinimumSize(new Dimension(800,600));
		uiFrame.pack();
		// Center of the screen.
		uiFrame.setLocationRelativeTo(null);
		// Always exit on close, even on a Mac.
		uiFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		uiFrame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent e) {
				whenDisposed();
			}
		});
		
		uiFrame.setVisible(true);
		
		fPanel.startSearch();
	}
	
	protected void autoImportFinder(File override) throws IOException, ClassNotFoundException, ClassCastException {
		File loc = (override != null)? override : new File(APP_DEFAULT_MEMORY_LOCATION);
		
		try {
			engine = importFinderFromFile(loc);
		} 
		catch(FileNotFoundException e) {
			// Try again with a null file.
			if(override != null) {
				autoImportFinder(null);
			}
			else {
				throw e;
			}
		} catch(IOException e) {
			throw e;
		} catch(ClassNotFoundException | ClassCastException e) {
			throw e;
		}
	}
	
	protected void autoExportFinder(File override) throws IOException, ClassCastException {
		File loc = (override != null)? override : new File(APP_DEFAULT_MEMORY_LOCATION);
	
		try {
			exportFinderToFile(loc, engine);
		} 
		catch(FileNotFoundException e) {
			// Try again with a null file.
			if(override != null) {
				autoExportFinder(null);
			}
			else {
				throw e;
			}
		} catch(IOException e) {
			throw e;
		} catch(ClassCastException e) {
			throw e;
		}
	}
	
	public static FinderSystem importFinderFromFile(File file) throws IOException, ClassNotFoundException {
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(file);
			
			FinderSystem fs = importFinder(fin);
			return fs;
		}
		finally {
			if(fin != null) fin.close();
		}
	}
	
	public static FinderSystem importFinder(InputStream stream) throws IOException, ClassNotFoundException {
		ObjectInputStream rd = null;
		try {
			rd = new ObjectInputStream(stream);
			
			Object obj = rd.readObject();
			
			// Throws class cast exception when it fails.
			return (FinderSystem) obj;
		}
		finally {
			if(rd != null) rd.close();
		}
	}
	
	public static void exportFinder(OutputStream stream, FinderSystem obj) throws IOException {
		ObjectOutputStream wt = null;
		try {
			if(obj == null) return;
			
			wt = new ObjectOutputStream(stream);
			wt.writeObject(obj);
		}
		finally {
			if(wt != null) wt.close();
		}
	}
	
	public static void exportFinderToFile(File file, FinderSystem obj) throws IOException {
		FileOutputStream fou = null;
		try {
			if(obj == null) return;
			
			fou = new FileOutputStream(file);
			
			exportFinder(fou, obj);
		}
		finally {
			if(fou != null) fou.close();
		}
	}
	
	public void load() {
		try {
			autoImportFinder(new File(APP_DEFAULT_MEMORY_LOCATION));
		} catch (ClassNotFoundException | ClassCastException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		try {
			autoExportFinder(new File(APP_DEFAULT_MEMORY_LOCATION));
		} catch (ClassCastException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void whenDisposed() {
		save();
		System.exit(0);
	}
}
