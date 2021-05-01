/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nfhsnetwork.calebsunitytool.ui;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 *
 * @author impro_000
 */
public class EventDetailsPane extends javax.swing.JPanel {

    /**
     * Creates new form EventDetailsPane
     */
    public EventDetailsPane() {
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

        subPanel_generalDetails = new javax.swing.JPanel();
        panel_general_tags = new javax.swing.JPanel();
        label_producerType_tag = new javax.swing.JLabel();
        label_eventID_tag = new javax.swing.JLabel();
        label_eventStatus_tag = new javax.swing.JLabel();
        label_broadcastKey_tag = new javax.swing.JLabel();
        label_focusType_tag = new javax.swing.JLabel();
        panel_general_fields = new javax.swing.JPanel();
        tf_bdcID_field = new javax.swing.JTextField();
        label_eventID_data = new javax.swing.JLabel();
        label_producerType_data = new javax.swing.JLabel();
        label_status_field = new javax.swing.JLabel();
        label_focusType_field = new javax.swing.JLabel();
        panel_miscDetails = new javax.swing.JPanel();
        label_startTime_tag = new javax.swing.JLabel();
        label_startTime_data = new javax.swing.JLabel();
        label_location_tag = new javax.swing.JLabel();
        label_location_data = new javax.swing.JLabel();
        label_redirect_tag = new javax.swing.JLabel();
        label_redirect_data = new javax.swing.JTextField();
        tag_eventtags = new javax.swing.JLabel();
        data_eventtags = new com.nfhsnetwork.calebsunitytool.ui.components.EditableLabel();
        panel_competitionDetails = new javax.swing.JPanel();
        label_sport_tag = new javax.swing.JLabel();
        tag_comptype = new javax.swing.JLabel();
        label_level_tag = new javax.swing.JLabel();
        label_gender_tag = new javax.swing.JLabel();
        l_sport_data = new javax.swing.JLabel();
        l_type_data = new javax.swing.JLabel();
        l_level_data = new javax.swing.JLabel();
        l_gender_data = new javax.swing.JLabel();
        p_participants = new javax.swing.JPanel();
        tabpane_participants = new javax.swing.JTabbedPane();

        subPanel_generalDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("General"));

        label_producerType_tag.setText("Producer Type:");

        label_eventID_tag.setText("Event ID:");

        label_eventStatus_tag.setText("Status:");

        label_broadcastKey_tag.setText("Broadcast ID:");

        label_focusType_tag.setText("Focus Type:");

