/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nfhsnetwork.calebsunitytool.ui;

import com.nfhsnetwork.calebsunitytool.common.UnityContainer;
import com.nfhsnetwork.calebsunitytool.types.NullNFHSObject;
import com.nfhsnetwork.calebsunitytool.ui.pixellotcsv.ImportCSVDialog;
import com.nfhsnetwork.calebsunitytool.utils.Util;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Caleb Brandt
 */

class MainWindow extends javax.swing.JFrame {

	protected UIController uic;
	protected MainWindowFields mwf;
	
    /**
     * Creates new form EventDetailsPane
     */
    public MainWindow() {
    	mwf = new MainWindowFields();
    	uic = new UIController(this);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        loginDialog = new javax.swing.JDialog();
        field_email = new com.nfhsnetwork.calebsunitytool.ui.components.PlaceholderTextField();
        field_password = new javax.swing.JPasswordField();
        l_email = new javax.swing.JLabel();
        l_password = new javax.swing.JLabel();
        panel_main = new javax.swing.JPanel();
        container_eventSpecific = new javax.swing.JPanel();
        tabPanel = new javax.swing.JTabbedPane();
        eventDetailsPane1 = new com.nfhsnetwork.calebsunitytool.ui.EventDetailsPane();
        productionTabPane1 = new com.nfhsnetwork.calebsunitytool.ui.ProductionTabPane();
        pixellotTabPane1 = new com.nfhsnetwork.calebsunitytool.ui.PixellotTabPane();
        panel_title = new javax.swing.JPanel();
        label_gameTitle = new javax.swing.JLabel();
        gameListContainer = new com.nfhsnetwork.calebsunitytool.ui.GameListContainer(uic);
        mb_topMenu = new javax.swing.JMenuBar();
        menu_file = new javax.swing.JMenu();
        menuItem_dologin = new javax.swing.JMenuItem();
        menuItem_importNewData = new javax.swing.JMenuItem();
        menuItem_openPixellotCSV = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuItem_exportToFocus = new javax.swing.JMenuItem();
        menu_edit = new javax.swing.JMenu();
        menuItemCB_enableEditing = new javax.swing.JCheckBoxMenuItem();
        menu_view = new javax.swing.JMenu();
        menu_subView_gameList = new javax.swing.JMenu();
        view_gameList_eventID = new javax.swing.JRadioButtonMenuItem();
        view_gameList_bdcID = new javax.swing.JRadioButtonMenuItem();
        view_gameList_title = new javax.swing.JRadioButtonMenuItem();
        menuItem_refresh = new javax.swing.JMenuItem();

        loginDialog.setResizable(false);

        field_password.setText("jPasswordField1");

        l_email.setText("Email Address");

        l_password.setText("Password");

        field_email.setPlaceholder("Email Address");

        javax.swing.GroupLayout loginDialogLayout = new javax.swing.GroupLayout(loginDialog.getContentPane());
        loginDialog.getContentPane().setLayout(loginDialogLayout);
        loginDialogLayout.setHorizontalGroup(
            loginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginDialogLayout.createSequentialGroup()
                .addContainerGap(94, Short.MAX_VALUE)
                .addGroup(loginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(l_password)
                    .addComponent(l_email)
                    .addGroup(loginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(field_email, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(field_password, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)))
                .addGap(84, 84, 84))
        );
        loginDialogLayout.setVerticalGroup(
            loginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginDialogLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(l_email)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(field_email, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(l_password)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(field_password, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(65, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Unity Tool - by Caleb Brandt");
        setLocationByPlatform(true);
        setName("UnityToolWindow"); // NOI18N
        setResizable(false);

        tabPanel.addTab("Event", eventDetailsPane1);
        tabPanel.addTab("Production", productionTabPane1);
        pixellotTabPane1.setVisible(true);
        tabPanel.addTab("Pixellot", pixellotTabPane1);

        panel_title.setBorder(javax.swing.BorderFactory.createTitledBorder("Title"));
        panel_title.setDoubleBuffered(false);
        panel_title.setFocusable(false);
        panel_title.setRequestFocusEnabled(false);
        panel_title.setVerifyInputWhenFocusTarget(false);

        label_gameTitle.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        label_gameTitle.setText("Varsity Football | Loyola Academy vs. St. Rita High School");

        javax.swing.GroupLayout panel_titleLayout = new javax.swing.GroupLayout(panel_title);
        panel_title.setLayout(panel_titleLayout);
        panel_titleLayout.setHorizontalGroup(
            panel_titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_titleLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(label_gameTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 602, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panel_titleLayout.setVerticalGroup(
            panel_titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_titleLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(label_gameTitle)
                .addGap(3, 3, 3))
        );

        javax.swing.GroupLayout container_eventSpecificLayout = new javax.swing.GroupLayout(container_eventSpecific);
        container_eventSpecific.setLayout(container_eventSpecificLayout);
        container_eventSpecificLayout.setHorizontalGroup(
            container_eventSpecificLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(container_eventSpecificLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(container_eventSpecificLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, container_eventSpecificLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(tabPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 616, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panel_title, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        container_eventSpecificLayout.setVerticalGroup(
            container_eventSpecificLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(container_eventSpecificLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panel_mainLayout = new javax.swing.GroupLayout(panel_main);
        panel_main.setLayout(panel_mainLayout);
        panel_mainLayout.setHorizontalGroup(
            panel_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_mainLayout.createSequentialGroup()
                .addComponent(gameListContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(container_eventSpecific, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panel_mainLayout.setVerticalGroup(
            panel_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(container_eventSpecific, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(gameListContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        menu_file.setText("File");

        menuItem_dologin.setText("Login to Unity");
        menuItem_dologin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_dologinActionPerformed(evt);
            }
        });
        menu_file.add(menuItem_dologin);

        menuItem_importNewData.setText("Import New Data");
        menuItem_importNewData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_importNewDataActionPerformed(evt);
            }
        });
        menuItem_importNewData.setVisible(false);
        menu_file.add(menuItem_importNewData);

        menuItem_openPixellotCSV.setText("Import Pixellot CSV");
        menuItem_openPixellotCSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_openPixellotCSVActionPerformed(evt);
            }
        });
        menu_file.add(menuItem_openPixellotCSV);
        menu_file.add(jSeparator1);

        menuItem_exportToFocus.setText("Export as Focus List Sheet");
        menuItem_exportToFocus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_exportToFocusActionPerformed(evt);
            }
        });
        menuItem_exportToFocus.setVisible(false);
        menu_file.add(menuItem_exportToFocus);

        mb_topMenu.add(menu_file);

        menu_edit.setText("Edit");

        menuItemCB_enableEditing.setText("Enable Editing");
        menuItemCB_enableEditing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemCB_enableEditingActionPerformed(evt);
            }
        });
        menu_edit.add(menuItemCB_enableEditing);

        menu_edit.setVisible(false);

        mb_topMenu.add(menu_edit);

        menu_view.setText("View");

        menu_subView_gameList.setText("Game List");

        view_gameList_eventID.setSelected(true);
        view_gameList_eventID.setText("Event ID");
        view_gameList_eventID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                view_gameList_eventIDActionPerformed(evt);
            }
        });
        menu_subView_gameList.add(view_gameList_eventID);

