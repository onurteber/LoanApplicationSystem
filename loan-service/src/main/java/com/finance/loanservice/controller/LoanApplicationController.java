package com.finance.loanservice.controller;

import com.finance.loanservice.dto.LoanApplicationRequestDto;
import com.finance.loanservice.dto.LoanApplicationResponseDto;
import com.finance.loanservice.service.impl.LoanApplicationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoanApplicationController {

    private final LoanApplicationServiceImpl loanApplicationServiceImpl;

    @PostMapping("/create-application")
    public ResponseEntity<LoanApplicationResponseDto> save(@RequestBody LoanApplicationRequestDto creditApplication) throws Exception {
        LoanApplicationResponseDto response = null;
        try{
            response = loanApplicationServiceImpl.save(creditApplication);
            return ResponseEntity.ok(response);
        }  catch (Exception e) {
            throw new Exception(e.getCause().getMessage());
        }
    }

}
