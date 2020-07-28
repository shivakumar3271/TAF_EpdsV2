package com.pages.EPDSPageObjects;

import java.util.Objects;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.beans.mdxjson.CredentialingDetail;
import com.beans.mdxjson.PdmIndicatorDetail;
import com.beans.mdxjson.ProviderDistinctionDetail;
import com.beans.mdxjson.TaxID;
import com.scripted.web.WebHandlers;
import com.scripted.web.WebWaitHelper;
import com.utilities.epds.EpdsConstants;

public class ProfilePage {

	public static Logger LOGGER = Logger.getLogger(ProfilePage.class);

	WebDriver driver;

	@FindBy(xpath = "//*[@id='prvInfo-form:effDate']")
	WebElement indEffDateLbl;

	@FindBy(xpath = "//*[@id=\"prvInfo-form:termDate\"]")
	WebElement indTermDateLbl;

	@FindBy(xpath = "//*[@id=\"prvInfo-form:indterreasoncode\"]")
	WebElement indTermReasonCode;

	@FindBy(xpath = "//label[@id='prvInfo-form:indfname']")
	WebElement indProvFnameLbl;

	@FindBy(xpath = "//label[@id='prvInfo-form:indmname']")
	WebElement indProvMnameLbl;

	@FindBy(xpath = "//label[@id='prvInfo-form:indlname']")
	WebElement indProvLnameLbl;

	@FindBy(xpath = "//*[@id='prvInfo-form:provPanel_content']/table[3]/tbody/tr[1]/td[4]/table/tbody/tr/td[2]/label")
	WebElement indProvSuffixLbl;

	@FindBy(xpath = "//*[@id='prvInfo-form:provPanel_content']/table[3]/tbody/tr[2]/td[1]/table/tbody/tr/td[2]/label")
	WebElement indProvGenderLbl;

	@FindBy(xpath = "//*[@id='prvInfo-form:provPanel_content']/table[3]/tbody/tr[2]/td[2]/table/tbody/tr/td[2]/label")
	WebElement indProvDobLbl;

	@FindBy(xpath = "//*[@id=\"prvInfo-form:indeth1\"]")
	WebElement indEthnicityLbl;

	@FindBy(xpath = "//*[@id='prvInfo-form:provPanel_content']/table[3]/tbody/tr[4]/td[1]/table/tbody/tr/td[2]/label")
	WebElement indAliasFirstName;

	@FindBy(xpath = "//*[@id='prvInfo-form:provPanel_content']/table[3]/tbody/tr[4]/td[2]/table/tbody/tr/td[2]/label")
	WebElement indAliasMiddleName;

	@FindBy(xpath = "//*[@id='prvInfo-form:provPanel_content']/table[3]/tbody/tr[4]/td[3]/table/tbody/tr/td[2]/label")
	WebElement indAliasLastName;

	@FindBy(xpath = "//*[@id='prvInfo-form:provPanel_content']/table[3]/tbody/tr[4]/td[4]/table/tbody/tr/td[2]/label")
	WebElement indAliasSuffix;

	@FindBy(xpath = "//*[@id=\"prvInfo-form:profTitleLess\"]")
	WebElement indProfessionalTitleTxt;

	@FindBy(xpath = "//*[@id=\"prvInfo-form:indAreaofFocus\"]/tbody/tr/td/textarea")
	WebElement indAreaofFocusTxt;

	@FindBy(xpath = "//*[@id=\"prvInfo-form:indLang\"]/tbody/tr/td/textarea")
	WebElement indProvLangTxt;

	@FindBy(xpath = "//label[@id='prvInfo-form:orgname']")
	WebElement orgProvNameLbl;

	@FindBy(xpath = "//*[@id='prvInfo-form:orgPanelGrid']/tbody/tr[2]/td[1]/table/tbody/tr/td[2]/span")
	WebElement orgProvEffDateTData;

	@FindBy(xpath = "//*[@id='prvInfo-form:orgPanelGrid']/tbody/tr[3]/td[1]/table/tbody/tr/td[2]/span")
	WebElement orgProvTermDateTData;

