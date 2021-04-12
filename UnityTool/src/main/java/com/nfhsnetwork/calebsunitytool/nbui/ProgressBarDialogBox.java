package com.nfhsnetwork.calebsunitytool.nbui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class ProgressBarDialogBox extends JDialog 
{
	private JProgressBar progressBar;
	
	public ProgressBarDialogBox(Component parent)
	{
		super(SwingUtilities.getWindowAncestor(parent), "Loading...", DEFAULT_MODALITY_TYPE);
		
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