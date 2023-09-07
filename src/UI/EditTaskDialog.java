package UI;

import javax.swing.JDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import Core.ToDoTask;

public class EditTaskDialog extends JDialog {
	
	private final int width = 400;
	private final int height = 200;

	// Containers
	private JPanel widgetPanel;
	
	// Widgets
	private JTextField taskText;
	private JButton okButton;
	private JButton cancelButton;
	private ToDoTask task;

	
	private static volatile EditTaskDialog instance;
	
	public static void EditTask(MainWindow parent, ToDoTask task) {
		
		EditTaskDialog dlg = getInstance(parent);
		dlg.SetTask(task);
		dlg.setTitle("Edit task: " + task.Text);
		dlg.setVisible(true);
	}
	
	public static ToDoTask CreateTask(MainWindow parent) {
		
		EditTaskDialog dlg = getInstance(parent);
		dlg.SetTask(new ToDoTask("new task"));
		dlg.setTitle("Creating new task");
		dlg.setVisible(true);
		
		return dlg.GetTask();
	}
	
	public ToDoTask GetTask() {
		return task;
	}
	
	public void SetTask(ToDoTask newTask) {
		task = newTask;
		
		taskText.setText(task.Text);
	}
	
	private static EditTaskDialog getInstance(MainWindow parent) {
		
		if(instance == null) {
			instance = new EditTaskDialog(parent);
		}
		
		return instance;
	}
	
	private EditTaskDialog(MainWindow parent) {
		
		super(parent);
		
		this.setModal(true);
		this.setSize(400, 200);
		this.setResizable(false);
		
		// Spawn in a middle of parent window
		this.setLocation(parent.getX() + parent.getWidth() / 2 - width / 2,  
				parent.getY() + parent.getHeight() / 2 - height / 2);
		
		initWidgets();
		initActions();
	}
	
	
	private void initWidgets() {
		
		widgetPanel = new JPanel();
		
		taskText = new JTextField();
		okButton = new JButton("Ok");
		cancelButton = new JButton("Cancel");
		
		widgetPanel.add(taskText);
		widgetPanel.add(cancelButton);
		widgetPanel.add(okButton);
		
		this.add(widgetPanel);
	}
	
	private void initActions() {
		
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				onOkButtonClicked();
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				onCancelButtonClicked();
			}
		});
		
		
	}
	
	private void onOkButtonClicked() {
		
		task.Text = taskText.getText();
		this.setVisible(false);
	}
	
	private void onCancelButtonClicked() {
		
		this.setVisible(false);
	}

}
