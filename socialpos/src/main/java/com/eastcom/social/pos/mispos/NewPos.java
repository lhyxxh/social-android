package com.eastcom.social.pos.mispos;

import com.eastcom.social.pos.mispos.model.Balance.BalanceReq;
import com.eastcom.social.pos.mispos.model.Balance.BalanceResp;
import com.eastcom.social.pos.mispos.model.ChangePIN.ChangePINReq;
import com.eastcom.social.pos.mispos.model.ChangePIN.ChangePINResp;
import com.eastcom.social.pos.mispos.model.Consume.ConsumeAuthReq;
import com.eastcom.social.pos.mispos.model.Consume.ConsumeReq;
import com.eastcom.social.pos.mispos.model.Consume.ConsumeResp;
import com.eastcom.social.pos.mispos.model.InsideAuthInfo.InsideAuthInfoReq;
import com.eastcom.social.pos.mispos.model.InsideAuthInfo.InsideAuthInfoResp;
import com.eastcom.social.pos.mispos.model.MachineInfo.MachineInfoReq;
import com.eastcom.social.pos.mispos.model.MachineInfo.MachineInfoResp;
import com.eastcom.social.pos.mispos.model.RePrint.RePrintReq;
import com.eastcom.social.pos.mispos.model.RePrint.RePrintResp;
import com.eastcom.social.pos.mispos.model.ReadCardInfo.ReadCardInfoReq;
import com.eastcom.social.pos.mispos.model.ReadCardInfo.ReadCardInfoResp;
import com.eastcom.social.pos.mispos.model.Return.ReturnAuthReq;
import com.eastcom.social.pos.mispos.model.Return.ReturnReq;
import com.eastcom.social.pos.mispos.model.Return.ReturnResp;
import com.eastcom.social.pos.mispos.model.Revoke.RevokeAuthReq;
import com.eastcom.social.pos.mispos.model.Revoke.RevokeReq;
import com.eastcom.social.pos.mispos.model.Revoke.RevokeResp;
import com.eastcom.social.pos.mispos.model.ScanInfo.ScanInfoReq;
import com.eastcom.social.pos.mispos.model.ScanInfo.ScanInfoResp;
import com.eastcom.social.pos.mispos.model.SignIn.SignInReq;
import com.eastcom.social.pos.mispos.model.SignIn.SignInResp;
import com.google.gson.Gson;
import com.landicorp.android.mispos.MisPosRequest;
import com.landicorp.android.mispos.MisPosResponse;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewPos {

    public static String outsideAuth(String in){
        String urlStr = "http://222.134.45.172:10000/social-kms-proxy-sdbz/api/data";
        Map<String, String> param;
        param = new HashMap();
        param.put("appid", "eastcom");
        param.put("secret", "lQhxAXfWWn1IA5RSFNmRY2WShCG2tkUs");
        param.put("data", in);
        String content = "";
        URL url = null;
        try {
            url = new URL(urlStr);
            byte[] data = new Gson().toJson(param).getBytes();

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(3000);     //设置连接超时时间
            httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST");     //设置以Post方式提交数据
            httpURLConnection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            //设置请求体的长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);

            int response = httpURLConnection.getResponseCode();            //获得服务器的响应码
            if (response == HttpURLConnection.HTTP_OK) {
                InputStream inptStream = httpURLConnection.getInputStream();
                String result = dealResponseResult(inptStream);                     //处理服务器的响应结果
                Gson gson = new Gson();
                MisPosOutauthRespMsg entity = gson.fromJson(result, MisPosOutauthRespMsg.class);
                return entity.getData();
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /*
     * Function  :   处理服务器的响应结果（将输入流转化成字符串）
     * Param     :   inputStream服务器的响应输入流
     */
    private static String dealResponseResult(InputStream inputStream) {
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }

    public static String mergeReqstr(MisPosRequest request) throws Exception {
        MisPosResponse response = new MisPosResponse();
        String strInput = "";
        String strOutput = "";
        strInput = strInput + String.format("%-8.8s", request.posid);
        strInput = strInput + String.format("%-8.8s", request.operid);
        strInput = strInput + String.format("%-2.2s", request.trans);
        strInput = strInput + String.format("%-2.2s", request.CardType);
        strInput = strInput + String.format("%-12.12s", request.amount);
        strInput = strInput + String.format("%-10.10s", request.old_date);
        strInput = strInput + String.format("%-12.12s", request.old_reference);
        strInput = strInput + String.format("%-6.6s", request.old_trace);
        strInput = strInput + String.format("%-37.37s", request.trk2);
        strInput = strInput + String.format("%-104.104s", request.trk3);
        strInput = strInput + "   ";
        strInput = strInput + String.format("%-6.6s", request.old_authID);
        strInput = strInput + String.format("%-500.500s", request.szRsv);
        return strInput;
    }
}
