package com.utilities.mdxjson;

// This class is used to hold the ODSM record detail values for reporting.
public class ProviderDetail {

	public String epdsId;
	public String spsId;
	public String providerType;

	public ProviderDetail(String epdsId, String spsId, String providerType) {

		this.epdsId = epdsId;
		this.spsId = spsId;
		this.providerType = providerType;
	}
}
