package com.nfhsnetwork.unitytool.utils;

import com.nfhsnetwork.unitytool.logging.Debug;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

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
	
	public static class Assets
	{
		private static final String ASSETS_PATH = Util.getAbsoluteDir("bin/assets").toString();
		private static final String ICONS_PATH = ASSETS_PATH + File.separator + "icons" + File.separator;
		
		static Map<String, ImageIcon> iconCache = new HashMap<>();
		
		@Nullable
		public static Icon getIconForComponent(final Component user, final String iconName) {
			ImageIcon icon = iconCache.get(iconName);
			if (icon == null) {
				try {
					final File f = new File(ICONS_PATH + iconName);
					Debug.out(f.toString());
					Debug.out("[DEBUG] " + user.getWidth());
					final BufferedImage img = ImageIO.read(f);
					final int size = user.getPreferredSize().height - 4;
					final Image resized = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
					icon = new ImageIcon(resized);
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
			iconCache.put(iconName, icon);
			return icon;
		}
	}
	
}
