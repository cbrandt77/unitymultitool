package com.nfhsnetwork.calebsunitytool.gui;


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
        g.drawString(placeholder, getInsets().left, pG.getFontMetrics()
            .getMaxAscent() + getInsets().top);
    }

    public void setPlaceholder(final String s) {
        placeholder = s;
    }

}