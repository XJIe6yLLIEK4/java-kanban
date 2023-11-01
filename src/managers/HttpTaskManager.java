package managers;

import api.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Epic;
import model.Subtask;
import model.Task;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HttpTaskManager extends FileBackedTasksManager {
    private static final String TASK = "task";
    private static final String EPIC = "epic";
    private static final String SUBTASK = "subtask";
    private static final String HISTORY = "history";

    KVTaskClient taskClient;
    private final Gson gson = Managers.getGson();

    public HttpTaskManager(String url) throws MalformedURLException {
        super(null);
        taskClient = new KVTaskClient(new URL(url));
        List<Task> taskList = listTaskFromJson(TASK);
        List<Epic> epicList = listTaskFromJson(EPIC);
        List<Subtask> subtaskList = listTaskFromJson(SUBTASK);
        getHistory().addAll(listTaskFromJson(HISTORY));


        distributeTask(taskList, getMapTasks());
        distributeTask(epicList, getMapEpic());
        distributeTask(subtaskList, getMapSubtask());
    }

    private <T extends Task> void distributeTask(List<T> list, Map<Integer, T> map) {
        for (T task : list) {
            map.put(task.getID(), task);
        }
    }

    private <T extends Task> List<T> listTaskFromJson(String typeTask) {
        List<T> taskList;
                taskList = gson.fromJson(taskClient.load(typeTask), List.class);
        return Objects.requireNonNullElseGet(taskList, ArrayList::new);
        }

    @Override
    public void save() {
        taskClient.put(TASK, gson.toJson(getAllTask()));
        taskClient.put(EPIC, gson.toJson(getAllEpic()));
        taskClient.put(SUBTASK, gson.toJson(getAllSubtasks()));
        taskClient.put(HISTORY, gson.toJson(getHistory()));
    }
}
