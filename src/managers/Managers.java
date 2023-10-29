package managers;

import java.net.MalformedURLException;

public final class Managers {

    public static HttpTaskManager getDefault(String url) {
        try {
            return new HttpTaskManager(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
