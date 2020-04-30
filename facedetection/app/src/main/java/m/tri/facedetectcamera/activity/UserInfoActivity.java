package m.tri.facedetectcamera.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import m.tri.facedetectcamera.R;
import m.tri.facedetectcamera.callback.FaceRetrieveCallBack;
import m.tri.facedetectcamera.callback.QueryPackageCallBack;
import m.tri.facedetectcamera.utils.IntentEnvoy;
import m.tri.facedetectcamera.utils.OkHttpManager;

public class UserInfoActivity extends AppCompatActivity {
    private TextView textViewFr;
    private TextView textViewPc;
    private Handler handler = new Handler();
    private Context context=null;
    private static ObjectMapper mapper = new ObjectMapper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        //MultiDex.install(this);
        setContentView(R.layout.activity_userinfo);
        textViewFr = (TextView) findViewById(R.id.baseCont);
        ViewHolder.getInstance().add(R.id.baseCont,textViewFr);
        textViewPc = (TextView) findViewById(R.id.packCont);
        Intent intent = getIntent();
       // String facebase64 = intent.getStringExtra("facebase64");
        String facebase64 = IntentEnvoy.getInstance().receive(IntentEnvoy.ENVOY_IMAGE_BASE64_KEY);
        System.out.println("faceImageBase64" + facebase64);



        Map<String, Object> params = new HashMap<>();
        //params.put("faceImageBase64", "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");
        //params.put("faceImageBase64","/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAYEBQYFBAYGBQYHBwYIChAKCgkJChQODwwQFxQYGBcUFhYaHSUfGhsjHBYWICwgIyYnKSopGR8tMC0oMCUoKSj/2wBDAQcHBwoIChMKChMoGhYaKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCj/wAARCAAeAB0DASIAAhEBAxEB/8QAFwABAQEBAAAAAAAAAAAAAAAABgcDBf/EACoQAAIBAgQGAQQDAAAAAAAAAAECAwQRAAUSIQYTMUFhkXEUIzKBFVGh/8QAFwEBAQEBAAAAAAAAAAAAAAAAAwIEBf/EAB0RAAICAgMBAAAAAAAAAAAAAAECAAMEIRESMUH/2gAMAwEAAhEDEQA/AMODcgqFWaGRTHUo/LbUt7Drc+8OcvpaSN5EWc1DRnS342VgPGM+F6yOplFUskckk8aNMoFtLhQpB/rdb/vHUzSMSyRagkYJZjYE9tvjviQB79nRcsr9DoCFM/pYnmaOGpVKgqG5Lp27HboPeJLxFkk75zOtLFJIAAzBEJ0k322HjF3Mf1GWxc8IdIC3G347AkfFsTDiHMv4/MpTTLDIznSwfewXp/pb1g3AB5E04ym1up3O3wTVikrnjN/ursfIvt6J9YfXef7qzvp07LYbYK8AZXHWzVU5tzoAujUdhqvc7fFv2fGF8caKlwNiL2vbCBSuoeder3sUh7NK4UNJIJJC53PToLYj1ezTVUkp6uxbF2p8pgrJJnnUNGqlmUi+rwb9sQuU3lYHtiWqZ9iLg5lVLEP6Z//Z\n");
        params.put("faceImageBase64",facebase64);
        params.put("threshold", "0.85");
        params.put("topn", 1);
        try {
            final String paramJson = mapper.writeValueAsString(params);
            /*final String paramJson = "{\n" +
                    "  \"sgwQueryReq\":{\n" +
                    "     \"mid\":\"CSB@021.ctnbc-bon.net;20150506150703921;0\",\n" +
                    "     \"scid\":\"RatableResource.Local@021.ctnbc-bon.net\",\n" +
                    "     \"uid\":\"CSB@021.ctnbc-bon.net\",\n" +
                    "     \"pwd\":\"C9E2BA6BAC682FB8605D1A8C3E617B15\",\n" +
                    "     \"reptag\":\"0\",\n" +
                    "     \"sig\": \"\",\n" +
                    "     \"rsv1\": \"\",\n" +
                    "     \"rsv2\": \"\",\n" +
                    "     \"data\":{\n" +
                    "        \"accountNbr\":\"18918582289\"\n" +
                    "     }\n" +
                    "  }\n" +
                    "}";*/
            Map<String, String> headers = new HashMap<>();
            headers.put("X-App-Id", "d2bf7bdb39683965379e8e9dea82eaa8");
            headers.put("X-App-Key", "511029d338486e548ae86b9ad83f983d");
            headers.put("Content-Type", "application/json");


            //String url = "http://10.145.205.53:7822/openApi/iam/queryPackageUse/queryPackageUse";
            //OkHttpManager.getInstance().asyncPost(url, headers, null, paramJson, new QueryPackageCallBack(handler, context));
            String url = "http://10.145.205.53:7822/openApi/ict/face/retrieve";
            OkHttpManager.getInstance().asyncPost(url, headers, null, paramJson, new FaceRetrieveCallBack(handler, context));
            /*OkHttpManager.getInstance().asyncPost(url, headers, null, paramJson, new OkHttpManager.OkHttpCallBack(handler) {
               @Override
                public void inflater(Response response) {
                    try {
                        String str = response.body().string();

                        JsonParser jp = new JsonParser();
                        JsonObject jo = jp.parse(str).getAsJsonObject();
                        String hasBase = jo.get("hasBase64").getAsString();
                        textViewFr.setText("hasBase64：" + hasBase);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });*/
            // }catch (JsonProcessingException e){
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("error"+e);
            textViewFr.setText("hasBase64：error" );
        }

}

    public static List<String> extractMessageByRegular(String msg) {

        List<String> list = new ArrayList<String>();
        Pattern p = Pattern.compile("(\\[[^\\]]*\\])");
        Matcher m = p.matcher(msg);
        while (m.find()) {
            list.add(m.group().substring(1, m.group().length() - 1));
        }
        return list;
    }
}
