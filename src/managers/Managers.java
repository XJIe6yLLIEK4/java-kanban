package managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    public static Gson getGson() {
         return new GsonBuilder()
                 .setDateFormat("dd.MM.yyyy HH:mm")
                 .create();
    }
}
