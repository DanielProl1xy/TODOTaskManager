package UI;

import javax.swing.JDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import Core.ToDoTask;
import Core.TaskStatus;

public class EditTaskDialog extends JDialog {
	
	private final int width = 400;
	private final int height = 200;

	// Containers
	private JPanel widgetPanel;
	private JPanel stateSelectPanel;
	// State selection
	private JCheckBox plannedCheck;
	private JCheckBox finishedCheck;
	private JCheckBox interruptedCheck;
	
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
		
		switchStatus(task.Status);
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
		
		stateSelectPanel = new JPanel();
		plannedCheck = new JCheckBox("Planned");
		finishedCheck = new JCheckBox("Finished");
		interruptedCheck = new JCheckBox("Interrupted");
		
		stateSelectPanel.add(plannedCheck);
		stateSelectPanel.add(finishedCheck);
		stateSelectPanel.add(interruptedCheck);
		
		widgetPanel.add(taskText);
		widgetPanel.add(cancelButton);
		widgetPanel.add(okButton);
		widgetPanel.add(stateSelectPanel);
		
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
		
		plannedCheck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				onCheckboxSwitched(e);
			}
		});
		
		finishedCheck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				onCheckboxSwitched(e);
			}
		});
		
		interruptedCheck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				onCheckboxSwitched(e);
			}
		});
		
	}
	
	private void onOkButtonClicked() {
		
		task.Text = taskText.getText();
		this.setVisible(false);
	}
	
	private void onCancelButtonClicked() {
		
		task = null;
		this.setVisible(false);
	}
	
	private void onCheckboxSwitched(ActionEvent e) {
		JCheckBox checked = (JCheckBox) e.getSource();
		boolean selected = checked.getModel().isSelected();
		
		if(!selected) {
			return;
		}
		
		if(checked == plannedCheck) {
			switchStatus(TaskStatus.Planned);
		}
		else if (checked == finishedCheck) {
			switchStatus(TaskStatus.Finished);
		}
		else if (checked == interruptedCheck) {
			switchStatus(TaskStatus.Interrupted);
		}
	}
	
	private void switchStatus(TaskStatus status)
	{
		task.Status = status;
		
		switch(status)
		{
		case Planned:
			plannedCheck.getModel().setSelected(true);		
			finishedCheck.getModel().setSelected(false);
			interruptedCheck.getModel().setSelected(false);
			break;
		case Finished:
			finishedCheck.getModel().setSelected(true);
			plannedCheck.getModel().setSelected(false);
			interruptedCheck.getModel().setSelected(false);
			break;
		case Interrupted:
			interruptedCheck.getModel().setSelected(true);
			finishedCheck.getModel().setSelected(false);
			plannedCheck.getModel().setSelected(false);
			break;
		}
	}

}
