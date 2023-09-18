package Tests;

import Interface.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

abstract class TaskManagerTest<T extends TaskManager> {

    T taskManager;
    List<Task> list;

    void createTaskManager(T taskManager) {
        this.taskManager = taskManager;
    }

    void checkTaskManagerIsEmpty() {
        list = new ArrayList<>();
        Assertions.assertEquals(list, taskManager.getAllTasks(), "Список менеджера не пустой");
    }

    @Test
    void removeAllTasks() {
        Task task = new Task("testTask", "Test", TaskStatus.NEW);
        Epic epic = new Epic("testEpic", "Test", TaskStatus.NEW);
        Subtask subtask = new Subtask("testSubtask", "Test", TaskStatus.NEW);

        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask, epic.getID());

        taskManager.removeAllTasks();
        Assertions.assertEquals(list, taskManager.getAllTasks(), "Не все задачи были удалены");
    }

    @Test
    void createTask() {
        Task task = new Task("testTask", "Test", TaskStatus.NEW);
        Task taskResult = new Task("testTask", "Test", 1, TaskStatus.NEW);
        taskManager.createTask(task);
        Task task1 = new Task("testTask1", "Test1",  TaskStatus.NEW);
        Task taskResult1 = new Task("testTask1", "Test1", 2, TaskStatus.NEW);
        taskManager.createTask(task1);

        Assertions.assertEquals(taskResult, taskManager.getTask(1), "Задачи не совпадают");
        Assertions.assertEquals(taskResult1, taskManager.getTask(2), "Задачи не совпадают");
    }

    @Test
    void createEpic() {
        Epic epic = new Epic("testEpic", "Test", TaskStatus.NEW);
        Task epicResult = new Epic("testEpic", "Test", 1, TaskStatus.NEW);
        taskManager.createTask(epic);
        Epic epic1 = new Epic("testEpic1", "Test1",  TaskStatus.NEW);
        Epic epicResult1 = new Epic("testEpic1", "Test1", 2, TaskStatus.NEW);
        taskManager.createTask(epic1);

        Assertions.assertEquals(epicResult, taskManager.getTask(1), "Задачи не совпадают");
        Assertions.assertEquals(epicResult1, taskManager.getTask(2), "Задачи не совпадают");
    }

    @Test
    void createSubtask() {
        Epic epic = new Epic("Epic", "TestEpic", 1, TaskStatus.NEW);
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("testSubtask", "Test",  TaskStatus.NEW);
        taskManager.createSubtask(subtask, 1);
        Subtask subtaskResult = new Subtask("testSubtask", "Test", 2, TaskStatus.NEW);
        subtaskResult.setIdEpic(1);
        Subtask subtask1 = new Subtask("testSubtask1", "Test1",  TaskStatus.NEW);
        taskManager.createSubtask(subtask1, 1);
        Subtask subtaskResult1 = new Subtask("testSubtask1", "Test1", 3, TaskStatus.NEW);
        subtaskResult1.setIdEpic(1);

        Assertions.assertEquals(subtaskResult, taskManager.getSubtask(2), "Задачи не совпадают");
        Assertions.assertEquals(subtaskResult1, taskManager.getSubtask(3), "Задачи не совпадают");
    }

    @Test
    void removeTask() {
        taskManager.removeTask(1);
        Assertions.assertEquals(0, taskManager.getAllTask().size(), "Неверное количество задач");
        Assertions.assertEquals(list, taskManager.getAllTask());

        Task task = new Task("testTask", "Test", 1, TaskStatus.NEW);
        list.add(task);
        taskManager.createTask(task);
        Assertions.assertEquals(list, taskManager.getAllTask(), "Добавленные задачи не совпадают");
        taskManager.removeTask(1);
        list.remove(0);
        Assertions.assertEquals(list, taskManager.getAllTask(), "Задача не была удалена");
    }

    @Test
    void removeEpic() {
        Epic epic = new Epic("testEpic", "Test", 1, TaskStatus.NEW);
        list.add(epic);
        taskManager.createEpic(epic);
        Assertions.assertEquals(list, taskManager.getAllEpic(), "Добавленные задачи не совпадают");
        taskManager.removeEpic(1);
        list.remove(0);
        Assertions.assertEquals(list, taskManager.getAllEpic(), "Задача не была удалена");
    }

    @Test
    void removeSubtask() {
        Epic epic = new Epic("testEpic", "Test", 1, TaskStatus.NEW);
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("testSubtask", "Test", 2, TaskStatus.NEW);
        list.add(subtask);
        taskManager.createSubtask(subtask, 1);
        Assertions.assertEquals(list, taskManager.getAllSubtasks(), "Добавленные задачи не совпадают");
        taskManager.removeSubtask(2);
        list.remove(0);
        Assertions.assertEquals(list, taskManager.getAllSubtasks(), "Задача не была удалена");
    }

    @Test
    void getAllTasks() {
        Task task = new Task("testTask", "Test", TaskStatus.NEW);
        list.add(task);
        taskManager.createTask(task);
        Assertions.assertEquals(list, taskManager.getAllTasks(), "Списки при добавлении Task не совпадают");

        Epic epic = new Epic("testEpic", "Test", TaskStatus.NEW);
        list.add(epic);
        taskManager.createEpic(epic);
        Assertions.assertEquals(list, taskManager.getAllTasks(), "Списки при добавлении Epic не совпадают");

        Subtask subtask = new Subtask("testSubtask", "Test", TaskStatus.NEW);
        list.add(subtask);
        taskManager.createSubtask(subtask, epic.getID());
        Assertions.assertEquals(list, taskManager.getAllTasks(), "Списки при добавлении Subtask не совпадают");
    }

    @Test
    void getAllTask() {
        Task task = new Task("testTask", "Test", TaskStatus.NEW);
        list.add(task);
        taskManager.createTask(task);
        Epic epic = new Epic("testEpic", "Test", TaskStatus.NEW);
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("testSubtask", "Test", TaskStatus.NEW);
        taskManager.createSubtask(subtask, epic.getID());
        Assertions.assertEquals(list, taskManager.getAllTask(), "Возвращает не только Task");
    }

    @Test
    void getAllEpic() {
        Task task = new Task("testTask", "Test", TaskStatus.NEW);
        taskManager.createTask(task);
        Epic epic = new Epic("testEpic", "Test", TaskStatus.NEW);
        list.add(epic);
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("testSubtask", "Test", TaskStatus.NEW);
        taskManager.createSubtask(subtask, epic.getID());
        Assertions.assertEquals(list, taskManager.getAllEpic(), "Возвращает не только Epic");
    }

    @Test
    void getAllSubtask() {
        Task task = new Task("testTask", "Test", TaskStatus.NEW);
        taskManager.createTask(task);
        Epic epic = new Epic("testEpic", "Test", TaskStatus.NEW);
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("testSubtask", "Test", TaskStatus.NEW);
        list.add(subtask);
        taskManager.createSubtask(subtask, epic.getID());
        Assertions.assertEquals(list, taskManager.getAllSubtasks(), "Возвращает не только Subtask");
    }

    @Test
    void getTask() {
        Assertions.assertNull(taskManager.getTask(1), "Возвращает не ноль при несуществующей задаче");

        Task task = new Task("testTask", "Test", TaskStatus.NEW);
        Task taskResult = new Task("testTask", "Test", 1, TaskStatus.NEW);
        taskManager.createTask(task);
        Epic epic = new Epic("testEpic", "Test", TaskStatus.NEW);
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("testSubtask", "Test", TaskStatus.NEW);
        taskManager.createSubtask(subtask, epic.getID());
        Assertions.assertEquals(taskResult, taskManager.getTask(1), "Неправильно возвращает задачу");
    }

    @Test
    void getEpic() {
        Assertions.assertNull(taskManager.getEpic(1), "Возвращает не ноль при несуществующей задаче");

        Task task = new Task("testTask", "Test", TaskStatus.NEW);
        taskManager.createTask(task);
        Epic epic = new Epic("testEpic", "Test", TaskStatus.NEW);
        Epic epicResult = new Epic("testEpic", "Test", 2, TaskStatus.NEW);
        taskManager.createEpic(epic);
        Assertions.assertEquals(epicResult, taskManager.getEpic(2), "Неправильно возвращает задачу");
    }

    @Test
    void getSubtask() {
        Assertions.assertNull(taskManager.getSubtask(1), "Возвращает не ноль при несуществующей задаче");

        Task task = new Task("testTask", "Test", TaskStatus.NEW);
        taskManager.createTask(task);
        Epic epic = new Epic("testEpic", "Test", TaskStatus.NEW);
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("testSubtask", "Test", TaskStatus.NEW);
        Subtask subtaskResult = new Subtask("testSubtask", "Test", 3, TaskStatus.NEW);
        subtaskResult.setIdEpic(2);
        taskManager.createSubtask(subtask, epic.getID());
        Assertions.assertEquals(subtaskResult, taskManager.getSubtask(3), "Неправильно возвращает задачу");
    }

    @Test
    void getEpicSubtasks() {
        Assertions.assertNull(taskManager.getEpicSubtasks(1), "Возвращает не ноль при несуществующей задаче");

        Epic epic = new Epic("testEpic", "Test", TaskStatus.NEW);
        taskManager.createEpic(epic);
        Assertions.assertEquals(list, taskManager.getEpicSubtasks(1), "В Epic есть подзадачи сразу после создания");

        epic.putSubtask(new Subtask("testSubtask", "Test", TaskStatus.NEW));
        list.add(new Subtask("testSubtask", "Test", TaskStatus.NEW));
        Assertions.assertEquals(list, taskManager.getEpicSubtasks(1));
    }

    @Test
    void updateTask() {
        Task task = new Task("testTask", "Test", TaskStatus.NEW);
        taskManager.updateTask(task);
        Assertions.assertEquals(list, taskManager.getAllTasks(), "Добавилась новая задача");

        taskManager.createTask(task);
        Task taskUpdate = new Task("UpdateTask", "Test", 1, TaskStatus.NEW);
        taskManager.updateTask(taskUpdate);
        Task result = new Task("UpdateTask", "Test", 1, TaskStatus.NEW);
        Assertions.assertEquals(result, taskManager.getTask(1));
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic("testEpic", "Test", TaskStatus.NEW);
        taskManager.updateEpic(epic);
        Assertions.assertEquals(list, taskManager.getAllTasks(), "Добавилась новая задача");

        taskManager.createEpic(epic);
        Epic epicUpdate = new Epic("UpdateEpic", "Test", 1, TaskStatus.NEW);
        taskManager.updateEpic(epicUpdate);
        Epic result = new Epic("UpdateEpic", "Test", 1, TaskStatus.NEW);
        Assertions.assertEquals(result, taskManager.getEpic(1));
    }

    @Test
    void updateSubtask() {
        Epic epic = new Epic("testEpic", "Test", TaskStatus.NEW);
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("testSubtask", "Test", TaskStatus.NEW);
        taskManager.updateSubtask(subtask);
        Assertions.assertEquals(list, taskManager.getAllSubtasks(), "Добавилась новая задача");

        taskManager.createSubtask(subtask, 1);
        Subtask subtaskUpdate = new Subtask("UpdateSubtask", "Test", 2, TaskStatus.NEW);
        subtaskUpdate.setIdEpic(1);
        taskManager.updateSubtask(subtaskUpdate);
        Subtask result = new Subtask("UpdateSubtask", "Test", 2, TaskStatus.NEW);
        result.setIdEpic(1);
        Assertions.assertEquals(result, taskManager.getSubtask(2));
    }

    @Test
    void getHistory() {
        Assertions.assertEquals(list, taskManager.getHistory(), "История не пустая");
        Task task = new Task("testTask", "Test", 1, TaskStatus.NEW);
        taskManager.createTask(task);
        Epic epic = new Epic("testEpic", "Test", 2,  TaskStatus.NEW);
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("testSubtask", "Test", 3, TaskStatus.NEW);
        subtask.setIdEpic(2);
        taskManager.createSubtask(subtask, epic.getID());

        taskManager.getTask(1);
        taskManager.getEpic(2);
        taskManager.getSubtask(3);

        list.add(task);
        list.add(epic);
        list.add(subtask);

        Assertions.assertEquals(3, taskManager.getHistory().size(), "Не совпадает размер истории");
        Assertions.assertEquals(list, taskManager.getHistory(), "История не совпадает с историей запросов");
    }
}
