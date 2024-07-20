package com.microservice.loan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.loan.exception.LoanApplicationException;
import com.microservice.loan.service.LoanService;
import com.microservice.loan.util.ConstantUtil;

@RestController
@RequestMapping("/api/v1/loan")
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("/check-eligibility/{userId}")
    public ResponseEntity<String> checkUserEligibility(@PathVariable Long userId) {
        boolean isEligible = loanService.isUserEligibleForLoan(userId);
        if (!isEligible) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ConstantUtil.USER_NOT_ELIGIBLE);
        }
        return ResponseEntity.status(HttpStatus.OK).body(ConstantUtil.USER_ELIGIBLE);
    }

    @PostMapping("/issue-loan")
    public ResponseEntity<String> issueLoan(@RequestParam Long userId, @RequestParam Double amount)
            throws LoanApplicationException {
        String response = loanService.issueLoan(userId, amount);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