        view_gameList_bdcID.setSelected(true);
        view_gameList_bdcID.setText("Broadcast ID");
        view_gameList_bdcID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                view_gameList_bdcIDActionPerformed(evt);
            }
        });
        menu_subView_gameList.add(view_gameList_bdcID);

        view_gameList_title.setSelected(true);
        view_gameList_title.setText("Title");
        view_gameList_title.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                view_gameList_titleActionPerformed(evt);
            }
        });
        menu_subView_gameList.add(view_gameList_title);

        menu_view.add(menu_subView_gameList);

        menuItem_refresh.setText("Refresh Data");
        menuItem_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_refreshActionPerformed(evt);
            }
        });
        menu_view.add(menuItem_refresh);

        menu_view.setVisible(false);

        mb_topMenu.add(menu_view);

        setJMenuBar(mb_topMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_main, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_main, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    

    private void menuItem_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_refreshActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuItem_refreshActionPerformed

    private void menuItem_importNewDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_importNewDataActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuItem_importNewDataActionPerformed

    private void menuItem_openPixellotCSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_openPixellotCSVActionPerformed
        Function<File, File> fileCallback = (f) -> {
        	try {
				UnityContainer.ClubInventory.parse(Util.IOUtils.readFromFile(f));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				UnityContainer.ClubInventory.clear();
				
				SwingUtilities.invokeLater(() -> {
					JOptionPane.showOptionDialog(this, "Failed to parse Pixellot CSV.", "Import Failed", JOptionPane.DEFAULT_OPTION,
							JOptionPane.ERROR_MESSAGE, null, null, null);
				});
			}
        	return f;
        };
        
        SwingUtilities.invokeLater(() -> {
			new ImportCSVDialog(this, fileCallback).setVisible(true);
		});
        
    }//GEN-LAST:event_menuItem_openPixellotCSVActionPerformed

    private void menuItem_exportToFocusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_exportToFocusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuItem_exportToFocusActionPerformed

    private void menuItemCB_enableEditingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemCB_enableEditingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuItemCB_enableEditingActionPerformed

    private void view_gameList_eventIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_view_gameList_eventIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_view_gameList_eventIDActionPerformed

    private void view_gameList_bdcIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_view_gameList_bdcIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_view_gameList_bdcIDActionPerformed

    private void view_gameList_titleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_view_gameList_titleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_view_gameList_titleActionPerformed

    private void menuItem_dologinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_dologinActionPerformed
        javax.swing.SwingUtilities.invokeLater(() -> loginDialog.setVisible(true));
    }//GEN-LAST:event_menuItem_dologinActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel container_eventSpecific;
    protected com.nfhsnetwork.calebsunitytool.ui.EventDetailsPane eventDetailsPane1;
    private com.nfhsnetwork.calebsunitytool.ui.components.PlaceholderTextField field_email;
    private javax.swing.JPasswordField field_password;
    private com.nfhsnetwork.calebsunitytool.ui.GameListContainer gameListContainer;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JLabel l_email;
    private javax.swing.JLabel l_password;
    private javax.swing.JLabel label_gameTitle;
    private javax.swing.JDialog loginDialog;
    private javax.swing.JMenuBar mb_topMenu;
    private javax.swing.JCheckBoxMenuItem menuItemCB_enableEditing;
    private javax.swing.JMenuItem menuItem_dologin;
    private javax.swing.JMenuItem menuItem_exportToFocus;
    private javax.swing.JMenuItem menuItem_importNewData;
    private javax.swing.JMenuItem menuItem_openPixellotCSV;
    private javax.swing.JMenuItem menuItem_refresh;
    private javax.swing.JMenu menu_edit;
    private javax.swing.JMenu menu_file;
    private javax.swing.JMenu menu_subView_gameList;
    private javax.swing.JMenu menu_view;
    private javax.swing.JPanel panel_main;
    private javax.swing.JPanel panel_title;
    protected com.nfhsnetwork.calebsunitytool.ui.PixellotTabPane pixellotTabPane1;
    protected com.nfhsnetwork.calebsunitytool.ui.ProductionTabPane productionTabPane1;
    private javax.swing.JTabbedPane tabPanel;
    private javax.swing.JRadioButtonMenuItem view_gameList_bdcID;
    private javax.swing.JRadioButtonMenuItem view_gameList_eventID;
    private javax.swing.JRadioButtonMenuItem view_gameList_title;
    // End of variables declaration//GEN-END:variables
	
    
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
        //</editor-fold>
        
        // [DEBUG]
        UnityContainer.makeOrGetInstance();
        UnityContainer.getInstance().getEventMap().put("gam1234567890", new NullNFHSObject("gam1234567890"));
        UnityContainer.getInstance().getEventMap().put("gam1234567890", new NullNFHSObject("gam1234567890"));
        UnityContainer.getInstance().getEventMap().put("gam1234567890", new NullNFHSObject("gam1234567890"));
        UnityContainer.getInstance().getEventMap().put("gam1234567890", new NullNFHSObject("gam1234567890"));
        
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
			public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }
    
