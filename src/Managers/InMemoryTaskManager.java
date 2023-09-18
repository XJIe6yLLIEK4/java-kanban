package Managers;

import Interface.HistoryManager;
import Interface.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private static Map<Integer, Task> mapTasks = new HashMap<>();
    private static Map<Integer, Epic> mapEpic = new HashMap<>();
    private static Map<Integer, Subtask> mapSubtask = new HashMap<>();
    private HistoryManager historyManager = Managers.getDefaultHistory();
    private TreeSet<Task> prioritizedTasks = new TreeSet<>();
    int ID = 0;

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public TreeSet<Task> getPrioritizedTasks() {
        prioritizedTasks = new TreeSet<>(getAllTask());
        prioritizedTasks.addAll(getAllSubtasks());
        return prioritizedTasks;
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
    public List<Task> getAllTask() {return new ArrayList<>(mapTasks.values());}

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
        if (mapTasks.containsKey(ID)) {
            historyManager.add(mapTasks.get(ID));
            return mapTasks.get(ID);
        } else {
            return null;
        }
    }

    @Override
    public Epic getEpic(int ID) {
        if (mapEpic.containsKey(ID)) {
            historyManager.add(mapEpic.get(ID));
            return mapEpic.get(ID);
        } else {
            return null;
        }
    }

    @Override
    public Subtask getSubtask(int ID) {
        if (mapSubtask.containsKey(ID)) {
            historyManager.add(mapSubtask.get(ID));
            return mapSubtask.get(ID);
        } else {
            return null;
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Subtask> getEpicSubtasks(int ID) {
        if (mapEpic.containsKey(ID)) {
            Epic epic = mapEpic.get(ID);
            System.out.println(epic.getListTasks());
            return epic.getListTasks();
        } else {
            return null;
        }
    }

    @Override
    public void createTask(Task task) {
        if (getPrioritizedTasks().isEmpty() || task.getStartTime() == null) {
            ID++;
            //Создаем задачу
            task.setID(ID);
            mapTasks.put(ID, task);

        } else {
            Task taskBefore = getPrioritizedTasks().lower(task);
            Task taskAfter = getPrioritizedTasks().higher(task);

            boolean firstCondition;
            if (taskBefore != null)
                firstCondition = taskBefore.getEndTime().isBefore(task.getStartTime());
            else
                firstCondition = true;

            boolean secondCondition;
            if (taskAfter != null)
                secondCondition = taskAfter.getStartTime().isAfter(task.getEndTime());
            else
                secondCondition = true;

            boolean isNotOverlapTime = firstCondition && secondCondition;

            if (isNotOverlapTime) {
                ID++;
                //Создаем задачу
                task.setID(ID);
                mapTasks.put(ID, task);
            }
        }
    }

    @Override
    public void createSubtask(Subtask subtask, int epicID) {
        if (getPrioritizedTasks().isEmpty() || subtask.getStartTime() == null) {
            ID++;
            //Создаем подзадачу
            subtask.setID(ID);
            subtask.setIdEpic(epicID);
            mapSubtask.put(subtask.getID(), subtask);
            mapEpic.get(epicID).putSubtask(subtask);
            mapEpic.get(subtask.getIdEpic()).updateStatus();
        } else {
            Task subtaskBefore = getPrioritizedTasks().lower(subtask);
            Task subtaskAfter = getPrioritizedTasks().higher(subtask);

            boolean firstCondition;
            if (subtaskBefore != null)
                firstCondition = subtaskBefore.getEndTime().isBefore(subtask.getStartTime());
            else
                firstCondition = true;

            boolean secondCondition;
            if (subtaskAfter != null)
                secondCondition = subtaskAfter.getStartTime().isAfter(subtask.getEndTime());
            else
                secondCondition = true;

            boolean isNotOverlapTime = firstCondition && secondCondition;

            if (isNotOverlapTime) {
                ID++;
                //Создаем подзадачу
                subtask.setID(ID);
                subtask.setIdEpic(epicID);
                mapSubtask.put(subtask.getID(), subtask);
                mapEpic.get(epicID).putSubtask(subtask);
                mapEpic.get(subtask.getIdEpic()).updateStatus();
            }
        }
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
