package com.craftsilicon.littlecabrider.models;

public class BankPayment {
	private String rowID;
	private String bankUserID;
	private String userBankName;
	private String userBankAccountNickName;
	private String userBankAccountNumber;
	public String getRowID() {
		return rowID;
	}
	public void setRowID(String rowID) {
		this.rowID = rowID;
	}
	public String getBankUserID() {
		return bankUserID;
	}
	public void setBankUserID(String bankUserID) {
		this.bankUserID = bankUserID;
	}
	public String getUserBankName() {
		return userBankName;
	}
	public void setUserBankName(String userBankName) {
		this.userBankName = userBankName;
	}
	public String getUserBankAccountNickName() {
		return userBankAccountNickName;
	}
	public void setUserBankAccountNickName(String userBankAccountNickName) {
		this.userBankAccountNickName = userBankAccountNickName;
	}
	public String getUserBankAccountNumber() {
		return userBankAccountNumber;
	}
	public void setUserBankAccountNumber(String userBankAccountNumber) {
		this.userBankAccountNumber = userBankAccountNumber;
	}

}
