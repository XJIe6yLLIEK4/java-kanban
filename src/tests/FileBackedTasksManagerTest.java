package tests;

import managers.FileBackedTasksManager;
import managers.Managers;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static model.TaskStatus.NEW;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    String nameFileAutoSave = "AutoSaveTest.cvs";

    @BeforeEach
    public void createFileBackedManager() {
        taskManager = new FileBackedTasksManager(nameFileAutoSave);
        list = new ArrayList<>();
    }

    @Test
    void read() throws IOException {
        File autoSave = new File(nameFileAutoSave);
        FileWriter fileWriter = new FileWriter(autoSave);
            fileWriter.write("id,type,name,status,description,epic,startTime,endTime,duration\n");
            fileWriter.write("1,Task,Test,NEW,Test, ,17.09.2023 16:37,17.09.2023 17:07,30\n");
            fileWriter.write("2,Epic,Test,NEW,Test, , , ,0\n");
            fileWriter.write("\n");
            fileWriter.close();

        System.out.println(Files.readAllLines(autoSave.toPath()).get(0));
            taskManager = FileBackedTasksManager.read(autoSave);
            Task task = new Task("Test", "Test", 1, NEW);
            task.setStartTime(LocalDateTime.parse("17.09.2023 16:37", Task.getFormater()));
            task.setDuration(30);
            Epic epic = new Epic("Test", "Test", 2, NEW);
            assertEquals(list, taskManager.getHistory(), "История не пуста");
            assertEquals(2, taskManager.getAllTasks().size(), "Неверное количество задач");
            assertEquals(task, taskManager.getTask(1), "Считанная задача не совпадает");
            assertEquals(epic, taskManager.getEpic(2), "Эпики не совпали");

            list.add(task);
            list.add(epic);
            assertEquals(2, taskManager.getHistory().size(), "Неверное количество задач в истории");
            assertEquals(list, taskManager.getHistory(), "Неверно считана задача из истории");
    }

    @Test
    void save() throws IOException {
        File autoSave = new File(nameFileAutoSave);
        try (FileReader fileReader = new FileReader(autoSave); BufferedReader br = new BufferedReader(fileReader)) {
            Task task = new Task("Test", "Test", 1, NEW);
            task.setStartTime(LocalDateTime.parse("17.09.2023 16:37", Task.getFormater()));
            task.setDuration(30);
            Epic epic = new Epic("Test", "Test", 2, NEW);
            taskManager.createTask(task);
            taskManager.createTask(epic);
            assertEquals("id,type,name,status,description,epic,startTime,endTime,duration", br.readLine(), "Не совпадает формат записи");
            assertEquals("1,Task,Test,NEW,Test, ,17.09.2023 16:37,17.09.2023 17:07,30", br.readLine(), "Не совпадает сохраненная задача");
            assertEquals("2,Epic,Test,NEW,Test, , , ,0", br.readLine(), "Не совпадает сохраненная задача");
            assertEquals("", br.readLine(), "Отсутствует пустая строчка между задачами и историей");
            assertNull(br.readLine(), "История не пуста");

            taskManager.getTask(1);
            String history = Files.readAllLines(Path.of(autoSave.getPath())).get(4);
            assertEquals("1,", history, "История неверно сохранилась");
        }
    }

    @Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", 1, NEW);
        taskManager.addTask(task);
        final int taskId = task.getID();

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addNewEpic() {
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description", 1, NEW);
        taskManager.addEpic(epic);
        final int epicId = epic.getID();

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(epic, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addNewSubtask() {
        Subtask subtask = new Subtask("Test addNewTask", "Test addNewTask description", 1, NEW);
        taskManager.addSubtask(subtask);
        final int taskId = subtask.getID();

        final Subtask savedSubtask = taskManager.getSubtask(taskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(subtask, tasks.get(0), "Задачи не совпадают.");
    }
}
