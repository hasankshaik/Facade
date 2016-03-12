package uk.co.listing.cucumber;

import static org.junit.Assert.assertTrue;
import uk.co.listing.SeleniumConnector;
import cucumber.api.java.en.Given;

public class AddRoomsSteps {
    @Given("^court room \"(.*?)\" already exists$")
    public void court_room_already_exists(String roomname) throws Throwable {
        // Room already added using import.sql
        SeleniumConnector.waitForTextPresent("Room Name");
        assertTrue(SeleniumConnector.isSelectOptionPresent("listOfRooms", roomname));
    }
}