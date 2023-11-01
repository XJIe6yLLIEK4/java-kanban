package tests;

import api.HttpTaskServer;
import api.KVServer;
import com.google.gson.Gson;
import managers.Managers;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

class HttpTaskServerTest {
    KVServer kvServer;
    HttpTaskServer httpTaskServer;
    HttpClient client = HttpClient.newHttpClient();
    String URL = "http://localhost:8002/tasks/";
    Gson gson = Managers.getGson();
     HttpResponse.BodyHandler<String> format = HttpResponse.BodyHandlers.ofString(UTF_8);

    HttpRequest generateRequest(String type, String uri, String body) {
        HttpRequest request;
        switch (type) {
            case ("GET"):
                request = HttpRequest.newBuilder().
                        GET().
                        uri(URI.create(URL + "" + uri)).
                        header("Content-Type", "application/json").
                        build();
                return request;
            case ("POST"):
                request = HttpRequest.newBuilder().
                        POST(HttpRequest.BodyPublishers.ofString(body)).
                        uri(URI.create(URL + "" + uri)).
                        header("Content-Type", "application/json").
                        build();
                return request;
            case ("DELETE"):
                request = HttpRequest.newBuilder().
                        DELETE().
                        uri(URI.create(URL + "" + uri)).
                        header("Content-Type", "application/json").
                        build();
                return request;

        }
        return null;
    }

