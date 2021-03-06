package com.utilities.epds;

public class MdxJsonQueries {

	public static final String MDX_JSON_IND_TXN_QUERY = "SELECT REQUEST\r\n" + 
			",PROV_EPDSV2_KEY\r\n" + 
			",PROV_MK\r\n" + 
			",MSTR_PROV_ID\r\n" + 
			",ENGINEBATCHID\r\n" + 
			"FROM\r\n" + 
			"(\r\n" + 
			"SELECT EQR.REQUEST\r\n" + 
			",IXTR.PROV_EPDSV2_KEY\r\n" + 
			",M.PROV_MK\r\n" + 
			",M.MSTR_PROV_ID\r\n" + 
			",EQ.ENGINEBATCHID\r\n" + 
			",DENSE_RANK() OVER (PARTITION BY IXTR.PROV_EPDSV2_KEY ORDER BY EQ.ENGINEBATCHID DESC) AS RNK\r\n" + 
			"FROM (\r\n" + 
			"SELECT * FROM SPSMDX_ODS.ODSM.PROV\r\n" + 
			"WHERE PROV_MK IN ( \r\n" + 
			"SELECT DISTINCT PROV_MK FROM SPSMDX_ODS.ODSX.PROV\r\n" + 
			"WHERE SYSTEM_ID='17') \r\n" + 
			")M\r\n" + 
			"INNER JOIN SPSMDX_ODS.ODSE_GA.PROV_IND_PROV_IND_X5002_LDG IXTR\r\n" + 
			"ON IXTR.PROV_MK=M.PROV_MK\r\n" + 
			"INNER JOIN (\r\n" + 
			"SELECT * FROM SPSMDX_ODS.OPS.ENGINEQUEUE\r\n" + 
			"WHERE OPERATION='DELTAEXTRACT' \r\n" + 
			"AND CODE='PROV_IND'\r\n" + 
			")EQ\r\n" + 
			"ON EQ.REQUESTPAYLOAD=IXTR.ODS_ID\r\n" + 
			"INNER JOIN SPSMDX_ODS.OPS.ENGINEBATCH EB\r\n" + 
			"ON EB.ENGINEBATCHID=EQ.ENGINEBATCHID\r\n" + 
			"INNER JOIN SPSMDX_ODS.OPS.ENGINEQUEUERESULTDETAIL EQR\r\n" + 
			"ON EQR.ENGINEQUEUEID=EQ.ENGINEQUEUEID\r\n" + 
			")XTR\r\n" + 
			"WHERE RNK=1\r\n" + 
			"AND (\r\n" +
			"(#TRANSACTION_BASED_FLAG#='Y' AND #ADD_FLOW_FLAG#='N' AND REQUEST LIKE '%#TRANSACTION_ID#%' AND #SAMPLE_RECORDS_FLAG#='Y' AND PROV_EPDSV2_KEY IN (#LEGACY_ID#)) OR\r\n" + 
			"(#TRANSACTION_BASED_FLAG#='Y' AND #ADD_FLOW_FLAG#='N' AND REQUEST LIKE '%#TRANSACTION_ID#%' AND #SAMPLE_RECORDS_FLAG#='N') OR\r\n" + 
			"(#TRANSACTION_BASED_FLAG#='Y' AND #ADD_FLOW_FLAG#='Y' AND REQUEST LIKE '%#TRANSACTION_ID#%' AND #SAMPLE_RECORDS_FLAG#='Y' AND MSTR_PROV_ID IN (#SPS_ID#)) OR\r\n" + 
			"(#TRANSACTION_BASED_FLAG#='Y' AND #ADD_FLOW_FLAG#='Y' AND REQUEST LIKE '%#TRANSACTION_ID#%' AND #SAMPLE_RECORDS_FLAG#='N') OR\r\n" + 
			"(#PROVIDER_BASED_FLAG#='Y' AND (PROV_EPDSV2_KEY IN (#LEGACY_ID#) OR MSTR_PROV_ID IN (#SPS_ID#))))";

