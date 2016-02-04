package com.cuong.gettext;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class I18nManager {

	private static I18nManager instance = new I18nManager();

	/** List of managed {@link I18n} objects. */
	List i18ns = Collections.synchronizedList(new ArrayList());

	/** List of managed {@link LocaleChangeListener} objects. */
	List localeChangeListeners = new ArrayList();

	private I18nManager()
	{
	}

	/**
	 * Returns the global <code>I18Manger</code> singleton.
	 * 
	 * @return the <code>I18Manger</code> instance
	 * @since 0.9
	 */
	public static I18nManager getInstance()
	{
		return instance;
	}

	/**
	 * Adds <code>i18n</code> to the list of managed <code>I18n</code>
	 * objects.
	 * 
	 * @param i18n
	 *            the <code>I18n</code> instance
	 * @see #setDefaultLocale(Locale)
	 * @see #remove(I18n)
	 * @since 0.9
	 */
	public void add(I18n i18n)
	{
		i18ns.add(i18n);
	}

	/**
	 * Sets the locale for all I18n instances that were instantiated through the
	 * factory.
	 * <p>
	 * Use this method to globally change the locale for all I18n based
	 * translations.
	 * <p>
	 * NOTE: This only works if the objects that display messages do not cache
	 * translated messages.
	 * <p>
	 * 
	 * @param locale
	 *            the new default locale
	 * @see I18n#setLocale(Locale)
	 * @since 0.9
	 */
	public void setDefaultLocale(Locale locale)
	{
		synchronized (i18ns) {
			for (Iterator it = i18ns.iterator(); it.hasNext();) {
				I18n i18n = (I18n)it.next();
				i18n.setLocale(locale);
			}
		}
		fireLocaleChangedEvent(locale);
	}

	/**
	 * Adds a listener that is notified when the default locale has been
	 * changed.
	 * 
	 * @param listener
	 *            the listener
	 * @see #setDefaultLocale(Locale)
	 * @since 0.9
	 */
	public void addLocaleChangeListener(LocaleChangeListener listener)
	{
		synchronized (localeChangeListeners) {
			localeChangeListeners.add(listener);
		}
	}

	/**
	 * Adds a listener that is notified when the default locale has been changed
	 * using a {@link WeakReference}. The listener is removed when it has been
	 * cleaned up by the garbage collection.
	 * <p>
	 * This is useful for temporary objects that may have an indeterminate
	 * lifetime such as dialogs.
	 * 
	 * @param listener
	 *            the listener
	 * @see #setDefaultLocale(Locale)
	 * @since 0.9
	 */
	public void addWeakLocaleChangeListener(LocaleChangeListener listener)
	{
		synchronized (localeChangeListeners) {
			localeChangeListeners.add(new WeakLocaleChangeListener(listener));
		}
	}

	/**
	 * Removes <code>i18n</code> from the list of managed <code>I18n</code>
	 * objects.
	 * 
	 * @param i18n
	 *            the <code>I18n</code> instance
	 * @see #add(I18n)
	 * @since 0.9
	 */
	public void remove(I18n i18n)
	{
		i18ns.remove(i18n);
	}

	/**
	 * Removes <code>listener</code> from the list of objects that are
	 * notified when the locale has changed.
	 * 
	 * @param listener
	 *            the listener
	 * @since 0.9
	 */
	public void removeLocaleChangeListener(LocaleChangeListener listener)
	{
		synchronized (localeChangeListeners) {
			localeChangeListeners.remove(listener);
		}
	}

	/**
	 * Notifies listeners of a locale change.
	 * 
	 * @param newLocale
	 *            new locale
	 * @since 0.9
	 */
	protected void fireLocaleChangedEvent(Locale newLocale)
	{
		LocaleChangeListener[] listeners;
		synchronized (localeChangeListeners) {
			listeners = (LocaleChangeListener[])localeChangeListeners.toArray(new LocaleChangeListener[0]);
		}
		if (listeners.length > 0) {
			LocaleChangeEvent event = new LocaleChangeEvent(I18nFactory.class, newLocale);
			for (int i = listeners.length - 1; i >= 0; i--) {
				listeners[i].localeChanged(event);
			}
		}
	}

	private static class WeakLocaleChangeListener implements LocaleChangeListener {

		private WeakReference reference;

		public WeakLocaleChangeListener(LocaleChangeListener listener)
		{
			reference = new WeakReference(listener);
		}

		public void localeChanged(LocaleChangeEvent event)
		{
			Object listener = reference.get();
			if (listener != null) {
				((LocaleChangeListener)listener).localeChanged(event);
			}
			else {
				I18nManager.getInstance().removeLocaleChangeListener(this);
			}
		}

	}

}
