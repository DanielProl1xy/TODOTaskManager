package UI;

import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import Core.ToDoTask;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.*;

public final class MainWindow extends JFrame {
		
	private final int width = 600;
	private final int height = 500;
	private final String title = "ToDo tasks manager";
	
	// Widgets
	private ToDoListWidget toDoListW;
	
	// Buttons
	private JButton addButton;
	private JButton remButton;
	private JButton editButton;
	
	// Containers
	private JPanel widgetsContainer;
	private JPanel buttonsContainer;
	
	public MainWindow() {
		
		super();
				
		this.setTitle(title);
		this.setSize(width, height);
		this.setResizable(false);
		
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int screenWidth = gd.getDisplayMode().getWidth();
		int screenHeight = gd.getDisplayMode().getHeight();
		
		// Spawn in a middle of current screen
		this.setLocation(screenWidth / 2 - width / 2, screenHeight / 2 - height / 2);
		
		initWidgets();
		initActions();
	}
	
	private void initWidgets() {		
		
		widgetsContainer = new JPanel();
		buttonsContainer = new JPanel();
		
		widgetsContainer.setLayout(new BoxLayout(widgetsContainer, BoxLayout.PAGE_AXIS));
		
		toDoListW = new ToDoListWidget();
		widgetsContainer.add(toDoListW);
		
		addButton = new JButton("Add");
		remButton = new JButton("Remove");
		editButton = new JButton("Edit");
		
		buttonsContainer.add(addButton);
		buttonsContainer.add(remButton);
		buttonsContainer.add(editButton);
		
		widgetsContainer.add(buttonsContainer);
		
		this.add(widgetsContainer);		
	}
	
	private void initActions() {
		
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				onAddButtonClicked();
			}
		});
		
		remButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				onRemButtonClicked();
			}
		});
		
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				onEditButtonClicked();
			}
		});
	}

	
	private void onAddButtonClicked() {
		
		ToDoTask task = EditTaskDialog.CreateTask(this);
		if(task == null)
			return;
		toDoListW.AddToDoTask(task);
	}
	
	private void onRemButtonClicked() {
		
		ToDoTask task = toDoListW.getSelectedValue();
		if(task == null)
			return;
			toDoListW.RemoveToDoTask(task);

	}
	
	private void onEditButtonClicked() {
		
		ToDoTask task = toDoListW.getSelectedValue();
		int id = toDoListW.getSelectedIndex();
		if(task == null)
			return;	
		
		EditTaskDialog.EditTask(this, task);
		toDoListW.UpdateTask(id, task);
	}


}
