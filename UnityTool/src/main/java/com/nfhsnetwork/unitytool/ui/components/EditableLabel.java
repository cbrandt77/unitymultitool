/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nfhsnetwork.unitytool.ui.components;

import javax.swing.JTextField;
import javax.swing.text.Document;

/**
 *
 * @author impro_000
 */
public class EditableLabel extends JTextField {

    public EditableLabel() {
        super();
        custom();
    }

    public EditableLabel(String string) {
        super(string);
        custom();
    }

    public EditableLabel(int i) {
        super(i);
        custom();
    }

    public EditableLabel(String string, int i) {
        super(string, i);
        custom();
    }

    public EditableLabel(Document dcmnt, String string, int i) {
        super(dcmnt, string, i);
        custom();
    }
    
    private void custom() {
        this.setBorder(null);
        this.setOpaque(false);
        this.setEditable(false);
    }
    
}
