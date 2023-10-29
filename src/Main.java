import api.KVServer;
import managers.HttpTaskManager;
import managers.Managers;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException {
        KVServer server = new KVServer();
        server.start();

        HttpTaskManager taskManager = Managers.getDefault("http://localhost:8078/");

        Task task2 = new Task("testTask2", "Test2", TaskStatus.NEW);
        taskManager.createTask(task2);

        Epic epic = new Epic("testEpic", "Test", 2, TaskStatus.NEW);
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("testSubtask", "Test", TaskStatus.NEW);
        subtask.setStartTime(LocalDateTime.parse("17.09.2023 18:00", Task.getFormater()));
        taskManager.createSubtask(subtask, 2);


        System.out.println("Task: " + taskManager.getTask(1));

        taskManager.removeTask(1);

        taskManager.getEpic(2);
        taskManager.getSubtask(3);
        System.out.println("история задач");
        System.out.println(taskManager.getHistory());


        System.out.println("Все задачи:");
        System.out.println(taskManager.getAllTasks());
    }
}