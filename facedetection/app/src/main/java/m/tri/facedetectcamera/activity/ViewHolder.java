package m.tri.facedetectcamera.activity;

import android.view.View;
import android.widget.TextView;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ViewHolder {
    private Map<Integer, View> viewMap = new ConcurrentHashMap<>(16);
    private static ViewHolder instance;

    private ViewHolder() {
    }

    public static ViewHolder getInstance() {
        if (instance == null) {
            synchronized (ViewHolder.class) {
                if (instance == null) {
                    instance = new ViewHolder();
                }
            }
        }
        return instance;
    }

    public void add(int key, View view) {
        synchronized (this) {
            viewMap.remove(key);
            viewMap.put(key, view);
        }
    }

    public void setText(int key, String text) {
        TextView view = (TextView) viewMap.get(key);
        if (view != null) {
            view.setText(text);
        }
    }
}
