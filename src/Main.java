import model.Task;
import model.TaskStatus;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1 - Создать задачу");
            System.out.println("2 - Удалить задачу");
            System.out.println("3 - Обновить задачу");
            System.out.println("4 - Посмотреть все задачи");
            System.out.println("5 - Удалить все задачи");
            System.out.println("6 - Посмотреть задачу");
            System.out.println("7 - Посмотреть все подзадачи эпика");
            System.out.println("0 - выход");
            int answer = scanner.nextInt();
            Task task = new Task("TestTask", "description", 0, TaskStatus.NEW);

            switch (answer) {
                case (1):
                    manager.createTask(task);
                    break;
                case (2):
                    System.out.println("Какую задачу хотите удалить?");
                    manager.removeTask(scanner.nextInt());
                    break;
                case (3):
                    manager.updateTask(task);
                    break;
                case (4):
                    System.out.println(manager.getAllTasks());
                    System.out.println(manager.getAllEpic());
                    System.out.println(manager.getAllSubtasks());
                    break;
                case (5):
                    manager.removeAllTasks();
                    break;
                case (6):
                    System.out.println("Какую задачу показать?");
                    System.out.println(manager.getTask(scanner.nextInt()));
                    break;
                case (7):
                    System.out.println("Подзадачи какого эпика показать?");
                    manager.getEpicSubtasks(scanner.nextInt());
                    break;
                case (0):
                    return;
            }
        }
    }
}