package Tests;

import Managers.InMemoryHistoryManager;
import Managers.Managers;
import model.Epic;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static model.TaskStatus.NEW;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryManagerTest {
    InMemoryHistoryManager historyManager;
    List<Task> list;

    @BeforeEach
    void createHistoryManager() {
        historyManager = Managers.getDefaultHistory();
        list = new ArrayList<>();
    }

    @Test
    void add() {
        Task task = new Task("Test", "Test", 1, NEW);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size(), "Неверное количество задач в истории");
        list.add(task);
        assertEquals(list, historyManager.getHistory(), "Задачи в истории не совпадают");
        historyManager.add(task);
        assertEquals(list, historyManager.getHistory(), "Задачи в истории дублируются");

        Task task2 = new Task("Test2", "Test2", 1, NEW);
        list.clear();
        list.add(task2);
        historyManager.add(task2);
        assertEquals(list, historyManager.getHistory(), "Задачи с одинаковым индексом не заменяются");
    }

    @Test
    void remove() {
        Task task = new Task("Test", "Test", 1, NEW);
        Epic epic = new Epic("Test", "Test", 2, NEW);
        Task task3 = new Task("Test", "Test", 3, NEW);
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(task3);

        list.add(epic);
        list.add(task3);
        historyManager.remove(1);
        assertEquals(2, historyManager.getHistory().size(), "Неверный размер истории");
        assertEquals(list, historyManager.getHistory(), "Неправильное удаление в начале");

        historyManager.remove(2);
        historyManager.remove(3);
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(task3);
        list.clear();
        list.add(task);
        list.add(task3);
        historyManager.remove(2);
        assertEquals(2, historyManager.getHistory().size(), "Неверный размер истории");
        assertEquals(list, historyManager.getHistory(), "Неправильное удаление в середине");

        historyManager.remove(1);
        historyManager.remove(3);
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(task3);
        list.clear();
        list.add(task);
        list.add(epic);
        historyManager.remove(3);
        assertEquals(2, historyManager.getHistory().size(), "Неверный размер истории");
        assertEquals(list, historyManager.getHistory(), "Неправильное удаление в конце");
    }
}
