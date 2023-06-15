import java.util.*;

public interface TaskManager {
    Map<Integer, Task> mapTasks = new HashMap<>();
    Map<Integer, Epic> mapEpic = new HashMap<>();
    Map<Integer, Subtask> mapSubtask = new HashMap<>();
    List<Task> history = new ArrayList<>();
    int ID = 0;

    List<Task> getAllTasks();

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
