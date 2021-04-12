package com.nfhsnetwork.calebsunitytool.gui;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.nfhsnetwork.calebsunitytool.MultiviewerTagScript;

/**
*
* @author c.brandt
*/
public class UnityTaggerFrame extends javax.swing.JFrame {

   /**
    * Creates new form UnityTaggerFrame
    */
   public UnityTaggerFrame() {
       initComponents();
   }

   private void initComponents() {
	   unityPost = new MultiviewerTagScript(this);
       scrollPane_textArea = new javax.swing.JScrollPane();
       textArea = new PlaceholderTextArea();
       confirmButton = new javax.swing.JButton();

       setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
       setTitle("Tag for Multiviewer");
       setLocationByPlatform(true);
       setResizable(false);

       textArea.setColumns(20);
       textArea.setRows(5);
       textArea.setPlaceholder("Paste Event IDs or NFHS links here. One ID/link per line.");
       scrollPane_textArea.setViewportView(textArea);
       
       confirmButton.setText("CONFIRM");
       confirmButton.addActionListener(new java.awt.event.ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
               confirmButtonActionPerformed(evt);
           }
       });

       javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
       getContentPane().setLayout(layout);
       layout.setHorizontalGroup(
           layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
           .addGroup(layout.createSequentialGroup()
               .addContainerGap()
               .addComponent(scrollPane_textArea)
               .addContainerGap())
           .addGroup(layout.createSequentialGroup()
               .addGap(160, 160, 160)
               .addComponent(confirmButton)
               .addContainerGap(160, Short.MAX_VALUE))
       );
       layout.setVerticalGroup(
           layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
           .addGroup(layout.createSequentialGroup()
               .addContainerGap()
               .addComponent(scrollPane_textArea, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
               .addComponent(confirmButton)
               .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
       );

       pack();
   }                    

   private void confirmButtonActionPerformed(java.awt.event.ActionEvent evt) {                                         
       // TODO add your handling code here:
	   String textFromSource = textArea.getText();
	   
	   sendTextToScript(textFromSource);
   }
   
   
   private void sendTextToScript(String textFromSource)
   {
	   new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				unityPost.inputIDs(textFromSource);
				return null;
			}
			
			@Override
			protected void done() {
				startBackendOperation();
			}
	   }.execute();
   }
   
   private void startBackendOperation()
   {
	   ProgressBarDialogBox pb = new ProgressBarDialogBox(UnityTaggerFrame.this) {
			@Override
			public void whenDone() {
				afterBackendOperation();
			}
	   };
	
	  
	   unityPost.execute(pb.getProgressBar());
	   
	   //TODO figure out why progress bar starts at 0 and doesn't end
	   
	   SwingUtilities.invokeLater(() -> pb.setVisible(true));
   }
   
   
   private void afterBackendOperation()
   {
	   //TODO show dialog boxes showing what had errors.
	   unityPost.getFailedToUpdate()
	   					.stream()
	   					.forEach(System.out::println);
   }
   
   
   /**
    * @param args the command line arguments
    */
   public static void main(String args[]) {
       /* Set the Nimbus look and feel */
       //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
       /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
        * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
        */
       try {
           for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
               if ("Nimbus".equals(info.getName())) {
                   javax.swing.UIManager.setLookAndFeel(info.getClassName());
                   break;
               }
           }
       } catch (ClassNotFoundException ex) {
           java.util.logging.Logger.getLogger(UnityTaggerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
       } catch (InstantiationException ex) {
           java.util.logging.Logger.getLogger(UnityTaggerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
       } catch (IllegalAccessException ex) {
           java.util.logging.Logger.getLogger(UnityTaggerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
       } catch (javax.swing.UnsupportedLookAndFeelException ex) {
           java.util.logging.Logger.getLogger(UnityTaggerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
       }
       
       
       /* Create and display the form */
       java.awt.EventQueue.invokeLater(new Runnable() {
           public void run() {
               new UnityTaggerFrame().setVisible(true);
           }
       });
   }

   // Variables declaration
   private MultiviewerTagScript unityPost;
   private javax.swing.JButton confirmButton;
   private javax.swing.JScrollPane scrollPane_textArea;
   private PlaceholderTextArea textArea;
   // End of variables declaration                   
}
