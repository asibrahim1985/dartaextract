package com.atos.bioscore.GetBioScoreBoot.dao;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="rid_data_idrepo",indexes= {@Index(name = "riddataidrepo_ind_crdt", columnList = "create_date",unique = false)})
public class RidDataIdrepo {

	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	@Column(name = "rid", length = 300)
	private String rid;
	
	@Column(name = "create_date")
	private LocalDateTime creationDate;
	
	@Column(name = "reg_type",length = 50)
	private String regType;
	
	@Column(name = "uin",length = 300)
	private String uin;
	
	@JsonProperty(value="lastName")
	@Column(name = "lastName",length = 300)
	private String lastName;
	
	@JsonProperty(value="firstName")
	@Column(name = "firstName",length = 300)
	private String firstName;
	
	@JsonProperty(value="yearOfBirth")
	@Column(name = "yearOfBirth",length = 300)
	private String yearOfBirth;
	
	@JsonProperty(value="monthOfBirth")
	@Column(name = "monthOfBirth",length = 300)
	private String monthOfBirth;
	
	@JsonProperty(value="dayOfBirth")
	@Column(name = "dayOfBirth",length = 300)
	private String dayOfBirth;
	
	@JsonProperty(value="dateOfBirth")
	@Column(name = "dateOfBirth",length = 300)
	private String dateOfBirth;
	
	@JsonProperty(value="gender")
	@Column(name = "gender",length = 300)
	private String gender;
	
	@JsonProperty(value="residenceStatus")
	@Column(name = "residenceStatus",length = 300)
	private String residenceStatus;
	
	@JsonProperty(value="referenceCNIENumber")
	@Column(name = "referenceCNIENumber",length = 300)
	private String referenceCNIENumber;
	
	@JsonProperty(value="referenceCNIENumberExpiry")
	@Column(name = "referenceCNIENumberExpiry",length = 300)
	private String referenceCNIENumberExpiry;
	
	@JsonProperty(value="nationality")
	@Column(name = "nationality",length = 300)
	private String nationality;
	
	@JsonProperty(value="resOuPass")
	@Column(name = "resOuPass",length = 300)
	private String resOuPass;
	
	@JsonProperty(value="referenceResidencyNumber")
	@Column(name = "referenceResidencyNumber",length = 300)
	private String referenceResidencyNumber;
	
	@JsonProperty(value="referenceResidencyNumberExpiry")
	@Column(name = "referenceResidencyNumberExpiry",length = 300)
	private String referenceResidencyNumberExpiry;
	
	@JsonProperty(value="passportNumber")
	@Column(name = "passportNumber",length = 300)
	private String passportNumber;
	
	@JsonProperty(value="passportNumberExpiry")
	@Column(name = "passportNumberExpiry",length = 300)
	private String passportNumberExpiry;
	
	@JsonProperty(value="flagb")
	@Column(name = "flagb",length = 300)
	private String flagb;
	
	@JsonProperty(value="placeOfBirthProv")
	@Column(name = "placeOfBirthProv",length = 300)
	private String placeOfBirthProv;
	
	@JsonProperty(value="placeOfBirth")
	@Column(name = "placeOfBirth",length = 300)
	private String placeOfBirth;
	
	@JsonProperty(value="listCountry")
	@Column(name = "listCountry",length = 300)
	private String listCountry;
	
	@JsonProperty(value="flagidcs")
	@Column(name = "flagidcs",length = 300)
	private String flagidcs;
	
	@JsonProperty(value="birthCertificateNumber")
	@Column(name = "birthCertificateNumber",length = 300)
	private String birthCertificateNumber;
	
	@JsonProperty(value="civilRegistryNumber")
	@Column(name = "civilRegistryNumber",length = 300)
	private String civilRegistryNumber;
	
	@JsonProperty(value="region")
	@Column(name = "region",length = 300)
	private String region;
	
	@JsonProperty(value="province")
	@Column(name = "province",length = 300)
	private String province;
	
	@JsonProperty(value="city")
	@Column(name = "city",length = 300)
	private String city;
	
	@JsonProperty(value="zone")
	@Column(name = "zone",length = 300)
	private String zone;
	
