package api;

import static java.nio.charset.StandardCharsets.UTF_8;

import adapters.LocalDateAdapter;
import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import managers.FileBackedTasksManager;
import managers.Managers;
import model.Epic;
import model.Subtask;
import model.Task;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class HttpTaskServer {
    final int PORT = 8003;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
            .create();
    private final FileBackedTasksManager tasksManager = Managers.getDefault("autoSave");

    public void createServer() throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", this::handleTasks);
        httpServer.start();
    }

    private void handleTasks(HttpExchange httpExchange) {
        try {
            String method = httpExchange.getRequestMethod();
            String URL = httpExchange.getRequestURI().toString();
            switch (method) {
                case ("GET"):
                    //Получить все задачи
                    if (Pattern.matches("^/tasks/task/$", URL)) {
                        System.out.println("Получил все задачи");
                        String response = gson.toJson(tasksManager.getAllTasks());
                        sendText(httpExchange, response);
                        System.out.println("List subtask");
                        System.out.println(tasksManager.getAllSubtasks());
                        break;
                    }

                    //Получить задачу по id
                    if (Pattern.matches("^/tasks/task\\Wid=\\S+$", URL)) {
                        String pathId = URL.replaceFirst("/tasks/task\\Wid=", "");
                        int id = parsePathId(pathId);
                        boolean firstCondition = id != -1;
                        boolean secondCondition = tasksManager.getMapTasks().containsKey(id);
                        if (firstCondition && secondCondition) {
                            String response = gson.toJson(tasksManager.getTask(id));
                            sendText(httpExchange, response);
                            break;
                        } else {
                            httpExchange.sendResponseHeaders(404, 0);
                            if (!firstCondition)
                                System.out.println("Получен некорректный id: " + pathId);
                            else
                                System.out.println("Задача с id = " + id + " не найдена");
                        }
                        break;
                    }

                    //Получить подзадачу по id
                    if (Pattern.matches("^/tasks/subtask\\Wid=\\d+$", URL)) {
                        String pathId = URL.replaceFirst("/tasks/subtask\\Wid=", "");
                        int id = parsePathId(pathId);
                        boolean firstCondition = id != -1;
                        boolean secondCondition = tasksManager.getMapSubtask().containsKey(id);
                        if (firstCondition && secondCondition) {
                            String response = gson.toJson(tasksManager.getSubtask(id));
                            sendText(httpExchange, response);
                            break;
                        } else {
                            httpExchange.sendResponseHeaders(404, 0);
                            if (!firstCondition)
                                System.out.println("Получен некорректный id: " + pathId);
                            else
                                System.out.println("Задача с id = " + id + " не найдена");
                        }
                        break;
                    }

                    //Получить эпик по id
                    if (Pattern.matches("^/tasks/epic\\Wid=\\d+$", URL)) {
                        String pathId = URL.replaceFirst("/tasks/epic\\Wid=", "");
                        int id = parsePathId(pathId);
                        boolean firstCondition = id != -1;
                        boolean secondCondition = tasksManager.getMapEpic().containsKey(id);
                        if (firstCondition && secondCondition) {
                            String response = gson.toJson(tasksManager.getEpic(id));
                            sendText(httpExchange, response);
                            break;
                        } else {
                            httpExchange.sendResponseHeaders(404, 0);
                            if (!firstCondition)
                                System.out.println("Получен некорректный id: " + pathId);
                            else
                                System.out.println("Задача с id = " + id + " не найдена");
                        }
                        break;
                    }

                    //Получить подзадачи внутри эпика
                    if (Pattern.matches("^/tasks/epic/subtasks\\Wid=\\S+$", URL)) {
                        String pathId = URL.replaceFirst("/tasks/epic/subtasks\\Wid=", "");
                        int id = parsePathId(pathId);
                        boolean firstCondition = id != -1;
                        boolean secondCondition = tasksManager.getMapEpic().containsKey(id);
                        if (firstCondition && secondCondition) {
                            String response = gson.toJson(tasksManager.getEpic(id).getListTasks());
                            sendText(httpExchange, response);
                            break;
                        } else {
                            httpExchange.sendResponseHeaders(404, 0);
                            if (!firstCondition)
                                System.out.println("Получен некорректный id: " + pathId);
                            else
                                System.out.println("Задача с id = " + id + " не найдена");
                        }
                        break;
                    }

                    //Получить историю
                    if (Pattern.matches("^/tasks/history$", URL)) {
                        String response = gson.toJson(tasksManager.getHistory());
                        sendText(httpExchange, response);
                        break;
                    }

                    //Получить отсортированный список задач
                    if (Pattern.matches("^/tasks/$", URL)) {
                        String response = gson.toJson(tasksManager.getPrioritizedTasks());
                        sendText(httpExchange, response);
                        break;
                    }

                    //Получить id эпика подзадачи
                    if (Pattern.matches("^/tasks/subtask/epic\\Wid=\\S+$", URL)) {
                        String pathId = URL.replaceFirst("/tasks/subtask/epic\\Wid=", "");
                        int id = parsePathId(pathId);
                        boolean firstCondition = id != -1;
                        boolean secondCondition = tasksManager.getMapSubtask().containsKey(id);
                        if (firstCondition && secondCondition) {
                            String response = gson.toJson(tasksManager.getSubtask(id).getIdEpic());
                            sendText(httpExchange, response);
                            break;
                        } else {
                            httpExchange.sendResponseHeaders(404, 0);
                            if (!firstCondition)
                                System.out.println("Получен некорректный id: " + pathId);
                            else
                                System.out.println("Задача с id = " + id + " не найдена");
                        }
                        break;
                    }
                    break;
                case ("POST"):
                    //Обновить или создать задачу
                    if (Pattern.matches("^/tasks/task/$", URL)) {
                        // разбираем строку в формате JSON на элементы
                        JsonElement jsonElement = JsonParser.parseString(readText(httpExchange));
                        if (jsonElement.isJsonObject()) { // проверяем, является ли элемент JSON-объектом
                            JsonObject jsonObject = jsonElement.getAsJsonObject();

                            if (jsonObject.get("ID").getAsInt() == 0)
                                createTaskFromGson(jsonObject, httpExchange);
                            else
                                updateTaskFromGson(jsonObject, httpExchange);

                            System.out.println(tasksManager.getAllTasks());
                            System.out.println("Задача добавлена");
                        } else {
                            System.out.println("Тело запроса не является объектом");
                            httpExchange.sendResponseHeaders(400, 0);
                            return;
                        }
                    }
                    break;
                case ("DELETE"):
                    //Удалить все задачи
                    if (Pattern.matches("^/tasks/task/$", URL)) {
                        tasksManager.removeAllTasks();
                        httpExchange.sendResponseHeaders(200, 0);
                    }

                    //Удалить задачу по id
                    if (Pattern.matches("^/tasks/task\\Wid=\\S+$", URL)) {
                        String pathId = URL.replaceFirst("/tasks/task\\Wid=", "");
                        int id = parsePathId(pathId);
                        boolean firstCondition = id != -1;
                        boolean secondCondition = tasksManager.getMapTasks().containsKey(id);
                        if (firstCondition && secondCondition) {
                            tasksManager.removeTask(id);
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        } else {
                            httpExchange.sendResponseHeaders(404, 0);
                            if (!firstCondition)
                                System.out.println("Получен некорректный id: " + pathId);
                            else
                                System.out.println("Задача с id = " + id + " не найдена");
                        }
                        break;
                    }

                    //Удалить epic по id
                    if (Pattern.matches("^/tasks/epic\\Wid=\\S+$", URL)) {
                        String pathId = URL.replaceFirst("/tasks/epic\\Wid=", "");
                        int id = parsePathId(pathId);
                        boolean firstCondition = id != -1;
                        boolean secondCondition = tasksManager.getMapEpic().containsKey(id);
                        if (firstCondition && secondCondition) {
                            tasksManager.removeEpic(id);
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        } else {
                            httpExchange.sendResponseHeaders(404, 0);
                            if (!firstCondition)
                                System.out.println("Получен некорректный id: " + pathId);
                            else
                                System.out.println("Задача с id = " + id + " не найдена");
                        }
                        break;
                    }

                    //Удалить подзадачу по id
                    if (Pattern.matches("^/tasks/subtask\\Wid=\\S+$", URL)) {
                        String pathId = URL.replaceFirst("/tasks/subtask\\Wid=", "");
                        int id = parsePathId(pathId);
                        boolean firstCondition = id != -1;
                        boolean secondCondition = tasksManager.getMapSubtask().containsKey(id);
                        if (firstCondition && secondCondition) {
                            tasksManager.removeSubtask(id);
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        } else {
                            httpExchange.sendResponseHeaders(404, 0);
                            if (!firstCondition)
                                System.out.println("Получен некорректный id: " + pathId);
                            else
                                System.out.println("Задача с id = " + id + " не найдена");
                        }
                        break;
                    }
                    break;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private int parsePathId(String pathId) {
        System.out.println("pathID: " + pathId);
        try {
            return Integer.parseInt(pathId);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void createTaskFromGson(JsonObject jsonObject, HttpExchange httpExchange) throws IOException {
        String clazz = jsonObject.get("clazz").getAsString();
        switch (clazz) {
            case ("Task"):
                Task task = gson.fromJson(jsonObject, Task.class);
                System.out.println(task);
                tasksManager.createTask(task);
                httpExchange.sendResponseHeaders(201, 0);
                break;
            case ("Epic"):
                Epic epic = gson.fromJson(jsonObject, Epic.class);
                tasksManager.createEpic(epic);
                httpExchange.sendResponseHeaders(201, 0);
                break;
            case ("Subtask"):
                Subtask subtask = gson.fromJson(jsonObject, Subtask.class);
                if (tasksManager.getMapEpic().containsKey(subtask.getIdEpic())) {
                    tasksManager.createSubtask(subtask, subtask.getIdEpic());
                    httpExchange.sendResponseHeaders(201, 0);
                } else {
                    httpExchange.sendResponseHeaders(400, 0);
                    System.out.println("Нет эпика с данным айди");
                }
                break;
            default:
                httpExchange.sendResponseHeaders(400, 0);
                System.out.println("Задача не распознана");
        }
    }

    protected void updateTaskFromGson(JsonObject jsonObject, HttpExchange httpExchange) throws IOException {
        String clazz = jsonObject.get("clazz").getAsString();
        switch (clazz) {
            case ("Task"):
                Task task = gson.fromJson(jsonObject, Task.class);
                if (tasksManager.getMapTasks().containsKey(task.getID())) {
                    tasksManager.updateTask(task);
                    httpExchange.sendResponseHeaders(200, 0);
                } else {
                    System.out.println("Задача с id: " + task.getID() + "не найдена");
                    httpExchange.sendResponseHeaders(404, 0);
                }
                break;
            case ("Epic"):
                Epic epic = gson.fromJson(jsonObject, Epic.class);
                if (tasksManager.getMapEpic().containsKey(epic.getID())) {
                    tasksManager.updateTask(epic);
                    httpExchange.sendResponseHeaders(200, 0);
                } else {
                    System.out.println("Задача с id: " + epic.getID() + "не найдена");
                    httpExchange.sendResponseHeaders(404, 0);
                }
                break;
            case ("Subtask"):
                Subtask subtask = gson.fromJson(jsonObject, Subtask.class);
                if (tasksManager.getMapSubtask().containsKey(subtask.getID())) {
                    tasksManager.updateSubtask(subtask);
                    httpExchange.sendResponseHeaders(200, 0);
                } else {
                    System.out.println("Задача с id: " + subtask.getID() + "не найдена");
                    httpExchange.sendResponseHeaders(404, 0);
                }
                break;
        }
    }
}
