package com.example.common.utils;

import com.apistd.uni.Uni;
import com.apistd.uni.UniException;
import com.apistd.uni.UniResponse;
import com.apistd.uni.sms.UniMessage;
import com.apistd.uni.sms.UniSMS;

import java.util.HashMap;
import java.util.Map;

/**
 * SMS API
 */
public class SMSUtils {

    /**
     * 飞鸽 API
     * https://e.4321.sh/#/dashboard
     * 速度慢，2-3分钟，可发送文本内容
     */
//    private static final String API = "https://api.4321.sh/sms/send";
//    private static final String APIKEY = "xxx";
//    private static final String SECRET = "xxxx";
//    // 签名ID
//    private static final String SIGNID = "xx";
//
//    /**
//     * 飞鸽 API
//     * 发送短信
//     *
//     * @param mobile
//     * @param message
//     * @throws Exception
//     */
//    public static void sendMsg(String mobile, String message) throws Exception {
//
//        HttpClient httpClient = HttpClients.createDefault();
//        HttpPost httpPost = new HttpPost(API);
//        httpPost.addHeader("Content-Type", "application/json");
//        Map<String, Object> map = new HashMap<>();
//        map.put("apikey", APIKEY);
//        map.put("secret", SECRET);
//        map.put("sign_id", SIGNID);
//        map.put("mobile", mobile);
//        map.put("content", message);
//        String json = JSON.toJSONString(map);
//        httpPost.setEntity(new StringEntity(json, "UTF-8"));
//        HttpResponse response = httpClient.execute(httpPost);
//        HttpEntity entity = response.getEntity();
//        String res = EntityUtils.toString(entity);
//        System.out.println(res);
//    }


    /**
     * uni-sms API
     * https://unisms.apistd.com/docs/tutorials
     * 速度快，发送内容只能为数字验证码
     */
    public static String ACCESS_KEY_ID = "xxx";
    private static String ACCESS_KEY_SECRET = "xxx";
    private static String SIGNATURE = "xxx";

    /**
     * 发送短信
     *
     * @param mobile
     * @param text
     */
    public static void sendMsg(String mobile, String text) {
        // 初始化
        Uni.init(ACCESS_KEY_ID, ACCESS_KEY_SECRET); // 若使用简易验签模式仅传入第一个参数即可

        // 设置自定义参数 (变量短信)
        Map<String, String> templateData = new HashMap<String, String>();
        templateData.put("code", text);
        // 构建信息
        UniMessage message = UniSMS.buildMessage()
                .setTo(mobile)
                .setSignature(SIGNATURE)
                .setTemplateId("pub_verif_short")
                .setTemplateData(templateData);

        // 发送短信
        try {
            UniResponse res = message.send();
            System.out.println(res);
        } catch (UniException e) {
            System.out.println("Error: " + e);
            System.out.println("RequestId: " + e.requestId);
        }
    }

//    public static void main(String[] args) throws Exception {
//        sendMsg("xxxx", "xxxx");
//    }
}
