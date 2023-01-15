package dn.cfind.app;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.Dialog.ModalityType;

import javax.swing.table.*;

import java.util.*;
import java.util.List;

import dn.cfind.model.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class KeywordMod extends JPanel {
	// A table model useful for modifiying keyword entries.
	public static class KeywordTableModel extends AbstractTableModel {
		static final int KEYWORD_LOC = 0;
		static final int CATEGORY_LOC = 1;
		static final int RELEVANCE_LOC = 2;
		
		private static final long serialVersionUID = 3132123130689529863L;
		
		private static enum ColumnID {
			KEYWORD(String.class, "Keyword"),
			CATEGORY(Keyword.Category.class, "Category"),
			RELEVANCE(Double.class, "Relevance");
			
			public final Class<?> type;
			public final String description;
			
			private ColumnID(Class<?> type, String description) {
				this.type = type;
				this.description = description;
			}
		}
		
		private static final ColumnID[] columnIDs = ColumnID.values();
		
		private Keywordable model;
		private List<Map.Entry<Keyword, Double>> arrangedModel;
		
		public KeywordTableModel() {
			this(null);
		}
		
		public KeywordTableModel(Keywordable kw) {
			if(kw != null) {
				this.model = kw;
				
				loadFromModel();
			}
			else {
				arrangedModel = new Vector<>();
			}
		}

		@Override
		public int getRowCount() {
			return arrangedModel.size();
		}
		
		@Override
		public String getColumnName(int col) {
			return columnIDs[col].description;
		}
		
		@Override
		public Class<?> getColumnClass(int col) {
			return columnIDs[col].type;
		}

		@Override
		public int getColumnCount() {
			return columnIDs.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			Map.Entry<Keyword, Double> selection = arrangedModel.get(rowIndex);

			switch(columnIndex) {
			case KEYWORD_LOC: //Keyword
				return selection.getKey().getName();
			case CATEGORY_LOC: // Category
				return selection.getKey().getCategory();
			case RELEVANCE_LOC: // Relevance
				return selection.getValue();
			default:
				return null;
			}
		}
		
		@Override 
		public void setValueAt(Object obj, int row, int col) {
			Map.Entry<Keyword, Double> selection = arrangedModel.get(row);
			
			switch(col) {
			case KEYWORD_LOC: //Keyword
				// Create a new object with the specified keyword changed.
				arrangedModel.set(row, Map.entry(
						new Keyword((String) obj, selection.getKey().getCategory()), 
						selection.getValue()
						)
					);
				break;
			case CATEGORY_LOC: // Category
				// Create a new object with the specified keyword changed.
				arrangedModel.set(row, Map.entry(
						new Keyword(selection.getKey().getName(), (Keyword.Category) obj), 
						selection.getValue()
						)
					);
				break;
			case RELEVANCE_LOC: // Relevance
				// Create a new object with the specified keyword changed.
				arrangedModel.set(row, Map.entry(
						selection.getKey(), 
						(Double) obj
						)
					);
				break;
			default:
				break;
			}
			
			fireTableCellUpdated(row, col);
		}
		
		@Override
		public boolean isCellEditable(int row, int col) {
			return true; // All cells are editable.
		}
		
		public void applyToModel() {
			// Remove all keywords and reapply.
			model.removeAllKeywords();
			
			for(Map.Entry<Keyword, Double> kw : arrangedModel) {
				model.addKeyword(kw.getKey(), kw.getValue());
			}
		}
		
		public void loadFromModel() {
			// Provide a consistent order when operating
			this.arrangedModel = new Vector<>(model.getKeywords().entrySet());
		}
		
		public void addKeyword(Keyword k, double relevance) {
			addKeyword(arrangedModel.size(), k, relevance);
		}
		
		public void addKeyword(int row, Keyword k, double relevance) {
			arrangedModel.add(row, Map.entry(k, relevance));
			
			fireTableRowsInserted(row, row);
		}
		
		public void removeKeyword(int row) {
			arrangedModel.remove(row);
			fireTableRowsDeleted(row, row);
		}
	}

	private static final long serialVersionUID = 1L;
	private JTable table;
	private KeywordTableModel model;
	
	private JButton addKeyword;
	private JButton deleteKeyword;

	public KeywordMod() {
		this(null);
	}
	
	/**
	 * Create the panel.
	 */
	public KeywordMod(Keywordable kw) {
		buildModel(kw);
		buildUI();
		
		buildTable();
		
		onChangedTable();
	}
	
	protected void buildModel(Keywordable kw) {
		model = new KeywordTableModel(kw);
	}
	
	protected void buildTable() {
		// Category column custom editor
		TableColumn colmod = table.getColumnModel().getColumn(KeywordTableModel.CATEGORY_LOC);
		
		JComboBox<Keyword.Category> categoryCombo = new JComboBox<>(Keyword.Category.values());
		TableCellEditor customEditor = new DefaultCellEditor(categoryCombo);
		
		colmod.setCellEditor(customEditor);
	}
	
	// Generated using window builder by Eclipse.
	protected void buildUI() {
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(table);
		
		JPanel controls = new JPanel();
		add(controls, BorderLayout.SOUTH);
		
		addKeyword = new JButton("Add Keyword");
		addKeyword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNewKeyword();
			}
		});
		addKeyword.setToolTipText("Add a new keyword");
		controls.add(addKeyword);
		
		deleteKeyword = new JButton("Delete Keyword");
		deleteKeyword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteSelectedKeyword();
			}
		});
		deleteKeyword.setToolTipText("Delete a keyword");
		controls.add(deleteKeyword);
		
		JButton saveBtn = new JButton("Save and Close");
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveAndClose();
			}
		});
		controls.add(saveBtn);
		
		model.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				onChangedTable();
			}
			
		});
	}
	
	protected void onChangedTable() {
		boolean empty = model.arrangedModel.isEmpty();
		deleteKeyword.setEnabled(!empty);
	}

	protected void addNewKeyword() {
		model.addKeyword(new Keyword("Keyword", Keyword.Category.GENERAL), 0);
		
		table.clearSelection();
		int lastRow = table.getRowCount() - 1;
		// Select the last row added.
		table.addRowSelectionInterval(lastRow, lastRow);
		// And start editing it.
		table.editCellAt(lastRow, 0);
	}
	
	protected void deleteSelectedKeyword() {
		// Prevent corruption of data when we stop
		table.getCellEditor().stopCellEditing();
		
		int selRow = table.getSelectedRow();
		
		if(selRow != -1) {
			model.removeKeyword(selRow);
		}
	}
	
	public void save() {
		model.applyToModel();
	}
	
	public void saveAndClose() {
		save();
		
		Window ancestor = SwingUtilities.getWindowAncestor(this);
		ancestor.dispose();
		
		createLock = false;
	}
	
	private static boolean createLock = false;
	
	public static void createFrom(Component parent, Keywordable kw) {
		if(createLock) return;
		
		Window ancestor = SwingUtilities.getWindowAncestor(parent);
		JDialog container = new JDialog(ancestor, "Edit Keywords");
		
		container.setResizable(true);
		container.setModalityType(ModalityType.APPLICATION_MODAL);
		container.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		KeywordMod newMod = new KeywordMod(kw);
		
		container.getContentPane().add(newMod);
		
		container.setMinimumSize(new Dimension(500, 400));
		container.pack();
		container.setLocationRelativeTo(parent);
		
		container.setVisible(true);
	}
}