	@JsonProperty(value="residence")
	@Column(name = "residence",length = 300)
	private String residence;
	
	@JsonProperty(value="street")
	@Column(name = "street",length = 300)
	private String street;
	
	@JsonProperty(value="commun")
	@Column(name = "commun",length = 300)
	private String commun;
	
	@JsonProperty(value="regionCode")
	@Column(name = "regionCode",length = 300)
	private String regionCode;
	
	@JsonProperty(value="provinceCode")
	@Column(name = "provinceCode",length = 300)
	private String provinceCode;
	
	@JsonProperty(value="cityCode")
	@Column(name = "cityCode",length = 300)
	private String cityCode;
	
	@JsonProperty(value="zoneCode")
	@Column(name = "zoneCode",length = 300)
	private String zoneCode;
	
	@JsonProperty(value="residenceCode")
	@Column(name = "residenceCode",length = 300)
	private String residenceCode;
	
	@JsonProperty(value="streetCode")
	@Column(name = "streetCode",length = 300)
	private String streetCode;
	
	@JsonProperty(value="communCode")
	@Column(name = "communCode",length = 300)
	private String communCode;
	
	@JsonProperty(value="addressType")
	@Column(name = "addressType",length = 300)
	private String addressType;
	
	@JsonProperty(value="portNo")
	@Column(name = "portNo",length = 300)
	private String portNo;
	
	@JsonProperty(value="apptNo")
	@Column(name = "apptNo",length = 300)
	private String apptNo;
	
	@JsonProperty(value="addressLine1")
	@Column(name = "addressLine1",length = 300)
	private String addressLine1;
	
	@JsonProperty(value="addressLine2")
	@Column(name = "addressLine2",length = 300)
	private String addressLine2;
	
	@JsonProperty(value="addressLine3")
	@Column(name = "addressLine3",length = 300)
	private String addressLine3;
	
	@JsonProperty(value="postalCode")
	@Column(name = "postalCode",length = 300)
	private String postalCode;
	
	@JsonProperty(value="phone")
	@Column(name = "phone",length = 300)
	private String phone;
	
	@JsonProperty(value="email")
	@Column(name = "email",length = 300)
	private String email;
	
	@JsonProperty(value="guardianType")
	@Column(name = "guardianType",length = 300)
	private String guardianType;
	
	@JsonProperty(value="parentOrGuardianfirstName")
	@Column(name = "parentOrGuardianfirstName",length = 300)
	private String parentOrGuardianfirstName;
	
	@JsonProperty(value="parentOrGuardianlastName")
	@Column(name = "parentOrGuardianlastName",length = 300)
	private String parentOrGuardianlastName;
	
	@JsonProperty(value="relationWithChild")
	@Column(name = "relationWithChild",length = 300)
	private String relationWithChild;
	
	@JsonProperty(value="parentOrGuardianRID")
	@Column(name = "parentOrGuardianRID",length = 300)
	private String parentOrGuardianRID;
	
	@JsonProperty(value="parentOrGuardianUIN")
	@Column(name = "parentOrGuardianUIN",length = 300)
	private String parentOrGuardianUIN;
	
	@JsonProperty(value="parentOrGuardianCNIE")
	@Column(name = "parentOrGuardianCNIE",length = 300)
	private String parentOrGuardianCNIE;
	
	@JsonProperty(value="parentOrGuardianCNIEExpiry")
	@Column(name = "parentOrGuardianCNIEExpiry",length = 300)
	private String parentOrGuardianCNIEExpiry;
	
	
	@JsonProperty(value="CNIElastName")
	@Column(name = "CNIElastName",length = 300)
	private String CNIElastName;
	
	@JsonProperty(value="CNIEfirstName")
	@Column(name = "CNIEfirstName",length = 300)
	private String CNIEfirstName;
	
	@JsonProperty(value="CivillastName")
	@Column(name = "CivillastName",length = 300)
	private String CivillastName;
	
	@JsonProperty(value="CivilfirstName")
	@Column(name = "CivilfirstName",length = 300)
	private String CivilfirstName;
	
	
	@Column(name="leftiris",columnDefinition="TEXT")
    private String leftIris;
	
