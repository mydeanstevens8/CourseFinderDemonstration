package dn.cfind.app;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import dn.cfind.model.*;

import java.util.*;
import java.util.List;

public class FinderPanel extends JPanel {

	private static final long serialVersionUID = -3334214216623518337L;
	
	private final JPanel searchBox = new JPanel();
	private JTextField searchField;
	private final JButton btnGo = new JButton("Go"); 
	private final JScrollPane resultsScroll = new JScrollPane();
	
	private final JPanel results = new JPanel();
	
	protected FinderSystem data;
	
	private Keyword.Category currentFindMode = Keyword.Category.HOBBY;
	
	private final JComboBox<Keyword.Category> searchModeBox = new JComboBox<>();

	/**
	 * Create the panel.
	 */
	// Auto-generated code using Eclipse WindowBuilder
	public FinderPanel() {
		setLayout(new BorderLayout(0, 0));
		
		searchBox.setBorder(new EmptyBorder(4, 4, 4, 4));
		add(searchBox, BorderLayout.NORTH);
		searchBox.setLayout(new BoxLayout(searchBox, BoxLayout.X_AXIS));
		
		JLabel searchLabel = new JLabel("Search by: ");
		searchBox.add(searchLabel);
		
		Component horizontalStrut = Box.createHorizontalStrut(4);
		searchBox.add(horizontalStrut);

		searchModeBox.setModel(new DefaultComboBoxModel<Keyword.Category>(Keyword.Category.values()));
		
		searchModeBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				changeSearchMode((Keyword.Category) searchModeBox.getSelectedItem());
			}
		});
		
		searchBox.add(searchModeBox);
		
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
	
	public void changeSearchMode(Keyword.Category newMode) {
		System.out.println("Search mode changed: " + newMode);
		currentFindMode = newMode;
	}
	
	public void loadData(FinderSystem data) {
		this.data = data;
	}
	
	
	public void startSearch() {
		Keyword term = new Keyword(searchField.getText(), currentFindMode);
		startSearchKeyword(term);
	}
	
	public void startSearchKeyword(Keyword kw) {
		System.out.println("Searching using keyword: "+kw.getName()+"...");
		List<Course> courses = data.getRelevantCourses(kw);
		
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
		newE.setCUIParam(c.getName(), "", c.getCampus().getName());
		
		return newE;
	}

}
