package shop.mtcoding.servicebank.model.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("select distinct ac.user.id from Account ac")
    List<Long> findUserIdDistinct();

    @Query("select ac from Account ac where ac.user.id in :userIds")
    List<Account> findByUserIds(@Param("userIds") List<Long> userIds);

    @Query("select ac from Account ac join fetch ac.user where ac.number = :number")
    Optional<Account> findByNumber(@Param("number") Integer number);

    @Query("select ac from Account ac where ac.user.id = :userId")
    List<Account> findByUserId(Long userId);

    @Query("select ac from Account ac join fetch ac.user")
    List<Account> mfindAll();

    void mFindAll();
}
