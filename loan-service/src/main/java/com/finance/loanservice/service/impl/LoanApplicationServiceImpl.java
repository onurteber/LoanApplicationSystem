package com.finance.loanservice.service.impl;

import com.finance.loanservice.dto.LoanApplicationRequestDto;
import com.finance.loanservice.dto.LoanApplicationResponseDto;
import com.finance.loanservice.dto.LoanNotification;
import com.finance.loanservice.dto.enums.NotificationType;
import com.finance.loanservice.entity.LoanApplication;
import com.finance.loanservice.repository.LoanApplicationRepository;
import com.finance.loanservice.service.LoanApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanApplicationServiceImpl implements LoanApplicationService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final NotificationProducer notificationService;
    private final MockCreditScoreService mockCreditScoreService;

    @Override
    @Transactional
    public LoanApplicationResponseDto save(@RequestBody LoanApplicationRequestDto creditApplicationRequest) throws Exception {
        LoanApplicationResponseDto creditApplicationResponse = new LoanApplicationResponseDto();
        if (creditApplicationRequest.getLastName() != null && creditApplicationRequest.getName() != null
           && creditApplicationRequest.getPhone() != null && creditApplicationRequest.getTckn() != null) {
            try {
                int creditScore = mockCreditScoreService.calculateCreditScore(creditApplicationRequest.getTckn());
                if (creditScore < 500) {
                    creditApplicationResponse.setStatus(false);
                } else if (isBetween(500, 1000, creditScore)) {
                    creditApplicationResponse.setStatus(true);
                    if(creditApplicationRequest.getMonthlyIncome() < 5000) {
                        creditApplicationResponse.setLimit(10000);
                    } else {
                        // Aylık geliri beş bin tl nin üzerindeyse ve skoru 500-1000 olduğu durumda veya 500 e eşit olduğu durum için kural belirtilmemiş.
                        // Bu durum için başvuru reddi dönüşü yapıldı.
                        creditApplicationResponse.setLimit(0);
                        creditApplicationResponse.setStatus(false);
                        System.out.println("Out of the task rules");
                    }
                } else if (isBetween(999,1901,creditScore)) {
                    creditApplicationResponse.setLimit(creditApplicationRequest.getMonthlyIncome() * 4);
                    creditApplicationResponse.setStatus(true);
                } else {
                    creditApplicationResponse.setLimit(0);
                    creditApplicationResponse.setStatus(false);
                    System.out.println("Out of the task rules");
                }
                loanApplicationResponseDtoMapper(creditApplicationRequest,creditApplicationResponse);
                LoanApplication save = loanApplicationRepository.save(loanApplicationMapper(creditApplicationResponse));
                notificationService.sendToQueue(prepareNotification(save));
                creditApplicationResponse.setMessage(getMessage(save));
                return creditApplicationResponse;

            } catch (Exception e) {
                throw new Exception(e);
            }
        } else {
            throw new IllegalArgumentException();
        }

    }

    private static boolean isBetween(int a, int b, int c) {
        return b > a ? c > a && c < b : c > b && c < a;
    }

    private LoanNotification prepareNotification(LoanApplication application) {
        LoanNotification loan = new LoanNotification();
        loan.setNotificationId(UUID.randomUUID().toString());
        loan.setNotificationType(NotificationType.SMS);
        loan.setPhoneNumber(application.getPhone());
        loan.setMessage(getMessage(application));
        return loan;
    }

    private String getMessage(LoanApplication application) {
        String message = "";
        if (application.isStatus()) {
            message = "Sayın " + application.getName() + " " + application.getLastName() +
                    " kredi başvurunuz onaylanmıştır. Kredi limitiniz: " + application.getLimit() + " TL' dir. İyi günler dileriz.";
        } else {
            message = "Sayın " + application.getName() + " " + application.getLastName() +
                    " kredi başvurunuz reddedilmiştir. İyi günler dileriz.";
        }
        return message;
    }

    private LoanApplicationResponseDto loanApplicationResponseDtoMapper(LoanApplicationRequestDto requestDto, LoanApplicationResponseDto responseDto){
        responseDto.setTckn(requestDto.getTckn());
        responseDto.setPhone(requestDto.getPhone());
        responseDto.setMonthlyIncome(requestDto.getMonthlyIncome());
        responseDto.setName(requestDto.getName());
        responseDto.setLastName(requestDto.getLastName());
        return responseDto;
    }

    private LoanApplication loanApplicationMapper(LoanApplicationResponseDto responseDto) {
        LoanApplication loanApp = new LoanApplication();
        loanApp.setLastName(responseDto.getLastName());
        loanApp.setMonthlyIncome(responseDto.getMonthlyIncome());
        loanApp.setName(responseDto.getName());
        loanApp.setPhone(responseDto.getPhone());
        loanApp.setLimit(responseDto.getLimit());
        loanApp.setTckn(responseDto.getTckn());
        loanApp.setStatus(responseDto.isStatus());
        return loanApp;
    }
}
