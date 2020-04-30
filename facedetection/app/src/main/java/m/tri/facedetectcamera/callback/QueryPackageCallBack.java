package m.tri.facedetectcamera.callback;

import android.content.Context;
import android.os.Handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import m.tri.facedetectcamera.R;
import m.tri.facedetectcamera.activity.ViewHolder;
import m.tri.facedetectcamera.utils.OkHttpManager;
import okhttp3.Response;

public class QueryPackageCallBack extends OkHttpManager.OkHttpCallBack{
    private static ObjectMapper mapper = new ObjectMapper();
    public QueryPackageCallBack(Handler handler, Context context) {
        super(handler,context);
    }

    @Override
    public void inflater(Response response) {
        final String resp;
        try {
            resp = response.body().string();
            if (resp != null && resp.length() > 0) {
                final JsonNode jsonNode = mapper.readValue(resp, JsonNode.class);
                String ratableResourceName = "新用户";
                final List<JsonNode> sgwQueryAck = jsonNode.findValues("sgwQueryAck");
                final List<JsonNode> productOffInfo =sgwQueryAck.get(0).findValues("productOffInfo");
                if (productOffInfo != null && productOffInfo.size() > 1) {
                    ratableResourceName = productOffInfo.get(0).findValue("ratableResourceName").textValue();
                }
                ViewHolder.getInstance().setText(R.id.baseCont, ratableResourceName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailed(Response response, Context context) {

    }
}
