package com.cuong.gettext;

import java.util.Locale;

public interface LocaleChangeListener {
	
	/**
	 * Invoked when the local has changed.
	 * 
	 * @param event provides information about the new locale
	 * @since 0.9
	 */
	void localeChanged(LocaleChangeEvent event);
	
}
