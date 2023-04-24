package shop.mtcoding.servicebank.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.servicebank.core.exception.Exception400;
import shop.mtcoding.servicebank.core.exception.Exception404;
import shop.mtcoding.servicebank.dto.account.AccountRequest;
import shop.mtcoding.servicebank.dto.account.AccountResponse;
import shop.mtcoding.servicebank.model.account.Account;
import shop.mtcoding.servicebank.model.account.AccountRepository;
import shop.mtcoding.servicebank.model.transaction.Transaction;
import shop.mtcoding.servicebank.model.transaction.TransactionRepository;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public AccountResponse.TransferOutDTO 계좌이체(AccountRequest.TransferInDTO transferInDTO, Long userId) {

        // 1. 출금계좌와 입금계좌 동일 여부
        if (transferInDTO.getWithdrawNumber() == transferInDTO.getDepositNumber()) {
            throw new Exception400("withdrawAccountNumber", "입출금계좌가 동일할 수 없습니다");
        }

        // 2. 출금계좌 확인
        Account withdrawAccountPS = accountRepository.findByNumber(transferInDTO.getWithdrawNumber())
                .orElseThrow(
                        () -> new Exception404("출금계좌를 찾을 수 없습니다"));

        // 3. 입금계좌 확인
        Account depositAccountPS = accountRepository.findByNumber(transferInDTO.getDepositNumber())
                .orElseThrow(
                        () -> new Exception404("입금계좌를 찾을 수 없습니다"));

        // 4. 출금 소유자 확인 (로그인한 사람과 동일한지)
        withdrawAccountPS.checkOwner(userId);

        // 5. 출금계좌 비빌번호 확인
        withdrawAccountPS.checkSamePassword(transferInDTO.getWithdrawPassword());

        // 6. 출금계좌 잔액 확인
        withdrawAccountPS.checkBalance(transferInDTO.getAmount());

        // 7. 이체하기
        withdrawAccountPS.withdraw(transferInDTO.getAmount());
        depositAccountPS.deposit(transferInDTO.getAmount());

        // 8. 거래내역 남기기
        Transaction transaction = Transaction.builder()
                .withdrawAccount(withdrawAccountPS)
                .depositAccount(depositAccountPS)
                .withdrawAccountBalance(withdrawAccountPS.getBalance())
                .depositAccountBalance(depositAccountPS.getBalance())
                .amount(transferInDTO.getAmount())
                .build();

        Transaction transactionPS = transactionRepository.save(transaction);

        // 9. DTO 응답
        return new AccountResponse.TransferOutDTO(withdrawAccountPS, transactionPS);
    }
}
