package com.utilities.epds;

public class EpdsConstants {

	public static final String TERMINATION_DATE = "12/31/9999";

	// Grouping Search Tab
	public static final int PROFILE_GROUPING_NAME = 0;
	public static final int PROFILE_GROUPING_CODE = 1;
	public static final int PROFILE_GROUPING_TYPE = 2;

	// Profile Tab
	public static final int PROFILE_PDM_INDICATOR = 0;
	public static final int PROFILE_PDM_EFF_DATE = 1;
	public static final int PROFILE_PDM_TERM_DATE = 2;
	public static final int PROFILE_PDM_TERM_REASON_CODE = 3;

	public static final int PROFILE_CREDENTIALING_SOURCE_SYSTEM = 0;
	public static final int PROFILE_CREDENTIALING_STATUS = 1;
	public static final int PROFILE_CREDENTIALING_EFF_DATE = 2;
	public static final int PROFILE_CREDENTIALING_TERM_DATE = 3;

	public static final int PROFILE_TAX_ID = 0;
	public static final int PROFILE_TAX_INDICATOR = 1;
	public static final int PROFILE_TAX_EFF_DATE = 2;
	public static final int PROFILE_TAX_TERM_DATE = 3;
	public static final int PROFILE_TAX_TERM_REASON_CODE = 4;

	public static final int PROFILE_DISTINCTION_CODE = 0;
	public static final int PROFILE_DISTINCTION_EFF_DATE = 1;
	public static final int PROFILE_DISTINCTION_TERM_DATE = 2;
	public static final int PROFILE_DISTINCTION_TERM_REASON_CODE = 3;

	// Alternate ID Tab
	public static final int NPI_TYPE = 0;
	public static final int NPI_VALUE = 2;

	public static final int NPI_EFF_DATE = 4;
	public static final int NPI_TERM_DATE = 5;
	public static final int NPI_TERM_REASON_CODE = 6;

	public static final int ALTID_TYPE = 0;
	public static final int ALTID_SRC = 1;
	public static final int ALTID_VALUE = 2;
	public static final int ALTID_EFF_DATE = 3;
	public static final int ALTID_TERM_DATE = 4;
	public static final int ALTID_TERM_REASON_CODE = 5;

	// Specialty/Taxonomy Tab
	public static final int SPECIALTY_TABLE_SPECIALTY = 0;
	public static final int SPECIALTY_PRIMARY_SPL_IND = 3;
	public static final int SPECIALTY_AGENCY_NAME = 5;
	public static final int SPECIALTY_RECERTIF_DATE = 8;
	public static final int SPECIALTY_BOARD_CERTIF_EFF_DATE = 9;
	public static final int SPECIALTY_BOARD_CERTIF_EXP_DATE = 10;
	public static final int SPECIALTY_EFF_DATE = 11;
	public static final int SPECIALTY_TERM_DATE = 12;
	public static final int SPECIALTY_TERM_REASON_CODE = 13;
	public static final int SPECIALTY_ADDRESS = 14;
	public static final int SPECIALTY_ADDRESS_TYPE = 15;

	public static final int SPECIALTY_PROFILE_BILLINGFROM = 0;
	public static final int SPECIALTY_PROFILE_PRIMARY_SPL_INDICATOR = 1;
	public static final int SPECIALTY_PROFILE_SPECIALITY = 2;
	public static final int SPECIALTY_PROFILE_EFF_DATE = 5;
	public static final int SPECIALTY_PROFILE_TERM_DATE = 6;
	public static final int SPECIALTY_PROFILE_TERM_REASON_CODE = 7;

	// Education Tab
	public static final int EDUCATION_DETAILS = 1;
	public static final int EDUCATION_SCHOOL_NAME = 2;
	public static final int EDUCATION_DEGREE = 3;
	public static final int EDUCATION_SCHOOL_GRADUATION_YEAR = 4;
	public static final int EDUCATION_START_DATE = 5;
	public static final int EDUCATION_END_DATE = 6;

