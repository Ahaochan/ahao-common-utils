package moe.ahao.transaction.bank.transfer.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import moe.ahao.transaction.bank.transfer.entity.BankTransferAccount;
import moe.ahao.transaction.bank.transfer.mapper.BankTransferAccountMapper;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@AllArgsConstructor
public class BankTransferService extends ServiceImpl<BankTransferAccountMapper, BankTransferAccount> {
    private BankTransferAccountMybatisService bankTransferAccountMybatisService;

    @Transactional(rollbackFor = Exception.class)
    public void transfer(Long outAccountId, Long inAccountId, BigDecimal amount) {
        bankTransferAccountMybatisService.decrease(outAccountId, amount);
        bankTransferAccountMybatisService.increase(inAccountId, amount);
    }
}
