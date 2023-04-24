package shop.mtcoding.servicebank.model;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import shop.mtcoding.servicebank.model.account.Account;
import shop.mtcoding.servicebank.model.account.AccountRepository;
import shop.mtcoding.servicebank.model.user.User;
import shop.mtcoding.servicebank.model.user.UserRepository;

@ActiveProfiles("test")
@DataJpaTest // 트랜잭션 걸려 있음
public class AccountRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntityManager em;

    private User newUser(String username) {
        return User.builder()
                .username(username)
                .password("1234")
                .email(username + "@nate.com")
                .fullName("이름" + username)
                .status(true)
                .build();
    }

    private Account newAccount(Integer number, User user) {
        return Account.builder()
                .number(number)
                .password(1234)
                .balance(1000L)
                .status(true)
                .user(user)
                .build();
    }

    @BeforeEach
    public void setUp() {
        User cos = userRepository.save(newUser("cos"));
        User love = userRepository.save(newUser("love"));

        accountRepository.save(newAccount(1111, cos));
        accountRepository.save(newAccount(2222, love));
        accountRepository.save(newAccount(3333, cos));
        accountRepository.save(newAccount(4444, cos));

        em.clear();
    }

    @Test
    public void findByNumber_test() {
        accountRepository.findByNumber(1111);
    }

    @Test
    public void findByUserId_test() {
        List<Account> accountList = accountRepository.findByUserId(2L);
        accountList.stream().forEach(
                (account) -> account.getUser().getUsername());
    }

    @Test
    public void findAll_test() {
        accountRepository.findAll();
    }

    @Test
    public void findAllV2_test() {
        List<Account> accountList = accountRepository.findAll();
        Long n1 = accountList.get(0).getUser().getId();
        Long n2 = accountList.get(1).getUser().getId();
        userRepository.mFindInQuery(n1, n2);
    }

    @Test
    public void findAllV3_test() {
        List<Long> userIds = accountRepository.findUserIdDistinct();
        userIds.stream().forEach(id -> {
            System.out.println("디버그 : " + id);
        });
        accountRepository.findByUserIds(userIds);
    }

    @Test
    public void mFindAll_test() {
        accountRepository.mFindAll();
    }

    @Test
    public void findById_test() {
        accountRepository.findById(1L);
    }
}