package com.atos.bioscore.GetBioScoreBoot.services;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.atos.bioscore.GetBioScoreBoot.dao.RidData;
import com.atos.bioscore.GetBioScoreBoot.repo.RidDataRepo;
import com.atos.bioscore.GetBioScoreBoot.utils.EncryptionUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.EncryptionMethod;



@Service
public class ExtractionService {
	
	
	@Autowired
	RidDataRepo riddataRepo;
	
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
		List<Long> dataToExtractid = riddataRepo.getExtractids();
//		List<RidData> dataToExtract = riddataRepo.findByExtractedAndStatus("","SUCCESS");
		try {
			logger.info("Start Data Extraction");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
			String currdate = LocalDateTime.now().format(formatter);
			String scorefilename = "./score/SocreFile_"+ currdate+ ".csv";
			FileWriter scoreFile = new FileWriter(scorefilename);
			PrintWriter socreWriter = new PrintWriter(scoreFile);
			socreWriter.println("RID,LETT-IRIS,RIGHT-IRIS,FACE");
			String demofilename = "./demodata/DemoData_" + currdate + ".csv";
			FileWriter demoData = new FileWriter(demofilename);
			PrintWriter demoDataWriter = new PrintWriter(demoData);
			demoDataWriter.println("DATA");
			demoDataWriter.print("\"" + "RID" + "\"" + feildsSep);
			for(String fname : allFields) {
				if(mapFields.contains(fname)) 
					demoDataWriter.print("\"" + fname + "_fra" + "\"" + feildsSep + "\"" + fname + "_ara" + "\"" + feildsSep);
				else
					demoDataWriter.print("\"" + fname + "\"" + feildsSep);
			}
			demoDataWriter.println();
			File dir = new File("./biodata/" + currdate + "/");
			dir.mkdir();
			for(Long riDaralong: dataToExtractid) {
				RidData riDara = riddataRepo.findById(riDaralong).get();
				try {
					logger.info("Extracting Data for RID/UIN:" + riDara.getRid());
					socreWriter.println(riDara.getRid() + "," + riDara.getLeftIrisScore() + ","
							+ riDara.getRightIrisScore() + "," + riDara.getFaceScore());
					FileWriter bioData = new FileWriter("./biodata/" + currdate + "/" + riDara.getRid() + "_" + currdate + ".out");
					PrintWriter bioDataWriter = new PrintWriter(bioData);
					bioDataWriter.println("IRIS");
					bioDataWriter.println("Left");
					if(encryptData)
						if(riDara.getLeftIris() != null)
							bioDataWriter.println("bdb:" +decryptField(riDara.getLeftIris(),riDara.getRid(),"leftiris"));
						else
							bioDataWriter.println("bdb:" +"NULL");
					else
						bioDataWriter.println("bdb:" + riDara.getLeftIris());
					bioDataWriter.println("Right");
					if(encryptData)
						if(riDara.getRightIris() != null)
							bioDataWriter.println("bdb:" +decryptField(riDara.getRightIris(),riDara.getRid(),"rightiris"));
						else
							bioDataWriter.println("bdb:" +"NULL");
					else
						bioDataWriter.println("bdb:" +riDara.getRightIris());
					bioDataWriter.println("FACE");
					if(encryptData)
						if( riDara.getFace() !=null)
							bioDataWriter.println("bdb:" +decryptField(riDara.getFace(),riDara.getRid(),"face"));
						else
							bioDataWriter.println("bdb:" +"NULL");
					else
						bioDataWriter.println("bdb:" +riDara.getFace());
					
						
					
					bioDataWriter.close();
					
					FileWriter bioDataGuardian = new FileWriter("./biodata/" + currdate + "/" + riDara.getRid() + "_Guardian_" + currdate + ".out");
					PrintWriter bioDataWriterGuardian = new PrintWriter(bioDataGuardian);
					bioDataWriterGuardian.println("IRIS");
					bioDataWriterGuardian.println("Left");
					if(riDara.getIrisleftGuaridan() != null)
						bioDataWriterGuardian.println("bdb:" + riDara.getIrisleftGuaridan());
					else
						bioDataWriterGuardian.println("bdb:" +"NULL");
					bioDataWriterGuardian.println("Right");
					if(riDara.getIrisrightGuaridan() != null)
						bioDataWriterGuardian.println("bdb:" + riDara.getIrisrightGuaridan());
					else
						bioDataWriterGuardian.println("bdb:" +"NULL");
					bioDataWriterGuardian.println("FACE");
					if(riDara.getFaceGuaridan() != null)
						bioDataWriterGuardian.println("bdb:" +riDara.getFaceGuaridan());
					else
						bioDataWriterGuardian.println("bdb:" +"NULL");
					bioDataWriterGuardian.close();
					riDara.setExtracted("X");
					ObjectMapper objectmap = new ObjectMapper().registerModule(new JavaTimeModule());
					objectmap.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
					objectmap.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
					objectmap.disable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
					Map<String,Object> dataMAp = objectmap.convertValue(riDara, new TypeReference<Map<String,Object>>(){});
					demoDataWriter.print("\"" + riDara.getRid() + "\"" + feildsSep);
					for(String fname : allFields) {
						if(dataMAp.get(fname) != null) {
							logger.debug("Extracting field " + fname + " for RID/UIN:" + riDara.getRid());
							if(encryptData) {
								String decfielddata = decryptField(dataMAp.get(fname).toString(),riDara.getRid(),fname);
								dataMAp.put(fname, decfielddata);
							}
							if(mapFields.contains(fname))  {
								String valuefra = "";
								String valueara = "";
								List<Map<String,Object>> fieldData = objectmap.readValue(dataMAp.get(fname).toString(),new TypeReference<List<Map<String,Object>>>(){});
								for(Map<String,Object> finalmap : fieldData) {
									if(finalmap.get("language").toString().equalsIgnoreCase("fra"))
										valuefra = finalmap.get("value").toString();
									else
										valueara = finalmap.get("value").toString();
								}
								demoDataWriter.print("\"" + valuefra + "\"" + feildsSep + "\"" +valueara + "\"" + feildsSep);
							}
								
							else
								demoDataWriter.print("\"" + dataMAp.get(fname).toString() + "\"" + feildsSep);
						} else {
							if(mapFields.contains(fname))
								demoDataWriter.print("\"NULL\"" + feildsSep + "\"NULL\"" + feildsSep);
							else
								demoDataWriter.print("\"NULL\"" + feildsSep);
						}
					}
					
					riddataRepo.save(riDara);
					logger.info("Extracting Data Success for RID/UIN:" + riDara.getRid());
				} catch(Exception e) {
					logger.error("Extracting Data FAILED for RID/UIN:" + riDara.getRid() + ":" + e.getMessage());
					riDara.setExtracted("FAIL");
				}
				demoDataWriter.println();
				riddataRepo.save(riDara);
			}
			demoDataWriter.close();
			socreWriter.close();
			ZipParameters zipParameters = new ZipParameters();
			zipParameters.setEncryptFiles(true);
			zipParameters.setCompressionLevel(CompressionLevel.HIGHER);
			zipParameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
			ZipFile zipFile = new ZipFile("./biodata/" + currdate + "_bio.zip", zipPassword.toCharArray());
			ZipFile zipFilescore = new ZipFile(scorefilename + ".zip", zipPassword.toCharArray());
			ZipFile zipFiledemo = new ZipFile(demofilename + ".zip", zipPassword.toCharArray());
			zipFile.addFolder(dir, zipParameters);
			zipFilescore.addFile(scorefilename);
			zipFiledemo.addFile(demofilename);
			zipFile.close();
			zipFilescore.close();
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
