import exception.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static File autoSave;

    public FileBackedTasksManager(String nameAutoSaveFile) {
        try {
            Files.createFile(Paths.get(nameAutoSaveFile));
            this.autoSave = new File(nameAutoSaveFile);
        } catch (IOException e) {
            System.out.println("Файл автосохранения уже создан");
            this.autoSave = new File(nameAutoSaveFile);
            try {
                read(autoSave);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void save() {
        try (FileWriter fileWriter = new FileWriter(autoSave)) {
            //Записываем формат записи
            fileWriter.write("id,type,name,status,description,epic\n");
            //Записываем все задачи
            for (Task task : getAllTasks()) {
                fileWriter.write(task.toString() + "\n");
            }
            //Записываем историю просмторов
            fileWriter.write("\n");
            fileWriter.write(historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось добавить запись в файл");
        }
    }

    public static void read(File autoSave) throws IOException {
        try (FileReader fileReader = new FileReader(autoSave); BufferedReader br = new BufferedReader(fileReader)) {
            //пропускаем первую строчку
            br.readLine();
            //дальше считываем файл
            while (br.ready()) {
                String line = br.readLine();
                if (!line.isEmpty() && !line.isBlank()) {
                    Task task = fromString(line);
                    //Распределяем задачи по мапам
                    if (task.getClass() == Task.class)
                        addTask(task);
                    else if (task.getClass() == Epic.class)
                        addEpic((Epic) task);
                    else if (task.getClass() == Subtask.class)
                        addSubtask((Subtask) task);
                    else
                        throw new ManagerSaveException("Не удалось считать задачу");
                } else {
                    //если строка пустя значит следущая строка будет с историей, пропускаем пустую строку и считываем историю
                    String lineHistory = br.readLine();
                    historyFromString(lineHistory);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Task fromString(String line) {
        String[] dataString = line.split(",");
        TaskStatus taskStatus = TaskStatus.valueOf(dataString[3]);
        switch (dataString[1]) {
            case ("Task"): {
                return new Task(dataString[2], dataString[4], Integer.parseInt(dataString[0]), taskStatus);
            }
            case ("Epic"): {
                return new Epic(dataString[2], dataString[4], Integer.parseInt(dataString[0]), taskStatus);
            }
            case ("Subtask"): {
                Subtask subtask = new Subtask(dataString[2], dataString[4], Integer.parseInt(dataString[0]), taskStatus);
                subtask.setIdEpic(Integer.parseInt(dataString[5]));
                return subtask;
            }
        }
        return null;
    }

    static String historyToString(HistoryManager manager) {
        String history = "";
        for (Task task : manager.getHistory())
            history = task.getID() + "," + history;
        return history;
    }

    static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        String[] strings = value.split(",");
        for (String string : strings)
            history.add(Integer.parseInt(string));
        return history;
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeTask(int ID) {
        super.removeTask(ID);
        save();
    }

    @Override
    public void removeEpic(int ID) {
        super.removeEpic(ID);
        save();
    }

    @Override
    public void removeSubtask(int ID) {
        super.removeSubtask(ID);
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask, int epicID) {
        super.createSubtask(subtask, epicID);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public Task getTask(int ID) {
        Task task = super.getTask(ID);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int ID) {
        Epic epic = super.getEpic(ID);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtask(int ID) {
        Subtask subtask = super.getSubtask(ID);
        save();
        return subtask;
    }
}