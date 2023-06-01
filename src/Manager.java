import java.util.*;

public class Manager {
    private Map<Integer, Task> mapTasks = new HashMap<>();
    private Map<Integer, Epic> mapEpic = new HashMap<>();
    private Map<Integer, Subtask> mapSubtask = new HashMap<>();
    int ID = 0;

    public List<Task> getAllTasks() {
        return new ArrayList<>(mapTasks.values());
    }

    public List<Epic> getAllEpic() {
        return new ArrayList<>(mapEpic.values());
    }

    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(mapSubtask.values());
    }

    public void removeAllTasks() {
        mapTasks.clear();
        mapEpic.clear();
        mapSubtask.clear();
        System.out.println("Все задачи удалены");
    }

    public Task getTask(int ID) {
        return mapTasks.get(ID);
    }

    public Epic getEpic(int ID) {
        return mapEpic.get(ID);
    }

    public Subtask getSubtask(int ID) {
        return mapSubtask.get(ID);
    }

    public List<Subtask> getEpicSubtasks(int ID) {
        Epic epic = mapEpic.get(ID);
        System.out.println(epic.getListTasks());
        return epic.getListTasks();
    }

    public void createTask(Task task) {
        ID++;
        //Создаем задачу
        task.setID(ID);
        mapTasks.put(ID, task);
        System.out.println("Задача создана");
    }

    public Subtask createSubtask(Subtask subtask, int epicID) {
        ID++;
        //Создаем подзадачу
        subtask.setID(ID);
        subtask.setIdEpic(epicID);
        mapSubtask.put(subtask.getID(), subtask);
        mapEpic.get(subtask.getIdEpic()).updateStatus();
        System.out.println("Подзадача создана");

        return subtask;
    }

    public void createEpic(Epic epic) {
        ID++;
        epic.setID(ID);
        mapEpic.put(ID, epic);
    }

    public void updateTask(Task task) {
        if (mapTasks.containsKey(task.getID()))
            mapTasks.put(task.getID(), task);
        else
            return;
    }

    public void updateEpic(Epic epic) {
        if (mapEpic.containsKey(epic.getID())) {
            mapEpic.put(epic.getID(),epic);
        } else
            return;
    }

    public void updateSubtask(Subtask subtask) {
        if (mapSubtask.containsKey(subtask.getID())) {
            mapSubtask.put(subtask.getID(),subtask);
            mapEpic.get(subtask.getIdEpic()).updateStatus();
        } else
            return;
    }

    public void removeTusk(int ID) {
        mapTasks.remove(ID);
    }

    public void removeEpic(int ID) {
        mapEpic.remove(ID);
    }

    public void removeSubtask(int ID) {
        Epic epic = mapEpic.get(mapSubtask.get(ID).getIdEpic());
        epic.removeSubtask(ID);
        epic.updateStatus();
        mapSubtask.remove(ID);
    }
}
