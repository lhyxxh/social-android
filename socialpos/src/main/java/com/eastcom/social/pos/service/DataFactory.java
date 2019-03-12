package com.eastcom.social.pos.service;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.eastcom.social.pos.config.Constance;
import com.eastcom.social.pos.entity.ReadDocument;
import com.eastcom.social.pos.entity.ResponseContext;
import com.eastcom.social.pos.util.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zhy.http.okhttp.OkHttpUtils;

/**
 * Created by Lee on 2015/12/03. 用于获取服务器上的数据
 */
public class DataFactory {
	private static final String PostVersion = "/s/api/launch/firmware/version";
	private static final String PostUpGrade = "/s/api/launch/firmware/upgrade";

	// 从服务器获取APP列表
	private static final String APP_List = "/s/api/store/app/list";
	// 获取APP详细信息
	private static final String APP_Detail = "/s/api/store/app/detail/";
	// 获取APP的apk
	private static final String APP_Apk = "/s/api/store/app/apk/";
	// 获取APP的Logo
	private static final String APP_Logo = "/s/api/store/app/logo/";
	// 获取APP图片photo
	private static final String APP_Photo = "/s/api/store/app/photo/";
	// 校验政策文件版本
	private static final String Document_Version = "/m/api/document/version";
	// 下载政策文件版本
	private static final String Document_Downloadfile = "/m/api/document/downloadfile";
	// 反馈是否已阅读文件
	private static final String Document_Readfile = "/m/api/document/readfile";
	
	public static final MediaType MEDIA_JSON = MediaType
			.parse("application/json; charset=utf-8");
	private static MediaType MEDIA_FILE = MediaType
			.parse("application/octet-stream");
	public static List<okhttp3.Cookie> cookies;

