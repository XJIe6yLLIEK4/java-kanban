package tests;

import managers.InMemoryTaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{

    @BeforeEach
    void createTaskManager() {
        super.createTaskManager(new InMemoryTaskManager());
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

    @Test
    void isValidate() {
        //Должна пройти
        Task task = new Task("testTask", "Test", TaskStatus.NEW);
        task.setStartTime(LocalDateTime.parse("17.09.2023 20:00", Task.getFormater()));
        task.setDuration(30);
        boolean validation = taskManager.isValidation(task);
        if (validation)
            taskManager.getPrioritizedTasks().add(task);
        System.out.println("First Setlist: " + taskManager.getPrioritizedTasks());
        Assertions.assertTrue(validation, "Ошибка первой задачи");

        //Не должна пройти
        Task task2 = new Task("testTask2", "Test2", TaskStatus.NEW);
        task2.setStartTime(LocalDateTime.parse("17.09.2023 20:15", Task.getFormater()));
        task2.setDuration(30);
        validation = taskManager.isValidation(task2);
        if (validation)
            taskManager.getPrioritizedTasks().add(task2);
        System.out.println("Second Setlist: " + taskManager.getPrioritizedTasks());
        Assertions.assertFalse(validation, "Ошибка второй задачи");

        //Должна пройти
        Task task3 = new Task("testTask", "Test", TaskStatus.NEW);
        task3.setStartTime(LocalDateTime.parse("17.09.2023 20:40", Task.getFormater()));
        task3.setDuration(30);
        validation = taskManager.isValidation(task3);
        if (validation)
            taskManager.getPrioritizedTasks().add(task3);
        System.out.println("Truth Setlist: " + taskManager.getPrioritizedTasks());
        Assertions.assertTrue(validation, "Ошибка третьей задачи");

        //Не должна пройти
        Task task4 = new Task("testTask", "Test", TaskStatus.NEW);
        task4.setStartTime(LocalDateTime.parse("17.09.2023 19:50", Task.getFormater()));
        task4.setDuration(55);
        validation = taskManager.isValidation(task4);
        if (validation)
            taskManager.getPrioritizedTasks().add(task4);
        System.out.println("Fourth Setlist: " + taskManager.getPrioritizedTasks());
        Assertions.assertFalse(validation, "Ошибка четвертой задачи");

        //Не должна пройти
        Task task5 = new Task("testTask", "Test", TaskStatus.NEW);
        task5.setStartTime(LocalDateTime.parse("17.09.2023 20:30", Task.getFormater()));
        task5.setDuration(1);
        validation = taskManager.isValidation(task5);
        if (validation)
            taskManager.getPrioritizedTasks().add(task5);
        System.out.println("Fifth Setlist: " + taskManager.getPrioritizedTasks());
        Assertions.assertFalse(validation, "Ошибка пятой задачи");
    }
}