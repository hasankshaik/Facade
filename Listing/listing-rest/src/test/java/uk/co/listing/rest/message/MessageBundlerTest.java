package uk.co.listing.rest.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class MessageBundlerTest {

    private static final String COURT_CENTRE_EMPTY_CODE = "court.centre.empty";

    @Test
    public void testGetMessage_defaultLocale() {
        final String message = MessageBundler.getMessage(COURT_CENTRE_EMPTY_CODE);
        assertNotNull(message);
        assertEquals("Court Centre is a required field", message);
    }

    @Test
    public void testGetMessage_fr_language() {
        final String message = MessageBundler.getMessage(COURT_CENTRE_EMPTY_CODE, "fr");
        assertNotNull(message);
        assertEquals("Centre Court est vide", message);
    }

    @Test
    public void testGetMessage_fr_GB() {
        final String message = MessageBundler.getMessage(COURT_CENTRE_EMPTY_CODE, "fr", "GB");
        assertNotNull(message);
        assertEquals("Centre Court est vide(GB)", message);
    }
}
