package moe.ahao.transaction.bank.transfer.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import moe.ahao.transaction.bank.transfer.entity.BankTransferAccount;
import moe.ahao.transaction.bank.transfer.mapper.BankTransferAccountMapper;

import java.math.BigDecimal;

public class BankTransferAccountMybatisService extends ServiceImpl<BankTransferAccountMapper, BankTransferAccount> {
    public static final String SELECT_SQL = "select * from bank_transfer_account";
    public static final String INCREASE_AMOUNT_SQL = "update bank_transfer_account set amount = amount + '%s' where account_id = '%s'";
    public static final String DECREASE_AMOUNT_SQL = "update bank_transfer_account set amount = amount - '%s' where account_id = '%s' and amount >= '%s'";

    public void increase(Long accountId, BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) > 0) {
            throw new IllegalArgumentException("金额为负数");
        }
        this.baseMapper.updateAmountWhenIncrease(accountId, amount);
    }
    public void decrease(Long accountId, BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) > 0) {
            throw new IllegalArgumentException("金额为负数");
        }
        this.baseMapper.updateAmountWhenDecrease(accountId, amount);
    }
    public void increaseTry(Long accountId, BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) > 0) {
            throw new IllegalArgumentException("金额为负数");
        }
        this.baseMapper.updateAmountWhenIncreaseTry(accountId, amount);
    }
    public void increaseConfirm(Long accountId, BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) > 0) {
            throw new IllegalArgumentException("金额为负数");
        }
        this.baseMapper.updateAmountWhenIncreaseConfirm(accountId, amount);
    }
    public void increaseCancel(Long accountId, BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) > 0) {
            throw new IllegalArgumentException("金额为负数");
        }
        this.baseMapper.updateAmountWhenIncreaseCancel(accountId, amount);
    }
    public void decreaseTry(Long accountId, BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) > 0) {
            throw new IllegalArgumentException("金额为负数");
        }
        this.baseMapper.updateAmountWhenDecreaseTry(accountId, amount);
    }
    public void decreaseConfirm(Long accountId, BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) > 0) {
            throw new IllegalArgumentException("金额为负数");
        }
        this.baseMapper.updateAmountWhenDecreaseConfirm(accountId, amount);
    }
    public void decreaseCancel(Long accountId, BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) > 0) {
            throw new IllegalArgumentException("金额为负数");
        }
        this.baseMapper.updateAmountWhenDecreaseCancel(accountId, amount);
    }
}
