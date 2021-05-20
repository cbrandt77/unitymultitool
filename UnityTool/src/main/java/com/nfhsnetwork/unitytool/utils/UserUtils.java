package com.nfhsnetwork.unitytool.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class UserUtils {

    public static class Transfer
    {
        public static void copyToClipboard(String s)
        {
            final Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
            final Transferable tText = new StringSelection(s);
            clip.setContents(tText, null);
        }
        
        public static void copyToClipboard(JComponent c)
        {
            final Action action = c.getActionMap().get("copy");
            final ActionEvent evt = new ActionEvent(action, ActionEvent.ACTION_PERFORMED, "");
            TransferHandler.getCopyAction().actionPerformed(evt);
        }
    }
    
    public static void openInBrowser(URI uri)
    {
        try {
            Desktop.getDesktop().browse(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void openInBrowser(String url)
    {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