	/**
	 * 根据类型获取对象共用方法1
	 * 
	 * @param url
	 * @param token
	 * @return
	 */
	public static <T> T getObjects(String url, TypeToken<T> token) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("Content-Type", "application/json; charset=utf-8");
			map.put("Accept", "application/json");
			Response response = OkHttpUtils.get().url(url).headers(map).build()
					.execute();
			String result = response.body().string();
			Gson g = new Gson();
			T fromJson = g.fromJson(result, token.getType());
			return fromJson;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 根据类型获取对象共用方法2
	 * 
	 * @param url
	 * @param clazz
	 * @return
	 */
	public static String getObjects(String url) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("Content-Type", "application/json; charset=utf-8");
			map.put("Accept", "application/json");
			Response response = OkHttpUtils.get().url(url).headers(map).build()
					.execute();
			String result = response.body().string();
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据类型获取对象共用方法2
	 * 
	 * @param url
	 * @param clazz
	 * @return
	 */
	public static String getObjects2(String url) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("Content-Type", "application/json; charset=utf-8");
			Response response = OkHttpUtils.get().url(url).headers(map).build()
					.execute();
			String result = response.body().string();
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static <T> T postObjects(String url, TypeToken<T> token,
			String jsonString) {
		try {
			Response response = OkHttpUtils.postString().content(jsonString)
					.mediaType(MEDIA_JSON).url(url)
					.addHeader("Accept", "application/json").build().execute();
			String result = response.body().string();
			Gson g = new Gson();
			return g.fromJson(result, token.getType());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String postObjects(String url, String jsonString) {
		try {
			Response response = OkHttpUtils.postString().content(jsonString)
					.mediaType(MEDIA_JSON).url(url)
					.addHeader("Accept", "application/json").build().execute();
			String result = response.body().string();
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * post 验证指静脉数据
	 * 
	 * @return
	 */
	public static String verify(String buf, int size, String group) {
		String url = Constance.API_FINGER_VEIN + "/apiCtrl/verify?buf="
				+ URLEncoder.encode(buf) + "&size=" + size + "&group=0";
		String result = postObjects(url, "");
		return result;
	}

	/**
	 * 保存指静脉及社保卡信息
	 * 
	 * @param buf
	 * @param size
	 * @param group
	 * @param sbCardId
	 * @param sbnName
	 * @param sbId
	 * @return
	 */
	public static String save(String buf, int size, String group,
			String sbCardId, String sbnName, String sbId) {
		String url = Constance.API_FINGER_VEIN + "/apiCtrl/save?buf="
				+ URLEncoder.encode(buf) + "&size=" + size + "&group=0"
				+ "&sbCardId=" + sbCardId + "&sbnName=" + sbnName + "&sbId="
				+ sbId;
		String result = postObjects(url, "");
		return result;
	}

	/**
	 * 保存指静脉及社保卡信息
	 * 
	 * @param buf
	 * @param sbCardId
	 * @return
	 */
	public static String save1(String buf, String sbCardId) {
		String url = Constance.API_FINGER_VEIN + "/apiCtrl/save1?buf="
				+ URLEncoder.encode(buf) + "&sbCardId=" + sbCardId;
		String result = postObjects(url, "");
		return result;
	}

	/**
	 * 上传录入信息的地址信息
	 * @param socialCardNo
	 * @param addr
	 * @param city
	 * @param district
	 * @param latitude
	 * @param longtitude
	 * @param street
	 * @param sbCardId
	 * @param sbnName
	 * @return
	 */
	public static String uploadLoaction(String socialCardNo, String addr,
			String city, String district, String latitude, String longtitude,
			String street, String sbCardId, String sbnName,String mechanism,String Department) {
		String url = Constance.API_FINGER_VEIN
				+ "/locationCtrl/uploadLoaction?" + "addr=" + addr + "&city="
				+ city + "&district=" + district + "&latitude=" + latitude
				+ "&longtitude=" + longtitude + "&sbCardId=" + sbCardId
				+ "&sbnName=" + sbnName + "&socialCardNo=" + socialCardNo
				+ "&street=" + street+"&mechanism="+mechanism+"&Department="+Department;
		String result = postObjects(url, "");
		return result;
	}

	/**
	 * 上传社保卡对应头像
	 * @param fileName
	 * @param file
	 * @param socialCardNo
	 * @return
	 */
	public static String uploadImg(String fileName, File file,
			String socialCardNo) {
		String result = "";
		String url = Constance.API_FINGER_VEIN + "/imgCtrl/uploadImg";
		try {
			RequestBody fileBody = RequestBody.create(MEDIA_FILE, file);
			MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
					.setType(MultipartBody.FORM);
			RequestBody requestBody = multipartBuilder
					.addFormDataPart("socialCardNo", socialCardNo)
					.addPart(
							Headers.of("Content-Disposition",
									"form-data; name=\"file\";filename=\""
											+ fileName + "\""), fileBody)
					.build();

			Request.Builder builder = new Builder();
			Request request = builder.url(url).post(requestBody).build();
			Call call = OkHttpUtils.getInstance().getOkHttpClient()
					.newCall(request);
			Response response = call.execute();
			result = response.body().string();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}
	
	/**
	 * 获取社保卡对应头像
	 * @param socialCardNo
	 * @return
	 */
	public static String getImg(
			String socialCardNo) {
		String result = "";
		String url = Constance.API_FINGER_VEIN+"/imgCtrl/getImg?socialCardNo="+socialCardNo;
		result = getObjects(url);
		return result;
		
	}
	
	
	/**
	 * 获取社保卡对应头像
	 * @param socialCardNo
	 * @return
	 */
	public static String getVersion(String signboardNo, String version) {
		String result = "";
		String url = Constance.API_HOST+"/s/api/launch/firmware/version";
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("signboardNo", signboardNo);
			jsonObject.put("version", version);
			String params = jsonObject.toString();
			result = postObjects(url, params);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * POST 获取待升级的版本
	 * 
	 * @return
	 */
	public static String postVersion(String signboardNo, String version) {
		String result = "";
		try {
			String url = Constance.API_HOST + PostVersion;
			JSONObject jsonObject2 = new JSONObject();
			jsonObject2.put("signboardNo", signboardNo);
			jsonObject2.put("version", version);
			String jsonString = jsonObject2.toString();
			ResponseContext responseContext = HttpExtensionUtil.invokePost(url,
					jsonString, null);
			result = responseContext.getResult();

		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * POST 获取升级的版本流 apk
	 * 
	 * @return
	 */
	public static String postUpGrade(String version) {
		String result = "";
		try {
			String url = Constance.API_HOST + PostUpGrade;
			JSONObject jsonObject2 = new JSONObject();
			jsonObject2.put("version", version);
			String jsonString = jsonObject2.toString();
			File file = new File(Constance.ApkDownloadFile);
			if (!file.exists()) {
				file.mkdir();
			}
			RequestParams requestParams = new RequestParams();
			// 绑定到请求 Entry
			StringEntity entity = new StringEntity(jsonString, "utf-8");
			entity.setContentType("application/json");
			entity.setContentEncoding("utf-8");

			requestParams.setBodyEntity(entity);
			HttpUtils http = new HttpUtils();
			ResponseStream stream = http.sendSync(HttpMethod.POST, url,
					requestParams);
			Header[] list = stream.getBaseResponse().getHeaders(
					"Content-Disposition");
			String fileName = list[0].getValue().split("=")[1];
			result = Constance.ApkDownloadFile + fileName + ".apk";

			stream.readFile(result);

		} catch (Exception e) {
			e.printStackTrace();
			result = "error";
		}
		return result;
	}
	
	/**
	 * GET 从服务器获取APP列表
	 * 
	 * @return
	 */
	public String getAppList() {
		String url = Constance.API_HOST + APP_List;
		ResponseContext responseContext = HttpExtensionUtil.invokeGetJson(url);
		String result = responseContext.getResult();
		return result;
	}
	
	/**
	 * POST 提交已阅读文件
	 * 
	 * @return
	 */
	public static String postDocument_Readfile(ArrayList<String> ids) {
		String result = "";
		try {
			String url = Constance.API_HOST+Document_Readfile;
			ReadDocument readDocument = new ReadDocument();
			readDocument.setIds(ids);
			String jsonString = JsonUtils.toJSONString(readDocument);
			ResponseContext responseContext = HttpExtensionUtil.invokePost(url,
					jsonString, null);
			result = responseContext.getResult();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * Get 获取APP详细信息
	 * 
	 * @return
	 */
	public String getAppDetail(String id) {
		String url = Constance.API_HOST + APP_Detail + id;
		ResponseContext responseContext = HttpExtensionUtil.invokeGetJson(url);
		String result = responseContext.getResult();
		return result;
	}
	
	/**
	 * 上传社保卡对应头像
	 * @param fileName
	 * @param file
	 * @param socialCardNo
	 * @return
	 */
	public static String uploadTradeFile( File file,String appid,
			String timestamp,String sign,String tradeDate,int tradeType) {
		String result = "";
		String url = "http://172.16.100.243/socialfile-server/api/trade/file";
		String fileName = file.getName();
		try {
			RequestBody fileBody = RequestBody.create(MEDIA_FILE, file);
			MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
					.setType(MultipartBody.FORM);
			RequestBody requestBody = multipartBuilder
					.addFormDataPart("appid", appid)
					.addFormDataPart("timestamp", timestamp)
					.addFormDataPart("sign", sign)
					.addFormDataPart("tradeDate", tradeDate)
					.addFormDataPart("tradeType", String.valueOf(tradeType))
					.addPart(
							Headers.of("Content-Disposition",
									"form-data; name=\"file\";filename=\""
											+ fileName + "\""), fileBody)
					.build();

			Request.Builder builder = new Builder();
			Request request = builder.url(url).post(requestBody).build();
			Call call = OkHttpUtils.getInstance().getOkHttpClient()
					.newCall(request);
			Response response = call.execute();
			result = response.body().string();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}
	
	/**
	 * 上传社保卡对应头像 
	 * @param fileName
	 * @param file
	 * @param socialCardNo
	 * @return
	 */
	public static String uploadMonitorPic( File file,String appid,
			String timestamp,String sign,String monitorTime) {
		String result = "";
		String url = "http://192.168.96.20:8080/socialfile-server/api/monitor/pic";
		String fileName = file.getName();
		try {
			RequestBody fileBody = RequestBody.create(MEDIA_FILE, file);
			MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
					.setType(MultipartBody.FORM);
			RequestBody requestBody = multipartBuilder
					.addFormDataPart("appid", appid)
					.addFormDataPart("timestamp", timestamp)
					.addFormDataPart("sign", sign)
					.addFormDataPart("monitorTime", monitorTime)
					.addPart(
							Headers.of("Content-Disposition",
									"form-data; name=\"file\";filename=\""
											+ fileName + "\""), fileBody)
					.build();

			Request.Builder builder = new Builder();
			Request request = builder.url(url).post(requestBody).build();
			Call call = OkHttpUtils.getInstance().getOkHttpClient()
					.newCall(request);
			Response response = call.execute();
			result = response.body().string();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

}
