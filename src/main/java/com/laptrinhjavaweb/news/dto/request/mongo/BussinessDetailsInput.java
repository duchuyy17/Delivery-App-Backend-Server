package com.laptrinhjavaweb.news.dto.request.mongo;

import lombok.Data;

@Data
public class BussinessDetailsInput {
    private String bankName;
    private Object accountName;
    private Object accountCode;
    private Object accountNumber;
    private Object bussinessRegNo;
    private Object companyRegNo;
    private Float taxRate;

    public String getAccountName() {
        return convertToString(accountName);
    }

    public String getAccountCode() {
        return convertToString(accountCode);
    }

    public String getAccountNumber() {
        return convertToString(accountNumber);
    }

    public String getBussinessRegNo() {
        return convertToString(bussinessRegNo);
    }

    public String getCompanyRegNo() {
        return convertToString(companyRegNo);
    }

    private String convertToString(Object value) {
        return value == null ? null : value.toString();
    }
}
