/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nfhsnetwork.calebsunitytool.ui;

/**
 *
 * @author impro_000
 */
public class PixellotTabPane extends javax.swing.JPanel {

    /**
     * Creates new form PixellotTabPane
     */
    public PixellotTabPane() {
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

        tab_pixellotDetails = new javax.swing.JPanel();
        panel_pixellotInfo = new javax.swing.JPanel();
        l_lmiName_tag = new javax.swing.JLabel();
        data_lminame = new javax.swing.JLabel();
        tag_prodname = new javax.swing.JLabel();
        data_prodname = new javax.swing.JLabel();
        tag_clubname = new javax.swing.JLabel();
        tag_sfname = new javax.swing.JLabel();
        data_clubname = new javax.swing.JLabel();
        data_sfname = new javax.swing.JLabel();
        p_eventInfo = new javax.swing.JPanel();
        tag_consoleEventID = new javax.swing.JLabel();
        tag_clubEventID = new javax.swing.JLabel();
        tag_clubEventStatus = new javax.swing.JLabel();
        tag_clubEventLink = new javax.swing.JLabel();
        data_consoleEventID = new com.nfhsnetwork.calebsunitytool.ui.components.EditableLabel();
        data_clubEventID = new com.nfhsnetwork.calebsunitytool.ui.components.EditableLabel();
        data_clubEventStatus = new com.nfhsnetwork.calebsunitytool.ui.components.EditableLabel();
        data_clubEventLink = new com.nfhsnetwork.calebsunitytool.ui.components.EditableLabel();
        p_sfinfo = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        p_unitinfo = new javax.swing.JPanel();
        tag_unitStatus = new javax.swing.JLabel();
        data_unitStatus = new javax.swing.JLabel();
        tag_version = new javax.swing.JLabel();
        data_version = new javax.swing.JLabel();

        panel_pixellotInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Names"));

        l_lmiName_tag.setText("LMI Name:");

        data_lminame.setText("Coming soon!");

        tag_prodname.setText("Producer Name:");

        data_prodname.setText("NHSAA - Sample Academy, Provo, Utah - Gym");

        tag_clubname.setText("Club Name:");

        tag_sfname.setText("SF Name:");

        data_clubname.setText("Requires CSV Import");

        data_sfname.setText("Coming soon!");

        javax.swing.GroupLayout panel_pixellotInfoLayout = new javax.swing.GroupLayout(panel_pixellotInfo);
        panel_pixellotInfo.setLayout(panel_pixellotInfoLayout);
        panel_pixellotInfoLayout.setHorizontalGroup(
            panel_pixellotInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_pixellotInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_pixellotInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(l_lmiName_tag)
                    .addComponent(tag_clubname)
                    .addComponent(tag_prodname)
                    .addComponent(tag_sfname))
                .addGap(23, 23, 23)
                .addGroup(panel_pixellotInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(data_prodname, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
                    .addComponent(data_sfname, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(data_clubname, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(data_lminame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18))
        );
        panel_pixellotInfoLayout.setVerticalGroup(
            panel_pixellotInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_pixellotInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_pixellotInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_lmiName_tag)
                    .addComponent(data_lminame))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_pixellotInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tag_clubname)
                    .addComponent(data_clubname))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_pixellotInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tag_sfname)
                    .addComponent(data_sfname))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_pixellotInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tag_prodname)
                    .addComponent(data_prodname))
                .addContainerGap())
        );

        p_eventInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Event Info"));

        tag_consoleEventID.setText("Console Event ID:");

        tag_clubEventID.setText("Club Event ID:");

        tag_clubEventStatus.setText("Club Event Status:");

        tag_clubEventLink.setText("Club Event Link:");

        data_consoleEventID.setText("gam123456789");

        data_clubEventID.setText("Req. CSV Import");

        data_clubEventStatus.setText("Coming soon!");

        data_clubEventLink.setText("Coming soon!");

        javax.swing.GroupLayout p_eventInfoLayout = new javax.swing.GroupLayout(p_eventInfo);
        p_eventInfo.setLayout(p_eventInfoLayout);
        p_eventInfoLayout.setHorizontalGroup(
            p_eventInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_eventInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(p_eventInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(p_eventInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(tag_consoleEventID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tag_clubEventID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tag_clubEventStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(tag_clubEventLink))
                .addGap(18, 18, 18)
                .addGroup(p_eventInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(data_clubEventID, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                    .addComponent(data_clubEventStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(data_clubEventLink, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(data_consoleEventID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        p_eventInfoLayout.setVerticalGroup(
            p_eventInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_eventInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(p_eventInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tag_consoleEventID)
                    .addComponent(data_consoleEventID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p_eventInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tag_clubEventID)
                    .addComponent(data_clubEventID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p_eventInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tag_clubEventStatus)
                    .addComponent(data_clubEventStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p_eventInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tag_clubEventLink)
                    .addComponent(data_clubEventLink, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        p_sfinfo.setBorder(javax.swing.BorderFactory.createTitledBorder("SF Info"));

        jLabel2.setText("Coming Soon!");

        javax.swing.GroupLayout p_sfinfoLayout = new javax.swing.GroupLayout(p_sfinfo);
        p_sfinfo.setLayout(p_sfinfoLayout);
        p_sfinfoLayout.setHorizontalGroup(
            p_sfinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_sfinfoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(111, 111, 111))
        );
        p_sfinfoLayout.setVerticalGroup(
            p_sfinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_sfinfoLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel2)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        p_unitinfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Unit Info"));

        tag_unitStatus.setText("Unit Status:");

        data_unitStatus.setText("Req. CSV Import");

        tag_version.setText("Version:");

        data_version.setText("v");

        javax.swing.GroupLayout p_unitinfoLayout = new javax.swing.GroupLayout(p_unitinfo);
        p_unitinfo.setLayout(p_unitinfoLayout);
        p_unitinfoLayout.setHorizontalGroup(
            p_unitinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_unitinfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(p_unitinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tag_unitStatus)
                    .addComponent(tag_version))
                .addGap(39, 39, 39)
                .addGroup(p_unitinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(data_unitStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                    .addComponent(data_version, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        p_unitinfoLayout.setVerticalGroup(
            p_unitinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_unitinfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(p_unitinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tag_unitStatus)
                    .addComponent(data_unitStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p_unitinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tag_version)
                    .addComponent(data_version))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout tab_pixellotDetailsLayout = new javax.swing.GroupLayout(tab_pixellotDetails);
        tab_pixellotDetails.setLayout(tab_pixellotDetailsLayout);
        tab_pixellotDetailsLayout.setHorizontalGroup(
            tab_pixellotDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_pixellotDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tab_pixellotDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_pixellotInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tab_pixellotDetailsLayout.createSequentialGroup()
                        .addGroup(tab_pixellotDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(p_unitinfo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(p_eventInfo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(p_sfinfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        tab_pixellotDetailsLayout.setVerticalGroup(
            tab_pixellotDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_pixellotDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_pixellotInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tab_pixellotDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(p_eventInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(p_sfinfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(p_unitinfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tab_pixellotDetails, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(tab_pixellotDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    com.nfhsnetwork.calebsunitytool.ui.components.EditableLabel data_clubEventID;
    com.nfhsnetwork.calebsunitytool.ui.components.EditableLabel data_clubEventLink;
    com.nfhsnetwork.calebsunitytool.ui.components.EditableLabel data_clubEventStatus;
    javax.swing.JLabel data_clubname;
    com.nfhsnetwork.calebsunitytool.ui.components.EditableLabel data_consoleEventID;
    javax.swing.JLabel data_lminame;
    javax.swing.JLabel data_prodname;
    javax.swing.JLabel data_sfname;
    javax.swing.JLabel data_unitStatus;
    javax.swing.JLabel data_version;
    javax.swing.JLabel jLabel2;
    javax.swing.JLabel l_lmiName_tag;
    javax.swing.JPanel p_eventInfo;
    javax.swing.JPanel p_sfinfo;
    javax.swing.JPanel p_unitinfo;
    javax.swing.JPanel panel_pixellotInfo;
    javax.swing.JPanel tab_pixellotDetails;
    javax.swing.JLabel tag_clubEventID;
    javax.swing.JLabel tag_clubEventLink;
    javax.swing.JLabel tag_clubEventStatus;
    javax.swing.JLabel tag_clubname;
    javax.swing.JLabel tag_consoleEventID;
    javax.swing.JLabel tag_prodname;
    javax.swing.JLabel tag_sfname;
    javax.swing.JLabel tag_unitStatus;
    javax.swing.JLabel tag_version;
    // End of variables declaration//GEN-END:variables
}
