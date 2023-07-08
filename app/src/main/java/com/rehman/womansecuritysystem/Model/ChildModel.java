package com.rehman.womansecuritysystem.Model;

public class ChildModel
{
   String fullName;
   String childName;
   String className;
   String instituteName;
   String instituteAddress;
   String phoneNumber;
   String address;
   String addKey;
   String userUsername;
   String userAccountType;
   String studentGetDriver;
   String parentUsername;
   String rollNumber;

   public ChildModel(String fullName, String childName, String className, String instituteName, String instituteAddress, String phoneNumber, String address, String addKey, String userUsername, String userAccountType, String studentGetDriver, String parentUsername, String rollNumber) {
      this.fullName = fullName;
      this.childName = childName;
      this.className = className;
      this.instituteName = instituteName;
      this.instituteAddress = instituteAddress;
      this.phoneNumber = phoneNumber;
      this.address = address;
      this.addKey = addKey;
      this.userUsername = userUsername;
      this.userAccountType = userAccountType;
      this.studentGetDriver = studentGetDriver;
      this.parentUsername = parentUsername;
      this.rollNumber = rollNumber;
   }

   public ChildModel() {
   }

   public String getFullName() {
      return fullName;
   }

   public void setFullName(String fullName) {
      this.fullName = fullName;
   }

   public String getChildName() {
      return childName;
   }

   public void setChildName(String childName) {
      this.childName = childName;
   }

   public String getClassName() {
      return className;
   }

   public void setClassName(String className) {
      this.className = className;
   }

   public String getInstituteName() {
      return instituteName;
   }

   public void setInstituteName(String instituteName) {
      this.instituteName = instituteName;
   }

   public String getInstituteAddress() {
      return instituteAddress;
   }

   public void setInstituteAddress(String instituteAddress) {
      this.instituteAddress = instituteAddress;
   }

   public String getPhoneNumber() {
      return phoneNumber;
   }

   public void setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
   }

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public String getAddKey() {
      return addKey;
   }

   public void setAddKey(String addKey) {
      this.addKey = addKey;
   }

   public String getUserUsername() {
      return userUsername;
   }

   public void setUserUsername(String userUsername) {
      this.userUsername = userUsername;
   }

   public String getUserAccountType() {
      return userAccountType;
   }

   public void setUserAccountType(String userAccountType) {
      this.userAccountType = userAccountType;
   }

   public String getStudentGetDriver() {
      return studentGetDriver;
   }

   public void setStudentGetDriver(String studentGetDriver) {
      this.studentGetDriver = studentGetDriver;
   }

   public String getParentUsername() {
      return parentUsername;
   }

   public void setParentUsername(String parentUsername) {
      this.parentUsername = parentUsername;
   }

   public String getRollNumber() {
      return rollNumber;
   }

   public void setRollNumber(String rollNumber) {
      this.rollNumber = rollNumber;
   }
}
