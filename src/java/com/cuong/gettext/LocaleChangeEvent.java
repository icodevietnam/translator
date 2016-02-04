package com.cuong.gettext;

import java.util.EventObject;
import java.util.Locale;

public class LocaleChangeEvent extends EventObject {

	private static final long serialVersionUID = 7640942181212009222L;
	
	private Locale newLocale;

	/**
	 * Constructs the event.
	 * @param source the source of the event
	 * @param newLocale the new locale
	 * @since 0.9
	 */
	public LocaleChangeEvent(Object source, Locale newLocale)
	{
		super(source);

		this.newLocale = newLocale;
	}

	/**
	 * Returns the new locale.
	 * 
	 * @return the new locale
	 * @since 0.9
	 */
	public Locale getNewLocale()
	{
		return newLocale;
	}

}
