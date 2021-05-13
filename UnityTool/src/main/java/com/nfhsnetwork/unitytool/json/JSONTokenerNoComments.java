package com.nfhsnetwork.unitytool.json;

import java.io.InputStream;
import java.io.Reader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JSONTokenerNoComments extends JSONTokener {

	public JSONTokenerNoComments(String s) {
		super(s);
	}
	
	public JSONTokenerNoComments(Reader reader)
	{
		super(reader);
	}
	
	public JSONTokenerNoComments(InputStream is)
	{
		super(is);
	}
	
	
	
	@Override
	public char nextClean() throws JSONException {
        for (;;) {
            char main = this.next();
            
            if (main == '/') // Skip C-style comments
            {
            	char c = this.next();
            	
            	if (c == '*') { // Skip multiline comments
            		char c2 = this.next();
            		for (;;) {
            			if (c == '*' && c2 == '/')
            				break;
            			
            			c = c2;
            			c2 = this.next();
            		}
            		main = this.next();
            	}
            	else if (c == '/') { // Skip EoL comments
            		this.skipTo('\n');
            		main = this.next();
            	}
            }
            else if (main == '#') // Skip hash comments
            {
            	this.skipTo('\n');
            	main = this.next();
            }
            
            if (main == 0 || main > ' ') {
                return main;
            }
        }
    }
	
	

	

}
