package shop.mtcoding.servicebank.dto.account;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.servicebank.core.util.MyDateUtils;
import shop.mtcoding.servicebank.model.account.Account;
import shop.mtcoding.servicebank.model.transaction.Transaction;

public class AccountResponse {
    @Setter
    @Getter
    public static class TransferOutDTO {
        private Long id;
        private Integer number;
        private Long balance;
        private TransactionDTO transaction;

        public TransferOutDTO(Account account, Transaction transaction) {
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
                this.sender = transaction.getWithdrawAccount().getNumber() + "";
                this.receiver = transaction.getDepositAccount().getNumber() + "";
                this.amount = transaction.getAmount();
                this.balance = transaction.getWithdrawAccountBalance();
                this.createdAt = MyDateUtils.toStringFormat(transaction.getCreatedAt());
            }
        }
    }
}