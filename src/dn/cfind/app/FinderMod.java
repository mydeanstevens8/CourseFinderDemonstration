package dn.cfind.app;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

import java.util.*;
import java.util.List;

import dn.cfind.Debug;
import dn.cfind.model.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class FinderMod extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JLayeredPane layers;
	private JPanel current;
	private JLabel titleLab;
	private JTextField name;
	private JPanel children;
	private JLabel saveFeedback;
	private JButton back;
	private JPanel keywordPanel;

	private JList<ModelObject> list;
	
	protected ModelObject model;
	// A separate list to track model children.
	private List<ModelObject> modelChildren = new Vector<>();
	private DefaultListModel<ModelObject> modelChildrenUI = new DefaultListModel<>();
	
	protected boolean changesSinceLastSave = false;
	
	// Model parameters
	private boolean modelHasParent = false;
	private boolean modelHasChildren = false;
	private boolean modelNameSettable = false;
	private boolean modelHasKeywords = false;

	/**
	 * Create the panel.
	 */
	public FinderMod() {
		this(null);
	}
	
	public FinderMod(ModelObject model) {
		Debug.out.println("Given a: "+model);
		this.model = model;
		
		inferModel();
		
		buildUI();
		buildModelUI();
	}
	
	protected void buildModelUI() {
		// Visibility of buttons and elements.
		children.setVisible(modelHasChildren);
		back.setVisible(modelHasParent);
		name.setEnabled(modelNameSettable);
		keywordPanel.setVisible(modelHasKeywords);
		
		if(model != null) {
			titleLab.setText("Editing: " + model.getName());
			name.setText(model.getName());
		}
	}
	
	protected void inferModel() {
		modelHasChildren = model instanceof HasChildren<?, ?>;
		modelHasParent = model instanceof HasParent<?>;
		
		modelNameSettable = model instanceof Named.Settable;
		
		modelHasKeywords = model instanceof Keywordable;
		
		if(modelHasChildren) {
			try {
				@SuppressWarnings("unchecked")
				HasChildren<Collection<ModelObject>, ModelObject> mc = (HasChildren<Collection<ModelObject>, ModelObject>) model;
				modelChildren.addAll(mc.getChildren());
			} 
			catch(ClassCastException | NullPointerException e) {
				// Casting error, give up and set has children to false.
				modelHasChildren = false;
			}
		}

		// When the model child is set, make sure to update the UI.
		modelChildrenUI.addAll(modelChildren);
	}
	
	protected void buildUI() {
		setLayout(new BorderLayout(0, 0));
		
		layers = new JLayeredPane();
		add(layers, BorderLayout.CENTER);
		layers.setLayout(new BorderLayout(0, 0));
		
		current = new JPanel();
		layers.add(current);
		current.setLayout(new BorderLayout(0, 0));
		
		JPanel essentials = new JPanel();
		current.add(essentials, BorderLayout.NORTH);
		essentials.setLayout(new BorderLayout(0, 0));
		
		JPanel title = new JPanel();
		title.setForeground(UIManager.getColor("TextField.light"));
		title.setBackground(UIManager.getColor("Panel.foreground"));
		title.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(128, 128, 128)));
		FlowLayout flowLayout = (FlowLayout) title.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		essentials.add(title, BorderLayout.NORTH);
		
		back = new JButton("<-");
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeSelf();
			}
		});
		back.setToolTipText("Go back");
		back.setMnemonic(KeyEvent.VK_ESCAPE);
		title.add(back);
		
		titleLab = new JLabel("Editing: Title");
		titleLab.setBackground(UIManager.getColor("Panel.foreground"));
		titleLab.setForeground(UIManager.getColor("TextField.light"));
		title.add(titleLab);
		
		JPanel params = new JPanel();
		params.setBorder(new EmptyBorder(5, 5, 5, 5));
		essentials.add(params, BorderLayout.CENTER);
		params.setLayout(new BoxLayout(params, BoxLayout.PAGE_AXIS));
		
		JPanel namePanel = new JPanel();
		params.add(namePanel);
		namePanel.setLayout(new BorderLayout(5, 5));
		
		JLabel nameLab = new JLabel("Name:");
		namePanel.add(nameLab, BorderLayout.WEST);
		
		name = new JTextField();
		name.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				changesSinceLastSave = true;
			}
		});
		namePanel.add(name, BorderLayout.CENTER);
		name.setColumns(10);
		
		keywordPanel = new JPanel();
		params.add(keywordPanel);
		
		JButton keywordBtn = new JButton("Edit Keywords...");
		keywordBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				browseKeywords();
			}
		});
		keywordPanel.add(keywordBtn);
		
		children = new JPanel();
		children.setBorder(new EmptyBorder(5, 5, 5, 5));
		current.add(children, BorderLayout.CENTER);
		children.setLayout(new BorderLayout(5, 5));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		children.add(scrollPane, BorderLayout.CENTER);
		
		list = new JList<ModelObject>();
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() > 1) {
					// Double or more click...
					browseSelectedChild();
				}
			}
		});
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
			}
		});
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setModel(modelChildrenUI);
		scrollPane.setViewportView(list);
		
		
		JLabel childrenLab = new JLabel("Children:");
		children.add(childrenLab, BorderLayout.NORTH);
		
		JPanel operations = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) operations.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		children.add(operations, BorderLayout.SOUTH);

		JButton addChild = new JButton("Add");
		addChild.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addDefaultChild();
			}
		});
		operations.add(addChild);
		
		JButton deleteSel = new JButton("Delete");
		deleteSel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeSelectedChild();
			}
		});
		operations.add(deleteSel);
		
		JPanel toolPanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) toolPanel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		current.add(toolPanel, BorderLayout.SOUTH);
		
		JButton saveBtn = new JButton("Save");
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		
		saveFeedback = new JLabel("Saved");
		saveFeedback.setVisible(false);
		toolPanel.add(saveFeedback);
		toolPanel.add(saveBtn);
	}
	
	// Add to model children prototype.
	protected void addChild(ModelObject child) {
		Debug.out.println("Add a child: " + child);
		
		modelChildren.add(child);
		changesSinceLastSave = true;
		
		modelChildrenUI.add(0, child);
		
		// Reset the model.
		list.setModel(modelChildrenUI);
		list.revalidate();
	}
	
	protected boolean removeChild(ModelObject child) {
		Debug.out.println("Remove a child: " + child);
		
		String[] delOpts = {"Delete", "Cancel"};
		
		int opt = JOptionPane.showOptionDialog(this, "Delete this child? This cannot be undone.", "Remove", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
				null, delOpts, delOpts[1]);
		
		if(opt != 0) {
			// Here we do not delete.
			return false;
		}
		
		modelChildren.remove(child);
		changesSinceLastSave = true;

		modelChildrenUI.removeElement(child);

		// Reset the model.
		list.setModel(modelChildrenUI);
		list.revalidate();
		
		return true;
	}
	
	protected void removeSelectedChild() {
		ModelObject selectedChild = list.getSelectedValue();
		if(selectedChild != null)
			removeChild(selectedChild);
	}
	
	protected void browseChild(ModelObject child) {
		FinderMod childComp = new FinderMod(child);

		changesSinceLastSave = true;
		
		// Layer 1 above parent component
		layers.setLayer(childComp, 1);
		layers.add(childComp);
		
		layers.revalidate();
	}
	
	protected void browseSelectedChild() {
		ModelObject selectedChild = list.getSelectedValue();
		if(selectedChild != null)
			browseChild(selectedChild);
	}
	
	protected void browseKeywords() {
		changesSinceLastSave = true;
		if(modelHasKeywords) {
			KeywordMod.createFrom(this, (Keywordable) model);
		}
	}
	
	protected void closeSelf() {
		if(changesSinceLastSave) {
			String[] gbOpts = {"Go back", "Cancel"};
			int confirm = JOptionPane.showOptionDialog(
					this, "There are unsaved changes that may be lost. Go back?", "Unsaved Changes", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, gbOpts, gbOpts[0]
					);
			
			if(confirm != 0) return;
		}
		// Remove ourselves from the equation. Don't save if changes are made.
		Container parent = this.getParent();
		parent.remove(this);

		parent.repaint();
		parent.revalidate();
		parent.repaint();
	}
	
	public void save() {
		changesSinceLastSave = false;
		
		if(modelNameSettable) {
			((Named.Settable) model).setName(name.getText());
		}
		
		if(modelHasChildren) {
			@SuppressWarnings("unchecked")
			HasChildren<Collection<ModelObject>, ModelObject> mc = 
					(HasChildren<Collection<ModelObject>, ModelObject>) model;
			
			// Clear and re-add.
			mc.clearChildren();
			mc.addChildren(modelChildren);
		}
	}
	
	protected void addDefaultChild() {
		if(modelHasChildren) {
			// Java generics reaches its limit here. So that means referencing models directly...
			if(model instanceof FinderSystem) addChild(new School());
			if(model instanceof School) addChild(new Campus());
			if(model instanceof Campus) addChild(new Course());
		}
	}
}
