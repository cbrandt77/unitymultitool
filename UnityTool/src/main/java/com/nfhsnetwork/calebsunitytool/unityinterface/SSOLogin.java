package com.nfhsnetwork.calebsunitytool.unityinterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.nfhsnetwork.calebsunitytool.common.UnityContainer;
import com.nfhsnetwork.calebsunitytool.utils.JSONUtils;

public final class SSOLogin
{
	private static final String SSO_ENDPOINT = "https://sso.nfhsnetwork.com/users/sign_in";
	
	
	private static final String RAWPOSTDATA = "utf8=%%E2%%9C%%93&authenticity_token=" + "%s" + "&user%%5Bemail%%5D=" + "%s" + "&user%%5Bpassword%%5D=" + "%s" + "&commit=Sign+in+%%3E";
	
	
	static {
		CookieHandler.setDefault(null);
	}
	
	static CookieManager cm; 
	
	
//	
//	public static void main(String[] args)
//	{
//		try {
//			loginToUnity("caleb.brandt@nfhsnetwork.com", "beelayboop".toCharArray());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	
//	public static String login(String email, char[] password) throws IOException
//	{
//		cm = new CookieManager();
//		HttpClient h = HttpClient.newBuilder()
//								 .cookieHandler(cm)
//								 .build();
//		
//		try {
//			HttpRequest.Builder getbuilder = HttpRequest.newBuilder(new URI(SSO_ENDPOINT))
//					   					 .GET();
//			getbuilder.header("Accept-Language", "en-US,en;q=0.9");
//			getbuilder.header("Accept-Encoding", "gzip, deflate");
//			getbuilder.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
//			getbuilder.header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_2_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");
//			getbuilder.header("Content-Type", "application/x-www-form-urlencoded");
//			getbuilder.header("Origin", "http://sso.nfhsnetwork.com");
//			getbuilder.header("Upgrade-Insecure-Requests", "1");
//			getbuilder.header("Cache-Control", "max-age=0");
//			//getbuilder.header("Connection", "keep-alive");
//			//getbuilder.header("Host", "sso.nfhsnetwork.com");
//			HttpRequest get = getbuilder.build();
//			
//			HttpResponse<String> s = h.send(get, BodyHandlers.ofString());
//			
//			System.out.println(s.body());
//			System.out.println(s.statusCode());
//			System.out.println(s.headers());
//		} catch (IOException | InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		
//		
//		return null;
//	}
	
	
	public static String loginToUnity(String email, char[] cs) throws IOException
	{
		cm = new CookieManager();
		CookieHandler.setDefault(cm);
		cm.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		
		String csrfToken = URLEncoder.encode(getCSRFToken(), "UTF-8");
		
		String dataRaw = String.format(RAWPOSTDATA, csrfToken, email, new String(cs));
		byte[] out = dataRaw.getBytes();
		
		System.out.println("[DEBUG]" + dataRaw);
		
		
		HttpURLConnection http = (HttpURLConnection)(new URL(SSO_ENDPOINT).openConnection());
		http.setRequestMethod("POST");
		http.setDoInput(true);
		http.setDoOutput(true);
		
		addHeaders(http);
		
		
		http.connect();
		
		try (OutputStream os = http.getOutputStream())
		{
			os.write(out);
		}
		
		System.out.println("[DEBUG] response content type: " + http.getContentType());
		
		try (InputStream is = http.getInputStream())
		{
			System.out.println("\n\n[DEBUG] Login Response:\n" + JSONUtils.JSONReader.readAllFromReader(new BufferedReader(new InputStreamReader(is))) + "\n\n");
		}
		
		http.disconnect();
		
		
		System.out.println("[DEBUG] response code: " + http.getResponseCode());
		
		cm.getCookieStore().getCookies().stream().forEach(System.out::println); //[DEBUG]
		
		List<HttpCookie> l = cm.getCookieStore().getCookies().stream().filter(e -> e.toString().contains("sso_access_token"))
							.collect(Collectors.toList());
		
		if (l.size() != 1)
			throw new IOException("[BTPOS] no cookies retrieved");
		
		String authToken = l.get(0)
								.toString()
									.substring("sso_access_token=".length());
		
		UnityContainer.setUserEmail(email);
		
		return authToken;
	}
	
	private static String getCSRFToken() throws IOException
	{
		HttpURLConnection http = (HttpURLConnection)(new URL(SSO_ENDPOINT).openConnection());
		http.setRequestMethod("GET");
		http.connect();
		
		String siteBody = null;
		if (http.getResponseCode() == 200) {
			try (InputStream is = http.getInputStream())
			{
				if ("gzip".equals(http.getContentEncoding())) 
				{
					try (GZIPInputStream g = new GZIPInputStream(is)) {
						siteBody = JSONUtils.JSONReader.readAllFromReader(new BufferedReader(new InputStreamReader(g)));
					}
				}
				else
				{
					siteBody = JSONUtils.JSONReader.readAllFromReader(new BufferedReader(new InputStreamReader(is)));
				}
			}
		}
		else {
			throw new IOException("[BTPOS] Could not connect to SSO.");
		}
		
		
		
		Document doc = Jsoup.parse(siteBody);
		
		System.out.println("\n\n[DEBUG] Site body:\n" + siteBody + "\n\n");
		
		// ! IS NOT DYNAMIC (but is faster)
		return doc.select("#new_user") 
				  .get(0)
				  .children()
				  .get(1)
				  .val();
	}
	
	private static void addHeaders(HttpURLConnection http)
	{
		http.addRequestProperty("Accept-Language", "en-US,en;q=0.9");
		http.addRequestProperty("Accept-Encoding", "gzip, deflate");
		http.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
		http.addRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_2_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");
		http.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		http.addRequestProperty("Origin", "http://sso.nfhsnetwork.com");
		http.addRequestProperty("Upgrade-Insecure-Requests", "1");
		http.addRequestProperty("Cache-Control", "max-age=0");
		http.addRequestProperty("Connection", "keep-alive");
		http.addRequestProperty("Host", "sso.nfhsnetwork.com");
	}
	
}










