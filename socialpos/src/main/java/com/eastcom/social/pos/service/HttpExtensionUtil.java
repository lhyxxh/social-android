package com.eastcom.social.pos.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.eastcom.social.pos.entity.ResponseContext;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.FileBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;

/**
 * SampleHttpUtil升级版,由HttpUtils扩展而来
 * 
 * @author wood
 * 
 */
public class HttpExtensionUtil {

	private final static String TAG = "HttpExtensionUtil";

	public static CookieStore cookieStore; // 会自动移除过期Cookie

	/**
	 * 获取对象公用方法
	 * 
	 * @param url
	 * @param token
	 * @return
	 */
	public static <T> T getObjects(String url, TypeToken<T> token) {
		try {
			ResponseContext responseContext = HttpExtensionUtil
					.invokeGetJson(url);
			String result = responseContext.getResult();
			Gson g = new Gson();
			return g.fromJson(result, token.getType());
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 获取对象公用方法
	 * 
	 * @param url
	 * @param clazz
	 * @return
	 */
	public static <T> T getObjects(String url, Class<T> clazz) {
		try {
			ResponseContext responseContext = HttpExtensionUtil
					.invokeGetJson(url);
			String result = responseContext.getResult();
			Gson g = new Gson();
			return g.fromJson(result, clazz);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 封装Get添加cookie支持，验证成功返回json格式
	 * 
	 * @param url
	 * @return
	 */
	public static ResponseContext invokeGetJson(String url) {
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		// 添加要传递的参数
		NameValuePair pair1 = new BasicNameValuePair("Content-Type",
				"application/json");
		headers.add(pair1);
		return invokeGet(url, headers);
	}

	/**
	 * 封装Post，支持传输json数据
	 * 
	 * @param url
	 * @param jsonString
	 * @return
	 */
	public static ResponseContext invokePostJson(String url, String jsonString) {
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		// 添加要传递的参数
		NameValuePair pair1 = new BasicNameValuePair("Content-Type",
				"application/json");
		headers.add(pair1);
		return invokePost(url, jsonString, headers);
	}

	/**
	 * 封装Get，添加cookie支持
	 * 
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 */
	public static ResponseContext invokeGet(String url,
			List<NameValuePair> headers) {
		ResponseContext result = new ResponseContext();
		try {
			RequestParams requestParams = new RequestParams();
			if (headers != null && headers.size() > 0) {
				requestParams.addBodyParameter(headers);
			}
			result = invokeRequest(url, requestParams,
					HttpRequest.HttpMethod.GET);
		} catch (Exception e) {
			String messageString = e.getMessage();
			result.setMessage(messageString);
			Log.e(TAG, messageString);
		}
		Log.d(TAG, "over");
		return result;
	}

	/**
	 * 封装Post，添加cookie支持
	 * 
	 * @param url
	 * @param jsonString
	 * @param headers
	 * @return
	 */
	public static ResponseContext invokePost(String url, String jsonString,
			List<NameValuePair> headers) {
		ResponseContext result = new ResponseContext();
		try {
			RequestParams requestParams = new RequestParams();
			if (headers != null && headers.size() > 0) {
				requestParams.addBodyParameter(headers);
			}
			// 绑定到请求 Entry
			StringEntity entity = new StringEntity(jsonString, "utf-8");
			entity.setContentType("application/json");
			entity.setContentEncoding("utf-8");
			requestParams.setBodyEntity(entity);
			result = invokeRequest(url, requestParams,
					HttpRequest.HttpMethod.POST);
		} catch (Exception e) {
			String messageString = e.getMessage();
			result.setMessage(messageString);
			Log.e(TAG, messageString);
		}
		Log.d(TAG, "over");
		return result;
	}

	public static byte[] invokePostStream(String url, String jsonString,
			List<NameValuePair> headers) {
		byte[] result = null;
		try {
			RequestParams requestParams = new RequestParams();
			if (headers != null && headers.size() > 0) {
				requestParams.addBodyParameter(headers);
			}
			// 绑定到请求 Entry
			StringEntity entity = new StringEntity(jsonString, "utf-8");
			entity.setContentType("application/json");
			entity.setContentEncoding("utf-8");
			requestParams.setBodyEntity(entity);
			result = invokeRequestStream(url, requestParams,
					HttpRequest.HttpMethod.POST);
		} catch (Exception e) {
			String messageString = e.getMessage();
			Log.e(TAG, messageString);
		}
		Log.d(TAG, "over");
		return result;
	}

	/**
	 * 封装Put，添加cookie支持
	 * 
	 * @param url
	 * @param jsonString
	 * @param headers
	 * @return
	 */
	public static ResponseContext invokePut(String url, String jsonString,
			List<NameValuePair> headers) {
		ResponseContext result = new ResponseContext();
		try {
			RequestParams requestParams = new RequestParams();
			if (headers != null && headers.size() > 0) {
				requestParams.addBodyParameter(headers);
			}
			// 绑定到请求 Entry
			StringEntity entity = new StringEntity(jsonString, "utf-8");
			entity.setContentType("application/json");
			entity.setContentEncoding("utf-8");
			requestParams.setBodyEntity(entity);
			result = invokeRequest(url, requestParams,
					HttpRequest.HttpMethod.PUT);
		} catch (Exception e) {
			String messageString = e.getMessage();
			result.setMessage(messageString);
			Log.e(TAG, messageString);
		}
		Log.d(TAG, "over");
		return result;
	}

	/**
	 * 封装Post，添加cookie支持
	 * 
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 */
	public static ResponseContext invokePost(String url,
			List<NameValuePair> params, List<NameValuePair> headers) {
		ResponseContext result = new ResponseContext();
		try {
			RequestParams requestParams = new RequestParams();
			if (headers != null && headers.size() > 0) {
				requestParams.addBodyParameter(headers);
			}
			if (params != null && params.size() > 0) {
				// 设置字符集
				HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
				requestParams.setBodyEntity(entity);
			}
			result = invokeRequest(url, requestParams,
					HttpRequest.HttpMethod.POST);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static ResponseContext invokeRequest(String url,
			RequestParams requestParams, HttpRequest.HttpMethod httpMethod) {
		ResponseContext result = new ResponseContext();
		try {
			Log.d(TAG, "url is:" + url);
			HttpUtils http = new HttpUtils();
			// 添加Cookie
			if (cookieStore != null) {
				http.configCookieStore(cookieStore);
			}
			ResponseStream stream = http.sendSync(httpMethod, url,
					requestParams);
			result.setStatusCode(stream.getStatusCode());
			result.setResult(stream.readString());
			DefaultHttpClient dh = (DefaultHttpClient) http.getHttpClient();
			// 保存Cookie
			CookieStore cs = dh.getCookieStore();
			if (cs != null && cs.getCookies() != null
					&& cs.getCookies().size() > 0) {
				cookieStore = cs;
			}
		} catch (Exception e) {
			String messageString = e.getMessage();
			result.setMessage(messageString);
			Log.e(TAG, messageString);
		}
		Log.d(TAG, "over");
		return result;
	}

	public static byte[] invokeRequestStream(String url,
			RequestParams requestParams, HttpRequest.HttpMethod httpMethod) {
		byte[] result = null;
		try {
			Log.d(TAG, "url is:" + url);
			HttpUtils http = new HttpUtils();
			// 添加Cookie
			if (cookieStore != null) {
				http.configCookieStore(cookieStore);
			}
			ResponseStream stream = http.sendSync(httpMethod, url,
					requestParams);
			Header[] list = stream.getBaseResponse().getHeaders(
					"Content-Disposition");
			String fileName = list[0].getValue().split("=")[1];
			stream.readFile("/sdcard/111/"+fileName+".apk");
		} catch (Exception e) {
			String messageString = e.getMessage();
			Log.e(TAG, messageString);
		}
		Log.d(TAG, "over");
		return result;
	}

	/**
	 * 提交参数里有文件的数据
	 * 
	 * @param url
	 *            服务器地址
	 * @param param
	 *            参数
	 * @return 服务器返回结果
	 * @throws Exception
	 */
	public static String uploadSubmit(String url, Map<String, String> param,
			String filePath) throws Exception {
		File file = new File(filePath);
		HttpPost post = new HttpPost(url);

		MultipartEntity entity = new MultipartEntity();
		if (param != null && !param.isEmpty()) {
			for (Map.Entry<String, String> entry : param.entrySet()) {
				entity.addPart(entry.getKey(), new StringBody(entry.getValue()));
			}
		}
		// 添加文件参数
		if (file != null && file.exists()) {
			entity.addPart("file", new FileBody(file));
		}
		post.setEntity(entity);
		// 取得默认的HttpClient
		DefaultHttpClient httpClient = new DefaultHttpClient();

		// 添加Cookie
		if (cookieStore != null) {
			httpClient.setCookieStore(cookieStore);
		}
		HttpResponse response = httpClient.execute(post);
		int stateCode = response.getStatusLine().getStatusCode();
		StringBuffer sb = new StringBuffer();
		if (stateCode == HttpStatus.SC_OK) {
			HttpEntity result = response.getEntity();
			if (result != null) {
				InputStream is = result.getContent();
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));
				String tempLine;
				while ((tempLine = br.readLine()) != null) {
					sb.append(tempLine);
				}
			}
		}
		post.abort();
		return sb.toString();
	}
}