	public static final String MDX_JSON_ORG_TXN_QUERY = "SELECT REQUEST\r\n" + 
			",PROV_EPDSV2_KEY\r\n" + 
			",PROV_MK\r\n" + 
			",MSTR_PROV_ID\r\n" + 
			",ENGINEBATCHID\r\n" + 
			"FROM\r\n" + 
			"(\r\n" + 
			"SELECT EQR.REQUEST\r\n" + 
			",IXTR.PROV_EPDSV2_KEY\r\n" + 
			",M.PROV_MK\r\n" + 
			",M.MSTR_PROV_ID\r\n" + 
			",EQ.ENGINEBATCHID\r\n" + 
			",DENSE_RANK() OVER (PARTITION BY IXTR.PROV_EPDSV2_KEY ORDER BY EQ.ENGINEBATCHID DESC) AS RNK\r\n" + 
			"FROM (\r\n" + 
			"SELECT * FROM SPSMDX_ODS.ODSM.PROV\r\n" + 
			"WHERE PROV_MK IN ( \r\n" + 
			"SELECT DISTINCT PROV_MK FROM SPSMDX_ODS.ODSX.PROV\r\n" + 
			"WHERE SYSTEM_ID='17') \r\n" + 
			")M\r\n" + 
			"INNER JOIN SPSMDX_ODS.ODSE_GA.PROV_ORG_PROV_ORG_X5003_LDG IXTR\r\n" + 
			"ON IXTR.PROV_MK=M.PROV_MK\r\n" + 
			"INNER JOIN (\r\n" + 
			"SELECT * FROM SPSMDX_ODS.OPS.ENGINEQUEUE\r\n" + 
			"WHERE OPERATION='DELTAEXTRACT'\r\n" + 
			"AND CODE='PROV_ORG'\r\n" + 
			")EQ\r\n" + 
			"ON EQ.REQUESTPAYLOAD=IXTR.ODS_ID\r\n" + 
			"INNER JOIN SPSMDX_ODS.OPS.ENGINEBATCH EB\r\n" + 
			"ON EB.ENGINEBATCHID=EQ.ENGINEBATCHID\r\n" + 
			"INNER JOIN SPSMDX_ODS.OPS.ENGINEQUEUERESULTDETAIL EQR\r\n" + 
			"ON EQR.ENGINEQUEUEID=EQ.ENGINEQUEUEID\r\n" + 
			")XTR\r\n" + 
			"WHERE RNK=1\r\n" + 
			"AND (\r\n" + 
			"(#TRANSACTION_BASED_FLAG#='Y' AND #ADD_FLOW_FLAG#='N' AND REQUEST LIKE '%#TRANSACTION_ID#%' AND #SAMPLE_RECORDS_FLAG#='Y' AND PROV_EPDSV2_KEY IN (#LEGACY_ID#)) OR\r\n" + 
			"(#TRANSACTION_BASED_FLAG#='Y' AND #ADD_FLOW_FLAG#='N' AND REQUEST LIKE '%#TRANSACTION_ID#%' AND #SAMPLE_RECORDS_FLAG#='N') OR\r\n" + 
			"(#TRANSACTION_BASED_FLAG#='Y' AND #ADD_FLOW_FLAG#='Y' AND REQUEST LIKE '%#TRANSACTION_ID#%' AND #SAMPLE_RECORDS_FLAG#='Y' AND MSTR_PROV_ID IN (#SPS_ID#)) OR\r\n" + 
			"(#TRANSACTION_BASED_FLAG#='Y' AND #ADD_FLOW_FLAG#='Y' AND REQUEST LIKE '%#TRANSACTION_ID#%' AND #SAMPLE_RECORDS_FLAG#='N') OR\r\n" + 
			"(#PROVIDER_BASED_FLAG#='Y' AND (PROV_EPDSV2_KEY IN (#LEGACY_ID#) OR MSTR_PROV_ID IN (#SPS_ID#))))";
	
