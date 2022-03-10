package com.atos.bioscore.GetBioScoreBoot.services;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.atos.bioscore.GetBioScoreBoot.dao.RidDataIdrepo;
import com.atos.bioscore.GetBioScoreBoot.repo.RidDataIdrepoRepo;
import com.atos.bioscore.GetBioScoreBoot.utils.EncryptionUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.EncryptionMethod;

@Service
public class ExtractionIDRepo {

	
	@Autowired
	RidDataIdrepoRepo idrepo;
	
	@Value("${mosip.fields.fetch}")
	private List<String> allFields;
	
	@Value("${encrypt.data:false}")
	private boolean encryptData;
	
	@Value("${mosip.fields.map}")
	private List<String> mapFields;
	
	@Value("${output.fields.sep}")
	private String feildsSep;
	
	@Value("${zip.password}")
	private String zipPassword;
	
	@Autowired
	EncryptionUtil encUtil;
	
	Logger logger = LoggerFactory.getLogger(ExtractionService.class);
	
	public void extractData() {
		//List<RidDataIdrepo> dataToExtract = idrepo.findByExtractedAndStatus("","SUCCESS");
		List<Long> dataToExtractid = idrepo.getExtractids();
		try {
			logger.info("Start Data Extraction");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
			String currdate = LocalDateTime.now().format(formatter);
			String demofilename = "./demodata_idrepo/DemoData_" + currdate + ".csv";
			FileWriter demoData = new FileWriter(demofilename);
			PrintWriter demoDataWriter = new PrintWriter(demoData);
			demoDataWriter.println("DATA");
			demoDataWriter.print("\"" + "UIN" + "\"" + feildsSep);
			demoDataWriter.print("\"" + "RID" + "\"" + feildsSep);
			for(String fname : allFields) {
				if(mapFields.contains(fname)) 
					demoDataWriter.print("\"" + fname + "_fra" + "\"" + feildsSep + "\"" + fname + "_ara" + "\"" + feildsSep);
				else
					demoDataWriter.print("\"" + fname + "\"" + feildsSep);
			}
			demoDataWriter.println();
			File dir = new File("./biodata_idrepo/" + currdate + "/");
			dir.mkdir();
			for(Long riDaralong: dataToExtractid){
				RidDataIdrepo idrepoDara = idrepo.findById(riDaralong).get();
				try {
					logger.info("Extracting Data for RID/UIN:" + idrepoDara.getUin());
					FileWriter bioData = new FileWriter("./biodata_idrepo/" + currdate + "/" + idrepoDara.getUin() + "_" + currdate + ".out");
					PrintWriter bioDataWriter = new PrintWriter(bioData);
					bioDataWriter.println("individualBiometrics");
					if(encryptData)
						if(idrepoDara.getBiodata() != null)
							bioDataWriter.println("bdb:" + decryptField(idrepoDara.getBiodata(),idrepoDara.getUin(),"biodata"));
						else
							bioDataWriter.println("bdb:" +"NULL");
					else
						bioDataWriter.println("bdb:" + idrepoDara.getBiodata());
					bioDataWriter.close();
					idrepoDara.setExtracted("X");
					ObjectMapper objectmap = new ObjectMapper().registerModule(new JavaTimeModule());
//					objectmap.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
//					objectmap.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//					objectmap.disable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
//					objectmap.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
//					objectmap.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
//					String jsonval = objectmap.writeValueAsString(idrepoDara);
//					logger.debug("JSON String of all: {} " , jsonval);
					Map<String,Object> dataMap = objectmap.convertValue(idrepoDara, new TypeReference<Map<String,Object>>(){});
					demoDataWriter.print("\"" + idrepoDara.getUin() + "\"" + feildsSep);
					demoDataWriter.print("\"" + idrepoDara.getRid() + "\"" + feildsSep);
					for(String fname : allFields) {
						if(dataMap.get(fname) != null) {
							logger.debug("Extracting field " + fname + " for RID/UIN:" + idrepoDara.getUin());
							if(encryptData) {
								String decfielddata = decryptField(dataMap.get(fname).toString(),idrepoDara.getUin(),fname);
								dataMap.put(fname, decfielddata);
							}
							if(mapFields.contains(fname))  {
								String valuefra = "";
								String valueara = "";
								List<Map<String,Object>> fieldData = objectmap.readValue(dataMap.get(fname).toString(),new TypeReference<List<Map<String,Object>>>(){});
								for(Map<String,Object> finalmap : fieldData) {
									if(finalmap.get("language").toString().equalsIgnoreCase("fra"))
										valuefra = finalmap.get("value").toString();
									else
										valueara = finalmap.get("value").toString();
								}
								demoDataWriter.print("\"" + valuefra + "\"" + feildsSep + "\"" +valueara + "\"" + feildsSep);
							} else {
								demoDataWriter.print("\"" + dataMap.get(fname).toString().replace("\"","") + "\"" + feildsSep);
							}
						} else {
							if(mapFields.contains(fname))
								demoDataWriter.print("\"NULL\"" + feildsSep + "\"NULL\"" + feildsSep);
							else
								demoDataWriter.print("\"NULL\"" + feildsSep);
						}
					}
				} catch(Exception e) {
					logger.error("Extracting Data FAILED for RID/UIN:" + idrepoDara.getUin() + ":" + e.getMessage());
					e.printStackTrace();
					idrepoDara.setExtracted("FAIL");
				}
				demoDataWriter.println();
				idrepo.save(idrepoDara);
			}
			demoDataWriter.close();
			ZipParameters zipParameters = new ZipParameters();
			zipParameters.setEncryptFiles(true);
			zipParameters.setCompressionLevel(CompressionLevel.HIGHER);
			zipParameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
			ZipFile zipFile = new ZipFile("./biodata_idrepo/" + currdate + "_bio.zip", zipPassword.toCharArray());
			ZipFile zipFiledemo = new ZipFile(demofilename + ".zip", zipPassword.toCharArray());
			zipFile.addFolder(dir, zipParameters);
			zipFiledemo.addFile(demofilename);
			zipFile.close();
			zipFiledemo.close();
		} catch(Exception e) {
			logger.error("Could not extract data:" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private String decryptField(String fielddata , String rid,String fKey) {
		try {
			String encryptedFieldData = encUtil.decryptTextAES(fielddata);
			return encryptedFieldData;
		}
		catch(Exception e) {
			e.printStackTrace();
			logger.error("Failed to decrypt RID/UIN " + rid  + " field " + fKey + ":" + e.getMessage());
			return fielddata;
		}
	}
}
