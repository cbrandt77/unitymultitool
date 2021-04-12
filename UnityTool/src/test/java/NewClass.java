
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;
import org.jsoup.select.NodeTraversor;

import com.nfhsnetwork.calebsunitytool.utils.JSONUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author calebbrandt
 */
public class NewClass 
{
    public static void main(String[] args)
    {
    	try {
			loginToUnity("caleb.brandt@nfhsnetwork.com", "beelayboop");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	System.out.println("End!");
    }
    
    private static final String SSO_ENDPOINT = "https://sso.nfhsnetwork.com/users/sign_in";
	
	
	private static final String RAWDATA = "utf8=%%E2%%9C%%93&authenticity_token=" + "%s" + "&user%%5Bemail%%5D=" + "%s" + "&user%%5Bpassword%%5D=" + "%s" + "&commit=Sign+in+%%3E";
	
	static CookieManager cm = new CookieManager();
	
    public static String loginToUnity(String email, String password) throws IOException
	{
    	CookieHandler.setDefault(cm);
    	cm.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		
		String csrfToken = getCSRFToken();
		System.out.println(csrfToken);
		
		String s;
		
		System.out.println(s = URLEncoder.encode(csrfToken, "UTF-8"));
		
		String dataRaw = String.format(RAWDATA, s, email, password);
		byte[] out = dataRaw.getBytes();
		
		System.out.println(dataRaw);
		
		HttpURLConnection http = (HttpURLConnection)(new URL(SSO_ENDPOINT).openConnection());
		http.setRequestMethod("POST");
		http.setDoOutput(true);
		
		addHeaders(http);
		
		
		http.connect();
		System.out.println("Connected!");
		
		try (OutputStream os = http.getOutputStream())
		{
			os.write(out);
		}
		
		try (InputStream is = http.getInputStream())
		{
			System.out.println("Input Stream Started with response code: " + http.getResponseCode());
			Reader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			int cp;
			while ((cp = rd.read()) != -1)
			{
				sb.append((char)cp);
			}
			System.out.println(sb.toString());
		}
		
		List<String> cookies = http.getHeaderFields().get("Set-Cookie");
		
		cookies.forEach(System.out::println);
		
		cm.getCookieStore().getCookies().stream()
		.filter(e -> e.toString().contains("sso_access_token"))
		.collect(Collectors.toList())
		.forEach(System.out::println);;
		
		http.disconnect();
		cm.getCookieStore().getCookies().forEach(e -> System.out.println("Cookie: " + e.toString()));
		
		
		
		return null;
	}
	
	private static String getCSRFToken() throws IOException
	{
		HttpURLConnection http = (HttpURLConnection)(new URL(SSO_ENDPOINT).openConnection());
		http.setRequestMethod("GET");
		http.connect();
		
		String siteBody = null;
		if (http.getResponseCode() == 200) {
			InputStream response = http.getInputStream();
			siteBody = JSONUtils.JSONReader.readAllFromReader(new InputStreamReader(response));
		}
		else 
			throw new IOException("Could not connect to Unity.");
		
		http.disconnect();
		
		cm.getCookieStore().getCookies().forEach(System.out::println);
		
		Document doc = Jsoup.parse(siteBody);
		
		
		
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
		http.addRequestProperty("Origin", "http://sso.nfhsnetwork.com");
		http.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		http.addRequestProperty("Cache-Control", "max-age=0");
		http.addRequestProperty("Connection", "keep-alive");
		http.addRequestProperty("Host", "sso.nfhsnetwork.com");
	}
	
	private class CustomCookieManager extends CookieManager
	{
		CustomCookieManager() {
			super();
		}
		
		
	}
}