	@FindBy(xpath = "//*[@id='prvInfo-form:orgPanelGrid']/tbody/tr[4]/td[1]/table/tbody/tr/td[2]")
	WebElement orgProvTermReasonCodeTData;

	@FindBy(xpath = "//*[@id=\"contentDivision\"]/div[2]/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr[2]/td[1]")
	WebElement grpNameTData;

	@FindBy(xpath = "//*[@id='contentDivision']/div[2]/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr[2]/td[2]")
	WebElement grpSiteCode;

	@FindBy(xpath = "//*[@id='contentDivision']/div[2]/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr[2]/td[3]")
	WebElement grpTypeCode;

	@FindBy(xpath = "//*[@id=\"contentDivision\"]/div[2]/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr[4]/td[2]")
	WebElement grpEffDateTData;

	@FindBy(xpath = "//*[@id=\"contentDivision\"]/div[2]/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr[4]/td[3]")
	WebElement grpTermDateTData;

	@FindBy(xpath = "//*[@id=\"contentDivision\"]/div[2]/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr[6]/td[1]")
	WebElement grpTermReasonCodeTData;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_pdmUpdate_data']")
	WebElement pdmIndicatorTable;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_pdmUpdate:globalFilter']")
	WebElement pdmIndicatorFilter;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_Credential_data']")
	WebElement credentialingTable;

	@FindBy(xpath = "//*[@id=\"prvInfo-form:dtble_Credential:globalFilter\"]")
	WebElement credentialingFilter;

	@FindBy(xpath = "//*[@id='prvInfo-form:Dt_taxIDViewUpdate_data']")
	WebElement taxIdTable;

	@FindBy(xpath = "//*[@id=\"prvInfo-form:dtble_taxID:globalFilter\"]")
	WebElement taxIdFilter;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_provDistinction_data']")
	WebElement distinctionTable;

	@FindBy(xpath = "//*[@id=\"prvInfo-form:dtble_provDistinction:globalFilter\"]")
	WebElement distinctionFilter;

	@FindBy(xpath = "//*[@id='prvInfo-form:Dt_taxIDViewUpdate_data']")
	WebElement grpTaxIdTable;

	public ProfilePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public String getIndEffectiveDate() {
		return (indEffDateLbl.getText());
	}

	public String getIndTerminationDate() {
		return (indTermDateLbl.getText());
	}

	public String getIndTermReasonCode() {
		return (indTermReasonCode.getText());
	}

	public String getIndProvFirstname() {
		return (indProvFnameLbl.getText());
	}

	public String getIndProvMidname() {
		return (indProvMnameLbl.getText());
	}

	public String getIndProvLastname() {
		return (indProvLnameLbl.getText());
	}

	public String getIndProvSuffix() {
		return (indProvSuffixLbl.getText());
	}

	public String getIndProvGender() {
		return (indProvGenderLbl.getText());
	}

	public String getIndProvDOB() {
		return (indProvDobLbl.getText());
	}

	public String getIndEthnicity() {
		return (indEthnicityLbl.getText());
	}

	public String getIndAliasFirstName() {
		return indAliasFirstName.getText();
	}

	public String getIndAliasMiddleName() {
		return indAliasMiddleName.getText();
	}

	public String getIndAliasLastName() {
		return indAliasLastName.getText();
	}

	public String getIndAliasSuffix() {
		return indAliasSuffix.getText();
	}

	public String getIndProfessionalTitle() {
		return (indProfessionalTitleTxt.getText());
	}

	public String getIndAreaOfFocus() {
		WebHandlers.scrollToElement(indAreaofFocusTxt);
		return (indAreaofFocusTxt.getText());
	}

	public String getIndProviderLanguages() {
		WebHandlers.scrollToElement(indProvLangTxt);
		return (indProvLangTxt.getText());
	}

	public String getOrgProvName() {
		return (orgProvNameLbl.getText());
	}

	public String getOrgProvEffDate() {
		return orgProvEffDateTData.getText();
	}

	public String getOrgProvTermDate() {
		return orgProvTermDateTData.getText();
	}

	public String getOrgProvTermReasonCode() {
		return orgProvTermReasonCodeTData.getText();
	}

