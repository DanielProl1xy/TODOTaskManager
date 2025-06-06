package Core;

import UI.MainWindow;

public final class ToDoApplication {
	
	private static ToDoApplication app;

	private MainWindow window;

	private String configPath;
	
	public static void main(String[] args) {
		
		if(app != null)
			return;
		
		app = new ToDoApplication(args);
		
		app.Exec();
		
	}	

	public String GetConfigPath() {	
		return configPath;
	}

	public ToDoApplication GetApp() {
		return app;
	}
	
	private ToDoApplication(String[] args_) {

		window = new MainWindow();
	}
	
	private void Exec() {
		window.setVisible(true);	
	}	
	
}
