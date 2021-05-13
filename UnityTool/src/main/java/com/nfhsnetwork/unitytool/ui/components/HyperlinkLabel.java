package com.nfhsnetwork.unitytool.ui.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.Icon;
import javax.swing.JLabel;

public class HyperlinkLabel extends JLabel {

	private String defText;
	private String onHover;
	private URI uri;
	
	public HyperlinkLabel() {
		this.defText = "";
		setText("");
		stuff();
	}

	public HyperlinkLabel(final String text) {
		super(text);
		this.defText = text;
		setText(text);
		stuff();
	}

	public HyperlinkLabel(final String text, final int horizontalAlignment) {
		super(text, horizontalAlignment);
		this.defText = text;
		setText(text);
		stuff();
	}

	public HyperlinkLabel(final String text, final Icon icon, final int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		this.defText = text;
		setText(text);
		stuff();
	}
	
	public HyperlinkLabel(final String defText, final String link)
	{
		super(defText);
	}
	
	@Override
	public void setText(final String s)
	{
		super.setText(s);
		defText = s;
		onHover = "<html><a href=''>" + s + "</a></html>";
		try {
			uri = new URI(s);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			uri = null;
		}
	}
	
	private void stuff()
	{
		this.setForeground(Color.BLUE.darker());
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		this.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	try {
					Desktop.getDesktop().browse(uri);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		    }
		    
		    @Override
		    public void mouseEntered(MouseEvent e) {
		    	HyperlinkLabel.super.setText(onHover);
		    }
		    
		    @Override
		    public void mouseExited(MouseEvent e) {
		        HyperlinkLabel.super.setText(defText);
		    }
		});
	}
}
