import java.util.*;

public class Manager {
    private Map<Integer, Task> mapTasks = new HashMap<>();
    //Ключем является ID подзадачи, а значением ID эпика
    private Map<Integer, Integer> mapIdSubtaskInEpic = new HashMap<>();
    Scanner scanner = new Scanner(System.in);
    int ID = 0;

    public Map<Integer, Task> printAllTasks() {
        System.out.println(mapTasks);
        return mapTasks;
    }

    public void removeAllTasks() {
        mapTasks.clear();
        System.out.println("Все задачи удалены");
    }

    public Task getTask(int ID) {
        if (mapTasks.containsKey(ID))
            return mapTasks.get(ID);
        else if (mapIdSubtaskInEpic.containsKey(ID)) {
            Epic epic = (Epic) mapTasks.get(mapIdSubtaskInEpic.get(ID));
            return epic.getSubtask(ID);
        }
        return null;
    }

    public List<Subtask> getEpicSubtasks(int ID) {
        Task task = mapTasks.get(ID);

        if (task.getClass().toString().equals("class Task")) {
            System.out.println("Подзадачи не найдены");
        } else {
            Epic epic = (Epic) mapTasks.get(ID);
            System.out.println(epic.getListTasks());
            return epic.getListTasks();
        }
        return null;
    }

    public Task createTask() {
        ID++;
        //Считываем данные пользователя
        System.out.println("Введите название задачи");
        String name = scanner.next();
        System.out.println("Опишите задачу");
        String description = scanner.next();
        System.out.println("Задайте статус задачи");
        String status = scanner.next();
        //Создаем задачу
        Task task = new Task(name, description, ID, status);
        mapTasks.put(ID, task);
        System.out.println("Задача создана");

        return task;
    }

    public Subtask createSubtask(String nameEpic, int epicID) {
        ID++;
        //Считываем данные пользователя
        System.out.println("Введите название подзадачи");
        String name = scanner.next();
        System.out.println("Опишите подзадачу");
        String description = scanner.next();
        System.out.println("Задайте статус подзадачи");
        String status = scanner.next();
        //Создаем подзадачу
        Subtask subtask = new Subtask(name, description, ID, status);
        subtask.setNameEpic(nameEpic);
        subtask.setIdEpic(epicID);
        saveIdInMap(subtask);
        System.out.println("Подзадача создана");

        return subtask;
    }

    public void saveIdInMap(Subtask subtask) {
        mapIdSubtaskInEpic.put(subtask.getID(), subtask.getIdEpic());
    }

    public void updateTask() {
        System.out.println("Какую задачу хотите обновить?");
        int number = scanner.nextInt();
        while (true) {
            System.out.println("1 - изменить название");
            System.out.println("2 - изменить описание");
            System.out.println("3 - изменить статус");
            System.out.println("4 - добавить подзадачу");
            System.out.println("0 - выход ");
            int answer = scanner.nextInt();

            Task task;
            if (mapTasks.containsKey(number))
                task = mapTasks.get(number);
            else if (mapIdSubtaskInEpic.containsKey(number)) {
                //Если ID задачи содержиться в Map с ID подзадач, то достаем из mapTasks эпик с данной подзадачей
                Epic epic = (Epic) mapTasks.get(mapIdSubtaskInEpic.get(number));
                task = epic.getSubtask(number);
            } else
                task = null;

            switch (answer) {
                case (1):
                    System.out.println("Введите новое название задачи");
                    String name = scanner.next();
                    task.setName(name);
                    break;
                case (2):
                    System.out.println("Введите новое описание задачи");
                    String description = scanner.next();
                    task.setDescription(description);
                    break;
                case (3):
                    System.out.println("Введите новый статус задачи");
                    String status = scanner.next();
                    task.setStatus(status);
                    //Если изменили подзадачу, обновляем статус эпика
                    if (task.getClass().toString().equals("class Subtask")) {
                        Subtask subtask = (Subtask) task;
                        Epic epic = (Epic) mapTasks.get(subtask.idEpic);
                        epic.updateStatus();
                    }
                    break;
                case (4):
                    if (task.getClass().toString().equals("class Task")) {
                        Epic epic = new Epic(task.getName(), task.getDescription(), task.getID(), task.getStatus());
                        epic.putSubtask(createSubtask(epic.getName(), epic.getID()));
                        epic.updateStatus();
                        removeTusk(number);
                        mapTasks.put(task.getID(), epic);
                    } else {
                        Epic epic = (Epic) mapTasks.get(number);
                        epic.putSubtask(createSubtask(epic.getName(), epic.getID()));
                        epic.updateStatus();
                    }
                    break;
                case (0):
                    return;
            }
        }
    }

    public void removeTusk(int ID) {
        if (mapTasks.containsKey(ID))
            mapTasks.remove(ID);
        else if (mapIdSubtaskInEpic.containsKey(ID)) {
            Epic epic = (Epic) mapTasks.get(mapIdSubtaskInEpic.get(ID));
            epic.removeSubtask(ID);
        }

    }
}
