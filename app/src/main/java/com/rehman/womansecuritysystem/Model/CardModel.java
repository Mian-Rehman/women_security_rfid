package com.rehman.womansecuritysystem.Model;

public class CardModel
{
    String currentDate,currentTime,cardEntryKey,rfidCardNumber;

    public CardModel(String currentDate, String currentTime, String cardEntryKey, String rfidCardNumber) {
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.cardEntryKey = cardEntryKey;
        this.rfidCardNumber = rfidCardNumber;
    }

    public CardModel() {
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getCardEntryKey() {
        return cardEntryKey;
    }

    public void setCardEntryKey(String cardEntryKey) {
        this.cardEntryKey = cardEntryKey;
    }

    public String getRfidCardNumber() {
        return rfidCardNumber;
    }

    public void setRfidCardNumber(String rfidCardNumber) {
        this.rfidCardNumber = rfidCardNumber;
    }
}
