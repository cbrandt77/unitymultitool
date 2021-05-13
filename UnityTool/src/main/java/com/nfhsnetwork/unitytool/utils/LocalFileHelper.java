package com.nfhsnetwork.unitytool.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public final class LocalFileHelper {
	
	public static class Config
	{
		//TODO move config stuff here
	}
	
	public static final class Version
	{
		public static final int THISRELEASE = 42437609;
		
	}
	
	
	public static class Credentials
	{
		private static final Path CREDENTIALS_FOLDER = Util.getAbsoluteDir("/bin/auth/");
		
		private static final String TOKEN_FILENAME = "token";
		
		public static boolean saveSSOToken(final String token)
		{
			try {
				IOUtils.createAndPrintToFile(token, CREDENTIALS_FOLDER.toFile(), TOKEN_FILENAME);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		public static String getSavedToken() throws IOException
		{
			return parseTokenFile(IOUtils.readFromFile(CREDENTIALS_FOLDER + File.separator + TOKEN_FILENAME));
		}
		
		private static String parseTokenFile(final String tokenFile)
		{
			return tokenFile; //This won't be necessary unless I do some weirdness with how it's stored, but I wanted to put this here nonetheless.
		}
		
		
		
		
		
		
		private static final String EMAIL_FILENAME = "user";
		
		
		public static boolean saveRawEmail(final String email)
		{
			try {
				IOUtils.createAndPrintToFile(email, CREDENTIALS_FOLDER.toFile(), EMAIL_FILENAME);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		public static String getSavedEmail() throws IOException
		{
			return parseEmailFile(IOUtils.readFromFile(CREDENTIALS_FOLDER + File.separator + EMAIL_FILENAME));
		}
		
		private static String parseEmailFile(final String emailFile)
		{
			return emailFile;
		}
	}
	
}
