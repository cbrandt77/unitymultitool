package com.nfhsnetwork.unitytool.ui.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.Icon;
import javax.swing.JLabel;

public class HyperlinkLabel extends JLabel {

	private String defText;
	private String onHover;
	private URI uri = null;
	
	public HyperlinkLabel() {
		super();
		setText("");
		stuff();
	}

	public HyperlinkLabel(final String text) {
		super(text);
		setText(text);
		stuff();
	}

	public HyperlinkLabel(final String text, final int horizontalAlignment) {
		super(text, horizontalAlignment);
		setText(text);
		stuff();
	}

	public HyperlinkLabel(final String text, final Icon icon, final int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		setText(text);
		stuff();
	}
	
//	public HyperlinkLabel(final String defText, final String link)
//	{
//		super(defText);
//		setText()
//	}
	
	@Override
	public void setText(final String s)
	{
		super.setText(s);
		defText = s;
		onHover = "<html><a href=''>" + s + "</a></html>";
		try {
			final URI test = new URI(s);
			this.uri = test;
		} catch (URISyntaxException e) {
			//Don't change this.uri then, leave as is
		}
	}
	
	public void setAddress(final String address)
	{
		try {
			this.uri = new URI(address);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			this.uri = null;
		}
	}
	
	private final MouseListener DEFMOUSELISTENER = new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
	    	try {
				Desktop.getDesktop().browse(uri);
			} catch (IOException | NullPointerException e1) {
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
	};
	
	public void setCustomMouseListener(MouseListener listener)
	{
		this.removeMouseListener(DEFMOUSELISTENER);
		this.addMouseListener(listener);
	}
	
	private void stuff()
	{
		this.setForeground(Color.BLUE.darker());
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		this.addMouseListener(DEFMOUSELISTENER);
	}
}
