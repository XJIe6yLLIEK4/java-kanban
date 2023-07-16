public final class Managers {

    public static FileBackedTasksManager getDefault(String nameAutoSaveFile) {
        return new FileBackedTasksManager(nameAutoSaveFile);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
