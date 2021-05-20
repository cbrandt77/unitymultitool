/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nfhsnetwork.unitytool.ui;

import java.awt.*;
import java.awt.Dialog;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import javax.swing.*;
import javax.swing.GroupLayout;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.*;

import com.nfhsnetwork.unitytool.common.StdPropertyChangeEvent;
import com.nfhsnetwork.unitytool.common.UnityContainer;
import com.nfhsnetwork.unitytool.common.UnityToolCommon;
import com.nfhsnetwork.unitytool.common.UnityContainer.ClubInventory;
import com.nfhsnetwork.unitytool.common.UnityContainer.ImportTypes;
import com.nfhsnetwork.unitytool.io.SSOLogin;
import com.nfhsnetwork.unitytool.logging.Debug;
import com.nfhsnetwork.unitytool.scripts.focuscompare.FocusCompareScript;
import com.nfhsnetwork.unitytool.scripts.focuscompare.FocusOutputFrame;
import com.nfhsnetwork.unitytool.scripts.multiviewertag.MultiviewerTagScript;
import com.nfhsnetwork.unitytool.ui.components.*;
import com.nfhsnetwork.unitytool.ui.components.ProgressBarDialogBox;
import com.nfhsnetwork.unitytool.ui.pixellotcsv.DragNDropCSV;
import com.nfhsnetwork.unitytool.utils.IOUtils;

/**
 *
 * @author calebbrandt
 */
public class ImportDataFrame extends javax.swing.JFrame {

	public static boolean DEBUGMODE = false;
	
	private ImportTypes importType = ImportTypes.OTHER;
	
    /**
     * Creates new form ImportDataFrame
     */
    public ImportDataFrame() {
        initComponents();
        this.button_mviewer.setEnabled(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dialog_helpPopup = new javax.swing.JDialog();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        hyperlinkLabel1 = new com.nfhsnetwork.unitytool.ui.components.HyperlinkLabel();
        jLabel12 = new javax.swing.JLabel();
        sp_textarea = new javax.swing.JScrollPane();
        placeholderTextArea1 = new com.nfhsnetwork.unitytool.ui.components.PlaceholderTextArea();
        p_sidebar = new javax.swing.JPanel();
        p_importtypecontainer = new javax.swing.JPanel();
        rb_it_focus = new javax.swing.JRadioButton();
        rb_it_other = new javax.swing.JRadioButton();
        p_buttonscontainer = new javax.swing.JPanel();
        p_importbuttoncontainer = new javax.swing.JPanel();
        button_import = new javax.swing.JButton();
        p_quickactionscontainer = new javax.swing.JPanel();
        button_focuscompare = new javax.swing.JButton();
        l_quickactions = new javax.swing.JLabel();
        button_mviewer = new javax.swing.JButton();
        menubar = new javax.swing.JMenuBar();
        menu_help = new javax.swing.JMenu();
        mi_helpfocuscmp = new javax.swing.JMenuItem();
        menu_about = new javax.swing.JMenu();

        dialog_helpPopup.setMinimumSize(new java.awt.Dimension(434, 549));

        jLabel1.setText("Note: Good-looking help section coming soon.");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Step 1: Select all rows of the focus events list");
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("that you wish to check, from the date column to");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Step 2: Copy the cells from the spreadsheet");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("the status column.");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("with CTRL+V (or CMD+V on MAC).");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Step 3: Paste the cells into the text field");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("and click \"FOCUS CMP\".");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Step 4: Download the Inventory CSV from Club");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("by clicking the download icon at the upper-right.");

        hyperlinkLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        hyperlinkLabel1.setText("https://club.pixellot.tv/inventory/systems");

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Step 5: Drop the CSV file onto the popup.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(hyperlinkLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(320, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hyperlinkLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel12)
                .addContainerGap(189, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout dialog_helpPopupLayout = new javax.swing.GroupLayout(dialog_helpPopup.getContentPane());
        dialog_helpPopup.getContentPane().setLayout(dialog_helpPopupLayout);
        dialog_helpPopupLayout.setHorizontalGroup(
            dialog_helpPopupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialog_helpPopupLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
                .addContainerGap())
        );
        dialog_helpPopupLayout.setVerticalGroup(
            dialog_helpPopupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialog_helpPopupLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Import Event Data");

        placeholderTextArea1.setColumns(20);
        placeholderTextArea1.setRows(5);
        placeholderTextArea1.setPlaceholder("Paste event data here.\nValid types: Focus sheet lines, game IDs, broadcasts IDs, URLs, etc.\nOne event per line.");
        sp_textarea.setViewportView(placeholderTextArea1);

        p_importtypecontainer.setBorder(javax.swing.BorderFactory.createTitledBorder("Import Type"));

        rb_it_focus.setFont(new java.awt.Font("Helvetica Neue", 0, 12)); // NOI18N
        rb_it_focus.setText("Focus");
        rb_it_focus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_it_focusActionPerformed(evt);
            }
        });

