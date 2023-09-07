package Core;

import UI.MainWindow;

public final class ToDoApplication {
	
	public static void main(String[] args) {
		
		ToDoApplication app = new ToDoApplication(args);
		
		app.Exec();
		
	}
	
	private MainWindow window;
	private String[] args;
	
	public ToDoApplication(String[] args) {
		this.args = args;
		window = MainWindow.GetInstance();
	}
	
	public int Exec() {
	
		window.setVisible(true);
		
		return 0;
	}
}
