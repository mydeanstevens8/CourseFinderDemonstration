package dn.cfind.app;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;

public class CourseUI extends JPanel {

	private static final long serialVersionUID = -3405668583705496587L;
	private JLabel courseName;
	private JTextPane courseDescription;
	private JLabel courseLocation;

	/**
	 * Create the panel.
	 */
	// Auto-generated code using Eclipse WindowBuilder
	public CourseUI() {
		setOpaque(false);
		setBorder(new EmptyBorder(4, 4, 4, 4));
		setLayout(new BorderLayout(4, 4));
		
		courseName = new JLabel("Course Name");
		courseName.setFont(new Font("Arial", Font.BOLD, 18));
		add(courseName, BorderLayout.NORTH);
		
		courseDescription = new JTextPane();
		courseDescription.setOpaque(false);
		courseDescription.setFont(new Font("Arial", Font.PLAIN, 12));
		courseDescription.setBorder(new EmptyBorder(0, 0, 0, 0));
		courseDescription.setBackground(UIManager.getColor("TextPane.background"));
		courseDescription.setText("Course description.");
		courseDescription.setEditable(false);
		add(courseDescription, BorderLayout.CENTER);
		
		JPanel utilityPanel = new JPanel();
		utilityPanel.setOpaque(false);
		FlowLayout flowLayout = (FlowLayout) utilityPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(utilityPanel, BorderLayout.SOUTH);
		
		JLabel courseLocationLab = new JLabel("Location:");
		courseLocationLab.setFont(new Font("Arial", Font.BOLD, 12));
		utilityPanel.add(courseLocationLab);
		
		courseLocation = new JLabel("Here");
		courseLocation.setFont(new Font("Arial", Font.BOLD, 12));
		utilityPanel.add(courseLocation);

	}
	
	public void setCUIParam(String name, String description, String location) {
		courseName.setText(name);
		courseDescription.setText(description);
		courseLocation.setText(location);
	}

}
