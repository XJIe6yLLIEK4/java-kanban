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

    public Task createTask(Task task) {
        ID++;
        //Создаем задачу
        task.setID(ID);
        mapTasks.put(ID, task);
        System.out.println("Задача создана");

        return task;
    }

    public Subtask createSubtask(Subtask subtask, String nameEpic, int epicID) {
        ID++;
        //Создаем подзадачу
        subtask.setID(ID);
        subtask.setNameEpic(nameEpic);
        subtask.setIdEpic(epicID);
        mapSubtask.put(subtask.getID(), subtask);
        System.out.println("Подзадача создана");

        return subtask;
    }

    public Epic createEpic(Epic epic) {
        ID++;
        epic.setID(ID);
        mapEpic.put(ID, epic);
        return epic;
    }

    public void updateTask(Task task) {
        System.out.println("Какую задачу хотите обновить?");
        int number = 1;
        while (true) {
            System.out.println("1 - изменить название");
            System.out.println("2 - изменить описание");
            System.out.println("3 - изменить статус");
            System.out.println("4 - добавить подзадачу");
            System.out.println("0 - выход ");

            int answer = 4;

            if (mapTasks.containsKey(number))
                task = mapTasks.get(number);
            else
                return;

            switch (answer) {
                case (1):
                    System.out.println("Введите новое название задачи");
                    String name = "TEST";
                    task.setName(name);
                    break;
                case (2):
                    System.out.println("Введите новое описание задачи");
                    String description = "TEST";
                    task.setDescription(description);
                    break;
                case (3):
                    System.out.println("Введите новый статус задачи");
                    String status = "NEW";
                    task.setStatus(status);
                    //Если изменили подзадачу, обновляем статус эпика
                    if (task.getClass().toString().equals("class Subtask")) {
                        Subtask subtask = (Subtask) task;
                        Epic epic = mapEpic.get(subtask.getIdEpic());
                        epic.updateStatus();
                    }
                    break;
                case (4):
                    if (task.getClass().toString().equals("class Task")) {
                        Epic epic = new Epic(task.getName(), task.getDescription(), task.getID(), task.getStatus());
                        Subtask subtask = null;
                        epic.putSubtask(createSubtask(subtask, epic.getName(), epic.getID()));
                        epic.updateStatus();
                        removeTusk(number);
                        mapEpic.put(epic.getID(), epic);
                    } else {
                        Epic epic = mapEpic.get(number);
                        Subtask subtask = null;
                        epic.putSubtask(createSubtask(subtask, epic.getName(), epic.getID()));
                        epic.updateStatus();
                    }
                    break;
                case (0):
                    return;
            }
        }
    }

    public void updateEpic(Epic epic) {
        if (mapEpic.containsKey(epic.getID())) {
            //do something
        } else
            return;
    }

    public void updateSubtask(Subtask subtask) {
        if (mapSubtask.containsKey(subtask.getID())) {
            //do something
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
