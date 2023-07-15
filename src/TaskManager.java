import model.Epic;
import model.Subtask;
import model.Task;

import java.util.*;

public interface TaskManager {

    List<Task> getAllTasks();

    List<Task> getAllTask();

    List<Epic> getAllEpic();

    List<Subtask> getAllSubtasks();

    void removeAllTasks();

    Task getTask(int ID);

    Epic getEpic(int ID);

    Subtask getSubtask(int ID);

    List<Task> getHistory();

    List<Subtask> getEpicSubtasks(int ID);

    void createTask(Task task);

    void createSubtask(Subtask subtask, int epicID);

    void createEpic(Epic epic);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void removeTask(int ID);

    void removeEpic(int ID);

    void removeSubtask(int ID);
}
