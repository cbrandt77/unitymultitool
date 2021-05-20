package com.nfhsnetwork.unitytool.io;

import java.awt.Window;
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
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import javax.swing.SwingUtilities;

import com.nfhsnetwork.unitytool.common.Endpoints;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.nfhsnetwork.unitytool.common.Config;
import com.nfhsnetwork.unitytool.common.UnityContainer;
import com.nfhsnetwork.unitytool.logging.Debug;
import com.nfhsnetwork.unitytool.ui.LoginDialog;
import com.nfhsnetwork.unitytool.utils.IOUtils;

public final class SSOLogin
{
	private static final String SSO_ENDPOINT = Endpoints.getSSOLoginEndpoint();
	
	
	private static final String RAWPOSTDATA = "utf8=%%E2%%9C%%93&authenticity_token=" + "%s" + "&user%%5Bemail%%5D=" + "%s" + "&user%%5Bpassword%%5D=" + "%s" + "&commit=Sign+in+%%3E";
	
	
	static {
		CookieHandler.setDefault(null);
	}
	
	static CookieManager cm;
	
	
	public static void showLoginDialog(final Window parent, final Function<Void, Void> callback)
	{
		final LoginDialog ld = new LoginDialog(parent);
		
		ld.addActionListener((evt) -> {
			ld.setVisible(false);
			ld.dispose();
			Debug.out("[DEBUG] {loginToUnity} event fired");
			
			callback.apply(null);
		});
		
		SwingUtilities.invokeLater(() -> ld.setVisible(true));
	}
	
	
	
	public static String loginToUnity(final String email, final char[] cs) throws IOException
	{
		cm = new CookieManager();
		CookieHandler.setDefault(cm);
		cm.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		
		final String csrfToken = URLEncoder.encode(getCSRFToken(), StandardCharsets.UTF_8);
		
		final String dataRaw = String.format(RAWPOSTDATA, csrfToken, email, new String(cs));
		final byte[] out = dataRaw.getBytes();
		
		Debug.out("[DEBUG]" + dataRaw);
		
		
		final HttpURLConnection http = (HttpURLConnection)(new URL(SSO_ENDPOINT).openConnection());
		http.setRequestMethod("POST");
		http.setDoInput(true);
		http.setDoOutput(true);
		
		addHeaders(http);
		
		
		http.connect();
		
		try (final OutputStream os = http.getOutputStream())
		{
			os.write(out);
		}
		
		Debug.out("[DEBUG] response content type: " + http.getContentType());
		
		try (final InputStream is = http.getInputStream())
		{
			System.out.println("\n\n[DEBUG] Login Response:\n" + IOUtils.readAllFromReader(new BufferedReader(new InputStreamReader(is))) + "\n\n");
		}
		
		http.disconnect();
		
		
		Debug.out("[DEBUG] response code: " + http.getResponseCode());
		
		cm.getCookieStore().getCookies().forEach(System.out::println); //[DEBUG]
		
		final List<HttpCookie> l = cm.getCookieStore().getCookies().stream().filter(e -> e.toString().contains("sso_access_token"))
							.collect(Collectors.toList());
		
		if (l.size() != 1)
			throw new IOException("[BTPOS] no cookies retrieved");
		
		final String authToken = l.get(0)
									.toString()
										.substring("sso_access_token=".length());
		
//		if (shouldSaveLoginInfo)
//			saveTokenToFile(authToken);
		
		UnityContainer.setUserEmail(email);
		
		return authToken;
	}
	
	private static String getCSRFToken() throws IOException
	{
		final HttpURLConnection http = (HttpURLConnection)(new URL(SSO_ENDPOINT).openConnection());
		http.setRequestMethod("GET");
		http.connect();
		
		final String siteBody;
		if (http.getResponseCode() == 200) {
			try (final InputStream is = http.getInputStream())
			{
				if ("gzip".equals(http.getContentEncoding())) 
				{
					try (GZIPInputStream g = new GZIPInputStream(is)) {
						siteBody = IOUtils.readAllFromReader(new BufferedReader(new InputStreamReader(g)));
					}
				}
				else
				{
					siteBody = IOUtils.readAllFromReader(new BufferedReader(new InputStreamReader(is)));
				}
			}
		}
		else {
			throw new IOException("[UnityTool] Could not connect to SSO.");
		}
		
		
		
		final Document doc = Jsoup.parse(siteBody);
		
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
		http.addRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac ISWINDOWS X 11_2_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");
		http.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		http.addRequestProperty("Origin", "http://sso.nfhsnetwork.com");
		http.addRequestProperty("Upgrade-Insecure-Requests", "1");
		http.addRequestProperty("Cache-Control", "max-age=0");
		http.addRequestProperty("Connection", "keep-alive");
		http.addRequestProperty("Host", "sso.nfhsnetwork.com");
	}
	
	
	private static final boolean shouldSaveLoginInfo = Config.shouldSaveToken();
	
	public static void saveTokenToFile(final String token)
	{
		final String raw = token.split("=")[1];
		
		try {
			IOUtils.printToFile(raw, Config.getCookieFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}










