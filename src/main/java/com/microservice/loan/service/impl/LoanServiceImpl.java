package com.microservice.loan.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.microservice.loan.client.UserClientHandler;
import com.microservice.loan.dao.LoanRepository;
import com.microservice.loan.entity.Loan;
import com.microservice.loan.exception.LoanApplicationException;
import com.microservice.loan.service.LoanService;
import com.microservice.loan.util.ConstantUtil;

@Service
public class LoanServiceImpl implements LoanService {
	@Value("${loan_microservice.verify_user.url}")
	private String verifyUserUrl;

	@Autowired
	private UserClientHandler userClientHandler;

	@Autowired
	private LoanRepository loanRepository;

	@Override
	public boolean isUserEligibleForLoan(Long userId) {
		return !isUserAlreadyLoaned(userId) && userClientHandler.verifyUser(userId);
	}

	@Override
	public String issueLoan(Long userId, Double amount) throws LoanApplicationException {
		if (!isUserEligibleForLoan(userId)) {
			throw new LoanApplicationException(HttpStatus.BAD_REQUEST, ConstantUtil.USER_NOT_ELIGIBLE);
		}
		
		// Logic to issue loan
		Loan loan = new Loan();
		loan.setUserId(userId);
		loan.setIssued(true);
		loan.setAmount(amount);
		loanRepository.save(loan);
		return ConstantUtil.LOAN_ISSUED;
	}

	private boolean isUserAlreadyLoaned(Long userId) {
		// Logic to check if the user is already present in the loan database
		Optional<Loan> loan = loanRepository.findByUserId(userId);
		return loan.isPresent();
	}

}
