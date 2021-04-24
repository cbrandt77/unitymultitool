package com.nfhsnetwork.calebsunitytool.ui.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JProgressBar;

public class ProgressBarDialogBox extends JDialog 
{
	private JProgressBar progressBar;
	
	public ProgressBarDialogBox(Window parent)
	{
		this(parent, "Loading...", DEFAULT_MODALITY_TYPE);
		
		
	}
	public ProgressBarDialogBox(Window parent, String title, ModalityType modalityType)
	{
		super(parent, title, modalityType);
		progressBar = new JProgressBar();
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridwidth = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		gbc.insets = new Insets(10, 10, 10, 10);
		
		add(progressBar, gbc);
		
		
		this.pack();
		this.setLocationRelativeTo(parent);
	}
	
	public JProgressBar getProgressBar() {
		return this.progressBar;
	}
	
}