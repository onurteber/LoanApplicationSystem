package com.finance.loanservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoanApplicationRequestDto {
    private String name;
    private String lastName;
    private String tckn;
    private double monthlyIncome;
    private String phone;
}
