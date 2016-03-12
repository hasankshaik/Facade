package uk.co.listing.rest.message;

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

/**
 * Class to bundle up your messages and return the messages as per the language set up
 *
 * @author rvinayak
 */
public abstract class MessageBundler {

    private static final String MESSAGE_RESOURCE = "i18n.ApplicationMessages";
    private static ResourceBundle resourceBundle;

    private MessageBundler() {
        super();
    }

    /**
     * returns the property defined in the default message property
     *
     * @param property
     * @return
     */
    public static String getMessage(final String property) {
        resourceBundle = ResourceBundle.getBundle(MESSAGE_RESOURCE);
        return resourceBundle.getString(property);
    }

    /**
     * returns the property defined in the default message property
     *
     * @param property
     * @return
     */
    public static String getMessage(final String property, final String[] keys, final String[] values) {
        resourceBundle = ResourceBundle.getBundle(MESSAGE_RESOURCE);
        return StringUtils.replaceEach(resourceBundle.getString(property), keys, values) ;
    }

    /**
     * @param property
     *            , property to look message for
     * @param language
     *            , language code, like en/fr etc
     * @return
     */
    public static String getMessage(final String property, final String language) {
        final Locale locale = new Locale(language);
        resourceBundle = ResourceBundle.getBundle(MESSAGE_RESOURCE, locale);
        return resourceBundle.getString(property);
    }

    /**
     * @param property
     * @param language
     *            , language code en/fr etc
     * @param country
     *            , country code like US/GB etc
     * @return
     */
    public static String getMessage(final String property, final String language, final String country) {
        final Locale locale = new Locale(language, country);
        resourceBundle = ResourceBundle.getBundle(MESSAGE_RESOURCE, locale);
        return resourceBundle.getString(property);
    }

    /**
     * @param property
     * @param locale
     *            , load a property with the passed in locale object
     * @return
     */
    public static String getMessage(final String property, final Locale locale) {
        resourceBundle = ResourceBundle.getBundle(MESSAGE_RESOURCE, locale);
        return resourceBundle.getString(property);
    }
}