	// Address Tab
	public static final int ADDR_DETAILS_PRAC_ADDR_TYPE = 0;
	public static final int ADDR_DETAILS_PRAC_ADDR = 2;
	public static final int ADDR_DETAILS_EFF_DATE = 4;
	public static final int ADDR_DETAILS_TERM_DATE = 5;
	public static final int ADDR_DETAILS_PAC_IND = 7;

	public static final int ORG_ADDR_DETAILS_EFF_DATE = 5;
	public static final int ORG_ADDR_DETAILS_TERM_DATE = 6;
	public static final int ORG_ADDR_DETAILS_PAC_IND = 8;

	public static final int GRP_ADDR_DETAILS_ADDR_TYPE = 0;
	public static final int GRP_ADDR_DETAILS_ADDR = 1;
	public static final int GRP_ADDR_DETAILS_EFF_DATE = 4;
	public static final int GRP_ADDR_DETAILS_TERM_DATE = 5;
	public static final int GRP_ADDR_DETAILS_TERM_REASON_CODE = 6;

	// Address - Contact Tab
	public static final int CONTACT_LASTNAME = 0;
	public static final int CONTACT_FIRSTNAME = 1;
	public static final int CONTACT_MIDNAME = 2;
	public static final int CONTACT_TITLE = 3;
	public static final int CONTACT_PHONE_TYPE = 4;
	public static final int CONTACT_PHONE_NUMBER = 5;
	public static final int CONTACT_FAX_TYPE = 7;
	public static final int CONTACT_FAX_NUMBER = 8;
	public static final int CONTACT_EMAIL_TYPE = 9;
	public static final int CONTACT_EMAIL_ADDRESS = 10;
	public static final int CONTACT_WEB_TYPE = 11;
	public static final int CONTACT_WEB_URL = 12;

	// Address - AltID Tab
	public static final int ALTID_ALTID_TYPE = 0;
	public static final int ALTID_ALTID_SRC = 1;

	// Address - Office Details Tab
	public static final int OFFICE_ACCESS_TABLE_ACCESS_CODE = 1;

	// Address - Schedule Tab
	public static final int SCHEDULE_TABLE_SCHEDULE_TYPE = 0;
	public static final int SCHEDULE_TABLE_SCHEDULE_DAYS = 1;
	public static final int SCHEDULE_TABLE_SCHEDULE_OPENED = 2;
	public static final int SCHEDULE_TABLE_SCHEDULE_CLOSED = 3;

	// Address - Specialty Tab
	public static final int SPECIALITY_CODE = 0;

	// Address - Special Programs Tab
	public static final int SPL_PRGM_TABLE_SPL_PRGM_TYPE = 0;
	public static final int SPL_PRGM_TABLE_SPL_PRGM_EFF_DATE = 3;

	// Address - Remit Info Tab
	public static final int REMIT_INFO_CHECKNAME = 0;
	public static final int REMIT_INFO_EFF_DATE = 1;
	public static final int REMIT_INFO_TERM_DATE = 2;
	public static final int REMIT_INFO_TERM_REASON_CODE = 3;
	public static final int REMIT_INFO_NPI = 5;

	// Reimbursement/Networks Tab
	public static final int AVALABLE_ADDR_TABLE_ADDR_COL = 1;
	public static final int AVALABLE_ADDR_TABLE_CHECKBOX_COL = 0;

	public static final int AFFILITION_ADDR_TABLE_ADDR_COL = 1;
	public static final int AFFI_ADDR_TABLE_CHECKBOX_COL = 0;

	public static final int NETWORK_TABLE_NETID_COL = 2;
	public static final int NETWORK_TABLE_EFFDATE_COL = 3;
	public static final int NETWORK_TABLE_TERMDATE_COL = 4;

	public static final int REIMBNETWK_ASSOCIATE_TABLE_RMBID_VALUE = 0;
	public static final int REIMBNETWK_ASSOCIATE_TABLE_RMB_EFFDATE = 5;
	public static final int REIMBNETWK_ASSOCIATE_TABLE_RMB_TERMDATE = 6;
	public static final int REIMBNETWK_ASSOCIATE_TABLE_RMB_TERMREASON = 7;

