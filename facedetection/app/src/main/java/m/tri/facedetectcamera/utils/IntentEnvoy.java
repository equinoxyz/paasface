package m.tri.facedetectcamera.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IntentEnvoy {
    public static final String ENVOY_IMAGE_BASE64_KEY = "envoy_image_base64_key";
    private Map<String, String> contentMap = new ConcurrentHashMap<>(16);
    private static IntentEnvoy instance;

    private IntentEnvoy() {
    }

    public static IntentEnvoy getInstance() {
        if (instance == null) {
            synchronized (IntentEnvoy.class) {
                if (instance == null) {
                    instance = new IntentEnvoy();
                }
            }
        }
        return instance;
    }

    /**
     * 投递 intent 需要传递的内容
     *
     * @param content
     * @return
     */
    public void deliver(String key, String content) {
        contentMap.put(key, content);
    }

    /**
     * 收取 intent 投递内容
     *
     * @param key
     * @return
     */
    public String receive(String key) {
        return contentMap.get(key);
    }
}
