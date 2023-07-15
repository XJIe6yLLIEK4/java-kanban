import model.Epic;
import model.Subtask;
import model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private static Map<Integer, Task> mapTasks = new HashMap<>();
    private static Map<Integer, Epic> mapEpic = new HashMap<>();
    private static Map<Integer, Subtask> mapSubtask = new HashMap<>();
    private HistoryManager historyManager = Managers.getDefaultHistory();
    int ID = 0;

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> allTask = new ArrayList<>();
        allTask.addAll(mapTasks.values());
        allTask.addAll(mapEpic.values());
        allTask.addAll(mapSubtask.values());
        return allTask;
    }

    @Override
    public List<Task> getAllTask() {
        return new ArrayList<>(mapTasks.values());
    }

    @Override
    public List<Epic> getAllEpic() {
        return new ArrayList<>(mapEpic.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(mapSubtask.values());
    }

    @Override
    public void removeAllTasks() {
        mapTasks.clear();
        mapEpic.clear();
        mapSubtask.clear();
        System.out.println("Все задачи удалены");
    }

    @Override
    public Task getTask(int ID) {
        historyManager.add(mapTasks.get(ID));
        return mapTasks.get(ID);
    }

    @Override
    public Epic getEpic(int ID) {
        historyManager.add(mapEpic.get(ID));
        return mapEpic.get(ID);
    }

    @Override
    public Subtask getSubtask(int ID) {
        historyManager.add(mapSubtask.get(ID));
        return mapSubtask.get(ID);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Subtask> getEpicSubtasks(int ID) {
        Epic epic = mapEpic.get(ID);
        System.out.println(epic.getListTasks());
        return epic.getListTasks();
    }

    @Override
    public void createTask(Task task) {
        ID++;
        //Создаем задачу
        task.setID(ID);
        mapTasks.put(ID, task);
        System.out.println("Задача создана");
    }

    @Override
    public void createSubtask(Subtask subtask, int epicID) {
        ID++;
        //Создаем подзадачу
        subtask.setID(ID);
        subtask.setIdEpic(epicID);
        mapSubtask.put(subtask.getID(), subtask);
        mapEpic.get(subtask.getIdEpic()).updateStatus();
        System.out.println("Подзадача создана");
    }

    @Override
    public void createEpic(Epic epic) {
        ID++;
        epic.setID(ID);
        mapEpic.put(ID, epic);
    }


    public static void addTask(Task task) {
        mapTasks.put(task.getID(), task);
    }

    public static void addEpic(Epic epic) {
        mapEpic.put(epic.getID(), epic);
    }

    public static void addSubtask(Subtask subtask) {
        mapSubtask.put(subtask.getID(), subtask);
    }

    @Override
    public void updateTask(Task task) {
        if (mapTasks.containsKey(task.getID())) {
            mapTasks.put(task.getID(), task);
        } else {
            return;
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (mapEpic.containsKey(epic.getID())) {
            mapEpic.put(epic.getID(), epic);
        } else {
            return;
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (mapSubtask.containsKey(subtask.getID())) {
            mapSubtask.put(subtask.getID(), subtask);
            mapEpic.get(subtask.getIdEpic()).updateStatus();
        } else {
            return;
        }
    }

    @Override
    public void removeTask(int ID) {
        mapTasks.remove(ID);
        historyManager.remove(ID);
    }

    @Override
    public void removeEpic(int ID) {
        for (Subtask subtask : mapSubtask.values()) {
            if (subtask.getIdEpic() == ID) {
                mapSubtask.remove(subtask);
                historyManager.remove(subtask.getID());
            }
        }
        mapEpic.remove(ID);
        historyManager.remove(ID);
    }

    @Override
    public void removeSubtask(int ID) {
        Epic epic = mapEpic.get(mapSubtask.get(ID).getIdEpic());
        epic.removeSubtask(ID);
        epic.updateStatus();
        mapSubtask.remove(ID);
        historyManager.remove(ID);
    }
}
