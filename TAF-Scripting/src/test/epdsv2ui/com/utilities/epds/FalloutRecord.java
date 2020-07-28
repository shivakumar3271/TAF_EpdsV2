package com.utilities.epds;

import java.util.List;

import com.beans.mdxjson.RPAJsonTransaction;

public class FalloutRecord {

	String ticketType;
	String providerType;
//	String providerState;
//	String networks;
//	String providersImpacted;
	String providerName;
	String providerTitle;
//	String licenseNumber;
	String taxId;
	String npi;
	String practiceAddressLine1;
	String practiceAddressLine2;
	String city;
	String state;
	String zip;
	String county;

	public FalloutRecord(RPAJsonTransaction json, String txnId) {

		ticketType = "";
		providerType = "";
		providerName = "";
		providerTitle = "";
		taxId = "";
		npi = "";
		practiceAddressLine1 = "";
		practiceAddressLine2 = "";
		city = "";
		state = "";
		zip = "";
		county = "";

		ticketType = "PDM ";
		if (txnId.contains("_ADD_")) {
			ticketType = ticketType + "ADD/";
		}
		if (txnId.contains("_EDIT_")) {
			ticketType = ticketType + "EDIT/";
		}
		if (txnId.contains("_TERM_")) {
			ticketType = ticketType + "TERM/";
		}
		if (ticketType.endsWith("/")) {
			ticketType = ticketType.substring(0, ticketType.length() - 1);
		}

		try {
			providerType = json.getProfile().getProviderType();

//		providerState = "GA";
//		networks = "Commercial";
//		providersImpacted = "1";

			if (providerType.equals("" + RpaTestUtilities.ProviderType.INDIVIDUAL)) {
				providerName = json.getProfile().getNameQualifier().getIndividualNameQualifier().getProfileName()
						.getFirstName() + " "
						+ json.getProfile().getNameQualifier().getIndividualNameQualifier().getProfileName()
								.getLastName();
			} else if (providerType.equals("" + RpaTestUtilities.ProviderType.ORGANIZATION)) {
				providerName = json.getProfile().getNameQualifier().getOrgNameQualifier().getOrgName();
			} else if (providerType.equals("" + RpaTestUtilities.ProviderType.GROUP)) {
				providerName = json.getProfile().getNameQualifier().getGroupingNameQualifier().getGroupingName();
			}

			providerTitle = "";
			if (providerType.equals("" + RpaTestUtilities.ProviderType.INDIVIDUAL)) {
				List<String> titles = json.getProfile().getNameQualifier().getIndividualNameQualifier().getProfileName()
						.getProfessionalTitleCodes();
				for (String title : titles) {
					providerTitle = providerTitle + title;
				}
			}

//		licenseNumber = "NA";

			taxId = json.getProfile().getTaxIDs().get(0).getTaxIDValue();

			if (json.getAlternateIDs().getNpiDetails() != null)
				npi = json.getAlternateIDs().getNpiDetails().get(0).getNpiValue();

			practiceAddressLine1 = json.getAddresses().get(0).getAddressDetails().getAddressLine1();
			practiceAddressLine2 = json.getAddresses().get(0).getAddressDetails().getAddressLine2();
			city = json.getAddresses().get(0).getAddressDetails().getCity();
			state = json.getAddresses().get(0).getAddressDetails().getState();
			zip = json.getAddresses().get(0).getAddressDetails().getZip();
			county = json.getAddresses().get(0).getAddressDetails().getCounty();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
