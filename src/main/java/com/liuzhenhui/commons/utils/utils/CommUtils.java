package com.liuzhenhui.commons.utils.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

/**
 * @Description: Http请求处理相关工具类
 * @see: CommUtils 此处填写需要参考的类
 * @version 2016年8月29日 上午10:45:54
 * @author qinji.xu
 */
public class CommUtils {

	/**
	 * 获取IP地址
	 * @param request
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * @Description 参数排序（不含签名字段）
	 * @param requestParam
	 *            请求参数
	 * @param insensitive
	 *            是否忽略大小写
	 * @return
	 * @throws Exception
	 * @see 需要参考的类或方法
	 */
	public static String sortRequestParam(Map<String, Object> requestParam,
			boolean insensitive) throws Exception {
		StringBuilder sb = new StringBuilder("");
		if (null != requestParam && !requestParam.isEmpty()) {
			String[] keys = new String[] {};
			keys = requestParam.keySet().toArray(keys);
			if (insensitive) {
				Arrays.sort(keys, String.CASE_INSENSITIVE_ORDER);
			} else {
				Arrays.sort(keys);
			}

			for (String key : keys) {
				if ("sign".equals(key.toString())) {
					continue;
				}
				String paramValue = String.valueOf(requestParam.get(key));
				// paramValue= URLEncoder.encode(paramValue,"UTF-8");
				sb.append(key).append("=").append(paramValue);
			}
		}
		return sb.toString();
	}

	/**
	 * @Description Md5加密
	 * @param str
	 *            要加密的字符串
	 * @param key
	 *            密钥
	 * @return 加密过的字符串
	 * @see 需要参考的类或方法
	 */
	public static String toHmac(String data, String key) {
		if (key == null || "".equals(key)) {
			throw new RuntimeException("所需密钥为空");
		}

		String hmac = "";//DigestUtil.md5(data + key);
 		return hmac;
	}

	/**
	 * @Description 校验加密字符串
	 * @param data
	 *            原字符串
	 * @param hmac
	 *            Md5加密过的字符串
	 * @param key
	 *            密钥
	 * @return 是否一致
	 * @see 需要参考的类或方法
	 */
	public static boolean checkHmac(String data, String hmac, String key) {
		return toHmac(data, key).equals(hmac) ? true : false;
	}

	public static String connect(String data, String url, String hmac)
			throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String sbStr = null;
		try {
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("data", data));
			nvps.add(new BasicNameValuePair("hmac", hmac));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			CloseableHttpResponse response2 = httpclient.execute(httpPost);
			try {
				HttpEntity entity2 = response2.getEntity();
				if (entity2 != null) {
					InputStream instream = entity2.getContent();
					try {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(instream, "UTF-8"));
						StringBuilder sb = new StringBuilder();
						String line = null;
						while ((line = reader.readLine()) != null) {
							sb.append(line);
						}
						sbStr = sb.toString();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						instream.close();
					}
				}
			} finally {
				response2.close();
			}
		} finally {
			httpclient.close();
		}
		return sbStr;
	}

	public static String invokeTongdunPost(Map<String, Object> params,
			String tongdunUrl, String method) {
		HttpURLConnection conn = null;
		BufferedReader bufferedReader = null;
		try {
			URL url = new URL(tongdunUrl);
			// 组织请求参数
			StringBuilder postBody = new StringBuilder();
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				if (entry.getValue() == null)
					continue;
				postBody.append(entry.getKey())
						.append("=")
						.append(URLEncoder.encode(entry.getValue().toString(),
								"utf-8")).append("&");
			}
			if (!params.isEmpty()) {
				postBody.deleteCharAt(postBody.length() - 1);
			}

			conn = (HttpURLConnection) url.openConnection();
			// 设置长链接
			conn.setRequestProperty("Connection", "Keep-Alive");
			// 设置连接超时
			conn.setConnectTimeout(30000);
			// 设置读取超时
			conn.setReadTimeout(30000);
			// 提交参数
			conn.setRequestMethod(method);
			conn.setDoOutput(true);
			conn.getOutputStream().write(postBody.toString().getBytes());
			conn.getOutputStream().flush();
			int responseCode = conn.getResponseCode();
			if (responseCode != 200) {
				System.out
						.println("[CommUtils] invokeTongdunPost failed, response status:"
								+ responseCode);
				return null;
			}
			bufferedReader = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "utf-8"));
			StringBuilder result = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				result.append(line).append("\n");
			}
			return result.toString().trim();
		} catch (Exception e) {
			System.out
					.println("[CommUtils] invokeTongdunPost throw exception, details: "
							+ e);
			e.printStackTrace();
		} finally {
			if (null != bufferedReader) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != conn) {
				conn.disconnect();
			}
		}
		return null;
	}

	public static String invokeTongdunGet(String tongdunUrl) {
		BufferedReader reader = null;
		HttpURLConnection connection = null;
		try {
			URL getUrl = new URL(tongdunUrl);
			// 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
			// 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
			connection = (HttpURLConnection) getUrl.openConnection();
			// 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到
			// 服务器
			connection.connect();
			// 取得输入流，并使用Reader读取
			reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			StringBuilder result = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				result.append(line).append("\n");
			}
			return result.toString().trim();

		} catch (Exception e) {
			System.out
					.println("[CommUtils] invokeTongdunGet throw exception, details: "
							+ e);
			e.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != connection) {
				connection.disconnect();
			}
		}
		return null;
	}

	/**
	 * 请求参数排序
	 *
	 * @param requestParam
	 *            请求参数
	 * @param separator
	 *            分隔符
	 * @return
	 * @throws Exception
	 */
	public static String sortParam(Map<String, Object> requestParam,
			String separator) throws Exception {
		StringBuilder sb = new StringBuilder("");
		if (null != requestParam && !requestParam.isEmpty()) {
			Object[] keys = requestParam.keySet().toArray();
			Arrays.sort(keys);
			for (Object key : keys) {
				if ("sign".equals(key.toString())) {
					continue;
				}
				String paramValue = String.valueOf(requestParam.get(key));
				sb.append(key).append("=").append(paramValue).append(separator);
			}
		}
		return sb.toString().substring(0, sb.length() - 1);
	}

	public static void main(String[] args) {
		try {
			Map<String, Object> requestParam = new HashMap<String, Object>();
			requestParam.put("applyAmt", "15117976409");
			requestParam.put("applyMonthlyrepay", "123456");
			requestParam.put("applyTerm", "ASDZXC");
			requestParam.put("appVersion", "IOS");
			requestParam.put("appId", "IOS");
			requestParam.put("CC", "IOS");
			String data = CommUtils.sortRequestParam(requestParam, true);
			System.out.println(data);
			String key = "1234Qwer";
			// String data =
			// "appId=888888applyAmt=15000.00applyMonthlyrepay=2623.91applyTerm=6appVersion=1.01deviceToken=6bae4cb78db9b670c4143de622cc9dc6f0d4182ddeviceType=IOSmessageToken=b079a0fefbf760c6ffccadbec0b58f159763e4fc119803cb06d9349323f4a41fproductNo=WLD_00000001_002userName=18514236638verificationcode=007405";
			System.out.println(CommUtils.toHmac(data, key));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
