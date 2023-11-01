package tests;

import api.KVServer;
import managers.HttpTaskManager;
import managers.Managers;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

public class HttpTaskManagerTest extends FileBackedTasksManagerTest{

    KVServer server;
    HttpTaskManager manager;

    @BeforeEach
    void initServerAndClient() throws IOException {
        System.out.println("Start before each");
        server = new KVServer();
        server.start();
        System.out.println("Start server");
        manager = Managers.getDefault("http://localhost:8078/");
        System.out.println("Start manager");
        Assertions.assertEquals(0, manager.getAllTasks().size(), "Изначально менеджер не пустой");
    }

    @AfterEach
    void stopServer() {
        server.stop();
    }

    @Test
    void save() {
        System.out.println("Start save test");
        Task task = new Task("testTask4", "Test4", TaskStatus.NEW);
        manager.createTask(task);

        Epic epic = new Epic("testEpic", "Test", 2, TaskStatus.NEW);
        manager.createEpic(epic);

        Subtask subtask = new Subtask("testSubtask", "Test", TaskStatus.NEW);
        subtask.setStartTime(LocalDateTime.parse("17.09.2023 18:00", Task.getFormater()));
        manager.createSubtask(subtask, epic.getID());

        Assertions.assertEquals(3, manager.getAllTasks().size(), "Количество сохранённых задач не совпадает");

    }
}
