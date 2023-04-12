package shop.mtcoding.servicebank.dto.account;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.servicebank.core.util.MyDateUtils;
import shop.mtcoding.servicebank.model.account.Account;
import shop.mtcoding.servicebank.model.transaction.Transaction;
import shop.mtcoding.servicebank.model.user.User;

import java.util.List;
import java.util.stream.Collectors;

public class AccountResponse {

    @Setter @Getter
    public static class SaveDTO {
        private Long id;
        private Integer number;
        private Long balance;

        public SaveDTO(Account account) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
        }
    }

    @Setter @Getter
    public static class ListDTO {
        private String fullName;
        private List<AccountDTO> accounts;

        public ListDTO(User user, List<Account> accounts) {
            this.fullName = user.getFullName();
            this.accounts = accounts.stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
        }

        @Setter @Getter
        public class AccountDTO {
            private Long id;
            private Integer number;
            private Long balance;

            public AccountDTO(Account account) {
                this.id = account.getId();
                this.number = account.getNumber();
                this.balance = account.getBalance();
            }
        }
    }
    
    // 계좌 주인은 sender
    @Setter @Getter
    public static class WithdrawDTO {
        private Long id;
        private Integer number; // 계좌번호
        private Long balance; // 현재 잔액
        private TransactionDTO transaction;

        public WithdrawDTO(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transaction = new TransactionDTO(transaction);
        }

        @Setter
        @Getter
        public class TransactionDTO {
            private Long id;
            private String sender;
            private String receiver;
            private Long amount;
            private Long balance; // 이체시점 잔액
            private String createdAt;

            public TransactionDTO(Transaction transaction) {
                this.id = transaction.getId();
                this.sender = transaction.getWithdrawAccount().getNumber()+"";
                this.receiver = transaction.getDepositAccount().getNumber()+"";
                this.amount = transaction.getAmount();
                this.balance = transaction.getWithdrawAccountBalance();
                this.createdAt = MyDateUtils.toStringFormat(transaction.getCreatedAt());
            }
        }
    }

    // 계좌 주인은 receiver
    @Setter @Getter
    public static class DepositDTO {
        private Long id;
        private Integer number; // 계좌번호
        private Long balance; // 잔액
        private TransactionDTO transaction;

        public DepositDTO(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transaction = new TransactionDTO(transaction);
        }

        @Setter
        @Getter
        public class TransactionDTO {
            private Long id;
            private String sender;
            private String receiver;
            private Long amount;
            private Long balance;
            private String createdAt;

            public TransactionDTO(Transaction transaction) {
                this.id = transaction.getId();
                this.sender = transaction.getWithdrawAccount().getNumber()+"";
                this.receiver = transaction.getDepositAccount().getNumber()+"";
                this.amount = transaction.getAmount();
                this.balance = transaction.getDepositAccountBalance();
                this.createdAt = MyDateUtils.toStringFormat(transaction.getCreatedAt());
            }
        }
    }

    // 계좌주인은 상황에 따라 다름 (출금 or 입금)
    @Setter @Getter
    public static class WithDrawAndDepositDTO {
        private Long id;
        private Integer number; // 계좌번호
        private Long balance; // 잔액
        private TransactionDTO transaction;

        public WithDrawAndDepositDTO(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transaction = new TransactionDTO(transaction, account);
        }

        @Setter
        @Getter
        public class TransactionDTO {
            private Long id;
            private String sender;
            private String receiver;
            private Long amount;
            private Long balance;
            private String createdAt;

            public TransactionDTO(Transaction transaction, Account account) {
                this.id = transaction.getId();
                this.sender = transaction.getWithdrawAccount().getNumber()+"";
                this.receiver = transaction.getDepositAccount().getNumber()+"";
                this.amount = transaction.getAmount();
                if(account.getNumber() == transaction.getWithdrawAccount().getNumber()){
                    this.balance = transaction.getWithdrawAccountBalance();
                }else{
                    this.balance = transaction.getDepositAccountBalance();
                }
                this.createdAt = MyDateUtils.toStringFormat(transaction.getCreatedAt());
            }
        }
    }

}
