import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
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

            switch (answer) {
                case (1):
                    manager.createTask();
                    break;
                case (2):
                    System.out.println("Какую задачу хотите удалить?");
                    manager.removeTusk(scanner.nextInt());
                    break;
                case (3):
                    manager.updateTask();
                    break;
                case (4):
                    manager.printAllTasks();
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
