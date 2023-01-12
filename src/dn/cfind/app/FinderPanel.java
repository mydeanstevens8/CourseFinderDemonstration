package dn.cfind.app;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.border.LineBorder;

import dn.cfind.*;

import java.awt.Color;
import javax.swing.border.CompoundBorder;
import java.awt.event.KeyEvent;
import java.util.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.GridLayout;

public class FinderPanel extends JPanel {

	private static final long serialVersionUID = -3334214216623518337L;
	
	private final JPanel searchBox = new JPanel();
	private JTextField searchField;
	private final JButton searchModeToggle = new JButton("Mode");
	private final JButton btnGo = new JButton("Go"); 
	private final JScrollPane resultsScroll = new JScrollPane();
	
	private final JPanel results = new JPanel();
	
	protected CourseCollection data;
	
	public static enum FindMode {
		HOBBY("By Hobby"),
		LOCATION("By Location");
		
		public final String description;
		
		private FindMode(String description) {
			this.description = description;
		}
	}
	
	private FindMode currentFindMode = FindMode.HOBBY;

	/**
	 * Create the panel.
	 */
	// Auto-generated code using Eclipse WindowBuilder
	public FinderPanel() {
		setLayout(new BorderLayout(0, 0));
		
		searchBox.setBorder(new EmptyBorder(4, 4, 4, 4));
		add(searchBox, BorderLayout.NORTH);
		searchBox.setLayout(new BoxLayout(searchBox, BoxLayout.X_AXIS));
		
		JLabel searchLabel = new JLabel("Search: ");
		searchBox.add(searchLabel);
		
		Component horizontalStrut = Box.createHorizontalStrut(4);
		searchBox.add(horizontalStrut);
		
		searchModeToggle.setText(currentFindMode.description);
		searchModeToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleFindMode();
			}
		});
		searchModeToggle.setToolTipText("Click to change search mode");
		searchModeToggle.setMnemonic(KeyEvent.VK_M);
		
		searchBox.add(searchModeToggle);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(4);
		searchBox.add(horizontalStrut_1);
		
		searchField = new JTextField();
		searchField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startSearch();
			}
		});
		searchField.setOpaque(true);
		searchField.setToolTipText("Enter a keyword or term");
		searchField.setBorder(new CompoundBorder(new LineBorder(new Color(128, 128, 128), 1, true), new EmptyBorder(0, 4, 0, 4)));
		searchBox.add(searchField);
		searchField.setColumns(10);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(4);
		searchBox.add(horizontalStrut_2);
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startSearch();
			}
		});
		btnGo.setToolTipText("Click to begin searching");
		btnGo.setMnemonic(KeyEvent.VK_ENTER);
		
		searchBox.add(btnGo);
		add(resultsScroll, BorderLayout.CENTER);
		resultsScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		resultsScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		resultsScroll.setViewportView(results);
		results.setLayout(new GridLayout(0, 1, 0, 4)); 
	}
	
	public void toggleFindMode() {
		switch(currentFindMode) {
		case HOBBY:
			changeFindMode(FindMode.LOCATION);
			break;
		case LOCATION:
			changeFindMode(FindMode.HOBBY);
			break;
		default:
			break;
		}
	}
	
	public void changeFindMode(FindMode mode) {
		this.currentFindMode = mode;
		searchModeToggle.setText(mode.description);
		
		// This component then needs to be laid out.
		searchBox.revalidate();
	}
	
	public void loadData(CourseCollection c) {
		data = c;
	}
	
	
	public void startSearch() {
		startSearchKeyword(searchField.getText());
	}
	
	public void startSearchKeyword(String keyword) {
		System.out.println("Searching using keyword: "+keyword+"...");
		List<Course> courses = data.provideCourseArrangement(keyword, currentFindMode == FindMode.LOCATION);
		
		fillResults(courses, true, 20);
	}
	
	public void fillResults(Collection<Course> cc, boolean clear, int limit) {
		if(clear) {
			results.removeAll();
		}
		
		int count = 0;
		for(Course c : cc) {
			if(limit >= 0 && count >= limit) {
				break;
			}
			
			CourseUI cui = elementForCourse(c);
			results.add(cui);
			
			count++;
		}
		
		// If the count is zero, add a dummy label.
		if(count == 0) {
			JLabel dummy = new JLabel("No results found. Please try a different term.");
			results.add(dummy);
		}
		
		results.revalidate();
	}
	
	public CourseUI elementForCourse(Course c) {
		CourseUI newE = new CourseUI();
		newE.setCUIParam(c.getName(), c.getDescription(), c.getLocation().getName());
		
		return newE;
	}

}
