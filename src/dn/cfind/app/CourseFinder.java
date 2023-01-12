package dn.cfind.app;

import java.awt.*;
import javax.swing.*;

import dn.cfind.*;
import dn.cfind.tests.TestData;

public class CourseFinder {
	JFrame uiFrame;
	FinderPanel fPanel;
	
	// Start!
	public static void main(String[] args) {
		CourseFinder f = new CourseFinder();
		
		CourseCollection cc = new CourseCollection();
		
		// Import test data
		TestData test = TestData.getTestData();
		test.export(cc);
		
		if(GraphicsEnvironment.isHeadless()) {
			// If we are here then a graphical UI is not supported.
		}
		else {
			f.UILaunch(cc);
		}
	}
	
	public void UILaunch(CourseCollection cc) {
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
		fPanel.loadData(cc);
		
		uiFrame.getContentPane().add(fPanel);

		uiFrame.setMinimumSize(new Dimension(800,600));
		uiFrame.pack();
		// Center of the screen.
		uiFrame.setLocationRelativeTo(null);
		// Always exit on close, even on a Mac.
		uiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		uiFrame.setVisible(true);
	}
}
