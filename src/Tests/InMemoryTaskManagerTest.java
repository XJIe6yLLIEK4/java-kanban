package Tests;

import Managers.InMemoryTaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{

    @BeforeEach
    void createTaskManager() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        inMemoryTaskManager.removeAllTasks();
        super.createTaskManager(inMemoryTaskManager);
        super.checkTaskManagerIsEmpty();
    }

    @Test
    void SortTask(){
        Task task = new Task("testTask", "Test", TaskStatus.NEW);
        task.setStartTime(LocalDateTime.MIN);
        taskManager.createTask(task);

        Task task2 = new Task("testTask2", "Test2", TaskStatus.NEW);
        task2.setStartTime(LocalDateTime.parse("17.09.2023 20:00", Task.getFormater()));
        taskManager.createTask(task2);

        Task task3 = new Task("testTask3", "Test3", TaskStatus.NEW);
        taskManager.createTask(task3);

        Task task4 = new Task("testTask4", "Test4", TaskStatus.NEW);
        taskManager.createTask(task4);

        Epic epic = new Epic("testEpic", "Test", 3, TaskStatus.NEW);
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("testSubtask", "Test", TaskStatus.NEW);
        subtask.setStartTime(LocalDateTime.parse("17.09.2023 18:00", Task.getFormater()));
        taskManager.createSubtask(subtask, epic.getID());
        Assertions.assertEquals(5, taskManager.getPrioritizedTasks().size(), "Неверное количество задач в отсортированном списке");
        Assertions.assertEquals("[1,Task,testTask,NEW,Test, ,01.01.+1000000000 00:00,01.01.+1000000000 00:00,0, " +
                "6,Subtask,testSubtask,NEW,Test,5,17.09.2023 18:00,17.09.2023 18:00,0, " +
                "2,Task,testTask2,NEW,Test2, ,17.09.2023 20:00,17.09.2023 20:00,0, " +
                "3,Task,testTask3,NEW,Test3, , , ,0, " +
                "4,Task,testTask4,NEW,Test4, , , ,0]", taskManager.getPrioritizedTasks().toString());
    }
}