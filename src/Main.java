import api.HttpTaskServer;
import api.KVServer;
import com.google.gson.Gson;
import managers.HttpTaskManager;
import managers.InMemoryTaskManager;
import managers.Managers;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task task = new Task("testTask", "Test", TaskStatus.NEW);
        Epic epic = new Epic("testTask", "Test",  TaskStatus.NEW);
        Subtask subtask = new Subtask("testTask", "Test", TaskStatus.NEW);
        subtask.setIdEpic(2);

        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask, 2);

        System.out.println(taskManager.getAllTasks());
        taskManager.removeEpic(2);
        System.out.println(taskManager.getAllTasks());

        /*HttpTaskManager taskManager = Managers.getDefault("http://localhost:8078/");

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

         */
    }
}