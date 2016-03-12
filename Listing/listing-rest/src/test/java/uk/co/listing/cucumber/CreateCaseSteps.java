package uk.co.listing.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriverException;

import uk.co.listing.SeleniumConnector;
import uk.co.listing.domain.constant.CustodyStatus;
import uk.co.listing.utils.DateFormats;
import uk.co.listing.utils.DateTimeUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CreateCaseSteps {
	public static Log log = LogFactory.getLog(CreateCaseSteps.class);

	// Text of size 1024 characters.
	public static final String longNote = "The Ministry of Justice (MOJ) is a ministerial department of the UK Government headed by the Secretary of "
			+ "State for Justice and Lord Chancellor (a combined position). The department is also responsible for areas of constitutional policy not"
			+ " transferred in 2010 to the Deputy Prime Minister, human rights law and information rights law across the UK. The ministry was formed "
			+ "in May 2007 when some functions of the Home Secretary were combined with the Department for Constitutional Affairs.[2] The latter had "
			+ "replaced the Lord Chancellor's Department in 2003. Its stated priorities are to reduce re-offending and protect the public, to provide"
			+ " access to justice, to increase confidence in the justice system, and uphold people’s civil liberties.[3] The Secretary of State is "
			+ "the minister responsible to Parliament for the judiciary, the court system and prisons and probation in England and Wales, with some "
			+ "additional UK-wide responsibilities e.g. the UK Supreme Court and judicial appointments by the Crown.Prior to the formation of the "
			+ "Coalition Government in May 2010, the ministry handled relations between the UK Government and the three devolved administrations: the"
			+ " Northern Ireland Executive; the Scottish Government; and the Welsh Government.Responsibility for devolution was then transferred to the"
			+ " re-established position of Deputy Prime Minister, based in the Cabinet Office. He also assumed responsibility for political and "
			+ "constitutional reform, including reform of the House of Lords, the West Lothian Question, electoral policy, political party funding "
			+ "reform and royal succession.The Deputy Prime Minister and Secretary of State for Justice have joint responsibility for a commission on "
			+ "a British bill of rights. The Ministry of Justice retained the following UK-wide remit. As the office of the Lord High Chancellor of Great"
			+ " Britain, the ministry is also responsible for policy relating to Lord Lieutenants (i.e. the personal representatives of the Queen), non-delegated"
			+ " royal, church and hereditary issues, and other constitutional issues, although the exact definition of these is unclear. The post of Lord Chancellor "
			+ "of Ireland was abolished in 1922 but Northern Ireland remains part of the UK, however the functions and responsibilities do belong from then to the "
			+ "Secretary of State for Northern Ireland, currently Theresa Villiers.England and Wales only.Further information: Legal system in England and Wales. "
			+ "The vast majority of the Ministry of Justice's work takes place in England and Wales. The ministry has no responsibility for devolved criminal justice"
			+ " policy, courts, prisons or probation matters in either Scotland or Northern Ireland. Within the jurisdiction of England and Wales, the Ministry of "
			+ "Justice is responsible for ensuring that all suspected offenders (including children and young people) are  are appropriately dealt with from the time"
			+ " they are arrested, until convicted offenders have completed their sentence.[6] The ministry is therefore responsible for all aspects of the criminal "
			+ "law, including the scope and content ofo";

	@Given("^I am on the Edit Case page$")
	public void i_am_on_the_Edit_Case_page() throws Throwable {
		SeleniumConnector.openAndWaitForManageCase();
		SeleniumConnector.waitForTextPresent("Case Crest Number");
	}

	@When("^I Select \"(.*?)\" in \"(.*?)\"$")
	public void i_Select_in(final String classOfOffence,
			final String offenceClassId) throws Throwable {
		// Don't do anything if empty string is passed as option.
		if ("".equals(classOfOffence)) {
			return;
		}
		SeleniumConnector.isSelectOptionPresent(offenceClassId, classOfOffence);
		SeleniumConnector.selectFromDropDown(offenceClassId, classOfOffence);
	}

	@When("^I enter date \"(.*?)\" in \"(.*?)\"$")
	public void i_enter_date_in(final String dateOfSending, final String dateId)
			throws Throwable {
		// Don't do anything if empty string is passed as date.
		if ("".equals(dateOfSending)) {
			return;
		}
		int noOfDays = 0;
		final Matcher expected = Pattern.compile("\\d+").matcher(dateOfSending);
		if (expected.find()) {
			noOfDays = Integer.parseInt(expected.group());
		}
		final Date dateToEnter = DateTimeUtils.getEndWorkingDateFromStartDate(
				new Date(), noOfDays + 1);
		SeleniumConnector.waitForElementClickableBySelector(dateId, "id");
		SeleniumConnector.enterDateInSelector(dateId, dateToEnter);
	}

	@When("^I view \"(.*?)\" in View Case page$")
	public void i_view_in_View_Case_page(final String caseCrestNo)
			throws Throwable {
		SeleniumConnector.openAndWait("case");
		SeleniumConnector.waitForTextPresent("Case Crest Number");
		SeleniumConnector.waitForElementPresent("caseCrestNumber");
		SeleniumConnector.setInputValue("caseCrestNumber", caseCrestNo);
		try {
			SeleniumConnector.pressKey("getCaseDetailsButton", Keys.ENTER);
		} catch (final Exception e) {
			// Trying this twice just in case
			SeleniumConnector.pressKey("getCaseDetailsButton", Keys.ENTER);
		}
	}

	@When("^I click \"(.*?)\" to create case$")
	public void i_click_to_create_case(final String buttonId) throws Throwable {
		SeleniumConnector.waitForTextPresent("Save Case");
		if (SeleniumConnector.isEnabled(buttonId)) {
			try {
				SeleniumConnector.clickAndWaitBySelector(buttonId, "id");
			} catch (final Exception e) {
			}
		}
	}

	@Then("^I see \"(.*?)\" button is disabled$")
	public void i_see_button_is_disabled(final String saveButtonId)
			throws Throwable {
		SeleniumConnector.waitForElementAttribute(saveButtonId, "disabled",
				"true");
	}

	@Then("^I see message \"(.*?)\" in \"(.*?)\"$")
	public void i_see_message(final String message, final String messageId)
			throws Throwable {
		SeleniumConnector.waitForTextPresent(message);
	}

	@Then("^I can not see case details like \"(.*?)\" on case page$")
	public void i_can_not_see_case_defendant_offence_on_case_page(
			final String def) throws Throwable {
		if ("".equals(def)) {
			return;
		}
		assertTrue(SeleniumConnector.isTextNotPresent(def));
	}

	@Given("^case with \"(.*?)\" already exist$")
	public void case_with_already_exist(final String arg1) throws Throwable {
		// Nothing to do here as Case T100 is setup via import.sql
	}

	@Then("^I see \"(.*?)\" response message$")
	public void i_see_response_message(final String msg) throws Throwable {
		SeleniumConnector.waitForTextPresent(msg);
	}

	@When("^I click \"(.*?)\" button$")
	public void i_click_button(final String buttonName) throws Throwable {
		String elementId = "addDefendentButton";
		if ("Add".equals(buttonName)) {
			elementId = "saveDefendentButton";
		} else if ("Add Defendant".equals(buttonName)) {
			elementId = "addDefendentButton";
		} else if ("Add Note".equals(buttonName)) {
			elementId = "addNoteButton";
		} else if ("Add Linked Case".equals(buttonName)) {
			elementId = "addLinkedCaseButton";
		} else if ("Add Non Available Date".equals(buttonName)) {
			elementId = "addNonAvailableDateButton";
		} else if ("Save Linked Case Button".equals(buttonName)) {
			elementId = "saveLinkedCaseButton";
		} else if ("Save Non Available Date Button".equals(buttonName)) {
			elementId = "saveNonAvailableDateButton";
		} else if ("Change Allocation Judge Button".equals(buttonName)) {
			elementId = "changeAllocationJudgeButton";
		} else if ("Save Judge Button".equals(buttonName)) {
			elementId = "saveJudgeButton";
		} else if ("De-allocate Judge Button".equals(buttonName)) {
			elementId = "deallocateJudgeButton";
		}
		SeleniumConnector.waitForElementClickableBySelector(elementId, "id");
		SeleniumConnector.clickAndWait(elementId);
	}

	@Then("^I click \"(.*?)\" button for \"(.*?)\" in Create Case page$")
	public void i_click_button_for_in_Create_Case_page(final String buttonText,
			final String defendantName) throws Throwable {
		final String buttonId = "Save".equals(buttonText) ? "editPersonInCase"
				+ getTableRowSuffix(defendantName) : "removePersonInCase"
				+ getTableRowSuffix(defendantName);
		SeleniumConnector.clickAndWait(buttonId);
	}

	@Then("^I click \"(.*?)\" linkedCase button for \"(.*?)\" in Create Case page$")
	public void i_click_linkedCase_button_for_in_Create_Case_page(
			final String buttonText, final String defendantName)
			throws Throwable {
		final String buttonId = "removeLinkedCase0";
		SeleniumConnector.clickAndWait(buttonId);
	}

	@Then("^I click \"(.*?)\" note button for \"(.*?)\" in Create Case page$")
	public void i_click_note_button_for_in_Create_Case_page(
			final String buttonText, final String defendantName)
			throws Throwable {
		final String buttonId = "removeNote0";
		SeleniumConnector.clickAndWait(buttonId);
	}

	@When("^I enter new case details \"(.*?)\" , \"(.*?)\" , \"(.*?)\" , \"(.*?)\" , \"(.*?)\" , \"(.*?)\" in Create Case page$")
	public void i_enter_new_case_details_in_Create_Case_page(
			final String crestCaseNumber, final String leadDefendant,
			final String numberOfDefendants, final String offenceClass,
			final String dateOfSending, final String dateOfCommittal)
			throws Throwable {
		SeleniumConnector.setInputValueBySelector("crestCaseNumber",
				crestCaseNumber, "id");
		SeleniumConnector.pressKey("addCase", Keys.ENTER);
		SeleniumConnector.waitForTextPresent("Date of Sending ");
		SeleniumConnector.setInputValueBySelector("leadDefendant",
				leadDefendant, "id");
		SeleniumConnector.setInputValueBySelector("numberOfDefendent",
				numberOfDefendants, "id");
		SeleniumConnector.selectFromDropDown("offenceClass", offenceClass);
		i_enter_date_in(dateOfSending, "dateOfSending");
		i_enter_date_in(dateOfCommittal, "dateOfCommittal");
	}

	@When("^I enter defendant name \"(.*?)\" ,custody status \"(.*?)\" ,custody expiry date \"(.*?)\" ,urn \"(.*?)\" in Create Case page$")
	public void i_enter_defendant_name_custody_status_custody_expiry_date_urn_in_Create_Case_page(
			final String defendantName, final String bailCustodyStatus,
			final String custodyTimeLimitExpiryDate, final String defendantURN)
			throws Throwable {
		SeleniumConnector.setInputValueBySelector("defendantName",
				defendantName, "id");
		SeleniumConnector.isSelectOptionPresent("custodyStatus",
				bailCustodyStatus);
		SeleniumConnector
				.selectFromDropDown("custodyStatus", bailCustodyStatus);
		i_enter_date_in(custodyTimeLimitExpiryDate, "ctlExpiryDate");
		SeleniumConnector.setInputValueBySelector("defendantURN", defendantURN,
				"id");
	}

	@Then("^I see defendant details \"(.*?)\" , \"(.*?)\" , \"(.*?)\" , \"(.*?)\" in View Case page$")
	public void i_see_defendant_details_in_View_Case_page(
			final String defendantName, final String bailCustodyStatus,
			final String custodyTimeLimitExpiryDate, final String defendantURN)
			throws Throwable {
		int row = 1; // Starting row for search
		int noOfDays = 0; // Expiry day from today
		boolean rowFound = false; // Expected table row is found or not
		String actualDefName, actualDefURN, actualCustStatus, actualExpiresOn;
		SeleniumConnector.waitForTextPresent(defendantName);
		final int defendantsOnPage = SeleniumConnector
				.getNumberOfTableBodyRows("peopleInCase");

		final Matcher expected = Pattern.compile("\\d+").matcher(
				custodyTimeLimitExpiryDate);
		if (expected.find()) {
			noOfDays = Integer.parseInt(expected.group());
		}
		final Date dateToEnter = DateTimeUtils.getEndWorkingDateFromStartDate(
				new Date(), noOfDays + 1);
		String expectedDate = DateFormatUtils.format(dateToEnter,
				DateFormats.STANDARD.getValue());
		if (bailCustodyStatus.equals(CustodyStatus.BAIL.getValue())) {
			expectedDate = "";
		}

		while (row <= defendantsOnPage) {
			actualDefName = SeleniumConnector.getTextFromTable("peopleInCase",
					row, 1);
			actualDefURN = SeleniumConnector.getTextFromTable("peopleInCase",
					row, 2);
			actualCustStatus = SeleniumConnector.getTextFromTable(
					"peopleInCase", row, 3);
			actualExpiresOn = SeleniumConnector.getTextFromTable(
					"peopleInCase", row, 4);
			log.info("ACTUAL DATA on PAGE: Def Name: " + actualDefName
					+ " URN: " + actualDefURN + " Cust Status: "
					+ actualCustStatus + " Expires On: " + actualExpiresOn);
			log.info("EXPECTED: Def Name: " + defendantName + " URN: "
					+ defendantURN + " Cust Status: " + bailCustodyStatus
					+ " Expires On: " + expectedDate);
			if (defendantName.equals(actualDefName)
					&& defendantURN.equals(actualDefURN)
					&& bailCustodyStatus.equals(actualCustStatus)
					&& expectedDate.equals(actualExpiresOn)) {
				rowFound = true;
				break;
			}
			row++;
		}
		// Row matching all defendant details have been found.
		assertTrue(rowFound);
	}

	// Find the table row which has the defendantName.
	public String getTableRowSuffix(final String defendantName) {
		String suffix = "0";
		String id = "0";
		final String rowFinder = ".//*[@id = 'peopleInCase']//tbody//td//input[contains(@value,'"
				+ defendantName + "')]";
		try {
			id = SeleniumConnector.getAttribute("id", rowFinder, "xpath");
		} catch (final Exception e) {
		}
		final Matcher expected = Pattern.compile("\\d+").matcher(id);
		if (expected.find()) {
			suffix = expected.group();
		}
		return suffix;
	}

	@Then("^I see defendant details \"(.*?)\" , \"(.*?)\" , \"(.*?)\" , \"(.*?)\" in Create Case page$")
	public void i_see_defendant_details_in_Create_Case_page(
			final String defendantName, final String bailCustodyStatus,
			final String custodyTimeLimitExpiryDate, final String defendantURN)
			throws Throwable {
		int noOfDays = 0; // Expiry day from today
		String row, expectedDate, actualDefName, actualDefURN, actualCustStatus, actualExpiresOn;
		SeleniumConnector.waitForElementToDisappear("defendantName", "id");
		row = String.valueOf(getTableRowSuffix(defendantName));
		final Matcher expected = Pattern.compile("\\d+").matcher(
				custodyTimeLimitExpiryDate);
		if (expected.find()) {
			noOfDays = Integer.parseInt(expected.group());
		}

		final Date dateToEnter = DateTimeUtils.getEndWorkingDateFromStartDate(
				new Date(), noOfDays + 1);
		expectedDate = DateFormatUtils.format(dateToEnter,
				DateFormats.STANDARD.getValue());
		if (bailCustodyStatus.equals(CustodyStatus.BAIL.getValue())) {
			expectedDate = "";
		}
		actualDefName = SeleniumConnector.getAttribute("value",
				"personInCaseFullnameSelected" + row, "id");
		actualDefURN = SeleniumConnector.getAttribute("value",
				"personInCaseCrestURNSelected" + row, "id");
		actualCustStatus = SeleniumConnector
				.getSelectedOption("personInCaseCustodyStatusSelected" + row);
		actualExpiresOn = SeleniumConnector.getAttribute("value",
				"personInCaseCtlExpiryDateSelected" + row, "id");
		log.info("ACTUAL DATA on PAGE: Def Name: " + actualDefName + " URN: "
				+ actualDefURN + " Cust Status: " + actualCustStatus
				+ " Expires On: " + actualExpiresOn);
		log.info("EXPECTED: Def Name: " + defendantName + " URN: "
				+ defendantURN + " Cust Status: " + bailCustodyStatus
				+ " Expires On: " + expectedDate);
		assertTrue(actualDefName.equals(defendantName));
		assertTrue(actualDefURN.equals(defendantURN));
		assertTrue(actualCustStatus.equals(bailCustodyStatus));
		assertTrue(actualExpiresOn.equals(expectedDate));
	}

	@Then("^I update defendant details for \"(.*?)\" to \"(.*?)\" ,custody status \"(.*?)\" ,custody expiry date \"(.*?)\" ,urn \"(.*?)\" in Create Case page$")
	public void i_update_defendant_name_custody_status_custody_expiry_date_urn_in_Create_Case_page(
			final String origDef, final String defendantName,
			final String bailCustodyStatus,
			final String custodyTimeLimitExpiryDate, final String defendantURN)
			throws Throwable {
		final String row = String.valueOf(getTableRowSuffix(origDef));
		SeleniumConnector.setInputValueBySelector(
				"personInCaseFullnameSelected" + row, defendantName, "id");
		SeleniumConnector.setInputValueBySelector(
				"personInCaseCrestURNSelected" + row, defendantURN, "id");
		SeleniumConnector.selectFromDropDown(
				"personInCaseCustodyStatusSelected" + row, bailCustodyStatus);
		int noOfDays = 0;
		final Matcher expected = Pattern.compile("\\d+").matcher(
				custodyTimeLimitExpiryDate);
		if (expected.find()) {
			noOfDays = Integer.parseInt(expected.group());
		}
		final Date dateToEnter = DateTimeUtils.getEndWorkingDateFromStartDate(
				new Date(), noOfDays + 1);
		SeleniumConnector.waitForElementClickableBySelector(
				"personInCaseCtlExpiryDateSelected" + row, "id");
		SeleniumConnector.enterDateInSelector(
				"personInCaseCtlExpiryDateSelected" + row, dateToEnter);
	}

	@Then("^I do not see defendant details \"(.*?)\" , \"(.*?)\"$")
	public void i_do_not_see_defendant_details_in_Create_Case_page(
			final String defendantName, final String defendantURN)
			throws Throwable {
		assertTrue(SeleniumConnector.isTextNotPresent(defendantName));
		assertTrue(SeleniumConnector.isTextNotPresent(defendantURN));
	}

	@Then("^I search for case \"(.*?)\" which has \"(.*?)\"$")
	public void i_search_for_case(final String crestCaseNumber,
			final String leadDefendant) throws Throwable {
		SeleniumConnector.openAndWaitForManageCase();
		SeleniumConnector.waitForTextPresent("Case Crest Number");
		SeleniumConnector.setInputValueBySelector("crestCaseNumber",
				crestCaseNumber, "id");
		SeleniumConnector.clickAndWait("getCaseDetailsButton");
		SeleniumConnector.waitForElementAttribute(
				"personInCaseFullnameSelected0", "value", leadDefendant);
	}

	@When("^I search for case \"(.*?)\" with number of defendants \"(.*?)\"$")
	public void i_search_for_case_with_number_of_defendants(
			final String crestCaseNumber, final String noOfDefendant)
			throws Throwable {
		SeleniumConnector.openAndWaitForManageCase();
		SeleniumConnector.waitForTextPresent("Case Crest Number");
		SeleniumConnector.setInputValueBySelector("crestCaseNumber",
				crestCaseNumber, "id");
		SeleniumConnector.clickAndWait("getCaseDetailsButton");
		SeleniumConnector.waitForElementAttribute("numberOfDefendent", "value",
				noOfDefendant);
	}

	@Then("^I look_for for case \"(.*?)\" with number of defendant \"(.*?)\"$")
	public void look_for_case_with_number_of_defendant(
			final String crestCaseNumber, final String numberOfDefendant)
			throws Throwable {
		SeleniumConnector.openAndWaitForManageCase();
		SeleniumConnector.waitForTextPresent("Case Crest Number");
		SeleniumConnector.setInputValueBySelector("crestCaseNumber",
				crestCaseNumber, "id");
		SeleniumConnector.clickAndWait("getCaseDetailsButton");
		SeleniumConnector.waitForElementAttribute("numberOfDefendent", "value",
				numberOfDefendant);
	}

	@Then("^I click Yes button for confirming \"(.*?)\"$")
	public void i_click_button_for_confrming(final String heading)
			throws Throwable {
		SeleniumConnector.waitForTextPresent(heading);
		SeleniumConnector.waitForElementClickableBySelector("saveButton", "id");
		SeleniumConnector.pressKey("saveButton", Keys.ENTER);
		try {
			SeleniumConnector.waitForElementToDisappear("saveButton", "id");
		} catch (final org.openqa.selenium.TimeoutException e) {
			SeleniumConnector.clickAndWait("saveButton");
			SeleniumConnector.waitForElementToDisappear("saveButton", "id");
		}
		SeleniumConnector.waitForTextPresent("Date of Committal");
	}

	@Then("^I click No button for confirming \"(.*?)\"$")
	public void i_click_no_button_for_confrming(final String heading)
			throws Throwable {
		SeleniumConnector.waitForTextPresent(heading);
		try {
			SeleniumConnector.waitForElementClickable("cancelButton");
			SeleniumConnector.pressKey("cancelButton", Keys.ENTER);
		} catch (final WebDriverException e) {
			SeleniumConnector.waitForElementClickable("cancelButton");
			SeleniumConnector.pressKey("cancelButton", Keys.ENTER);
		}
		SeleniumConnector.waitForElementToDisappear("cancelButton", "id");
		SeleniumConnector.waitForTextPresent("Date of Committal");
	}

	@When("^I enter Note \"(.*?)\"$")
	public void i_enter_Note(final String noteText) throws Throwable {
		SeleniumConnector.waitForElementPresent("noteDesc");
		SeleniumConnector.setInputValueBySelector("noteDesc", noteText, "id");
	}

	@When("^I enter Linked Case \"(.*?)\"$")
	public void i_enter_linked_case(final String noteText) throws Throwable {
		SeleniumConnector.waitForElementPresent("linkedCaseNumber");
		SeleniumConnector.setInputValueBySelector("linkedCaseNumber", noteText,
				"id");
	}

	@When("^I enter reason \"(.*?)\" with start date \"(.*?)\" and end date \"(.*?)\"$")
	public void i_enter_reason(final String noteText, final String startDate,
			final String endDate) throws Throwable {
		SeleniumConnector.waitForElementPresent("notAvailabilityReason");
		SeleniumConnector.setInputValueBySelector("notAvailabilityReason",
				noteText, "id");
		i_enter_date_in(startDate, "notAvailabilityStartDate");
		i_enter_date_in(endDate, "notAvailabilityEndDate");
	}

	@When("^I click \"(.*?)\" button to Save the Note$")
	public void i_click_button_to_Save_the_Note(final String addButton)
			throws Throwable {
		SeleniumConnector.waitForElementClickableBySelector("saveNoteButton",
				"id");
		SeleniumConnector.clickAndWaitBySelector("saveNoteButton", "id");
	}

	@Then("^I see Note \"(.*?)\"$")
	public void i_see_Note(final String noteText) throws Throwable {
		SeleniumConnector.waitForTextPresent(noteText);
	}

	@Then("^I see Linked Case \"(.*?)\"$")
	public void i_see_linked_case(final String linkedCase) throws Throwable {
		SeleniumConnector.waitForTextPresent(linkedCase);
	}

	@Then("^I see reason \"(.*?)\"$")
	public void i_see_reason(final String noteText) throws Throwable {
		SeleniumConnector.waitForTextPresent(noteText);
	}

	@Then("^I find reason \"(.*?)\"$")
	public void i_find_reason(final String noteText) throws Throwable {
		assertEquals(SeleniumConnector.getValue("nonAvailableReasonSelected0"),
				noteText);
	}

	@Then("^I see Creation Date is \"(.*?)\" for \"(.*?)\"$")
	public void i_see_Creation_Date_is_for(final String day,
			final String noteText) throws Throwable {
		final String expectedDate = DateFormatUtils.format(new Date(),
				DateFormats.STANDARD.getValue());
		assertTrue(expectedDate.equals(SeleniumConnector.getTextFromTable(
				"caseNotes", 1, 1)));
	}

	@Then("^I see Diary date is \"(.*?)\"$")
	public void i_see_Diary_date_is(final String diaryDate) throws Throwable {
		String expectedDate = "";
		if (diaryDate.equals("today")) {
			expectedDate = DateFormatUtils.format(new Date(),
					DateFormats.STANDARD.getValue());
		}
		assertTrue(expectedDate.equals(SeleniumConnector.getTextFromTable(
				"caseNotes", 1, 3)));
	}

	@When("^I enter Diary date as \"(.*?)\"$")
	public void i_enter_Diary_date_as(final String day) throws Throwable {
		SeleniumConnector.enterDateInSelector("dateNote", new Date());
	}

	@Then("^I can not see Linked Case \"(.*?)\"$")
	public void i_can_not_see_Linked_Case(final String linkedCase)
			throws Throwable {
		assertTrue(SeleniumConnector.isTextNotPresent(linkedCase));
	}

	@Then("^I can not see Note \"(.*?)\"$")
	public void i_can_not_see_Note(final String noteText) throws Throwable {
		assertTrue(SeleniumConnector.isTextNotPresent(noteText));
	}

	@When("^I enter a total of (\\d+) characters in Notes field$")
	public void i_enter_a_total_of_characters_in_Notes_field(final int arg1)
			throws Throwable {
		SeleniumConnector.waitForElementPresent("noteDesc");
		SeleniumConnector.setInputValueBySelector("noteDesc", longNote, "id");
	}

	@Then("^I see save note error message \"(.*?)\"$")
	public void i_see_save_note_error_message(final String message)
			throws Throwable {
		assertTrue(message.contains(SeleniumConnector
				.getTextFromElementBySelector("errorLenghtMessage", "id")));
	}

	@Then("^I see save non availability error message \"(.*?)\"$")
	public void i_see_save_non_availability_error_message(final String message)
			throws Throwable {
		assertTrue(SeleniumConnector.getTextFromElementBySelector(
				"errorLenghtAvailabilityMessage", "id").contains(message));
	}

	@Then("^I can not see Note with (\\d+) characters$")
	public void i_can_not_see_Note_with_characters(final int arg1)
			throws Throwable {
		assertTrue(SeleniumConnector.isTextNotPresent(longNote));
	}

	@Then("^I can not see linked case \"(.*?)\"$")
	public void i_can_not_see_linked_case(final String noteText)
			throws Throwable {
		assertTrue(SeleniumConnector.isTextNotPresent(noteText));
	}

	@Then("^I can not see reason \"(.*?)\"$")
	public void i_can_not_see_reason(final String noteText) throws Throwable {
		assertTrue(SeleniumConnector.isTextNotPresent(noteText));
	}

	@Then("^I see linked case error message \"(.*?)\"$")
	public void i_see_linked_case_error_message(final String message)
			throws Throwable {
		assertTrue(message.contains(SeleniumConnector
				.getTextFromElementBySelector("errorAddNoteMessage", "id")));
	}

}