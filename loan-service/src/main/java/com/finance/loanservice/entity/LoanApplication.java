package com.finance.loanservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "loan-application")
public class LoanApplication {
    @Id
    private String id;
    private String name;
    private String lastName;
    private String tckn;
    private double monthlyIncome;
    private String phone;
    private boolean status;
    private double limit;
}