        rb_it_other.setFont(new java.awt.Font("Helvetica Neue", 0, 12)); // NOI18N
        rb_it_other.setSelected(true);
        rb_it_other.setText("Other");
        rb_it_other.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_it_otherActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout p_importtypecontainerLayout = new javax.swing.GroupLayout(p_importtypecontainer);
        p_importtypecontainer.setLayout(p_importtypecontainerLayout);
        p_importtypecontainerLayout.setHorizontalGroup(
            p_importtypecontainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_importtypecontainerLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(p_importtypecontainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rb_it_other)
                    .addComponent(rb_it_focus))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        p_importtypecontainerLayout.setVerticalGroup(
            p_importtypecontainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_importtypecontainerLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(rb_it_focus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rb_it_other)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        p_buttonscontainer.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        p_importbuttoncontainer.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        button_import.setText("IMPORT");
        button_import.setToolTipText("Open events in the Unity Tool");
        button_import.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_importActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout p_importbuttoncontainerLayout = new javax.swing.GroupLayout(p_importbuttoncontainer);
        p_importbuttoncontainer.setLayout(p_importbuttoncontainerLayout);
        p_importbuttoncontainerLayout.setHorizontalGroup(
            p_importbuttoncontainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_importbuttoncontainerLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(button_import, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        p_importbuttoncontainerLayout.setVerticalGroup(
            p_importbuttoncontainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_importbuttoncontainerLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(button_import)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        p_quickactionscontainer.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        button_focuscompare.setText("FOCUS CMP");
        button_focuscompare.setToolTipText("Compare Focus Event data against Console.");
        button_focuscompare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_focuscompareActionPerformed(evt);
            }
        });

        l_quickactions.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        l_quickactions.setText("Quick Actions");

        button_mviewer.setText("MVIEWER");
        button_mviewer.setEnabled(false);
        button_mviewer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_mviewerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout p_quickactionscontainerLayout = new javax.swing.GroupLayout(p_quickactionscontainer);
        p_quickactionscontainer.setLayout(p_quickactionscontainerLayout);
        p_quickactionscontainerLayout.setHorizontalGroup(
            p_quickactionscontainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_quickactionscontainerLayout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(p_quickactionscontainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(button_focuscompare, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(l_quickactions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button_mviewer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        p_quickactionscontainerLayout.setVerticalGroup(
            p_quickactionscontainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_quickactionscontainerLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(l_quickactions)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(button_focuscompare)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(button_mviewer)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout p_buttonscontainerLayout = new javax.swing.GroupLayout(p_buttonscontainer);
        p_buttonscontainer.setLayout(p_buttonscontainerLayout);
        p_buttonscontainerLayout.setHorizontalGroup(
            p_buttonscontainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_buttonscontainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(p_buttonscontainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(p_quickactionscontainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(p_importbuttoncontainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        p_buttonscontainerLayout.setVerticalGroup(
            p_buttonscontainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_buttonscontainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_importbuttoncontainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(p_quickactionscontainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout p_sidebarLayout = new javax.swing.GroupLayout(p_sidebar);
        p_sidebar.setLayout(p_sidebarLayout);
        p_sidebarLayout.setHorizontalGroup(
            p_sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_sidebarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(p_sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(p_importtypecontainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(p_buttonscontainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        p_sidebarLayout.setVerticalGroup(
            p_sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_sidebarLayout.createSequentialGroup()
                .addComponent(p_importtypecontainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(p_buttonscontainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menu_help.setText("Help");

        mi_helpfocuscmp.setText("Focus Compare");
        mi_helpfocuscmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi_helpfocuscmpActionPerformed(evt);
            }
        });
        menu_help.add(mi_helpfocuscmp);

        menubar.add(menu_help);

        menu_about.setText("About");
        menubar.add(menu_about);

        setJMenuBar(menubar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sp_textarea, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(p_sidebar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(p_sidebar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sp_textarea, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rb_it_otherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_it_otherActionPerformed
        rb_it_other.setSelected(true);
        rb_it_focus.setSelected(false);
        importType = UnityContainer.ImportTypes.OTHER;
    }//GEN-LAST:event_rb_it_otherActionPerformed
    
    private void rb_it_focusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_it_focusActionPerformed
        rb_it_other.setSelected(false);
        rb_it_focus.setSelected(true);
        importType = UnityContainer.ImportTypes.FOCUS;
    }//GEN-LAST:event_rb_it_focusActionPerformed

    
    
    
    private boolean hasEnoughText(final JTextArea textArea)
    {
    	return textArea.getText().length() >= 13;
    }
    
    
    private void button_importActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_importActionPerformed
    	
    	if (!hasEnoughText(placeholderTextArea1))
    	{
    		JOptionPane.showOptionDialog(this, "No data entered.", "Invalid Import", JOptionPane.DEFAULT_OPTION,
					JOptionPane.ERROR_MESSAGE, null, null, null);
    		return;
    	}

        UnityContainer.makeOrGetInstance();

        SwingWorker<Void, Void> importWorker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                UnityContainer.getInstance().importEventData(placeholderTextArea1.getText(), ImportDataFrame.this.importType);
                return null;
            }
        };

        //TODO show a Loading... dialog box that appears and then disappears

        SwingUtilities.invokeLater(() -> {
            disableAllComponents();

            UnityContainer.getInstance().addPropertyChangeListener((e) -> {
                Debug.out("[DEBUG] {button_importActionPerformed} action event fired");
                ImportDataFrame.this.setVisible(false);
                new MainWindow().setVisible(true);
                ImportDataFrame.this.dispose();
            });

            importWorker.execute();
        });
    }//GEN-LAST:event_button_importActionPerformed

    
    
    
    
    
    private FocusCompareScript fe;
    
    private void button_focuscompareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_focuscompareActionPerformed
    	if (!hasEnoughText(this.placeholderTextArea1))
    	{
    		JOptionPane.showOptionDialog(this, "No data entered.", "Invalid Import", JOptionPane.DEFAULT_OPTION,
					JOptionPane.ERROR_MESSAGE, null, null, null);
    		return;
    	}
    	
    	disableAllComponents();
        
        fe = new FocusCompareScript();
        importPxlCSV();
    }//GEN-LAST:event_button_focuscompareActionPerformed
    
    
    
    
    private void importPxlCSV()
    {
    	final Function<File, File> onFileSelected = (file) -> {
			executeFocusScript(file);
			if (file == null)
				Debug.out("[DEBUG] Script executed with null csv.");
			else
				Debug.out("[DEBUG] Script executed with csv: " + file.getAbsolutePath());
			return null;
		};
    	
    	
    	final Function<Void, Void> onCancel = (obj) -> {
    		enableAllComponents();
    		return null;
    	};
    	
    	SwingUtilities.invokeLater(() -> {
			new DragNDropCSV(SwingUtilities.windowForComponent(ImportDataFrame.this), "Import Pixellot CSV", Dialog.DEFAULT_MODALITY_TYPE, onFileSelected, onCancel)
					.setVisible(true);
		});
    }
    
	private void executeFocusScript(File f)
	{
		if (f != null)
		{
			parseClubCsv(f); // parses clubcsv, then calls this again with null param
			return;
		}
		
        SwingWorker<Integer, Void> setDataWorker = new SwingWorker<Integer, Void>() {
			@Override
			protected Integer doInBackground() throws Exception {
				Debug.out("[DEBUG] {doInBackground} SetDataWorker executed");
				return fe.setFocusData(ImportDataFrame.this.placeholderTextArea1.getText());
			}
			
			@Override
			protected void done()
			{
				try {
					Debug.out("[DEBUG] {executeFocusScript | done} finished nominally");
					afterSetFocusData(get());
				} catch (InterruptedException | ExecutionException e1) {
					Debug.out("[DEBUG] {executeFocusScript | done} finished with exception");
					e1.printStackTrace();
					afterSetFocusData(UnityToolCommon.FAILED);
				}
			}
        };
        
        SwingUtilities.invokeLater(() -> {
    		ProgressBarDialogBox pbdb = new ProgressBarDialogBox(SwingUtilities.windowForComponent(ImportDataFrame.this));
    		JProgressBar bar = pbdb.getProgressBar();
    		
    		pbdb.setLocationRelativeTo(ImportDataFrame.this);
    		
    		fe.addPropertyChangeListener((e) -> {
        		if (e.getPropertyName() == StdPropertyChangeEvent.PropertyType.PROGRESS)
        		{
        			//Debug.out("[DEBUG] {executeFocusScript} set value");
        			bar.setValue((Integer)e.getNewValue() * 100 / (Integer)e.getOldValue());
        		}
        		else if (e.getPropertyName() == StdPropertyChangeEvent.PropertyType.DONE)
        		{
        			SwingUtilities.invokeLater(() -> {
						pbdb.setVisible(false);
						pbdb.dispose();
					});
        		}
        	});
    		
    		Debug.out("[DEBUG] {executeFocusScript} setDataWorker executed");
		    setDataWorker.execute();
    		pbdb.setVisible(true);
		});
	}
	
    private void parseClubCsv(File f) 
    {
    	SwingWorker<Void, Void> parseClubCsvWorker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				Debug.out("[DEBUG] {doInBackground} parseclubcsvworker started");
				
				String s = IOUtils.readFromFile(f);
				
//				Debug.out("[DEBUG] {doInBackground} CSV file readout: " + s);
				
				int res = ClubInventory.parse(s);
				
				if (res == UnityToolCommon.FAILED)
				{
					SwingUtilities.invokeLater(() -> {
						JOptionPane.showOptionDialog(ImportDataFrame.this, "Failed to parse CSV. Executing focus script without.", "CSV Parse Failed", JOptionPane.DEFAULT_OPTION,
								JOptionPane.ERROR_MESSAGE, null, null, null);
					});
					
					return null;
				}
				
				return null;
			}
			
			@Override
			public void done() {
				Debug.out("[DEBUG] {parseclubcsv | done} club csv parse complete");
				executeFocusScript(null);
			}
		};
		
		SwingUtilities.invokeLater(() -> {
			parseClubCsvWorker.execute();
		});
	}

	private void afterSetFocusData(Integer x) 
    {
    	Debug.out("[DEBUG] {afterSetFocusData} method call");
    	if (x == UnityToolCommon.FAILED)
        {
        	JOptionPane.showOptionDialog(this, "Focus Compare failed.", "ERROR", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
					null, null, null);

			enableAllComponents();
			return;
			
        }
        else if (x == UnityToolCommon.SUCCESSFUL)
        {
        	SwingWorker<String, Void> compareWorker = new SwingWorker<String, Void>() {
        		@Override
        		public String doInBackground() throws IOException {
        			Debug.out("[DEBUG] {afterSetFocusData} {doInBackground} Comparing focus");
        			return fe.compareFocus();
        		}
        		
        		@Override
        		public void done()
        		{
        			Debug.out("[DEBUG] {afterSetFocusData} {done()} focus compare done");
        			String output;
        			try {
        				output = get();
        			}
        			catch (ExecutionException | InterruptedException e)
        			{
        				e.printStackTrace();
        				output = "Operation failed for some reason. Run this again through Powershell with the command \"debug=true UnityTool.exe --debug\" and send me the generated log through Slack.";
        			}
        			afterFocusScriptOperation(output);
        		}
        	};
        	compareWorker.execute();
        }
	}
    

	private void afterFocusScriptOperation(String s)
    {
		fe.firePropertyChangeEvent(StdPropertyChangeEvent.PropertyType.DONE, null, null);
    	SwingUtilities.invokeLater(() -> {
    		new FocusOutputFrame(s).setVisible(true);
    	});
    	
    }
	
	
    private void button_mviewerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_mviewerActionPerformed
    	
    	if (!hasEnoughText(this.placeholderTextArea1))
    	{
    		JOptionPane.showOptionDialog(this, "No data entered.", "Invalid Import", JOptionPane.DEFAULT_OPTION,
					JOptionPane.ERROR_MESSAGE, null, null, null);
    		return;
    	}
    	
    	
    	disableAllComponents();
        
        final ProgressBarDialogBox bar = new ProgressBarDialogBox(this);
        //bar.setVisible(true);
        bar.setAlwaysOnTop(true);
        
        final MultiviewerTagScript mts = new MultiviewerTagScript(this);
        
        
        
        
        
        mts.inputIDs(placeholderTextArea1.getText());
        
        final int total = placeholderTextArea1.getLineCount();
        
        mts.addPropertyChangeListener((e) -> {
        	if (e.getPropertyName().equals(StdPropertyChangeEvent.PropertyType.DONE.toString())) 
        	{
        		afterMultiviewerOperation();
        	}
        	else if (e.getPropertyName().equals(StdPropertyChangeEvent.PropertyType.PROGRESS.toString()))
        	{
        		bar.getProgressBar().setValue((Integer)e.getNewValue() * 100 / total);
        	}
        });
        
        SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				mts.startTagOperation();
				return null;
			}
        };
        
        //TODO figure out loading bar
        SwingUtilities.invokeLater(() -> {
        	SSOLogin.showLoginDialog(
        			SwingUtilities.windowForComponent(ImportDataFrame.this),
        			(v) -> {
        				sw.execute();
        	        	bar.setVisible(true);
        				return null;
        			});
        });
        
    }//GEN-LAST:event_button_mviewerActionPerformed

    private void mi_helpfocuscmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mi_helpfocuscmpActionPerformed
        SwingUtilities.invokeLater(() -> {
            dialog_helpPopup.setLocationRelativeTo(ImportDataFrame.this);
            Debug.out("Focus Help clicked.");
            dialog_helpPopup.setVisible(true);
        });
    }//GEN-LAST:event_mi_helpfocuscmpActionPerformed
    
    
    private void afterMultiviewerOperation()
    {
    	int res = JOptionPane.showOptionDialog(this, "All games tagged for multiviewer.", "Task Complete.", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
    	if (res != 4) {
    		System.exit(0);
    	}
    }
    
    
    

    
    protected void disableAllComponents() {
    	this.setEnabled(false);
    	rb_it_other.setEnabled(false);
    	rb_it_focus.setEnabled(false);
    	placeholderTextArea1.setEnabled(false);
    	this.button_focuscompare.setEnabled(false);
    	this.button_import.setEnabled(false);
    	this.button_mviewer.setEnabled(false);
    	this.sp_textarea.setEnabled(false);
    }
    
    protected void enableAllComponents() {
    	this.setEnabled(true);
    	rb_it_other.setEnabled(true);
    	rb_it_focus.setEnabled(true);
    	placeholderTextArea1.setEnabled(true);
    	this.button_focuscompare.setEnabled(true);
    	this.button_import.setEnabled(true);
    	this.button_mviewer.setEnabled(true); //TODO
    	this.sp_textarea.setEnabled(true);
    }
    
   
    
    
    
   
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_focuscompare;
    private javax.swing.JButton button_import;
    private javax.swing.JButton button_mviewer;
    private javax.swing.JDialog dialog_helpPopup;
    private com.nfhsnetwork.unitytool.ui.components.HyperlinkLabel hyperlinkLabel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel l_quickactions;
    private javax.swing.JMenu menu_about;
    private javax.swing.JMenu menu_help;
    private javax.swing.JMenuBar menubar;
    private javax.swing.JMenuItem mi_helpfocuscmp;
    private javax.swing.JPanel p_buttonscontainer;
    private javax.swing.JPanel p_importbuttoncontainer;
    private javax.swing.JPanel p_importtypecontainer;
    private javax.swing.JPanel p_quickactionscontainer;
    private javax.swing.JPanel p_sidebar;
    private com.nfhsnetwork.unitytool.ui.components.PlaceholderTextArea placeholderTextArea1;
    private javax.swing.JRadioButton rb_it_focus;
    private javax.swing.JRadioButton rb_it_other;
    private javax.swing.JScrollPane sp_textarea;
    // End of variables declaration//GEN-END:variables
}
