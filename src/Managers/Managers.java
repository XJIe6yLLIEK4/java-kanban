package managers;

public final class Managers {

    public static FileBackedTasksManager getDefault(String nameAutoSaveFile) {
        return new FileBackedTasksManager(nameAutoSaveFile);
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
