/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nfhsnetwork.calebsunitytool.ui;

import java.util.List;
import java.util.Set;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import com.nfhsnetwork.calebsunitytool.common.NFHSGameObject;
import com.nfhsnetwork.calebsunitytool.common.UnityContainer;
import java.awt.Component;
import java.awt.Dimension;

/**
 *
 * @author calebbrandt
 */
public class GameListContainer extends javax.swing.JPanel {

    //private Map<String, NFHSGameObject> eventMap;

    private UIController c;
    
    
   
    
    
	
    /**
     * Creates new form GameListContainer
     */
    public GameListContainer() { //TODO figure out why this doesn't appear in mainwindow
        initComponents();
    }
    
    public GameListContainer(UIController c)
    {
    	initComponents();
    	this.c = c;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        container_gameList = new javax.swing.JPanel();
        scrollPane_gameIDs = new javax.swing.JScrollPane();
        list_gameIds = new javax.swing.JList<>();

        container_gameList.setBorder(javax.swing.BorderFactory.createTitledBorder("Events"));

        scrollPane_gameIDs.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        scrollPane_gameIDs.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane_gameIDs.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane_gameIDs.setAutoscrolls(true);

        list_gameIds.setModel(new UnityListModel());
        list_gameIds.setCellRenderer(new CustomListCellRenderer());
        list_gameIds.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        list_gameIds.setMaximumSize(new java.awt.Dimension(143, 32767));
        list_gameIds.setMinimumSize(new java.awt.Dimension(143, 20));
        list_gameIds.setPreferredSize(new java.awt.Dimension(143, 0));
        list_gameIds.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
			public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                list_gameIdsValueChanged(evt);
            }
        });
        scrollPane_gameIDs.setViewportView(list_gameIds);

        javax.swing.GroupLayout container_gameListLayout = new javax.swing.GroupLayout(container_gameList);
        container_gameList.setLayout(container_gameListLayout);
        container_gameListLayout.setHorizontalGroup(
            container_gameListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(container_gameListLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPane_gameIDs, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                .addContainerGap())
        );
        container_gameListLayout.setVerticalGroup(
            container_gameListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(container_gameListLayout.createSequentialGroup()
                .addComponent(scrollPane_gameIDs, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(container_gameList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(container_gameList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    
    private void list_gameIdsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_list_gameIdsValueChanged
        gameIDsListValueChanged(evt);
    }//GEN-LAST:event_list_gameIdsValueChanged
    
    

    protected void gameIDsListValueChanged(ListSelectionEvent e) 
    {
    	if (e.getValueIsAdjusting())
    		return;
    	
    	
    	JList<NFHSGameObject> source = (JList<NFHSGameObject>)e.getSource();
    	
    	List<NFHSGameObject> selected = source.getSelectedValuesList();
    	
    	int x = selected.size();
    	if (x == 1)
    	{
    		System.out.println("[DEBUG] Selected size ONE");
    		c.onOneSelected(selected.get(0));
    	}
    	else if (x > 1)
    	{
    		System.out.println("[DEBUG] Selected size MULT, numSelected: " + x);
    		c.onMultipleSelected(selected);
    	}
    	else
    	{
    		System.out.println("[DEBUG] Selected size ELSE, numSelected: " + x); //TODO debug
    		c.onNoneSelected();
    	}
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel container_gameList;
    private javax.swing.JList<NFHSGameObject> list_gameIds;
    private javax.swing.JScrollPane scrollPane_gameIDs;
    // End of variables declaration//GEN-END:variables

    private class UnityListModel extends AbstractListModel<NFHSGameObject> 
    {
    	private final String[] strings;
    	
    	UnityListModel() 
    	{
    		Set<String> keys = UnityContainer.getContainer().getEventMap().keySet();
    		strings = keys.toArray(new String[keys.size()]);
    	}
    	
    	@Override
    	public int getSize() {
    		return strings.length;
    	}

    	@Override
    	public NFHSGameObject getElementAt(int index) {
    		return UnityContainer.getContainer().getEventMap().get(strings[index]);
    	}

    }

    	protected enum VIEWTYPE {
            GAMEID,
            BDCID,
            TITLE
        }
    
    /**
     *
     * @author impro_000
     */
    public class CustomListCellRenderer extends javax.swing.JPanel implements javax.swing.ListCellRenderer {

        private final int SCROLLBAR_WIDTH = ((Integer)UIManager.get("ScrollBar.width")).intValue();
        private VIEWTYPE viewType = VIEWTYPE.GAMEID;

        /**
         * Creates new form CustomListCellRenderer
         */
        public CustomListCellRenderer() {
            initComponents();
        }

        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
                              
        private void initComponents() {

            label_gameID = new javax.swing.JLabel();

            setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
            
            
            
            label_gameID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
            label_gameID.setHorizontalAlignment(SwingConstants.CENTER);
            label_gameID.setVerticalAlignment(SwingConstants.CENTER);
            
            this.setPreferredSize(new Dimension(GameListContainer.this.scrollPane_gameIDs.getWidth() - 20, 32));
            add(label_gameID);
        }                       

        @Override
        public Component getListCellRendererComponent(JList list,
                                                      Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus) {
            String labelText;

            if (value instanceof NFHSGameObject)
            {
                    NFHSGameObject nValue = (NFHSGameObject)value;
                    switch (viewType)
                    {
                            case GAMEID:
                                    labelText = nValue.getGameID();
                                    break;
                            case BDCID:
                                    labelText = nValue.getBdcIDs()[0];
                                    break;
                            case TITLE:
                                    labelText = nValue.getTitle(); //TODO actually hook this up to be a sort-by-title
                                    break;
                            default:
                                    labelText = "?"; //TODO
                    }
            }
            else
            {
                    labelText = "null";
            }


            label_gameID.setText(labelText);
            label_gameID.setOpaque(true);

            setBorder(javax.swing.BorderFactory.createEtchedBorder());

            if (isSelected)
            {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
                label_gameID.setBackground(list.getSelectionBackground());
                label_gameID.setForeground(list.getSelectionForeground());
            }
            else
            {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
                label_gameID.setBackground(list.getBackground());
                label_gameID.setForeground(list.getForeground());
            }

            setEnabled(list.isEnabled());
            label_gameID.setFont(list.getFont());

            //TODO change what the label says based on the viewtype.

            return this;
        }

        public void setViewType(VIEWTYPE v)
        {
            viewType = v;
        }

        // Variables declaration - do not modify                     
        private javax.swing.JLabel label_gameID;
        // End of variables declaration                   
    }

}