	public static final String MDX_JSON_GRP_TXN_QUERY = "SELECT REQUEST" + 
			",GRPG_EPDSV2_KEY" + 
			",GRPG_MK" + 
			",GRPG_ID" + 
			",ENGINEBATCHID" + 
			"FROM (" +
			"SELECT EQR.REQUEST" + 
			",IXTR.GRPG_EPDSV2_KEY" + 
			",M.GRPG_MK" + 
			",M.GRPG_ID" + 
			",EQ.ENGINEBATCHID" + 
			",DENSE_RANK() OVER (PARTITION BY IXTR.GRPG_EPDSV2_KEY ORDER BY EQ.ENGINEBATCHID DESC) AS RNK" + 
			"FROM (" + 
			"SELECT * FROM SPSMDX_ODS.ODSM.GRPG" + 
			"WHERE GRPG_MK IN ( " + 
			"SELECT DISTINCT GRPG_MK FROM SPSMDX_ODS.ODSX.GRPG" + 
			"WHERE SYSTEM_ID='17') " + 
			")M" + 
			"INNER JOIN SPSMDX_ODS.ODSE_GA.GRPG_RPA_GRPG_RPA_X5001_LDG IXTR" + 
			"ON IXTR.GRPG_MK=M.GRPG_MK" + 
			"INNER JOIN (" + 
			"SELECT * FROM SPSMDX_ODS.OPS.ENGINEQUEUE" + 
			"WHERE OPERATION='DELTAEXTRACT' " + 
			"AND CODE='GRPG_RPA'" + 
			")EQ" + 
			"ON EQ.REQUESTPAYLOAD=IXTR.ODS_ID" + 
			"INNER JOIN SPSMDX_ODS.OPS.ENGINEBATCH EB" + 
			"ON EB.ENGINEBATCHID=EQ.ENGINEBATCHID" + 
			"INNER JOIN SPSMDX_ODS.OPS.ENGINEQUEUERESULTDETAIL EQR" + 
			"ON EQR.ENGINEQUEUEID=EQ.ENGINEQUEUEID" + 
			")XTR" + 
			"WHERE RNK=1" + 
			"AND (" + 
			"(#TRANSACTION_BASED_FLAG#='Y' AND #ADD_FLOW_FLAG#='N' AND REQUEST LIKE '%#TRANSACTION_ID#%' AND #SAMPLE_RECORDS_FLAG#='Y' AND GRPG_EPDSV2_KEY IN (#LEGACY_ID#)) OR" + 
			"(#TRANSACTION_BASED_FLAG#='Y' AND #ADD_FLOW_FLAG#='N' AND REQUEST LIKE '%#TRANSACTION_ID#%' AND #SAMPLE_RECORDS_FLAG#='N') OR" + 
			"(#TRANSACTION_BASED_FLAG#='Y' AND #ADD_FLOW_FLAG#='Y' AND REQUEST LIKE '%#TRANSACTION_ID#%' AND #SAMPLE_RECORDS_FLAG#='Y' AND GRPG_ID IN (#SPS_ID#)) OR" + 
			"(#TRANSACTION_BASED_FLAG#='Y' AND #ADD_FLOW_FLAG#='Y' AND REQUEST LIKE '%#TRANSACTION_ID#%' AND #SAMPLE_RECORDS_FLAG#='N') OR" + 
			"(#PROVIDER_BASED_FLAG#='Y' AND (GRPG_EPDSV2_KEY IN (#LEGACY_ID#) OR GRPG_ID IN (#SPS_ID#))))";
	
	public static final String SAMPLE_QUERY = "WITH JSONQ AS (" 
			+ "SELECT"
			+ "Q.DURATIONMS,"
			+ "D.STATUS,"
			+ "D.REQUEST, "
			+ "CAST(JSON_VALUE(D.REQUEST, '$.header.transactionTypes[0]') AS VARCHAR(255)) AS TRANSACTIONTYPES,"
			+ "CAST(JSON_VALUE(D.REQUEST, '$.header.legacyID') AS VARCHAR(255)) AS LEGACY_ID"
			+ "FROM SPSMDX_ODS.OPS.ENGINEQUEUE (NOLOCK) Q"
			+ "INNER JOIN SPSMDX_ODS.ODSE_GA.PROV_IND_PROV_IND_X5002_LDG (NOLOCK) I"
			+ "ON Q.REQUESTPAYLOAD = I.ODS_ID" 
			+ "INNER JOIN SPSMDX_ODS.OPS.ENGINEQUEUERESULTDETAIL (NOLOCK) D"
			+ "ON D.ENGINEQUEUEID = Q.ENGINEQUEUEID)" 
			+ "SELECT TOP 2 REQUEST,TRANSACTIONTYPES" 
			+ "FROM JSONQ" + "WHERE ( " + "(1=1" + "AND ISJSON(REQUEST)>0 "
			+ "AND TRANSACTIONTYPES IN ('#TRANSACTION_ID#')" + ")" + "OR" + "(0=1"
			+ "AND ISJSON(REQUEST)>0 " + "AND TRANSACTIONTYPES IN ('#TRANSACTION_ID#')))";
}
