package com.finance.loanservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationResponseDto {
    private String name;
    private String lastName;
    private String tckn;
    private double monthlyIncome;
    private String phone;
    private boolean status;
    private double limit;
    private String message;
    private String errorMessage;
}
