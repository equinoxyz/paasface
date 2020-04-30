package m.tri.facedetectcamera.utils;

import android.content.Context;
import android.os.Handler;

import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class OkHttpManager {
    private OkHttpClient client;
    private static OkHttpManager instance;

    //MEDIA_TYPE_JSON
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");

    private OkHttpManager() {
        client = new OkHttpClient();
    }

    public static OkHttpManager getInstance() {
        if (instance == null) {
            synchronized (OkHttpManager.class) {
                if (instance == null) {
                    instance = new OkHttpManager();
                }
            }
        }
        return instance;
    }

    public void asyncPost(String url, Map<String, String> headers, MediaType mediaType, String content, final OkHttpCallBack callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        addHeader(builder, headers);
        final RequestBody requestBody = requestBody(content, mediaType);
        builder.post(requestBody);

        final Request request = builder.build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response != null) {
                    callback.handle(response);
                }
            }
        });
    }

    private void addHeader(Request.Builder builder, Map<String, String> headers) {
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    private RequestBody requestBody(String content, MediaType mediaType) {
        if (mediaType == null) {
            return RequestBody.create(MEDIA_TYPE_JSON, content);
        } else {
            return RequestBody.create(mediaType, content);
        }
    }

    public abstract static class OkHttpCallBack {
        private final Handler handler;
        private final Context context;

        public OkHttpCallBack(final Handler handler, final Context context) {
            this.handler = handler;
            this.context = context;
        }

        public void handle(final Response response) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (response.isSuccessful()) {
                        inflater(response);
                    } else {
                        onFailed(response, context);
                    }

                }
            });
        }

        public abstract void inflater(final Response response);

        public abstract void onFailed(final Response response, final Context context);
    }
}
