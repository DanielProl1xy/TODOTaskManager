package Core;

import UI.MainWindow;

public final class ToDoApplication {
	
	private static ToDoApplication app;
	
	public static void main(String[] args) {
		
		if(app != null)
			return;
		
		app = new ToDoApplication(args);
		
		app.Exec();
		
	}
	
	private MainWindow window;
	
	private ToDoApplication(String[] args_) {

		window = MainWindow.GetInstance();
	}
	
	private int Exec() {
	
		window.setVisible(true);
		
		return 0;
	}	

	private static  String configPath;
	
	public static String GetConfigPath() {	
		return configPath;
	}
}
