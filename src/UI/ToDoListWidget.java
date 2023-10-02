package UI;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import Core.ToDoTask;
import Core.ToDoApplication;

public class ToDoListWidget extends JList<ToDoTask> {
	
	
	DefaultListModel<ToDoTask> model;
	
	public ToDoListWidget() {
		
		model = new DefaultListModel<ToDoTask>();
		
		setModel(model);
		
		InitToDoList();
	}
	
	public void UpdateTask(int id, ToDoTask newTask) {
		
		model.setElementAt(newTask, id);
	}
	
	public void AddToDoTask(ToDoTask newTask) {
		
		model.addElement(newTask);;
	}
	
	public void RemoveToDoTask(ToDoTask newTask) {
		
		model.removeElement(newTask);
	}
	
	private void InitToDoList()
	{
		// TODO Load from .json config
	}
}
