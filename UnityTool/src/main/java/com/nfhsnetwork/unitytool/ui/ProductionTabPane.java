/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nfhsnetwork.unitytool.ui;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
import javax.swing.border.*;

import com.nfhsnetwork.unitytool.utils.Util.TimeUtils;

/**
 *
 * @author impro_000
 */
public class ProductionTabPane extends javax.swing.JPanel {

    /**
     * Creates new form ProductionTabPane
     */
    public ProductionTabPane() {
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

        jFrame1 = new javax.swing.JFrame();
        tab_productionInfo = new javax.swing.JPanel();
        panel_territoryManager = new javax.swing.JPanel();
        label_tm_name = new javax.swing.JLabel();
        label_tm_number = new javax.swing.JLabel();
        p_details = new javax.swing.JPanel();
        l_focusType_tag = new javax.swing.JLabel();
        l_focusType_data = new javax.swing.JLabel();
        l_productionType_tag = new javax.swing.JLabel();
        l_productionType_data = new javax.swing.JLabel();
        l_hlsStatus_tag = new javax.swing.JLabel();
        l_onAirStatus_tag = new javax.swing.JLabel();
        l_hlsStatus_data = new javax.swing.JLabel();
        l_onAirStatus_data = new javax.swing.JLabel();
        p_prodhistory = new javax.swing.JPanel();
        scroll_prodhistory = new javax.swing.JScrollPane();
        p_prodhistory_listcontainer = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        p_prodInfo = new javax.swing.JPanel();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        panel_territoryManager.setBorder(javax.swing.BorderFactory.createTitledBorder("Territory Manager"));

        label_tm_name.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        label_tm_name.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_tm_name.setText("Ben Halpern");

        label_tm_number.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        label_tm_number.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_tm_number.setText("(770) 555-1234");

        javax.swing.GroupLayout panel_territoryManagerLayout = new javax.swing.GroupLayout(panel_territoryManager);
        panel_territoryManager.setLayout(panel_territoryManagerLayout);
        panel_territoryManagerLayout.setHorizontalGroup(
            panel_territoryManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_territoryManagerLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panel_territoryManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(label_tm_name, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                    .addComponent(label_tm_number, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_territoryManagerLayout.setVerticalGroup(
            panel_territoryManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_territoryManagerLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(label_tm_name)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(label_tm_number)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        p_details.setBorder(javax.swing.BorderFactory.createTitledBorder("Details"));

        l_focusType_tag.setText("Focus Type:");

        l_focusType_data.setText("State Assoc.");

        l_productionType_tag.setText("Production Type:");

        l_productionType_data.setText("Producer/RTMP");

        l_hlsStatus_tag.setText("HLS Status:");

        l_onAirStatus_tag.setText("On Air Status:");

        l_hlsStatus_data.setText("HLSStreaming");

        l_onAirStatus_data.setText("Complete");

        javax.swing.GroupLayout p_detailsLayout = new javax.swing.GroupLayout(p_details);
        p_details.setLayout(p_detailsLayout);
        p_detailsLayout.setHorizontalGroup(
            p_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_detailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(p_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(l_onAirStatus_tag, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(l_hlsStatus_tag, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(l_productionType_tag, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(l_focusType_tag, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(p_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(l_productionType_data, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addComponent(l_focusType_data, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(l_hlsStatus_data, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(l_onAirStatus_data, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        p_detailsLayout.setVerticalGroup(
            p_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_detailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(p_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_focusType_tag)
                    .addComponent(l_focusType_data))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_productionType_tag)
                    .addComponent(l_productionType_data))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_hlsStatus_tag)
                    .addComponent(l_hlsStatus_data))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_onAirStatus_tag)
                    .addComponent(l_onAirStatus_data))
                .addContainerGap())
        );

        p_prodhistory.setBorder(javax.swing.BorderFactory.createTitledBorder("History"));

        scroll_prodhistory.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        scroll_prodhistory.setMinimumSize(new java.awt.Dimension(471, 66));

        p_prodhistory_listcontainer.setMinimumSize(new java.awt.Dimension(471, 66));

        jLabel1.setText("Coming Soon!");
        p_prodhistory_listcontainer.add(jLabel1);

        scroll_prodhistory.setViewportView(p_prodhistory_listcontainer);

        javax.swing.GroupLayout p_prodhistoryLayout = new javax.swing.GroupLayout(p_prodhistory);
        p_prodhistory.setLayout(p_prodhistoryLayout);
        p_prodhistoryLayout.setHorizontalGroup(
            p_prodhistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_prodhistoryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scroll_prodhistory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        p_prodhistoryLayout.setVerticalGroup(
            p_prodhistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_prodhistoryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scroll_prodhistory, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                .addContainerGap())
        );

        p_prodInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Producer"));

        javax.swing.GroupLayout p_prodInfoLayout = new javax.swing.GroupLayout(p_prodInfo);
        p_prodInfo.setLayout(p_prodInfoLayout);
        p_prodInfoLayout.setHorizontalGroup(
            p_prodInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 161, Short.MAX_VALUE)
        );
        p_prodInfoLayout.setVerticalGroup(
            p_prodInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 405, Short.MAX_VALUE)
        );

        p_prodInfo.setVisible(false); //TODO

        javax.swing.GroupLayout tab_productionInfoLayout = new javax.swing.GroupLayout(tab_productionInfo);
        tab_productionInfo.setLayout(tab_productionInfoLayout);
        tab_productionInfoLayout.setHorizontalGroup(
            tab_productionInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_productionInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tab_productionInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(p_prodhistory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tab_productionInfoLayout.createSequentialGroup()
                        .addComponent(p_details, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panel_territoryManager, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(p_prodInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        tab_productionInfoLayout.setVerticalGroup(
            tab_productionInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_productionInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tab_productionInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(p_prodInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tab_productionInfoLayout.createSequentialGroup()
                        .addGroup(tab_productionInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(p_details, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panel_territoryManager, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(p_prodhistory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tab_productionInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tab_productionInfo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JFrame jFrame1;
    javax.swing.JLabel jLabel1;
    javax.swing.JLabel l_focusType_data;
    javax.swing.JLabel l_focusType_tag;
    javax.swing.JLabel l_hlsStatus_data;
    javax.swing.JLabel l_hlsStatus_tag;
    javax.swing.JLabel l_onAirStatus_data;
    javax.swing.JLabel l_onAirStatus_tag;
    javax.swing.JLabel l_productionType_data;
    javax.swing.JLabel l_productionType_tag;
    javax.swing.JLabel label_tm_name;
    javax.swing.JLabel label_tm_number;
    javax.swing.JPanel p_details;
    javax.swing.JPanel p_prodInfo;
    javax.swing.JPanel p_prodhistory;
    javax.swing.JPanel p_prodhistory_listcontainer;
    javax.swing.JPanel panel_territoryManager;
    javax.swing.JScrollPane scroll_prodhistory;
    javax.swing.JPanel tab_productionInfo;
    // End of variables declaration//GEN-END:variables

    
    
    
    
    private static final DateTimeFormatter d = DateTimeFormatter.ofPattern("MM-dd-YYYY");
    private static final DateTimeFormatter t = DateTimeFormatter.ofPattern("h:mm a");
    
    protected class ProdHistoryEntry extends javax.swing.JPanel {
        
        public ProdHistoryEntry(long epoch, String status, String email) 
        {
            LocalDateTime date = TimeUtils.convertEpochSecondToEST(epoch);
            
        	l_date = new javax.swing.JLabel();
            l_time = new javax.swing.JLabel();
            l_status = new javax.swing.JLabel();
            l_email = new javax.swing.JLabel();
        	
        	l_date.setText(date.format(d));
            l_time.setText(date.format(t));
            l_status.setText("Status: " + status);
            l_email.setText(email);
        	
            initComponents();
        }

        private void initComponents() {

            prodhistory_entry_template = new javax.swing.JPanel();
            

            setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

            

            javax.swing.GroupLayout prodhistory_entry_templateLayout = new javax.swing.GroupLayout(prodhistory_entry_template);
            prodhistory_entry_template.setLayout(prodhistory_entry_templateLayout);
            prodhistory_entry_templateLayout.setHorizontalGroup(
                prodhistory_entry_templateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(prodhistory_entry_templateLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(prodhistory_entry_templateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(l_date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(l_time, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(prodhistory_entry_templateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(l_email, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                        .addComponent(l_status, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap(26, Short.MAX_VALUE))
            );
            prodhistory_entry_templateLayout.setVerticalGroup(
                prodhistory_entry_templateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(prodhistory_entry_templateLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(prodhistory_entry_templateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(l_date)
                        .addComponent(l_status))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(prodhistory_entry_templateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(l_time)
                        .addComponent(l_email))
                    .addContainerGap())
            );

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
            this.setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(prodhistory_entry_template, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(prodhistory_entry_template, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            );
        }



        private javax.swing.JLabel l_date;
        private javax.swing.JLabel l_time;
        private javax.swing.JLabel l_status;
        private javax.swing.JLabel l_email;
        private javax.swing.JPanel prodhistory_entry_template;
    }


}
