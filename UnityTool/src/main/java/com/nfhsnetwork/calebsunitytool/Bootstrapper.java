package com.nfhsnetwork.calebsunitytool;

import com.nfhsnetwork.calebsunitytool.nbui.ImportDataFrame;
import com.nfhsnetwork.calebsunitytool.updater.UpdateManager;

public class Bootstrapper 
{
	public static final String OS = System.getProperty("os.name");
	
	
	// TODO set to false
	// also maybe use a logger instead of sysout?
	public static boolean isDebugMode = true;
	
	
	public static void main(String[] args) 
	{
		setLookAndFeel();
		
		boolean hasUpdated = UpdateManager.checkAndGetUpdates();
		
		if (hasUpdated)
		{
			restartProgram();
		}
		
		if (args.length != 0)
        {
        	if (args[0].equals("--debug")) //TODO figure out how args are parsed
        		isDebugMode = true;
        }
        
		
		
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
			public void run() {
                new ImportDataFrame().setVisible(true);
            }
        });
	}
	
	
	private static void restartProgram()
	{
		//TODO
		System.out.println("[DEBUG] {restartProgram} ");
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
