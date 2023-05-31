import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1 - ������� ������");
            System.out.println("2 - ������� ������");
            System.out.println("3 - �������� ������");
            System.out.println("4 - ���������� ��� ������");
            System.out.println("5 - ������� ��� ������");
            System.out.println("6 - ���������� ������");
            System.out.println("7 - ���������� ��� ��������� �����");
            System.out.println("0 - �����");
            int answer = scanner.nextInt();

            switch (answer) {
                case (1):
                    manager.createTask();
                    break;
                case (2):
                    System.out.println("����� ������ ������ �������?");
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
                    System.out.println("����� ������ ��������?");
                    System.out.println(manager.getTask(scanner.nextInt()));
                    break;
                case (7):
                    System.out.println("��������� ������ ����� ��������?");
                    manager.getEpicSubtasks(scanner.nextInt());
                    break;
                case (0):
                    return;
            }
        }
    }
}