    @BeforeEach
    void initServers() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.createServer();
    }

    @AfterEach
    void stopServers() {
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    void getAllTasks() throws IOException, InterruptedException {
        HttpRequest getAllRequest = generateRequest("GET", "task/", "");
        HttpResponse<String> response = client.send(getAllRequest, format);
        Assertions.assertEquals(200, response.statusCode(), "Код ошибки: " + response.statusCode());

        Task task = new Task("testTask", "Test", TaskStatus.NEW);
        String gsonTask = gson.toJson(task);

        HttpRequest postRequest = generateRequest("POST", "task/", gsonTask);
        response = client.send(postRequest, format);
        Assertions.assertEquals(201, response.statusCode(), "Ошибка добавления задачи");

        Task taskAnswer = new Task("testTask", "Test",1, TaskStatus.NEW);
        List<Task> taskList = new ArrayList<>();
        taskList.add(taskAnswer);
        String gsonTaskAnswer = gson.toJson(taskList);
        String answer = client.send(getAllRequest, format).body();
        Assertions.assertEquals(gsonTaskAnswer, answer , "Задачи не совпадают");
    }

    @Test
    void getTaskById() throws IOException, InterruptedException {
        Task task = new Task("testTask", "Test", TaskStatus.NEW);
        String gsonTask = gson.toJson(task);

        HttpRequest postRequest = generateRequest("POST", "task/", gsonTask);
        HttpResponse<String> response = client.send(postRequest, format);
        Assertions.assertEquals(201, response.statusCode(), "Ошибка добавления задачи");

        HttpRequest resultRequest = generateRequest("GET", "task?id=1", "");
        Task taskAnswer = new Task("testTask", "Test",1, TaskStatus.NEW);
        String gsonTaskAnswer = gson.toJson(taskAnswer);
        String answer = client.send(resultRequest, format).body();
        Assertions.assertEquals(gsonTaskAnswer, answer , "Задачи не совпадают");
    }

    @Test
    void getEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("testTask", "Test", TaskStatus.NEW);
        String gsonEpic = gson.toJson(epic);

        HttpRequest postRequest = generateRequest("POST", "task/", gsonEpic);
        HttpResponse<String> response = client.send(postRequest, format);
        Assertions.assertEquals(201, response.statusCode(), "Ошибка добавления задачи");

        HttpRequest resultRequest = generateRequest("GET", "epic?id=1", "");
        Epic epicAnswer = new Epic("testTask", "Test", 1, TaskStatus.NEW);
        String gsonTaskAnswer = gson.toJson(epicAnswer);
        String answer = client.send(resultRequest, format).body();
        Assertions.assertEquals(gsonTaskAnswer, answer , "Задачи не совпадают");
    }

    @Test
    void getSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("testTask", "Test",  TaskStatus.NEW);
        String gsonEpic = gson.toJson(epic);

        HttpRequest postRequest = generateRequest("POST", "task/", gsonEpic);
        HttpResponse<String> response = client.send(postRequest, format);
        Assertions.assertEquals(201, response.statusCode(), "Ошибка добавления задачи");

        Subtask subtask = new Subtask("testTask", "Test", TaskStatus.NEW);
        subtask.setIdEpic(1);
        String gsonSubtask = gson.toJson(subtask);

        postRequest = generateRequest("POST", "task/", gsonSubtask);
        response = client.send(postRequest, format);
        Assertions.assertEquals(201, response.statusCode(), "Ошибка добавления задачи");

        HttpRequest resultRequest = generateRequest("GET", "subtask?id=2", "");
        Subtask subtaskAnswer = new Subtask("testTask", "Test", 2, TaskStatus.NEW);
        subtaskAnswer.setIdEpic(1);
        String gsonTaskAnswer = gson.toJson(subtaskAnswer);
        String answer = client.send(resultRequest, format).body();
        Assertions.assertEquals(gsonTaskAnswer, answer , "Задачи не совпадают");
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        Epic epic = new Epic("testTask", "Test",  TaskStatus.NEW);
        String gsonEpic = gson.toJson(epic);

        HttpRequest postRequest = generateRequest("POST", "task/", gsonEpic);
        HttpResponse<String> response = client.send(postRequest, format);
        Assertions.assertEquals(201, response.statusCode(), "Ошибка добавления задачи");

        Subtask subtask = new Subtask("testTask", "Test", TaskStatus.NEW);
        subtask.setIdEpic(1);
        String gsonSubtask = gson.toJson(subtask);

        postRequest = generateRequest("POST", "task/", gsonSubtask);
        response = client.send(postRequest, format);
        Assertions.assertEquals(201, response.statusCode(), "Ошибка добавления задачи");

        HttpRequest resultRequest = generateRequest("GET", "history", "");
        Epic epicResult = new Epic("testTask", "Test", 1,  TaskStatus.NEW);
        Subtask subtaskAnswer = new Subtask("testTask", "Test", 2, TaskStatus.NEW);
        subtaskAnswer.setIdEpic(1);
        epicResult.putSubtask(subtaskAnswer);

        List<Task> result = new ArrayList<>();
        result.add(subtaskAnswer);
        result.add(epicResult);
        String gsonTaskAnswer = gson.toJson(result);

        client.send(generateRequest("GET", "subtask?id=2", ""), format);
        client.send(generateRequest("GET", "epic?id=1", ""), format);

        String answer = client.send(resultRequest, format).body();
        Assertions.assertEquals(gsonTaskAnswer, answer , "Задачи не совпадают");
    }

    @Test
    void epicIdFromSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("testTask", "Test",  TaskStatus.NEW);
        String gsonEpic = gson.toJson(epic);

        HttpRequest postRequest = generateRequest("POST", "task/", gsonEpic);
        HttpResponse<String> response = client.send(postRequest, format);
        Assertions.assertEquals(201, response.statusCode(), "Ошибка добавления задачи");

        Subtask subtask = new Subtask("testTask", "Test", TaskStatus.NEW);
        subtask.setIdEpic(1);
        String gsonSubtask = gson.toJson(subtask);

        postRequest = generateRequest("POST", "task/", gsonSubtask);
        response = client.send(postRequest, format);
        Assertions.assertEquals(201, response.statusCode(), "Ошибка добавления задачи");

        HttpRequest getRequest = generateRequest("GET", "subtask/epic?id=2", "");
        response = client.send(getRequest, format);

        Assertions.assertEquals("1", response.body(), "ID не совпали");
    }

    @Test
    void getSortList() throws IOException, InterruptedException {
        Task task = new Task("testTask", "Test", TaskStatus.NEW);
        task.setStartTime(LocalDateTime.MIN);
        HttpRequest postRequest = generateRequest("POST", "task/", gson.toJson(task));
        client.send(postRequest, format);

        Task task2 = new Task("testTask2", "Test2", TaskStatus.NEW);
        task2.setStartTime(LocalDateTime.parse("17.09.2023 20:00", Task.getFormater()));
        postRequest = generateRequest("POST", "task/", gson.toJson(task2));
        client.send(postRequest, format);

        Task task3 = new Task("testTask3", "Test3", TaskStatus.NEW);
        postRequest = generateRequest("POST", "task/", gson.toJson(task3));
        client.send(postRequest, format);

        Task taskResult = new Task("testTask", "Test", 1, TaskStatus.NEW);
        taskResult.setStartTime(LocalDateTime.MIN);
        Task taskResult2 = new Task("testTask2", "Test2", 2, TaskStatus.NEW);
        taskResult2.setStartTime(LocalDateTime.parse("17.09.2023 20:00", Task.getFormater()));
        Task taskResult3 = new Task("testTask3", "Test3", 3, TaskStatus.NEW);

        List<Task> taskList = new ArrayList<>();
        taskList.add(taskResult);
        taskList.add(taskResult2);
        taskList.add(taskResult3);

        HttpRequest getRequest = generateRequest("GET", "", "");
        HttpResponse<String> response = client.send(getRequest, format);

        Assertions.assertEquals(gson.toJson(taskList), response.body(), "Списки не совпали");
    }

    @Test
    void getEpicSubtasks() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("testTask", "Test", TaskStatus.NEW);
        subtask.setIdEpic(1);
        String gsonSubtask = gson.toJson(subtask);

        Epic epic = new Epic("testTask", "Test",  TaskStatus.NEW);
        String gsonEpic = gson.toJson(epic);
        epic.putSubtask(subtask);

        HttpRequest postRequest = generateRequest("POST", "task/", gsonEpic);
        HttpResponse<String> response = client.send(postRequest, format);
        Assertions.assertEquals(201, response.statusCode(), "Ошибка добавления задачи");

        postRequest = generateRequest("POST", "task/", gsonSubtask);
        response = client.send(postRequest, format);
        Assertions.assertEquals(201, response.statusCode(), "Ошибка добавления задачи");

        Subtask subtaskResult = new Subtask("testTask", "Test", 2, TaskStatus.NEW);
        subtaskResult.setIdEpic(1);
        List<Subtask> subtaskList = new ArrayList<>();
        subtaskList.add(subtaskResult);

        HttpRequest getRequest = generateRequest("GET", "epic/subtasks?id=1", "");
        response = client.send(getRequest, format);

        Assertions.assertEquals(gson.toJson(subtaskList), response.body(), "Списки подзадач не совпали");
    }

    @Test
    void postTask() throws IOException, InterruptedException {
        Task task = new Task("testTask", "Test", TaskStatus.NEW);
        String gsonTask = gson.toJson(task);
        HttpRequest postRequest = generateRequest("POST", "task/", gsonTask);
        HttpResponse<String> response = client.send(postRequest, format);
        Assertions.assertEquals(201, response.statusCode(), "Ошибка добавления задачи");

        Epic epic = new Epic("testTask", "Test",  TaskStatus.NEW);
        String gsonEpic = gson.toJson(epic);
        postRequest = generateRequest("POST", "task/", gsonEpic);
        response = client.send(postRequest, format);
        Assertions.assertEquals(201, response.statusCode(), "Ошибка добавления задачи");

        Subtask subtask = new Subtask("testTask", "Test", TaskStatus.NEW);
        subtask.setIdEpic(2);
        String gsonSubtask = gson.toJson(subtask);
        postRequest = generateRequest("POST", "task/", gsonSubtask);
        response = client.send(postRequest, format);
        Assertions.assertEquals(201, response.statusCode(), "Ошибка добавления задачи");

        Task taskResult = new Task("testTask", "Test", 1, TaskStatus.NEW);
        Epic epicResult = new Epic("testTask", "Test", 2,  TaskStatus.NEW);
        Subtask subtaskResult = new Subtask("testTask", "Test", 3, TaskStatus.NEW);
        subtaskResult.setIdEpic(2);
        epicResult.putSubtask(subtaskResult);

        List<Task> taskList = new ArrayList<>();
        taskList.add(taskResult);
        taskList.add(epicResult);
        taskList.add(subtaskResult);

        HttpRequest getRequest = generateRequest("GET", "task/", "");
        response = client.send(getRequest, format);
        Assertions.assertEquals(gson.toJson(taskList), response.body(), "Задачи были добавлены неправильно");
    }

    @Test
    void updateTask() throws IOException, InterruptedException {
        Task task = new Task("testTask", "Test", TaskStatus.NEW);
        String gsonTask = gson.toJson(task);
        HttpRequest postRequest = generateRequest("POST", "task/", gsonTask);
        HttpResponse<String> response = client.send(postRequest, format);
        Assertions.assertEquals(201, response.statusCode(), "Ошибка добавления задачи");

        Task updateTask = new Task("updateTask", "Test", 1, TaskStatus.NEW);
        String gsonUpdateTask = gson.toJson(updateTask);
        postRequest = generateRequest("POST", "task/", gsonUpdateTask);
        response = client.send(postRequest, format);
        Assertions.assertEquals(200, response.statusCode(), "Ошибка добавления задачи");

        HttpRequest getRequest = generateRequest("GET", "task?id=1", "");
        response = client.send(getRequest, format);

        Assertions.assertEquals(gsonUpdateTask, response.body(), "Задача обновлена неправильно");
    }

    @Test
    void deleteById() throws IOException, InterruptedException {
        Task task = new Task("testTask", "Test", TaskStatus.NEW);
        String gsonTask = gson.toJson(task);
        HttpRequest postRequest = generateRequest("POST", "task/", gsonTask);
        HttpResponse<String> response = client.send(postRequest, format);
        Assertions.assertEquals(201, response.statusCode(), "Ошибка добавления задачи");

        Epic epic = new Epic("testTask", "Test",  TaskStatus.NEW);
        String gsonEpic = gson.toJson(epic);
        postRequest = generateRequest("POST", "task/", gsonEpic);
        response = client.send(postRequest, format);
        Assertions.assertEquals(201, response.statusCode(), "Ошибка добавления задачи");

        Subtask subtask = new Subtask("testTask", "Test", TaskStatus.NEW);
        subtask.setIdEpic(2);
        String gsonSubtask = gson.toJson(subtask);
        postRequest = generateRequest("POST", "task/", gsonSubtask);
        response = client.send(postRequest, format);
        Assertions.assertEquals(201, response.statusCode(), "Ошибка добавления задачи");

        HttpRequest deleteRequest = generateRequest("DELETE", "epic?id=2", "");
        response = client.send(deleteRequest, format);
        Assertions.assertEquals(200, response.statusCode(), "Ошибка удаления задачи");

        Task taskResult = new Task("testTask", "Test", 1, TaskStatus.NEW);
        List<Task> taskList = new ArrayList<>();
        taskList.add(taskResult);

        HttpRequest getRequest = generateRequest("GET", "task/", "");
        response = client.send(getRequest, format);

        Assertions.assertEquals(gson.toJson(taskList), response.body());
    }

    @Test
    void deleteAllTasks() throws IOException, InterruptedException {
        Task task = new Task("testTask", "Test", TaskStatus.NEW);
        String gsonTask = gson.toJson(task);
        HttpRequest postRequest = generateRequest("POST", "task/", gsonTask);
        HttpResponse<String> response = client.send(postRequest, format);
        Assertions.assertEquals(201, response.statusCode(), "Ошибка добавления задачи");

        Epic epic = new Epic("testTask", "Test",  TaskStatus.NEW);
        String gsonEpic = gson.toJson(epic);
        postRequest = generateRequest("POST", "task/", gsonEpic);
        response = client.send(postRequest, format);
        Assertions.assertEquals(201, response.statusCode(), "Ошибка добавления задачи");

        Subtask subtask = new Subtask("testTask", "Test", TaskStatus.NEW);
        subtask.setIdEpic(2);
        String gsonSubtask = gson.toJson(subtask);
        postRequest = generateRequest("POST", "task/", gsonSubtask);
        response = client.send(postRequest, format);
        Assertions.assertEquals(201, response.statusCode(), "Ошибка добавления задачи");

        HttpRequest deleteRequest = generateRequest("DELETE", "task/", "");
        response = client.send(deleteRequest, format);
        Assertions.assertEquals(200, response.statusCode(), "Ошибка удаления задач");

        List<Task> taskList = new ArrayList<>();

        HttpRequest getRequest = generateRequest("GET", "task/", "");
        response = client.send(getRequest, format);

        Assertions.assertEquals(gson.toJson(taskList), response.body());
    }

}