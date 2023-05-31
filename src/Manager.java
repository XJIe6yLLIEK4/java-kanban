import java.util.*;

public class Manager {
    private Map<Integer, Task> mapTasks = new HashMap<>();
    //������ �������� ID ���������, � ��������� ID �����
    private Map<Integer, Integer> mapIdSubtaskInEpic = new HashMap<>();
    Scanner scanner = new Scanner(System.in);
    int ID = 0;

    public Map<Integer, Task> printAllTasks() {
        System.out.println(mapTasks);
        return mapTasks;
    }

    public void removeAllTasks() {
        mapTasks.clear();
        System.out.println("��� ������ �������");
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
            System.out.println("��������� �� �������");
        } else {
            Epic epic = (Epic) mapTasks.get(ID);
            System.out.println(epic.getListTasks());
            return epic.getListTasks();
        }
        return null;
    }

    public Task createTask() {
        ID++;
        //��������� ������ ������������
        System.out.println("������� �������� ������");
        String name = scanner.next();
        System.out.println("������� ������");
        String description = scanner.next();
        System.out.println("������� ������ ������");
        String status = scanner.next();
        //������� ������
        Task task = new Task(name, description, ID, status);
        mapTasks.put(ID, task);
        System.out.println("������ �������");

        return task;
    }

    public Subtask createSubtask(String nameEpic, int epicID) {
        ID++;
        //��������� ������ ������������
        System.out.println("������� �������� ���������");
        String name = scanner.next();
        System.out.println("������� ���������");
        String description = scanner.next();
        System.out.println("������� ������ ���������");
        String status = scanner.next();
        //������� ���������
        Subtask subtask = new Subtask(name, description, ID, status);
        subtask.setNameEpic(nameEpic);
        subtask.setIdEpic(epicID);
        saveIdInMap(subtask);
        System.out.println("��������� �������");

        return subtask;
    }

    public void saveIdInMap(Subtask subtask) {
        mapIdSubtaskInEpic.put(subtask.getID(), subtask.getIdEpic());
    }

    public void updateTask() {
        System.out.println("����� ������ ������ ��������?");
        int number = scanner.nextInt();
        while (true) {
            System.out.println("1 - �������� ��������");
            System.out.println("2 - �������� ��������");
            System.out.println("3 - �������� ������");
            System.out.println("4 - �������� ���������");
            System.out.println("0 - ����� ");
            int answer = scanner.nextInt();

            Task task;
            if (mapTasks.containsKey(number))
                task = mapTasks.get(number);
            else if (mapIdSubtaskInEpic.containsKey(number)) {
                //���� ID ������ ����������� � Map � ID ��������, �� ������� �� mapTasks ���� � ������ ����������
                Epic epic = (Epic) mapTasks.get(mapIdSubtaskInEpic.get(number));
                task = epic.getSubtask(number);
            } else
                task = null;

            switch (answer) {
                case (1):
                    System.out.println("������� ����� �������� ������");
                    String name = scanner.next();
                    task.setName(name);
                    break;
                case (2):
                    System.out.println("������� ����� �������� ������");
                    String description = scanner.next();
                    task.setDescription(description);
                    break;
                case (3):
                    System.out.println("������� ����� ������ ������");
                    String status = scanner.next();
                    task.setStatus(status);
                    //���� �������� ���������, ��������� ������ �����
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
