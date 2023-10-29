package managers;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> mapTasks = new HashMap<>();
    private final Map<Integer, Epic> mapEpic = new HashMap<>();
    private final Map<Integer, Subtask> mapSubtask = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    Comparator<Task> comparator = (o1, o2) -> {
        if (o1.getStartTime() == null || o2.getStartTime() == null)
            return 1;
        else
            return o1.getStartTime().compareTo(o2.getStartTime());
    };
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(comparator);
    private int ID = 0;

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public Map<Integer, Task> getMapTasks() {
        return mapTasks;
    }
    public Map<Integer, Epic> getMapEpic() {
        return mapEpic;
    }
    public Map<Integer, Subtask> getMapSubtask() {
        return mapSubtask;
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
        getPrioritizedTasks().clear();
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

    public boolean isValidation(Task task) {
        System.out.println();
        if (getPrioritizedTasks().isEmpty() || task.getStartTime() == null) {
            return true;
        } else {
            boolean validation = true;
            System.out.println("SetList: " + getPrioritizedTasks());
            for (Task t : getPrioritizedTasks()) {
                System.out.println(t);
                if (t.getStartTime() == null)
                    continue;
                LocalDateTime start1 = task.getStartTime();
                LocalDateTime end1 = task.getEndTime();
                LocalDateTime start2 = t.getStartTime();
                LocalDateTime end2 = t.getEndTime();

                Duration intersection = Duration.between(
                        start1.isAfter(start2) ? start1 : start2,
                        end1.isBefore(end2) ? end1 : end2);
                //Если интервал отрицательный значит отрезки не пересекаются
                if (!intersection.isNegative())
                    validation = false;
            }
            return validation;
        }
    }

    @Override
    public void createTask(Task task) {
        if (isValidation(task)) {
            ID++;
            //Создаем задачу
            task.setID(ID);
            mapTasks.put(ID, task);
            getPrioritizedTasks().add(task);
        }
    }

    @Override
    public void createSubtask(Subtask subtask, int epicID) {
        if (isValidation(subtask)) {
            ID++;
            //Создаем подзадачу
            subtask.setID(ID);
            subtask.setIdEpic(epicID);
            mapSubtask.put(subtask.getID(), subtask);
            mapEpic.get(epicID).putSubtask(subtask);
            mapEpic.get(subtask.getIdEpic()).updateStatus();
            getPrioritizedTasks().add(subtask);
        }
    }

    @Override
    public void createEpic(Epic epic) {
        ID++;
        epic.setID(ID);
        mapEpic.put(ID, epic);
    }


    public void addTask(Task task) {
        mapTasks.put(task.getID(), task);
    }

    public void addEpic(Epic epic) {
        mapEpic.put(epic.getID(), epic);
    }

    public void addSubtask(Subtask subtask) {
        mapSubtask.put(subtask.getID(), subtask);
    }

    @Override
    public void updateTask(Task task) {
        if (mapTasks.containsKey(task.getID())) {
            getPrioritizedTasks().remove(mapTasks.get(task.getID()));
            if (isValidation(task)) {
                mapTasks.put(task.getID(), task);
                getPrioritizedTasks().add(task);
            } else {
                getPrioritizedTasks().add(mapTasks.get(task.getID()));
            }
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (mapEpic.containsKey(epic.getID())) {
            mapEpic.put(epic.getID(), epic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (mapSubtask.containsKey(subtask.getID())) {
            getPrioritizedTasks().remove(mapSubtask.get(subtask.getID()));
            if (isValidation(subtask)) {
                mapSubtask.put(subtask.getID(), subtask);
                mapEpic.get(subtask.getIdEpic()).updateStatus();
                getPrioritizedTasks().add(subtask);
            } else {
                getPrioritizedTasks().add(mapSubtask.get(subtask.getID()));
            }
        }
    }

    @Override
    public void removeTask(int ID) {
        getPrioritizedTasks().remove(mapTasks.get(ID));
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
        getPrioritizedTasks().remove(mapSubtask.get(ID));
        Epic epic = mapEpic.get(mapSubtask.get(ID).getIdEpic());
        epic.removeSubtask(ID);
        epic.updateStatus();
        mapSubtask.remove(ID);
        historyManager.remove(ID);
    }
}
