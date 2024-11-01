package managers;

import interfaces.HistoryManager;
import exception.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static File autoSave;
    private static final DateTimeFormatter formatter = Task.getFormater();

    public FileBackedTasksManager(String nameAutoSaveFile) {
        if (nameAutoSaveFile != null) {
            try {
                Files.createFile(Paths.get(nameAutoSaveFile));
                autoSave = new File(nameAutoSaveFile);
            } catch (IOException e) {
                System.out.println("Файл автосохранения уже создан");
                autoSave = new File(nameAutoSaveFile);
            }
        }
    }

    public void save() {
        try (FileWriter fileWriter = new FileWriter(autoSave)) {
            //Записываем формат записи
            fileWriter.write("id,type,name,status,description,epic,startTime,endTime,duration\n");
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

    public static FileBackedTasksManager read(File autoSave) throws IOException {
        FileBackedTasksManager tasksManager = new FileBackedTasksManager(autoSave.getName());
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
                        tasksManager.addTask(task);
                    else if (task.getClass() == Epic.class)
                        tasksManager.addEpic((Epic) task);
                    else if (task.getClass() == Subtask.class)
                        tasksManager.addSubtask((Subtask) task);
                    else
                        throw new ManagerSaveException("Не удалось считать задачу");
                } else {
                    //если строка пустая значит следующая строка будет с историей, пропускаем пустую строку и считываем историю
                    String lineHistory = br.readLine();
                    if (lineHistory != null)
                        historyFromString(lineHistory);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }
        return tasksManager;
    }

    private static Task fromString(String line) {
        String[] dataString = line.split(",");
        //id,type,name,status,description,epic,startTime,endTime,duration
        int ID = Integer.parseInt(dataString[0]);
        String type = dataString[1];
        String name = dataString[2];
        TaskStatus taskStatus = TaskStatus.valueOf(dataString[3]);
        String description = dataString[4];

        int IdEpic;
        if (!dataString[5].equals(" "))
            IdEpic = Integer.parseInt(dataString[5]);
        else
            IdEpic = -1;

        LocalDateTime startTime;
        if (!dataString[6].equals(" "))
            startTime = LocalDateTime.parse(dataString[6], formatter);
        else
            startTime = null;

        long duration;
        if (!dataString[8].equals(" "))
            duration = Long.parseLong(dataString[8]);
        else
            duration = 0;

        switch (type) {
            case ("Task"): {
                Task task = new Task(name, description, ID, taskStatus);
                task.setStartTime(startTime);
                task.setDuration(duration);
                return task;
            }
            case ("Epic"): {
                Epic epic = new Epic(name, description, ID, taskStatus);
                epic.setStartTime(startTime);
                epic.setDuration(duration);
                return epic;
            }
            case ("Subtask"): {
                Subtask subtask = new Subtask(name, description, ID, taskStatus);
                subtask.setIdEpic(IdEpic);
                subtask.setStartTime(startTime);
                subtask.setDuration(duration);
                return subtask;
            }
        }
        return null;
    }

    private static String historyToString(HistoryManager manager) {
        String history = "";
        for (Task task : manager.getHistory())
            history = task.getID() + "," + history;
        return history;
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        String[] strings = value.split(",");
        for (String string : strings)
            history.add(Integer.parseInt(string));
        return history;
    }

    @Override
    public Map<Integer, Task> getMapTasks() {
        return super.getMapTasks();
    }
    @Override
    public Map<Integer, Epic> getMapEpic() {
        return super.getMapEpic();
    }
    @Override
    public Map<Integer, Subtask> getMapSubtask() {
        return super.getMapSubtask();
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
    public List<Task> getAllTasks() {
        return super.getAllTasks();
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