	public String getGroupingName() {
		return (grpNameTData.getText());
	}

	public String getGroupingSiteCode() {
		return (grpSiteCode.getText());
	}

	public String getGroupingTypeCode() {
		return (grpTypeCode.getText());
	}

	public String getGroupingEffDate() {
		return (grpEffDateTData.getText());
	}

	public String getGroupingTermDate() {
		return (grpTermDateTData.getText());
	}

	public String getGroupingTermReasonCode() {
		return (grpTermReasonCodeTData.getText());
	}

	public boolean searchPDMIndicators(PdmIndicatorDetail pdmIndDetails) throws InterruptedException {

		String pdmIndicator = pdmIndDetails.getPdmIndicator();
		String pdmEffectiveDate = pdmIndDetails.getPdmIndicatorActive().getEffectiveDate();
		String pdmTermDate = pdmIndDetails.getPdmIndicatorActive().getTerminationDate();
		String pdmTermCode = Objects.toString(pdmIndDetails.getPdmIndicatorActive().getTerminationReasonCode(), "");
		String terminationDate = EpdsConstants.TERMINATION_DATE;

		WebWaitHelper.waitForElement(pdmIndicatorFilter);
		pdmIndicatorFilter.click();
		pdmIndicatorFilter.clear();
		Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);

		WebHandlers.enterText(pdmIndicatorFilter, pdmIndicator);
		Thread.sleep(EpdsConstants.MEDIUM_THREAD_VALUE);

