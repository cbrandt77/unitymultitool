package com.nfhsnetwork.calebsunitytool;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import com.nfhsnetwork.calebsunitytool.common.UnityToolCommon;
import com.nfhsnetwork.calebsunitytool.ui.ImportDataFrame;
import com.nfhsnetwork.calebsunitytool.updater.UpdateManager;
import com.nfhsnetwork.calebsunitytool.utils.Util;

public class Wrapper 
{
	public static void main(String[] args) 
	{
		if (UnityToolCommon.isDebugMode) {
			System.out.println("[DEBUG] {main} Debug mode active.");
		}
		
		
		UpdateManager.deleteUpdateScriptIfPresent();
        
		if (UpdateManager.checkAndGetUpdates()) {
			//UpdateManager.printAndRunUpdateScript(); //TODO uncomment
			
		}
		
		
		
		setLookAndFeel();
		
		
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
			public void run() {
                new ImportDataFrame().setVisible(true);
            }
        });
	}
	
	private static void setLookAndFeel()
	{
		try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ImportDataFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ImportDataFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ImportDataFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ImportDataFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
	}


	
}