//    @FunctionalInterface
//	public interface DataChangeListener 
//	{
//		public void onEvent();
//	}
    
    
    
    private static final String startTimeDTFPattern = "MMM dd, yyyy - hh:mm a";
    
    @SuppressWarnings("unused")
    class MainWindowFields
    {
        private String gender;
        private String prodType;
        private String onairstatus;
        private String gameid;
        private String bdcid;
        private LocalDateTime starttime;
        private List<String> eventTags;
        private List<String> participants;
		
    	private String ed_level;
    	private String ed_sport;
    	private String ed_type;
    	private String ed_location;
    	private String ed_redirect;
    	
    	private String pxl_status;
    	private String pxl_lminame;
    	private String pxl_clubname;
    	private String pxl_sfname;
    	private String pxl_currentCal;
    	private String pxl_acctmgr;
    	private String pxl_clubeventurl;
    	private String pxl_clubsystemurl;
    	private String pxl_clubeventstatus;
    	private String pxl_clubeventid;
    	
    	private String prod_hlsstatus;
    	private String prod_producer;
    	private String prod_publisher;
    	private String prod_mgr_name;
    	private String prod_mgr_phone;
    	private String prod_focustype;
    	
    	private String prod_monitoring_;
    	//TODO figure out monitoring details
    	// can get from STATS json
    	
    	//TODO remove all of these variables cause I really don't even need them
    	
    	
    	protected void setTitle(String title)
    	{
    		MainWindow.this.label_gameTitle.setText(title);
    	}

    	protected void setEventTags(JSONArray eventTags)
    	{
    		if (eventTags == null)
    		{
    			eventDetailsPane1.data_eventtags.setText("");
    		}
    		
    		StringBuilder sb = new StringBuilder();
    		sb.append("[ ");
    		for (int i = 0, length = eventTags.length(); i < length; i++)
    		{
    			String e = eventTags.getString(i);
    			sb.append("\"" + e + "\"");
    			sb.append(", ");
        		 //TODO fix extra comma
    		}
    		sb.append("]");
    		eventDetailsPane1.data_eventtags.setText(sb.toString());
    	}
    	
		protected void setGender(String gender) {
			this.gender = gender;
			eventDetailsPane1.l_gender_data.setText(gender);
		}

		protected void setProdType(String prodType) {
			this.prodType = prodType;
			
			eventDetailsPane1.label_producerType_data.setText(prodType);
		}

		protected void setOnairstatus(String onairstatus) {
			this.onairstatus = onairstatus;
			eventDetailsPane1.label_status_field.setText(onairstatus);
			productionTabPane1.l_onAirStatus_data.setText(onairstatus);
		}

		protected void setGameid(String gameid) {
			this.gameid = gameid;
			eventDetailsPane1.label_eventID_data.setText(gameid);
		}

		protected void setBdcid(String bdcid) {
			this.bdcid = bdcid;
			eventDetailsPane1.tf_bdcID_field.setText(bdcid);
			
		}
		
		protected void setStarttime(LocalDateTime localDateTime) {
			if (localDateTime == null)
			{
				eventDetailsPane1.label_startTime_data.setText("");
				return;
			}
			
			this.starttime = localDateTime;
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(startTimeDTFPattern);
			
			eventDetailsPane1.label_startTime_data.setText(localDateTime.format(dtf));
			
		}

		protected void setEd_level(String ed_level) {
			this.ed_level = ed_level;
			eventDetailsPane1.l_level_data.setText(ed_level);
		}

		protected void setParticipants(JSONArray list)
		{
			eventDetailsPane1.clearParticipants();
			
			if (list == null)
				return;
			
			for (int i = 0, length = list.length(); i < length; i++)
			{
				JSONObject team = list.getJSONObject(i);
				
				String name = null;
				try {
					name = team.getString("name");
				} catch (JSONException e3) {
					e3.printStackTrace();
				}
				
				String city = null;
				try {
					city = team.getString("city");
				} catch (JSONException e2) {
					e2.printStackTrace();
				}
				
				String state = null;
				try {
					state = team.getString("state_code");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				
				String assoc = null;
				try {
					assoc = team.getString("state_association_acronym");
				} catch (JSONException e) {
					//e.printStackTrace();
				}
				
				eventDetailsPane1.addParticipant(name, city + ", " + state, assoc, "P" + (i + 1));
			}
			//eventDetailsPane1.p_participants_container.repaint();
		}

		protected void setEd_sport(String ed_sport) {
			this.ed_sport = ed_sport;
			eventDetailsPane1.l_sport_data.setText(ed_sport);
			
		}

		protected void setEd_type(String ed_type) {
			this.ed_type = ed_type;
			eventDetailsPane1.l_type_data.setText(ed_type);
			
		}

		protected void setEd_location(String ed_location) {
			this.ed_location = ed_location;
			eventDetailsPane1.label_location_data.setText(ed_location);
		}

		protected void setEd_redirect(String ed_redirect) {
			this.ed_redirect = ed_redirect;
			eventDetailsPane1.label_redirect_data.setText(ed_redirect);
		}

		protected void setPxl_status(String pxl_status) {
			this.pxl_status = pxl_status;
			pixellotTabPane1.tag_unitStatus.setText(pxl_status);
		}

		protected void setPxl_lminame(String pxl_lminame) {
			this.pxl_lminame = pxl_lminame;
			pixellotTabPane1.data_lminame.setText(pxl_lminame);
		}

		protected void setPxl_clubname(String pxl_clubname) {
			this.pxl_clubname = pxl_clubname;
			pixellotTabPane1.data_clubname.setText(pxl_clubname);
		}

		protected void setPxl_sfname(String pxl_sfname) {
			this.pxl_sfname = pxl_sfname;
			pixellotTabPane1.data_sfname.setText(pxl_sfname);
		}

		protected void setPxl_currentCal(String pxl_currentCal) {
			this.pxl_currentCal = pxl_currentCal;
			//TODO
		}

		protected void setPxl_acctmgr(String pxl_acctmgr) {
			this.pxl_acctmgr = pxl_acctmgr;
			//TODO
		}

		protected void setPxl_clubeventurl(String pxl_clubeventurl) {
			this.pxl_clubeventurl = pxl_clubeventurl;
			pixellotTabPane1.data_clubEventLink.setText(pxl_clubeventurl);
		}

		protected void setPxl_clubsystemurl(String pxl_clubsystemurl) {
			this.pxl_clubsystemurl = pxl_clubsystemurl;
			//TODO
		}

		protected void setPxl_clubeventstatus(String pxl_clubeventstatus) {
			this.pxl_clubeventstatus = pxl_clubeventstatus;
			pixellotTabPane1.data_clubEventStatus.setText(pxl_clubeventstatus);
		}

		protected void setPxl_clubeventid(String pxl_clubeventid) {
			this.pxl_clubeventid = pxl_clubeventid;
			pixellotTabPane1.data_clubEventID.setText(pxl_clubeventid);
		}

		protected void setProd_hlsstatus(String prod_hlsstatus) {
			this.prod_hlsstatus = prod_hlsstatus;
			productionTabPane1.l_hlsStatus_data.setText(prod_hlsstatus);
		}

		protected void setProd_producer(String prod_producer) {
			this.prod_producer = prod_producer;
			//TODO
		}

		protected void setProd_publisher(String prod_publisher) {
			this.prod_publisher = prod_publisher;
			//TODO
		}

		protected void setProd_mgr_name(String prod_mgr_name) {
			this.prod_mgr_name = prod_mgr_name;
			productionTabPane1.label_tm_name.setText(prod_mgr_name);
		}

		protected void setProd_mgr_phone(String prod_mgr_phone) {
			this.prod_mgr_phone = prod_mgr_phone;
			productionTabPane1.label_tm_number.setText(prod_mgr_phone);
		}

		protected void setProd_focustype(String prod_focustype) {
			this.prod_focustype = prod_focustype;
			productionTabPane1.l_focusType_data.setText(prod_focustype);
		}

		
    	
    	
    	
    }
}
	
    

    

    
    

