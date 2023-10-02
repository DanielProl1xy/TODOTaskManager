package Core;

import java.time.LocalDateTime;

// Class for To do task
public class ToDoTask  {

	public String Text;
	
	public TaskStatus Status;

	public LocalDateTime StartTime;
	public LocalDateTime EndTime;
	
	// Set default values
	public ToDoTask(String text) {
		
		Text = text;
		Status = TaskStatus.Planned;
	}
	
	// Overrides toString function for JList widget
	@Override
	public String toString() {
		return this.Text + "(" + Status.toString() + ")";
	}
}
