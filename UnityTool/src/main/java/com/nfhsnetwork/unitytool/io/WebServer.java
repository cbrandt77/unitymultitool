package com.nfhsnetwork.unitytool.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

import com.nfhsnetwork.unitytool.utils.IOUtils;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class WebServer {
	
	
	public static final void initWebServer() throws IOException
	{
		HttpServer http = HttpServer.create(new InetSocketAddress(8080), 0);
		
		
		http.createContext("/salesforce/oauth/_callback", new OAuthHandler());
		
		
	}
	
	
	static class OAuthHandler implements HttpHandler
	{
		@Override
		public void handle(HttpExchange exchange) throws IOException
		{
			switch (exchange.getRequestMethod()) {
			case "POST":
				handleInput(exchange);
				break;
			case "OPTIONS":
				handleOptions(exchange);
				break;
			case "PUT":
			case "GET":
			default:
				break;
			}
			
		}
		
		private void handleInput(final HttpExchange exchange) throws IOException
		{
			final String s;
			try (InputStream is = exchange.getRequestBody();
					BufferedReader rd = new BufferedReader(new InputStreamReader(is))) {
				s = IOUtils.readAllFromReader(rd);
			}
			
			//TODO
		}
		
		
		
		private void handleOptions(final HttpExchange exchange) throws IOException
		{
			Headers h = exchange.getResponseHeaders();
			h.add("Allow", "OPTIONS, POST");
			
			exchange.sendResponseHeaders(200, -1);
			
			exchange.close();
		}
	}
	
	
}









interface HttpListener
{
	public void actionPerformed(HttpEvent e);
}

class HttpEvent 
{
	private final Object source;
	private final HttpContext context;
	private final String requestMethod;
	private final Object payload;
	
	public HttpEvent(Object source, HttpContext context, String requestMethod, Object payload) {
		super();
		this.source = source;
		this.context = context;
		this.requestMethod = requestMethod;
		this.payload = payload;
	}
	
	
	public Object getSource() {
		return source;
	}
	public HttpContext getContext() {
		return context;
	}
	public String getRequestMethod() {
		return requestMethod;
	}
	public Object getPayload() {
		return payload;
	}
	
}