	@Column(name="rightiris" , columnDefinition="TEXT")
    private String rightIris;
	
	@Column(name="face",columnDefinition="TEXT")
    private String face;
	
	@Column(name="biodata",columnDefinition="TEXT")
    private String biodata;
	
	@Column(name = "leftiris_score",length = 5)
	private String leftIrisScore;
	
	@Column(name = "rightiris_score",length = 5)
	private String rightIrisScore;
	
	@Column(name = "face_score",length = 5)
	private String faceScore;
	
	@Column(name = "errormsg",length = 500)
	private String errormsg;
	
	@Column(name = "extracted",length = 5)
	private String extracted;
	
	@Column(name = "status",length = 10)
	private String status;

	public RidDataIdrepo() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public RidDataIdrepo(long id, String rid, LocalDateTime creationDate, String regType, String uin, String lastName,
			String firstName, String yearOfBirth, String monthOfBirth, String dayOfBirth, String dateOfBirth,
			String gender, String residenceStatus, String referenceCNIENumber, String referenceCNIENumberExpiry,
			String nationality, String resOuPass, String referenceResidencyNumber,
			String referenceResidencyNumberExpiry, String passportNumber, String passportNumberExpiry, String flagb,
			String placeOfBirthProv, String placeOfBirth, String listCountry, String flagidcs,
			String birthCertificateNumber, String civilRegistryNumber, String region, String province, String city,
			String zone, String residence, String street, String commun, String regionCode, String provinceCode,
			String cityCode, String zoneCode, String residenceCode, String streetCode, String communCode,
			String addressType, String portNo, String apptNo, String addressLine1, String addressLine2,
			String addressLine3, String postalCode, String phone, String email, String guardianType,
			String parentOrGuardianfirstName, String parentOrGuardianlastName, String relationWithChild,
			String parentOrGuardianRID, String parentOrGuardianUIN, String parentOrGuardianCNIE,
			String parentOrGuardianCNIEExpiry, String cNIElastName, String cNIEfirstName, String civillastName,
			String civilfirstName, String leftIris, String rightIris, String face, String biodata, String leftIrisScore,
			String rightIrisScore, String faceScore, String errormsg, String extracted, String status) {
		super();
		this.id = id;
		this.rid = rid;
		this.creationDate = creationDate;
		this.regType = regType;
		this.uin = uin;
		this.lastName = lastName;
		this.firstName = firstName;
		this.yearOfBirth = yearOfBirth;
		this.monthOfBirth = monthOfBirth;
		this.dayOfBirth = dayOfBirth;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.residenceStatus = residenceStatus;
		this.referenceCNIENumber = referenceCNIENumber;
		this.referenceCNIENumberExpiry = referenceCNIENumberExpiry;
		this.nationality = nationality;
		this.resOuPass = resOuPass;
		this.referenceResidencyNumber = referenceResidencyNumber;
		this.referenceResidencyNumberExpiry = referenceResidencyNumberExpiry;
		this.passportNumber = passportNumber;
		this.passportNumberExpiry = passportNumberExpiry;
		this.flagb = flagb;
		this.placeOfBirthProv = placeOfBirthProv;
		this.placeOfBirth = placeOfBirth;
		this.listCountry = listCountry;
		this.flagidcs = flagidcs;
		this.birthCertificateNumber = birthCertificateNumber;
		this.civilRegistryNumber = civilRegistryNumber;
		this.region = region;
		this.province = province;
		this.city = city;
		this.zone = zone;
		this.residence = residence;
		this.street = street;
		this.commun = commun;
		this.regionCode = regionCode;
		this.provinceCode = provinceCode;
		this.cityCode = cityCode;
		this.zoneCode = zoneCode;
		this.residenceCode = residenceCode;
		this.streetCode = streetCode;
		this.communCode = communCode;
		this.addressType = addressType;
		this.portNo = portNo;
		this.apptNo = apptNo;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.addressLine3 = addressLine3;
		this.postalCode = postalCode;
		this.phone = phone;
		this.email = email;
		this.guardianType = guardianType;
		this.parentOrGuardianfirstName = parentOrGuardianfirstName;
		this.parentOrGuardianlastName = parentOrGuardianlastName;
		this.relationWithChild = relationWithChild;
		this.parentOrGuardianRID = parentOrGuardianRID;
		this.parentOrGuardianUIN = parentOrGuardianUIN;
		this.parentOrGuardianCNIE = parentOrGuardianCNIE;
		this.parentOrGuardianCNIEExpiry = parentOrGuardianCNIEExpiry;
		CNIElastName = cNIElastName;
		CNIEfirstName = cNIEfirstName;
		CivillastName = civillastName;
		CivilfirstName = civilfirstName;
		this.leftIris = leftIris;
		this.rightIris = rightIris;
		this.face = face;
		this.biodata = biodata;
		this.leftIrisScore = leftIrisScore;
		this.rightIrisScore = rightIrisScore;
		this.faceScore = faceScore;
		this.errormsg = errormsg;
		this.extracted = extracted;
		this.status = status;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public String getRegType() {
		return regType;
	}

	public void setRegType(String regType) {
		this.regType = regType;
	}

	public String getUin() {
		return uin;
	}

	public void setUin(String uin) {
		this.uin = uin;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getYearOfBirth() {
		return yearOfBirth;
	}

	public void setYearOfBirth(String yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}

	public String getMonthOfBirth() {
		return monthOfBirth;
	}

	public void setMonthOfBirth(String monthOfBirth) {
		this.monthOfBirth = monthOfBirth;
	}

	public String getDayOfBirth() {
		return dayOfBirth;
	}

	public void setDayOfBirth(String dayOfBirth) {
		this.dayOfBirth = dayOfBirth;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getResidenceStatus() {
		return residenceStatus;
	}

	public void setResidenceStatus(String residenceStatus) {
		this.residenceStatus = residenceStatus;
	}

	public String getReferenceCNIENumber() {
		return referenceCNIENumber;
	}

	public void setReferenceCNIENumber(String referenceCNIENumber) {
		this.referenceCNIENumber = referenceCNIENumber;
	}

	public String getReferenceCNIENumberExpiry() {
		return referenceCNIENumberExpiry;
	}

	public void setReferenceCNIENumberExpiry(String referenceCNIENumberExpiry) {
		this.referenceCNIENumberExpiry = referenceCNIENumberExpiry;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getResOuPass() {
		return resOuPass;
	}

	public void setResOuPass(String resOuPass) {
		this.resOuPass = resOuPass;
	}

	public String getReferenceResidencyNumber() {
		return referenceResidencyNumber;
	}

	public void setReferenceResidencyNumber(String referenceResidencyNumber) {
		this.referenceResidencyNumber = referenceResidencyNumber;
	}

	public String getReferenceResidencyNumberExpiry() {
		return referenceResidencyNumberExpiry;
	}

	public void setReferenceResidencyNumberExpiry(String referenceResidencyNumberExpiry) {
		this.referenceResidencyNumberExpiry = referenceResidencyNumberExpiry;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public String getPassportNumberExpiry() {
		return passportNumberExpiry;
	}

	public void setPassportNumberExpiry(String passportNumberExpiry) {
		this.passportNumberExpiry = passportNumberExpiry;
	}

	public String getFlagb() {
		return flagb;
	}

	public void setFlagb(String flagb) {
		this.flagb = flagb;
	}

	public String getPlaceOfBirthProv() {
		return placeOfBirthProv;
	}

	public void setPlaceOfBirthProv(String placeOfBirthProv) {
		this.placeOfBirthProv = placeOfBirthProv;
	}

	public String getPlaceOfBirth() {
		return placeOfBirth;
	}

	public void setPlaceOfBirth(String placeOfBirth) {
		this.placeOfBirth = placeOfBirth;
	}

	public String getListCountry() {
		return listCountry;
	}

	public void setListCountry(String listCountry) {
		this.listCountry = listCountry;
	}

	public String getFlagidcs() {
		return flagidcs;
	}

	public void setFlagidcs(String flagidcs) {
		this.flagidcs = flagidcs;
	}

	public String getBirthCertificateNumber() {
		return birthCertificateNumber;
	}

	public void setBirthCertificateNumber(String birthCertificateNumber) {
		this.birthCertificateNumber = birthCertificateNumber;
	}

	public String getCivilRegistryNumber() {
		return civilRegistryNumber;
	}

	public void setCivilRegistryNumber(String civilRegistryNumber) {
		this.civilRegistryNumber = civilRegistryNumber;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getResidence() {
		return residence;
	}

	public void setResidence(String residence) {
		this.residence = residence;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCommun() {
		return commun;
	}

	public void setCommun(String commun) {
		this.commun = commun;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}

	public String getResidenceCode() {
		return residenceCode;
	}

	public void setResidenceCode(String residenceCode) {
		this.residenceCode = residenceCode;
	}

	public String getStreetCode() {
		return streetCode;
	}

	public void setStreetCode(String streetCode) {
		this.streetCode = streetCode;
	}

	public String getCommunCode() {
		return communCode;
	}

	public void setCommunCode(String communCode) {
		this.communCode = communCode;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getPortNo() {
		return portNo;
	}

	public void setPortNo(String portNo) {
		this.portNo = portNo;
	}

	public String getApptNo() {
		return apptNo;
	}

	public void setApptNo(String apptNo) {
		this.apptNo = apptNo;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGuardianType() {
		return guardianType;
	}

	public void setGuardianType(String guardianType) {
		this.guardianType = guardianType;
	}

	public String getParentOrGuardianfirstName() {
		return parentOrGuardianfirstName;
	}

	public void setParentOrGuardianfirstName(String parentOrGuardianfirstName) {
		this.parentOrGuardianfirstName = parentOrGuardianfirstName;
	}

	public String getParentOrGuardianlastName() {
		return parentOrGuardianlastName;
	}

	public void setParentOrGuardianlastName(String parentOrGuardianlastName) {
		this.parentOrGuardianlastName = parentOrGuardianlastName;
	}

	public String getRelationWithChild() {
		return relationWithChild;
	}

	public void setRelationWithChild(String relationWithChild) {
		this.relationWithChild = relationWithChild;
	}

	public String getParentOrGuardianRID() {
		return parentOrGuardianRID;
	}

	public void setParentOrGuardianRID(String parentOrGuardianRID) {
		this.parentOrGuardianRID = parentOrGuardianRID;
	}

	public String getParentOrGuardianUIN() {
		return parentOrGuardianUIN;
	}

	public void setParentOrGuardianUIN(String parentOrGuardianUIN) {
		this.parentOrGuardianUIN = parentOrGuardianUIN;
	}

	public String getParentOrGuardianCNIE() {
		return parentOrGuardianCNIE;
	}

	public void setParentOrGuardianCNIE(String parentOrGuardianCNIE) {
		this.parentOrGuardianCNIE = parentOrGuardianCNIE;
	}

	public String getParentOrGuardianCNIEExpiry() {
		return parentOrGuardianCNIEExpiry;
	}

	public void setParentOrGuardianCNIEExpiry(String parentOrGuardianCNIEExpiry) {
		this.parentOrGuardianCNIEExpiry = parentOrGuardianCNIEExpiry;
	}

	public String getCNIElastName() {
		return CNIElastName;
	}

	public void setCNIElastName(String cNIElastName) {
		CNIElastName = cNIElastName;
	}

	public String getCNIEfirstName() {
		return CNIEfirstName;
	}

	public void setCNIEfirstName(String cNIEfirstName) {
		CNIEfirstName = cNIEfirstName;
	}

	public String getCivillastName() {
		return CivillastName;
	}

	public void setCivillastName(String civillastName) {
		CivillastName = civillastName;
	}

	public String getCivilfirstName() {
		return CivilfirstName;
	}

	public void setCivilfirstName(String civilfirstName) {
		CivilfirstName = civilfirstName;
	}

	public String getLeftIris() {
		return leftIris;
	}

	public void setLeftIris(String leftIris) {
		this.leftIris = leftIris;
	}

	public String getRightIris() {
		return rightIris;
	}

	public void setRightIris(String rightIris) {
		this.rightIris = rightIris;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	public String getLeftIrisScore() {
		return leftIrisScore;
	}

	public void setLeftIrisScore(String leftIrisScore) {
		this.leftIrisScore = leftIrisScore;
	}

	public String getRightIrisScore() {
		return rightIrisScore;
	}

	public void setRightIrisScore(String rightIrisScore) {
		this.rightIrisScore = rightIrisScore;
	}

	public String getFaceScore() {
		return faceScore;
	}

	public void setFaceScore(String faceScore) {
		this.faceScore = faceScore;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public String getExtracted() {
		return extracted;
	}

	public void setExtracted(String extracted) {
		this.extracted = extracted;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public String getBiodata() {
		return biodata;
	}


	public void setBiodata(String biodata) {
		this.biodata = biodata;
	}


	@Override
	public String toString() {
		return "RidDataIdrepo [id=" + id + ", rid=" + rid + ", creationDate=" + creationDate + ", regType=" + regType
				+ ", uin=" + uin + ", lastName=" + lastName + ", firstName=" + firstName + ", yearOfBirth="
				+ yearOfBirth + ", monthOfBirth=" + monthOfBirth + ", dayOfBirth=" + dayOfBirth + ", dateOfBirth="
				+ dateOfBirth + ", gender=" + gender + ", residenceStatus=" + residenceStatus + ", referenceCNIENumber="
				+ referenceCNIENumber + ", referenceCNIENumberExpiry=" + referenceCNIENumberExpiry + ", nationality="
				+ nationality + ", resOuPass=" + resOuPass + ", referenceResidencyNumber=" + referenceResidencyNumber
				+ ", referenceResidencyNumberExpiry=" + referenceResidencyNumberExpiry + ", passportNumber="
				+ passportNumber + ", passportNumberExpiry=" + passportNumberExpiry + ", flagb=" + flagb
				+ ", placeOfBirthProv=" + placeOfBirthProv + ", placeOfBirth=" + placeOfBirth + ", listCountry="
				+ listCountry + ", flagidcs=" + flagidcs + ", birthCertificateNumber=" + birthCertificateNumber
				+ ", civilRegistryNumber=" + civilRegistryNumber + ", region=" + region + ", province=" + province
				+ ", city=" + city + ", zone=" + zone + ", residence=" + residence + ", street=" + street + ", commun="
				+ commun + ", regionCode=" + regionCode + ", provinceCode=" + provinceCode + ", cityCode=" + cityCode
				+ ", zoneCode=" + zoneCode + ", residenceCode=" + residenceCode + ", streetCode=" + streetCode
				+ ", communCode=" + communCode + ", addressType=" + addressType + ", portNo=" + portNo + ", apptNo="
				+ apptNo + ", addressLine1=" + addressLine1 + ", addressLine2=" + addressLine2 + ", addressLine3="
				+ addressLine3 + ", postalCode=" + postalCode + ", phone=" + phone + ", email=" + email
				+ ", guardianType=" + guardianType + ", parentOrGuardianfirstName=" + parentOrGuardianfirstName
				+ ", parentOrGuardianlastName=" + parentOrGuardianlastName + ", relationWithChild=" + relationWithChild
				+ ", parentOrGuardianRID=" + parentOrGuardianRID + ", parentOrGuardianUIN=" + parentOrGuardianUIN
				+ ", parentOrGuardianCNIE=" + parentOrGuardianCNIE + ", parentOrGuardianCNIEExpiry="
				+ parentOrGuardianCNIEExpiry + ", CNIElastName=" + CNIElastName + ", CNIEfirstName=" + CNIEfirstName
				+ ", CivillastName=" + CivillastName + ", CivilfirstName=" + CivilfirstName + ", leftIris=" + leftIris
				+ ", rightIris=" + rightIris + ", face=" + face + ", biodata=" + biodata + ", leftIrisScore="
				+ leftIrisScore + ", rightIrisScore=" + rightIrisScore + ", faceScore=" + faceScore + ", errormsg="
				+ errormsg + ", extracted=" + extracted + ", status=" + status + "]";
	}
	
	

}
