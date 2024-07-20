package com.microservice.loan.service;

import com.microservice.loan.exception.LoanApplicationException;

public interface LoanService {

    boolean isUserEligibleForLoan(Long userId);

    String issueLoan(Long userId, Double amount) throws LoanApplicationException;
}
