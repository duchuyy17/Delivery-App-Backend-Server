package com.laptrinhjavaweb.news.dto.data;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BussinessDetails {
    String bankName;
    String accountName;
    String accountNumber;
    String accountCode;
    String businessRegNo;
    String companyRegNo;
    Float taxRate;
}
