package com.nfhsnetwork.unitytool.ui;

import java.awt.Dimension;

import javax.swing.JComponent;

public class ParticipantTemplate extends javax.swing.JPanel {
	
	private final JComponent parent;
    
    public ParticipantTemplate(JComponent parent, String name, String location, String assoc) {
    	
    	l_schoolname = new javax.swing.JLabel(name);
        l_citystate = new javax.swing.JLabel(location);
        l_division = new javax.swing.JLabel(assoc);
        
        this.parent = parent;
        
        initComponents();
    }

    
    @SuppressWarnings("unchecked")
    private void initComponents() {

        participantTemplate = new javax.swing.JPanel();
        
        
        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        participantTemplate.setFocusable(false);

        l_schoolname.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        l_schoolname.setFocusable(false);

        l_citystate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        l_citystate.setFocusable(false);

        l_division.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        l_division.setFocusable(false);

        javax.swing.GroupLayout participantTemplateLayout = new javax.swing.GroupLayout(participantTemplate);
        participantTemplate.setLayout(participantTemplateLayout);
        participantTemplateLayout.setHorizontalGroup(
            participantTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(participantTemplateLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(participantTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(l_schoolname, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(participantTemplateLayout.createSequentialGroup()
                        .addGap(0, 2, Short.MAX_VALUE)
                        .addComponent(l_division, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(l_citystate, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        participantTemplateLayout.setVerticalGroup(
            participantTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(participantTemplateLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(l_schoolname)
                .addGap(4, 4, 4)
                .addGroup(participantTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_citystate)
                    .addComponent(l_division))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(participantTemplate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(participantTemplate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        
        parent.add(this);
    }
    
    @Override
    public Dimension getPreferredSize()
    {
    	return new Dimension(parent.getWidth(), 30);
    }


    private javax.swing.JLabel l_citystate;
    private javax.swing.JLabel l_division;
    private javax.swing.JLabel l_schoolname;
    private javax.swing.JPanel participantTemplate;
}



