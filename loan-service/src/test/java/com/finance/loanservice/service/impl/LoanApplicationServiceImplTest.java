package com.finance.loanservice.service.impl;

import com.finance.loanservice.dto.LoanApplicationRequestDto;
import com.finance.loanservice.dto.LoanApplicationResponseDto;
import com.finance.loanservice.entity.LoanApplication;
import com.finance.loanservice.repository.LoanApplicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanApplicationServiceImplTest {

    @InjectMocks
    private LoanApplicationServiceImpl loanApplicationService;
    @Mock
    private  LoanApplicationRepository loanApplicationRepository;
    @Mock
    private NotificationProducer notificationService;
    @Mock
    private MockCreditScoreService mockCreditScoreService;

    @Test
    public void testSave() throws Exception {
        LoanApplicationRequestDto dto = new LoanApplicationRequestDto();
        dto.setName("test-user-name");
        dto.setLastName("test-user-last-name");
        dto.setMonthlyIncome(7250);
        dto.setPhone("5422222323");
        dto.setTckn("12345678912");

        LoanApplication loanMock = mock(LoanApplication.class);
        int mockCreditSkore = ThreadLocalRandom.current().nextInt(0, 1900 + 1);
        when(loanApplicationRepository.save(ArgumentMatchers.any(LoanApplication.class))).thenReturn(loanMock);
        when(mockCreditScoreService.calculateCreditScore(ArgumentMatchers.any(String.class))).thenReturn(ThreadLocalRandom.current().nextInt(0, 1900 + 1));

        LoanApplicationResponseDto save = loanApplicationService.save(dto);

        assertEquals(save.getName(),dto.getName());
        if(save.getLimit() != 0) {
            assertEquals(save.isStatus(),(true));
        } else {
            assertEquals(save.isStatus(),(false));
        }


    }
}
