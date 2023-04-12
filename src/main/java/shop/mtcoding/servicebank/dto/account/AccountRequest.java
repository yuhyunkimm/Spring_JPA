package shop.mtcoding.servicebank.dto.account;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.servicebank.model.account.Account;
import shop.mtcoding.servicebank.model.user.User;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

public class AccountRequest {

    @Setter
    @Getter
    public static class SaveDTO {
        @NotNull
        @Digits(integer = 4, fraction = 0)
        private Integer number;
        @NotNull
        @Digits(integer = 4, fraction = 0)
        private Integer password;

        public Account toEntity(User user) {
            return Account.builder()
                    .user(user)
                    .number(number)
                    .password(password)
                    .balance(1000L)
                    .status(true)
                    .build();
        }
    }

    @Setter
    @Getter
    public static class TransferDTO {
        @NotNull
        @Digits(integer = 4, fraction = 0)
        private Integer withdrawNumber;
        @NotNull
        @Digits(integer = 4, fraction = 0)
        private Integer depositNumber;
        @NotNull
        @Digits(integer = 4, fraction = 0)
        private Integer withdrawPassword;
        @NotNull
        private Long amount;
    }

}