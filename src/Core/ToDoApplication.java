package Core;

import UI.MainWindow;

public final class ToDoApplication {
	
	private static ToDoApplication app;

	private MainWindow window;

	private final String savingPath = "./tasks.bin";
	
	public static void main(String[] args) {
		
		if(app != null)
			return;
		
		app = new ToDoApplication(args);
		
		app.Exec();

	}	

	public String GetSavingPath() {	
		return savingPath;
	}

	public static ToDoApplication GetApp() {
		return app;
	}
	
	private ToDoApplication(String[] args_) {

		window = new MainWindow();
	}
	
	private void Exec() {
		System.out.println("INFO: Starting app");
		window.setVisible(true);	
	}
	
}
