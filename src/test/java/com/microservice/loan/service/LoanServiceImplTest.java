package com.microservice.loan.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.microservice.loan.client.UserClientHandler;
import com.microservice.loan.dao.LoanRepository;
import com.microservice.loan.entity.Loan;
import com.microservice.loan.exception.LoanApplicationException;
import com.microservice.loan.service.impl.LoanServiceImpl;
import com.microservice.loan.util.ConstantUtil;

@ExtendWith(MockitoExtension.class)
public class LoanServiceImplTest {

    @InjectMocks
    private LoanServiceImpl loanService;

    @Mock
    private UserClientHandler userClientHandler;

    @Mock
    private LoanRepository loanRepository;

    @BeforeEach
    void setUp() throws Exception {
        // Use reflection to set the value of verifyUserUrl
        Field verifyUserUrlField = LoanServiceImpl.class.getDeclaredField("verifyUserUrl");
        verifyUserUrlField.setAccessible(true);
        verifyUserUrlField.set(loanService, "http://example.com/verify-user");
    }

    @Test
    void testIsUserEligibleForLoan_UserIsEligible() {
        when(loanRepository.findByUserId(anyLong())).thenReturn(Optional.empty());
        when(userClientHandler.verifyUser(anyLong())).thenReturn(true);

        boolean result = loanService.isUserEligibleForLoan(1L);
        assertTrue(result);
    }

    @Test
    void testIsUserEligibleForLoan_UserAlreadyLoaned() {
        Loan loan = new Loan();
        loan.setUserId(1L);
        when(loanRepository.findByUserId(anyLong())).thenReturn(Optional.of(loan));

        boolean result = loanService.isUserEligibleForLoan(1L);
        assertFalse(result);
    }

    @Test
    void testIsUserEligibleForLoan_UserNotVerified() {
        when(loanRepository.findByUserId(anyLong())).thenReturn(Optional.empty());
        when(userClientHandler.verifyUser(anyLong())).thenReturn(false);

        boolean result = loanService.isUserEligibleForLoan(1L);
        assertFalse(result);
    }

    @Test
    void testIssueLoan_UserNotEligible() {
        when(loanRepository.findByUserId(anyLong())).thenReturn(Optional.empty());
        when(userClientHandler.verifyUser(anyLong())).thenReturn(false);

        LoanApplicationException exception = assertThrows(LoanApplicationException.class, () -> {
            loanService.issueLoan(1L, 1000.0);
        });

        assertTrue(exception.getMessage().contains(ConstantUtil.USER_NOT_ELIGIBLE));
    }

    @Test
    void testIssueLoan_UserEligible() throws LoanApplicationException {
        when(loanRepository.findByUserId(anyLong())).thenReturn(Optional.empty());
        when(userClientHandler.verifyUser(anyLong())).thenReturn(true);

        String result = loanService.issueLoan(1L, 1000.0);

        assertTrue(result.contains(ConstantUtil.LOAN_ISSUED));
    }
}
