package com.rehman.womansecuritysystem.Model;

public class DropModel
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
    String dropTime;
    String dropDate;
    String dropStatus;
    String studentDropKey;

    public DropModel(String fullName, String childName, String className, String instituteName, String instituteAddress, String phoneNumber, String address, String addKey, String userUsername, String userAccountType, String studentGetDriver, String parentUsername, String dropTime, String dropDate, String dropStatus, String studentDropKey) {
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
        this.dropTime = dropTime;
        this.dropDate = dropDate;
        this.dropStatus = dropStatus;
        this.studentDropKey = studentDropKey;
    }

    public DropModel() {
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

    public String getDropTime() {
        return dropTime;
    }

    public void setDropTime(String dropTime) {
        this.dropTime = dropTime;
    }

    public String getDropDate() {
        return dropDate;
    }

    public void setDropDate(String dropDate) {
        this.dropDate = dropDate;
    }

    public String getDropStatus() {
        return dropStatus;
    }

    public void setDropStatus(String dropStatus) {
        this.dropStatus = dropStatus;
    }

    public String getStudentDropKey() {
        return studentDropKey;
    }

    public void setStudentDropKey(String studentDropKey) {
        this.studentDropKey = studentDropKey;
    }
}
