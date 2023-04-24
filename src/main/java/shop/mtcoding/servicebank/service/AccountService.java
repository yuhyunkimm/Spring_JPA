package shop.mtcoding.servicebank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.servicebank.core.exception.Exception400;
import shop.mtcoding.servicebank.core.exception.Exception404;
import shop.mtcoding.servicebank.dto.account.AccountRequest;
import shop.mtcoding.servicebank.dto.account.AccountResponse;
import shop.mtcoding.servicebank.dto.user.UserResponse;
import shop.mtcoding.servicebank.model.account.Account;
import shop.mtcoding.servicebank.model.account.AccountRepository;
import shop.mtcoding.servicebank.model.transaction.Transaction;
import shop.mtcoding.servicebank.model.transaction.TransactionRepository;
import shop.mtcoding.servicebank.model.user.User;
import shop.mtcoding.servicebank.model.user.UserRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public AccountResponse.SaveOutDTO 계좌등록(AccountRequest.SaveInDTO saveInDTO, Long userId) {

        // 1. 회원 존재 여부
        User userPS = userRepository.findById(userId).orElseThrow(
                () -> new Exception404("유저를 찾을 수 없습니다"));

        // 2. 계좌 존재 여부
        Optional<Account> accountOP = accountRepository.findByNumber(saveInDTO.getNumber());
        if (accountOP.isPresent()) {
            throw new Exception400("number", "해당 계좌가 이미 존재합니다");
        }

        // 3. 계좌 등록
        Account accountPS = accountRepository.save(saveInDTO.toEntity(userPS));

        // 4. DTO 응답
        return new AccountResponse.SaveOutDTO(accountPS);
    }

    @Transactional(readOnly = true)
    public AccountResponse.ListOutDTO 유저계좌목록보기(Long userId) {
        // 1. 회원 존재 여부
        User userPS = userRepository.findById(userId).orElseThrow(
                () -> new Exception404("유저를 찾을 수 없습니다"));

        // 2. 유저 계좌목록 조회
        List<Account> accountListPS = accountRepository.findByUserId(userId);

        // 3. DTO 응답
        return new AccountResponse.ListOutDTO(userPS, accountListPS);
    }

    @Transactional(readOnly = true)
    public AccountResponse.DetailOutDTO 계좌상세보기(Integer number, Long userId) {
        // 1. 계좌 확인
        Account accountPS = accountRepository.findByNumber(number)
                .orElseThrow(
                        () -> new Exception404("계좌를 찾을 수 없습니다"));

        // 2. 계좌 소유주 확인
        accountPS.checkOwner(userId);

        // 3. DTO 응답
        return new AccountResponse.DetailOutDTO(accountPS);
    }

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
