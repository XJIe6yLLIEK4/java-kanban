public final class Managers {

    public static TaskManager getDefault() {
        TaskManager taskManager = null;
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        return inMemoryHistoryManager;
    }
}