	public static final int NETWORK_TABLE_ORG_SPECIALITY = 0;

	// added murali

	public static final int NETWORK_TABLE_ORG_REMBIDTYPE_COL = 1;
	public static final int NETWORK_TABLE_ORG_REMBIDVALUE_COL = 2;
	public static final int NETWORK_TABLE_ORG_REMEFFDATE_COL = 4;
	public static final int NETWORK_TABLE_ORG_REMTERDATE_COL = 5;
	public static final int NETWORK_TABLE_ORG_NETID_COL = 6;
	public static final int NETWORK_TABLE_ORG_NETEFFDATE_COL = 8;
	public static final int NETWORK_TABLE_ORG_NETTERDATE_COL = 9;
	public static final int NETWORK_TABLE_ORG_NETPHYADDRS_COL = 13;
	public static final int NETWORK_TABLE_ORG_NETID_COL1 = 7;

	// AffiliationsRelationships Tab
	public static final int AFFIL_TYPE = 4;
	public static final int AFFIL_LEGACY_ID = 6;
	public static final int AFFIL_PHY_ADDRESS = 8;
	public static final int AFFIL_EFF_DATE = 9;
	public static final int AFFIL_TERM_DATE = 10;
	public static final int AFFIL_TERM_REASON_CODE = 11;

	public static final int AFFIL_ORG_RELATION_TYPE = 3;
	public static final int AFFIL_ORG_PHYSICAL_ADDR = 7;

	public static final int AFFIL_GRPREL_REL_TYPE = 1;
	public static final int AFFIL_GRPREL_GRP_CODE = 2;
	public static final int AFFIL_GRPREL_PRAC_ADDRESS = 5;
	public static final int AFFIL_GRPREL_EFF_DATE = 7;
	public static final int AFFIL_GRPREL_TERM_DATE = 8;
	public static final int AFFIL_GRPREL_TERM_REASON_CODE = 9;

	// Claim Action/Sanction Tab
	public static final int CLAIM_TYPE = 0;
	public static final int CLAIM_VALUE = 1;
	public static final int CLAIM_REASON_CRITERIA = 2;
	public static final int CLAIM_LOW_RANGE = 3;
	public static final int CLAIM_HIGH_RANGE = 4;
	public static final int CLAIM_EFF_DATE = 5;
	public static final int CLAIM_TERM_DATE = 6;
	public static final int CLAIM_TERM_REASON_CODE = 7;

	// Network Tab for Group
	public static final int NTWRK_SRC_SYSTEM = 0;
	public static final int NTWRK_GRP_NTWRK_ID = 1;
	public static final int NTWRK_EFF_DATE = 5;
	public static final int NTWRK_TERM_DATE = 6;
	public static final int NTWRK_TERM_REASON_CODE = 7;

	// Network Tab for Group
	public static final int AFFIL_PROV_NAME = 0;
	public static final int AFFIL_GRP_CODE = 1;
	public static final int AFFIL_ENT_ID = 2;
	public static final int AFFIL_TAX_ID = 3;
	public static final int AFFIL_ADDRESS = 5;
	public static final int AFFIL_EFFECTIVE_DATE = 6;
	public static final int AFFIL_TERMINATION_DATE = 7;
	public static final int AFFIL_TERMINATION_CODE = 8;

	public static final int PROV_NAME = 0;
	public static final int PROV_CODE = 1;
	public static final int PROV_PCP_ID = 2;
	public static final int PROV_ADDRESS = 5;
	public static final int PROV_EFFECTIVE_DATE = 7;
	public static final int PROV_TERMINATION_DATE = 8;
	public static final int PROV_TERMINATION_CODE = 9;

	// Wait variables
	public static final long HIGH_THREAD_VALUE = 15000;
	public static final long MEDIUM_THREAD_VALUE = 6000;
	public static final long LOW_THREAD_VALUE = 3000;
	
	public static final String TIMELY_FILING = "180";

}
