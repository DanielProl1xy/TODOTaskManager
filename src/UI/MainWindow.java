package UI;

import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import Core.TaskStatus;
import Core.ToDoApplication;
import Core.ToDoTask;

import static java.nio.file.StandardOpenOption.*;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.*;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public final class MainWindow extends JFrame {
		
	private final int width = 600;
	private final int height = 500;
	private final String title = "ToDo tasks manager";
	
	// Widgets
	private ToDoListWidget toDoListW;

	private List<ToDoTask> taskList;
	
	// Buttons
	private JButton addButton;
	private JButton remButton;
	private JButton editButton;
	
	// Containers
	private JPanel widgetsContainer;
	private JPanel buttonsContainer;
	
	public MainWindow() {
		
		super();

		taskList = new ArrayList<>();
				
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

		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				LoadTasks();
			}

			@Override
			public void windowClosing(WindowEvent e) {
				SaveTasks();
				System.exit(0);
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}
			
		});
	}

	
	private void onAddButtonClicked() {
		
		ToDoTask task = EditTaskDialog.CreateTask(this);

		if(task == null)
			return;
			
		taskList.add(task);
		toDoListW.AddToDoTask(task);
	}
	
	private void onRemButtonClicked() {
		
		ToDoTask task = toDoListW.getSelectedValue();
		if(task == null)
			return;
			toDoListW.RemoveToDoTask(task);

	}
	
	private void onEditButtonClicked() {
		
		int id = toDoListW.getSelectedIndex();
		ToDoTask task = taskList.get(id);
		if(task == null)
			return;	
		
		EditTaskDialog.EditTask(this, task);
		toDoListW.UpdateTask(id, task);
	}

	private void LoadTasks() {

		System.out.println("INFO: Loading tasks");
		Path savePath = Paths.get(ToDoApplication.GetApp().GetSavingPath());
		try {
			
			byte[] fileContent = Files.readAllBytes(savePath);

			if(fileContent == null || fileContent.length <= 0) return;

			ByteBuffer buff = ByteBuffer.wrap(fileContent);
			
			while (buff.remaining() > 0) {	

				final short taskStartMagic = 0x3444;
				final short taskEndMagic   = 0x00AF;
				
				long startMilli;
				long endMilli;
				if(buff.getShort() == taskStartMagic) 
				{
					int length = buff.getInt();	
					byte[] bytes = new byte[length];
					buff.get(bytes);
					String text = new String(bytes);
					TaskStatus status = TaskStatus.values()[buff.getInt()];
					startMilli = buff.getLong();
					endMilli = buff.getLong();

					if(buff.getShort() != taskEndMagic) break;

					ToDoTask task = new ToDoTask(text);
					task.Status = status;
					taskList.add(task);
					toDoListW.AddToDoTask(task);
				}
				else break;
			}
			System.out.println("INFO: Loaded " + taskList.size() + " tasks");
		} catch (IOException x) {
			System.err.println(x);
		}

	}

	private void SaveTasks() {

		System.out.println("INFO: Saving tasks");

		// overwrite existing file
		// writing in a binary format 
		Path savePath = Paths.get(ToDoApplication.GetApp().GetSavingPath());
		try {
			Files.deleteIfExists(savePath);
		} catch (IOException e) {
			System.err.println(e);
		}
		try (OutputStream out = new BufferedOutputStream(
			Files.newOutputStream(savePath, CREATE, APPEND))) {

			for (ToDoTask toDoTask : taskList) {

				long startMilli = -1;
				long endMilli = -1;

				if(toDoTask.StartTime != null)
					startMilli = toDoTask.StartTime.toInstant(ZoneOffset.UTC).toEpochMilli();
				if(toDoTask.EndTime != null)
					endMilli = toDoTask.EndTime.toInstant(ZoneOffset.UTC).toEpochMilli();

				final short taskStartMagic = 0x3444;
				final short taskEndMagic   = 0x00AF;

				// size = (text.length + 2 * sizof(int) + 2 * sizeof(long) + 2 * sizeof(short))
				ByteBuffer buff = ByteBuffer.allocate(toDoTask.Text.length() + 2 * 4 + 2 * 8 + 2 * 2);
				buff.putShort(taskStartMagic);
				buff.putInt(toDoTask.Text.length());	
				buff.put(toDoTask.Text.getBytes());
				buff.putInt(toDoTask.Status.ordinal());
				buff.putLong(startMilli);
				buff.putLong(endMilli);
				buff.putShort(taskEndMagic);

				out.write(buff.array());
		
			}

			System.out.println("INFO: Saved " + taskList.size() + " tasks");
		} catch (IOException x) {
			System.err.println(x);
		}
	}

}
