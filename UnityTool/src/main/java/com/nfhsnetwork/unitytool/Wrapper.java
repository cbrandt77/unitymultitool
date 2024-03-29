package com.nfhsnetwork.unitytool;

import com.nfhsnetwork.unitytool.common.Config;
import com.nfhsnetwork.unitytool.common.Endpoints;
import com.nfhsnetwork.unitytool.logging.Debug;
import com.nfhsnetwork.unitytool.ui.ImportDataFrame;
import com.nfhsnetwork.unitytool.updater.UpdateManager;
import com.nfhsnetwork.unitytool.utils.Util;

import javax.swing.*;

public class Wrapper 
{
	
	
	public static void main(String[] args) 
	{
		Debug.checkIfDebug();
		
		Config.init();
		
		Debug.out("[DEBUG] {main} Current directory: " + Util.getCurrentDirectory());
		Debug.out("[DEBUG] {main} GetClass directory: " + Wrapper.class.getProtectionDomain().getCodeSource().getLocation());
		
		UpdateManager.update();

        Endpoints.genEndpoints();
		
		//createFiles(); //TODO check if required folders exist and, if not, create them (e.g. outputs and other potential folders)
				//TODO make config.json that saves configuration settings between sessions
		
		
		
        SwingUtilities.invokeLater(() -> {
            setLookAndFeel();
            new ImportDataFrame().setVisible(true);
        });
	}
	
//	//TODO
//	private static void createInitialFiles()
//	{
//		File outputsfolder = new File(Util.getCurrentDirectory() + File.separator + "outputs");
//		
//		assert(outputsfolder.isDirectory());
//		
//		if (!outputsfolder.exists())
//			outputsfolder.mkdir();
//		
//		//TODO
//	}
	
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