        javax.swing.GroupLayout panel_general_tagsLayout = new javax.swing.GroupLayout(panel_general_tags);
        panel_general_tags.setLayout(panel_general_tagsLayout);
        panel_general_tagsLayout.setHorizontalGroup(
            panel_general_tagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_general_tagsLayout.createSequentialGroup()
                .addGroup(panel_general_tagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_eventID_tag)
                    .addComponent(label_broadcastKey_tag)
                    .addComponent(label_producerType_tag)
                    .addComponent(label_eventStatus_tag)
                    .addComponent(label_focusType_tag))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panel_general_tagsLayout.setVerticalGroup(
            panel_general_tagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_general_tagsLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(label_eventID_tag)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_broadcastKey_tag)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_producerType_tag)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_eventStatus_tag)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_focusType_tag))
        );

        tf_bdcID_field.setEditable(false);
        tf_bdcID_field.setText("bdc1234567890");
        tf_bdcID_field.setAutoscrolls(false);
        tf_bdcID_field.setBorder(null);
        tf_bdcID_field.setDoubleBuffered(true);
        tf_bdcID_field.setOpaque(true);
        tf_bdcID_field.setRequestFocusEnabled(false);

        label_eventID_data.setText("gam1234567890");

        label_producerType_data.setText("Producer");

        label_status_field.setText("Scheduled");

        label_focusType_field.setText("Req. Focus List Import");

        javax.swing.GroupLayout panel_general_fieldsLayout = new javax.swing.GroupLayout(panel_general_fields);
        panel_general_fields.setLayout(panel_general_fieldsLayout);
        panel_general_fieldsLayout.setHorizontalGroup(
            panel_general_fieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_general_fieldsLayout.createSequentialGroup()
                .addGroup(panel_general_fieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_status_field, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tf_bdcID_field)
                    .addComponent(label_producerType_data, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_focusType_field, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_eventID_data, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
                .addContainerGap())
        );
        panel_general_fieldsLayout.setVerticalGroup(
            panel_general_fieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_general_fieldsLayout.createSequentialGroup()
                .addComponent(label_eventID_data, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tf_bdcID_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_producerType_data)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_status_field, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_focusType_field, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout subPanel_generalDetailsLayout = new javax.swing.GroupLayout(subPanel_generalDetails);
        subPanel_generalDetails.setLayout(subPanel_generalDetailsLayout);
        subPanel_generalDetailsLayout.setHorizontalGroup(
            subPanel_generalDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subPanel_generalDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_general_tags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panel_general_fields, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        subPanel_generalDetailsLayout.setVerticalGroup(
            subPanel_generalDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subPanel_generalDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(subPanel_generalDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panel_general_fields, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_general_tags, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(13, 13, 13))
        );

        panel_miscDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("idk"));
        panel_miscDetails.setMinimumSize(new java.awt.Dimension(269, 122));

        label_startTime_tag.setText("Start Time:");

        label_startTime_data.setText("Dec 30, 2028 - 12:00 PM EST");

        label_location_tag.setText("Location:");

        label_location_data.setText("Provo, UT");

        label_redirect_tag.setText("Redirect:");

        label_redirect_data.setEditable(false);
        label_redirect_data.setText("gam1234567890");
        label_redirect_data.setBorder(null);

        tag_eventtags.setText("Tags:");

        data_eventtags.setText("placeholder");

        javax.swing.GroupLayout panel_miscDetailsLayout = new javax.swing.GroupLayout(panel_miscDetails);
        panel_miscDetails.setLayout(panel_miscDetailsLayout);
        panel_miscDetailsLayout.setHorizontalGroup(
            panel_miscDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_miscDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_miscDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_miscDetailsLayout.createSequentialGroup()
                        .addComponent(label_startTime_tag)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(label_startTime_data))
                    .addGroup(panel_miscDetailsLayout.createSequentialGroup()
                        .addComponent(label_location_tag)
                        .addGap(18, 18, 18)
                        .addComponent(label_location_data))
                    .addGroup(panel_miscDetailsLayout.createSequentialGroup()
                        .addGroup(panel_miscDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_redirect_tag)
                            .addComponent(tag_eventtags))
                        .addGap(18, 18, 18)
                        .addGroup(panel_miscDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_redirect_data)
                            .addComponent(data_eventtags, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        panel_miscDetailsLayout.setVerticalGroup(
            panel_miscDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_miscDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_miscDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_startTime_tag)
                    .addComponent(label_startTime_data))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_miscDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_location_tag)
                    .addComponent(label_location_data))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_miscDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_redirect_tag)
                    .addComponent(label_redirect_data, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_miscDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tag_eventtags)
                    .addComponent(data_eventtags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panel_competitionDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Competition Details"));

        label_sport_tag.setText("Sport:");

        tag_comptype.setText("Type:");

        label_level_tag.setText("Level:");

        label_gender_tag.setText("Gender:");

        l_sport_data.setText("Basketball");

        l_type_data.setText("Regular Season");

        l_level_data.setText("Varsity");

        l_gender_data.setText("Girls");

        javax.swing.GroupLayout panel_competitionDetailsLayout = new javax.swing.GroupLayout(panel_competitionDetails);
        panel_competitionDetails.setLayout(panel_competitionDetailsLayout);
        panel_competitionDetailsLayout.setHorizontalGroup(
            panel_competitionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_competitionDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_competitionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_sport_tag)
                    .addComponent(tag_comptype)
                    .addComponent(label_level_tag)
                    .addComponent(label_gender_tag))
                .addGap(25, 25, 25)
                .addGroup(panel_competitionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(l_gender_data, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(l_type_data, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                    .addComponent(l_sport_data, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(l_level_data, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panel_competitionDetailsLayout.setVerticalGroup(
            panel_competitionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_competitionDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_competitionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_sport_tag)
                    .addComponent(l_sport_data))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_competitionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tag_comptype)
                    .addComponent(l_type_data))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_competitionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_level_tag)
                    .addComponent(l_level_data))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_competitionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_gender_tag)
                    .addComponent(l_gender_data))
                .addContainerGap())
        );

        p_participants.setBorder(javax.swing.BorderFactory.createTitledBorder("Participants"));
        p_participants.setMinimumSize(new java.awt.Dimension(350, 209));

        javax.swing.GroupLayout p_participantsLayout = new javax.swing.GroupLayout(p_participants);
        p_participants.setLayout(p_participantsLayout);
        p_participantsLayout.setHorizontalGroup(
            p_participantsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_participantsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabpane_participants, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        p_participantsLayout.setVerticalGroup(
            p_participantsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_participantsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabpane_participants)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panel_miscDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(p_participants, javax.swing.GroupLayout.PREFERRED_SIZE, 283, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(subPanel_generalDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panel_competitionDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(116, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(subPanel_generalDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_competitionDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panel_miscDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(p_participants, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    com.nfhsnetwork.calebsunitytool.ui.components.EditableLabel data_eventtags;
    javax.swing.JLabel l_gender_data;
    javax.swing.JLabel l_level_data;
    javax.swing.JLabel l_sport_data;
    javax.swing.JLabel l_type_data;
    javax.swing.JLabel label_broadcastKey_tag;
    javax.swing.JLabel label_eventID_data;
    javax.swing.JLabel label_eventID_tag;
    javax.swing.JLabel label_eventStatus_tag;
    javax.swing.JLabel label_focusType_field;
    javax.swing.JLabel label_focusType_tag;
    javax.swing.JLabel label_gender_tag;
    javax.swing.JLabel label_level_tag;
    javax.swing.JLabel label_location_data;
    javax.swing.JLabel label_location_tag;
    javax.swing.JLabel label_producerType_data;
    javax.swing.JLabel label_producerType_tag;
    javax.swing.JTextField label_redirect_data;
    javax.swing.JLabel label_redirect_tag;
    javax.swing.JLabel label_sport_tag;
    javax.swing.JLabel label_startTime_data;
    javax.swing.JLabel label_startTime_tag;
    javax.swing.JLabel label_status_field;
    javax.swing.JPanel p_participants;
    javax.swing.JPanel panel_competitionDetails;
    javax.swing.JPanel panel_general_fields;
    javax.swing.JPanel panel_general_tags;
    javax.swing.JPanel panel_miscDetails;
    javax.swing.JPanel subPanel_generalDetails;
    javax.swing.JTabbedPane tabpane_participants;
    javax.swing.JLabel tag_comptype;
    javax.swing.JLabel tag_eventtags;
    javax.swing.JTextField tf_bdcID_field;
    // End of variables declaration//GEN-END:variables


    
    public void addParticipant(String name, String loc, String assoc, String tabName)
    {
        JLabel tag_name = new JLabel("Name:");

        JLabel data_name = new JLabel(name);

        JLabel tag_assoc = new JLabel("Assoc:");

        JLabel data_assoc = new JLabel(assoc);

        JLabel tag_location = new JLabel("Location:");

        JLabel data_loc = new JLabel(loc);
        
        JPanel participantTemplate = new JPanel();
        
        javax.swing.GroupLayout participantTemplateLayout = new javax.swing.GroupLayout(participantTemplate);
        participantTemplateLayout.setHorizontalGroup(
            participantTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(participantTemplateLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(participantTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(participantTemplateLayout.createSequentialGroup()
                        .addGroup(participantTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tag_name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tag_assoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(25, 25, 25)
                        .addGroup(participantTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(data_name, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                            .addComponent(data_assoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(participantTemplateLayout.createSequentialGroup()
                        .addComponent(tag_location)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(data_loc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        participantTemplateLayout.setVerticalGroup(
            participantTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(participantTemplateLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(participantTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(data_name)
                    .addComponent(tag_name, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(participantTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tag_assoc)
                    .addComponent(data_assoc))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(participantTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tag_location)
                    .addComponent(data_loc))
                .addContainerGap(62, Short.MAX_VALUE))
        );
        participantTemplate.setLayout(participantTemplateLayout);
        
        this.tabpane_participants.add(participantTemplate, tabName);
    }
    
    public void clearParticipants()
    {
    	this.tabpane_participants.removeAll();
    }
}
