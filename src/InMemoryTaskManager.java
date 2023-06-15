import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> mapTasks = new HashMap<>();
    private Map<Integer, Epic> mapEpic = new HashMap<>();
    private Map<Integer, Subtask> mapSubtask = new HashMap<>();
    int ID = 0;

    @Override
    public List<Task> getAllTasks() {
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
        history.add(mapTasks.get(ID));
        return mapTasks.get(ID);
    }

    @Override
    public Epic getEpic(int ID) {
        history.add(mapEpic.get(ID));
        return mapEpic.get(ID);
    }

    @Override
    public Subtask getSubtask(int ID) {
        history.add(mapSubtask.get(ID));
        return mapSubtask.get(ID);
    }

    @Override
    public List<Task> getHistory() {
        return Managers.getDefaultHistory().getHistory();
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
    }

    @Override
    public void removeEpic(int ID) {
        for (Subtask subtask : mapSubtask.values()) {
            if (subtask.getIdEpic() == ID) {
                mapSubtask.remove(subtask);
            }
        }
        mapEpic.remove(ID);
    }

    @Override
    public void removeSubtask(int ID) {
        Epic epic = mapEpic.get(mapSubtask.get(ID).getIdEpic());
        epic.removeSubtask(ID);
        epic.updateStatus();
        mapSubtask.remove(ID);
    }
}