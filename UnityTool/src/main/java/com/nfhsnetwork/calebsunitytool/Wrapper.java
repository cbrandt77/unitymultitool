package com.nfhsnetwork.calebsunitytool;

import java.io.File;

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
			if (UnityToolCommon.isDebugMode) {
				System.out.println("[DEBUG] {main} Update downloaded, executing update script.");
			}
			
			UpdateManager.printAndRunUpdateScript();
		}
		
		//createFiles(); //TODO check if required folders exist and, if not, create them (e.g. outputs and other potential folders)
				//TODO make config.json that saves configuration settings between sessions
				//TODO hide token better
		
		setLookAndFeel();
		
		
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
			public void run() {
                new ImportDataFrame().setVisible(true);
            }
        });
	}
	
	//TODO
	private static void createFiles()
	{
		File outputsfolder = new File(Util.getCurrentDirectory() + File.separator + "outputs");
		
		assert(outputsfolder.isDirectory());
		
		if (!outputsfolder.exists())
			outputsfolder.mkdir();
		
		//TODO
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
