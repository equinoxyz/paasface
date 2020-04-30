package m.tri.facedetectcamera.callback;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import m.tri.facedetectcamera.R;
import m.tri.facedetectcamera.activity.ViewHolder;
import m.tri.facedetectcamera.utils.OkHttpManager;
import okhttp3.Response;

public  class FaceRetrieveCallBack extends OkHttpManager.OkHttpCallBack {
    private static ObjectMapper mapper = new ObjectMapper();
    public FaceRetrieveCallBack(Handler handler, Context context) {
        super(handler,context);
    }

    @Override
    public void inflater(Response response) {
        final String resp;
        try {
            resp = response.body().string();
            System.out.println("resp"+resp);
            if (resp != null && resp.length() > 0) {
                final JsonNode jsonNode = mapper.readValue(resp, JsonNode.class);
                final String hasBase64 = jsonNode.findValue("hasBase64").textValue();
                final int base64log_enabled = jsonNode.findValue("base64log_enabled").intValue();
                String personId = "新用户";
                double similarity = 0;
                String face_image_uri;
                String face_image_base64;
                final List<JsonNode> matchList = jsonNode.findValues("matchList");
                JsonNode jsonNode1 =jsonNode.findValue("matchList");
                boolean isEmpty = true;
                if(jsonNode1!=null&&jsonNode1.isArray()){
                    if(jsonNode1.iterator().hasNext()){
                        isEmpty = false;
                    }
                }
                if (!isEmpty) {
                    //String strmatch  = String.valueOf(matchList.get(0));
                    personId = matchList.get(0).findValue("personId").textValue();
                    // matchList.get(0).findValue("personId").textValue().toString();
                    similarity = matchList.get(0).findValue("similarity").doubleValue();
                    face_image_uri = matchList.get(0).findValue("face_image_uri").textValue();
                    face_image_base64 = matchList.get(0).findValue("face_image_base64").textValue();
                }
                ViewHolder.getInstance().setText(R.id.baseCont, personId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onFailed(Response response, Context context) {
        try {
            Toast.makeText(context,response.body().string(), Toast.LENGTH_SHORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
