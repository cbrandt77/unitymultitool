package com.nfhsnetwork.unitytool.ui.components;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Function;

import javax.swing.JDialog;

@SuppressWarnings("rawtypes")
public abstract class CallbackDialog<T, R> extends JDialog {
	private final Function<T, R> onSuccess;
	private final Function onFail;
	private final Function onCancel;
	
	//TODO just set this to return an int containing the error code instead of having functions for every case dummy
	
	public CallbackDialog(final Window parent, final String title, ModalityType modalityType, final Function<T, R> onSuccess, final Function onFail, final Function onCancel)
	{
		super(parent, title, modalityType);
		this.onSuccess = onSuccess;
		
		if (onFail == null)
			this.onFail = (o) -> null;
		else
			this.onFail = onFail;
		
		if (onCancel == null)
    		this.onCancel = onFail;
		else
			this.onCancel = onCancel;
		
		this.addWindowListener(getCloseHandler());
	}
	
	public CallbackDialog(final Window parent, final String title, final ModalityType modalityType, final Function<T, R> onSuccess, final Function onFail)
	{
		this(parent, title, modalityType, onSuccess, onFail, null);
	}
	
	public CallbackDialog(final Window parent, final String title, final Function<T, R> onComplete, final Function onFail, final Function onCancel)
	{
		this(parent, title, JDialog.DEFAULT_MODALITY_TYPE, onComplete, onFail, onCancel);
	}
	
	public CallbackDialog(final Window parent, final String title, final Function<T, R> onComplete, final Function onFail)
	{
		this(parent, title, JDialog.DEFAULT_MODALITY_TYPE, onComplete, null, onFail);
	}
	
	public CallbackDialog(final Window parent, final Function<T, R> onComplete, final Function onFail, final Function onCancel)
	{
		this(parent, "", JDialog.DEFAULT_MODALITY_TYPE, onComplete, onFail, onCancel);
	}
	
	public CallbackDialog(final Window parent, final Function<T, R> onComplete, final Function onFail)
	{
		this(parent, "", JDialog.DEFAULT_MODALITY_TYPE, onComplete, null, onFail);
	}
	
	public CallbackDialog(final Window parent, final String title, final Function<T, R> onComplete)
	{
		this(parent, title, JDialog.DEFAULT_MODALITY_TYPE, onComplete, null, null);
	}
	
	public CallbackDialog(final String title, final Function<T, R> onComplete, final Function onFail)
	{
		this(null, title, JDialog.DEFAULT_MODALITY_TYPE, onComplete, onFail, null);
	}
	
	public CallbackDialog(final String title, final Function<T, R> onComplete)
	{
		this(null, title, JDialog.DEFAULT_MODALITY_TYPE, onComplete, null, null);
	}
	
	public CallbackDialog(final Window parent, final ModalityType modalityType, final Function<T, R> onComplete, final Function onFail)
	{
		this(parent, "", modalityType, onComplete, onFail, null);
	}
	
	public CallbackDialog(final Window parent, final ModalityType modalityType, final Function<T, R> onComplete)
	{
		this(parent, "", modalityType, onComplete, null, null);
	}
	
	
	
	protected void onSuccess(final T t)
	{
		this.onSuccess.apply(t);
	}
	
	protected void onFail()
	{
		this.onFail.apply(null);
	}
	
	protected void onCancel()
	{
		this.onCancel.apply(null);
	}
	
	/**
	 * Should call the onSuccess(T t) and onCancel(T t) methods.
	 * @return
	 */
	protected abstract CloseHandler getCloseHandler();
	
	protected abstract class CloseHandler extends WindowAdapter {
		@Override
		public abstract void windowClosed(WindowEvent e);
	}
}