		WebWaitHelper.waitForElement(pdmIndicatorTable);
		String srchStr = WebHandlers.getTblTdVal(pdmIndicatorTable, 0, 0);
		if (!(srchStr.equals("No records found."))) {
			int rowCount = WebHandlers.getTblRowCount(pdmIndicatorTable);
			LOGGER.info("PDM Indicator table row count : " + rowCount);

			for (int i = 0; i < rowCount; i++) {
				srchStr = WebHandlers.getTblTdVal(pdmIndicatorTable, i, EpdsConstants.PROFILE_PDM_INDICATOR);
				if (srchStr.equalsIgnoreCase(pdmIndicator)) {
					srchStr = WebHandlers.getTblTdVal(pdmIndicatorTable, i, EpdsConstants.PROFILE_PDM_EFF_DATE);
					if (srchStr.equals(pdmEffectiveDate)) {
						srchStr = WebHandlers.getTblTdVal(pdmIndicatorTable, i, EpdsConstants.PROFILE_PDM_TERM_DATE);
						if (srchStr.equals(pdmTermDate) && srchStr.equals(terminationDate)) {
							return true;
						} else {
							if (srchStr.equalsIgnoreCase(pdmTermDate)) {
								srchStr = WebHandlers.getTblTdVal(pdmIndicatorTable, i,
										EpdsConstants.PROFILE_PDM_TERM_REASON_CODE);
								System.out.println(srchStr);
								if (srchStr.equalsIgnoreCase(pdmTermCode) && srchStr != null) {
									return true;
								}
							}
						}
					}
				}
			}
		}
		LOGGER.info("Matching PDM indicator record is not found");
		return false;
	}

	public boolean searchCredentialingDetails(CredentialingDetail credential) throws InterruptedException {

		String credentialingSource = credential.getCredentialingSource();
		String credentialingStatus = credential.getCredentialingStatus();
		String effDate = credential.getCredentialingActive().getEffectiveDate();
		String termDate = Objects.toString(credential.getCredentialingActive().getTerminationDate(), "");
		String terminationDate = EpdsConstants.TERMINATION_DATE;

		WebWaitHelper.waitForElement(credentialingTable);
		credentialingFilter.click();
		credentialingFilter.clear();
		Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);
		WebHandlers.enterText(credentialingFilter, credentialingSource);
		Thread.sleep(EpdsConstants.MEDIUM_THREAD_VALUE);
		String srchStr = WebHandlers.getTblTdVal(credentialingTable, 0, 0);
		if (!(srchStr.equals("No records found."))) {
			int rowCount = WebHandlers.getTblRowCount(credentialingTable);
			LOGGER.info("Credentialing table row count : " + rowCount);

			for (int i = 0; i < rowCount; i++) {
				srchStr = WebHandlers.getTblTdVal(credentialingTable, i,
						EpdsConstants.PROFILE_CREDENTIALING_SOURCE_SYSTEM);
				if (srchStr.equalsIgnoreCase(credentialingSource)) {
					srchStr = WebHandlers.getTblTdVal(credentialingTable, i,
							EpdsConstants.PROFILE_CREDENTIALING_STATUS);
					if (srchStr.equalsIgnoreCase(credentialingStatus)) {
						srchStr = WebHandlers.getTblTdVal(credentialingTable, i,
								EpdsConstants.PROFILE_CREDENTIALING_EFF_DATE);
						if (srchStr.equals(effDate)) {
							srchStr = WebHandlers.getTblTdVal(credentialingTable, i,
									EpdsConstants.PROFILE_CREDENTIALING_TERM_DATE);
							if (srchStr.equals(termDate) && srchStr.equals(terminationDate)) {
								return true;
							} else {
								if (srchStr.equalsIgnoreCase(termDate)) {
									srchStr = WebHandlers.getTblTdVal(pdmIndicatorTable, i,
											EpdsConstants.PROFILE_PDM_TERM_REASON_CODE);
									System.out.println(srchStr);
									if (srchStr.equalsIgnoreCase(termDate) && srchStr != null) {
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		LOGGER.info("Matching Credentialing detail record is not found.");
		return false;
	}

	public boolean searchTaxDetails(TaxID taxIdDetails) throws InterruptedException {

		String expTaxIdVal = taxIdDetails.getTaxIDValue();
		String expTaxIdIndicator = taxIdDetails.getTaxIDIndicator();
		String expEffDate = taxIdDetails.getTaxIDActive().getEffectiveDate();
		String expTermDate = taxIdDetails.getTaxIDActive().getTerminationDate();
		String expTermReasonCode = Objects.toString(taxIdDetails.getTaxIDActive().getTerminationReasonCode(), "");
		String terminationDate = EpdsConstants.TERMINATION_DATE;
		WebWaitHelper.waitForElement(taxIdTable);
		taxIdFilter.click();
		taxIdFilter.clear();
		Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);
		WebHandlers.enterText(taxIdFilter, expTaxIdVal);
		Thread.sleep(EpdsConstants.HIGH_THREAD_VALUE);
		String srchStr = WebHandlers.getTblTdVal(taxIdTable, 0, 0);
		
		if (!(srchStr.equalsIgnoreCase("No records found."))) {
			int rowCount = WebHandlers.getTblRowCount(taxIdTable);
			LOGGER.info("Tax ID table row count : " + rowCount);
			for (int i = 0; i < rowCount; i++) {
				srchStr = WebHandlers.getTblTdVal(taxIdTable, i, EpdsConstants.PROFILE_TAX_ID);
				if (srchStr.equals(expTaxIdVal)) {
					srchStr = WebHandlers.getTblTdVal(taxIdTable, i, EpdsConstants.PROFILE_TAX_INDICATOR);
					if (srchStr.equalsIgnoreCase(expTaxIdIndicator)) {
						srchStr = WebHandlers.getTblTdVal(taxIdTable, i, EpdsConstants.PROFILE_TAX_EFF_DATE);
						if (srchStr.equals(expEffDate)) {
							srchStr = WebHandlers.getTblTdVal(taxIdTable, i, EpdsConstants.PROFILE_TAX_TERM_DATE);
							if (srchStr.equals(expTermDate) && srchStr.equals(terminationDate)) {
								return true;
							} else {
								if (srchStr.equals(expTermDate)) {
									srchStr = WebHandlers.getTblTdVal(taxIdTable, i,
											EpdsConstants.PROFILE_DISTINCTION_TERM_REASON_CODE);
									if (srchStr.equalsIgnoreCase(expTermReasonCode) && srchStr != null) {
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		LOGGER.info("Matching Tax Id record is not found.");
		return false;
	}

	public boolean searchGrpTaxId(TaxID taxId) throws InterruptedException {
		String expTaxIdEffDate = taxId.getTaxIDActive().getEffectiveDate();
		String expTaxIdTermDate = taxId.getTaxIDActive().getTerminationDate();
		String expTaxIdTermReasonCode = Objects.toString(taxId.getTaxIDActive().getTerminationReasonCode(),"");
		if (expTaxIdTermReasonCode == null)
			expTaxIdTermReasonCode = "";
		WebWaitHelper.waitForElement(grpTaxIdTable);
		Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);
		String srchStr = WebHandlers.getTblTdVal(taxIdTable, 0, 0);

		if (!(srchStr.equalsIgnoreCase("No records found."))) {
			int rowCount = WebHandlers.getTblRowCount(grpTaxIdTable);
			LOGGER.info("Tax ID table row count : " + rowCount);
			for (int i = 0; i < rowCount; i++) {
				srchStr = WebHandlers.getTblTdVal(grpTaxIdTable, i, EpdsConstants.PROFILE_TAX_EFF_DATE);
				if (srchStr.equals(expTaxIdEffDate)) {
					srchStr = WebHandlers.getTblTdVal(grpTaxIdTable, i, EpdsConstants.PROFILE_TAX_TERM_DATE);
					if (srchStr.equals(expTaxIdTermDate)) {
						srchStr = WebHandlers.getTblTdVal(grpTaxIdTable, i, EpdsConstants.PROFILE_TAX_TERM_REASON_CODE);
						if (srchStr.equalsIgnoreCase(expTaxIdTermReasonCode)) {
							return true;
						}
					}
				}
			}
		}
		LOGGER.info("Matching Tax Id record is not found.");
		return false;
	}

	public boolean searchDistinctionDetails(ProviderDistinctionDetail distinctionDetails) throws InterruptedException {

		String expProviderDistinctionCode = distinctionDetails.getProviderDistinctionCode();
		String expEffectiveDate = distinctionDetails.getProviderDistinctionDetailsActive().getEffectiveDate();
		String expTerminationDate = distinctionDetails.getProviderDistinctionDetailsActive().getTerminationDate();
		String expTermCode = Objects.toString(distinctionDetails.getProviderDistinctionDetailsActive().getTerminationReasonCode(), "");
		String terminationDate = EpdsConstants.TERMINATION_DATE;
		WebWaitHelper.waitForElement(distinctionTable);
		distinctionFilter.click();
		distinctionFilter.clear();
		Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);
		WebHandlers.enterText(distinctionFilter, expProviderDistinctionCode);
		Thread.sleep(EpdsConstants.MEDIUM_THREAD_VALUE);
		String srchStr = WebHandlers.getTblTdVal(distinctionTable, 0, 0);
		
		if (!(srchStr.equals("No records found."))) {
			int rowCount = WebHandlers.getTblRowCount(distinctionTable);
			LOGGER.info("Distinction table row count : " + rowCount);
			for (int i = 0; i < rowCount; i++) {
				srchStr = WebHandlers.getTblTdVal(distinctionTable, i, EpdsConstants.PROFILE_DISTINCTION_CODE);
				if (srchStr.equalsIgnoreCase(expProviderDistinctionCode)) {
					srchStr = WebHandlers.getTblTdVal(distinctionTable, i, EpdsConstants.PROFILE_DISTINCTION_EFF_DATE);
					if (srchStr.equals(expEffectiveDate)) {
						srchStr = WebHandlers.getTblTdVal(distinctionTable, i,
								EpdsConstants.PROFILE_DISTINCTION_TERM_DATE);
						if (srchStr.equals(expTerminationDate) && srchStr.equals(terminationDate)) {
							return true;
						} else {
							if (srchStr.equals(expTerminationDate)) {
								srchStr = WebHandlers.getTblTdVal(distinctionTable, i,
										EpdsConstants.PROFILE_DISTINCTION_TERM_REASON_CODE);
								if (srchStr.equalsIgnoreCase(expTermCode) && srchStr != null) {
									return true;
								}
							}

						}
					}
				}
			}
		}
		LOGGER.info("Matching Distinction detail record is not found.");
		return false;
	}
}
