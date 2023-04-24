package shop.mtcoding.servicebank.model.transaction;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.servicebank.dto.transaction.AllOutDTO;

@RequiredArgsConstructor
@Repository
public class TransactionQueryRepository {

    private final EntityManager em;

    public void allOrWithdrawOrDeposit(String gubun) {
        String sql = "";
        if (gubun.equals("all")) {
            sql = "select * from account ac " +
                    "left outer join transactiont ts ON ac.id = ts.withdrawAccountId "
                    + "left outer join transactiont ts ON ac.id = ts.depositAccountId "
                    + "where ts.withdrawAccountId = ? or ts.depositAccountId = ?";
        } else if (gubun.equals("withdraw")) {
            sql = "select * from account ac "
                    + "left outer join transactiont ts ON ac.id = ts.withdrawAccountId "
                    + "where ts.withdrawAccountId = ?";
        } else {
            sql = "select * from account ac " +
                    "left outer join transactiont ts ON ac.id = ts.withdrawAccountId "
                    + "left outer join transactiont ts ON ac.id = ts.depositAccountId "
                    + "where ts.withdrawAccountId = ? or ts.depositAccountId = ?";
        }

        Query query = em.createNativeQuery(sql);
        JpaResultMapper result = new JpaResultMapper();
        AllOutDTO dto = result.uniqueResult(query, AllOutDTO.class);

    }
}