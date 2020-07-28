package com.pages.EPDSPageObjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.scripted.web.WebHandlers;

public class LoginPage {

	public static Logger LOGGER = Logger.getLogger(LoginPage.class);

	WebDriver driver;

	@FindBy(xpath = "//input[@id='USER']")
	WebElement userNameTxt;

	@FindBy(xpath = "//input[@id='PASSWORD']")
	WebElement passwordTxt;

	@FindBy(xpath = "//button[@name='Logon']")
	WebElement logonBtn;

	@FindBy(linkText = "Profile")
	WebElement profileTab;

	@FindBy(linkText = "Alternate ID")
	WebElement altIdTab;

	@FindBy(linkText = "Alternate IDs")
	WebElement grpAltIdTab;

	@FindBy(linkText = "Specialty / Taxonomy")
	WebElement specialityTaxonomy;

	@FindBy(linkText = "Education")
	WebElement educationTab;

	@FindBy(linkText = "Address")
	WebElement addressTab;

	@FindBy(linkText = "Reimbursement/Networks")
	WebElement reimbursementNtwrk;

	@FindBy(linkText = "Legacy Info")
	WebElement legacyInfo;

	@FindBy(linkText = "Affiliations/Relationships")
	WebElement affilRelation;

	@FindBy(linkText = "Claim Action/Sanction")
	WebElement claimActnSanctn;

	@FindBy(linkText = "Notes")
	WebElement notesTab;

	@FindBy(linkText = "Affiliation/Relationship")
	WebElement grpAffiliations;

	@FindBy(linkText = "History")
	WebElement historyTab;

	@FindBy(linkText = "Network")
	WebElement grpNetworkTab;

	@FindBy(linkText = "Reimbursement")
	WebElement grpReimbTab;

	public LoginPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void loginToEpdsv2(String username, String pwd) {
		WebHandlers.enterText(userNameTxt, username);
		WebHandlers.enterText(passwordTxt, pwd);
		WebHandlers.click(logonBtn);
	}

	public void navToProfileTab() {
		WebHandlers.click(profileTab);
	}

	public void navToAltIdTab() {
		WebHandlers.click(altIdTab);
	}

	public void navToGrpAltIdTab() {
		WebHandlers.click(grpAltIdTab);
	}

	public void navToSpecialityTaxonomyTab() {
		WebHandlers.click(specialityTaxonomy);
	}

	public void navToEducationTab() {
		WebHandlers.click(educationTab);
	}

	public void navToAddressTab() {
		WebHandlers.click(addressTab);
	}

	public void navToReimbNtwrkTab() {
		WebHandlers.click(reimbursementNtwrk);
	}

	public void navToLegacyInfoTab() {
		WebHandlers.click(legacyInfo);
	}

	public void navToAffilRelationTab() {
		WebHandlers.click(affilRelation);
	}

	public void navToClaimActSanctTab() {
		WebHandlers.click(claimActnSanctn);
	}

	public void navToNotesTab() {
		WebHandlers.click(notesTab);
	}

	public void navToGrpngAffilTab() {
		WebHandlers.click(grpAffiliations);
	}

	public void navToHistoryTab() {
		WebHandlers.click(historyTab);
	}

	public void navToGrpNetworkTab() {
		WebHandlers.click(grpNetworkTab);
	}

	public void navToGrpReimbTab() {
		WebHandlers.click(grpReimbTab);
	}

}