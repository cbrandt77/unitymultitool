package com.nfhsnetwork.calebsunitytool.ui.components;


// Credit: https://stackoverflow.com/questions/16213836/java-swing-jtextfield-set-placeholder


import java.awt.*;

import javax.swing.*;
import javax.swing.text.Document;

@SuppressWarnings("serial")
public class PlaceholderTextArea extends JTextArea {

    private String placeholder;

    public PlaceholderTextArea() {
    }

    public PlaceholderTextArea(
        final Document pDoc,
        final String pText,
        final int pRows,
        final int pColumns)
    {
        super(pDoc, pText, pRows, pColumns);
    }

    public PlaceholderTextArea(final int pRows, final int pColumns) {
        super(pRows, pColumns);
    }

    public PlaceholderTextArea(final String pText) {
        super(pText);
    }

    public PlaceholderTextArea(final String pText, final int pRows, final int pColumns) {
        super(pText, pRows, pColumns);
    }

    public String getPlaceholder() {
        return placeholder;
    }

    @Override
    protected void paintComponent(final Graphics pG) {
        super.paintComponent(pG);

        if (placeholder == null || placeholder.length() == 0 || getText().length() > 0) {
            return;
        }

        final Graphics2D g = (Graphics2D) pG;
        g.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(getDisabledTextColor());
        // g.drawString(placeholder, getInsets().left, pG.getFontMetrics()
            //.getMaxAscent() + getInsets().top);
            
        drawString(pG, placeholder, getInsets().left, pG.getFontMetrics().getMaxAscent() + getInsets().top);
        
    }
    
    private void drawString(Graphics g, String text, int x, int y)
    {
       y = y - g.getFontMetrics().getHeight();
       
       for (String line : text.split("\n"))
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
    }

    public void setPlaceholder(final String s) {
        placeholder = s;
    }

}