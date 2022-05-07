package moe.ahao.transaction.bank.transfer.service;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@AllArgsConstructor
public class BankTransferTCCService {
    private BankTransferAccountMybatisService bankTransferAccountMybatisService;

    @Transactional(rollbackFor = Exception.class)
    public void transfer(Long outAccountId, Long inAccountId, BigDecimal amount) {
        bankTransferAccountMybatisService.decreasePrepare(outAccountId, amount);
        bankTransferAccountMybatisService.increasePrepare(inAccountId, amount);
    }
}
