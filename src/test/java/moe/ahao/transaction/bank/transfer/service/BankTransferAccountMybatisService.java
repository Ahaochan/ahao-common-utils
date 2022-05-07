package moe.ahao.transaction.bank.transfer.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import moe.ahao.transaction.bank.transfer.entity.BankTransferAccount;
import moe.ahao.transaction.bank.transfer.mapper.BankTransferAccountMapper;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public class BankTransferAccountMybatisService extends ServiceImpl<BankTransferAccountMapper, BankTransferAccount> {
    public static final String SELECT_SQL = "select * from bank_transfer_account";
    public static final String INCREASE_AMOUNT_SQL = "update bank_transfer_account set amount = amount + '%s' where account_id = '%s'";
    public static final String DECREASE_AMOUNT_SQL = "update bank_transfer_account set amount = amount - '%s' where account_id = '%s' and amount >= '%s'";

    @Transactional(rollbackFor = Exception.class)
    public void increase(Long accountId, BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) > 0) {
            throw new IllegalArgumentException("金额为负数");
        }
        int count = this.baseMapper.updateAmountWhenIncrease(accountId, amount);
        if(count <= 0) {
            throw new IllegalStateException("更新失败");
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public void decrease(Long accountId, BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) > 0) {
            throw new IllegalArgumentException("金额为负数");
        }
        int count = this.baseMapper.updateAmountWhenDecrease(accountId, amount);
        if(count <= 0) {
            throw new IllegalStateException("更新失败");
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public void increasePrepare(Long accountId, BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) > 0) {
            throw new IllegalArgumentException("金额为负数");
        }
        int count = this.baseMapper.updateAmountWhenIncreasePrepare(accountId, amount);
        if(count <= 0) {
            throw new IllegalStateException("更新失败");
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public void increaseConfirm(Long accountId, BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) > 0) {
            throw new IllegalArgumentException("金额为负数");
        }
        int count = this.baseMapper.updateAmountWhenIncreaseConfirm(accountId, amount);
        if(count <= 0) {
            throw new IllegalStateException("更新失败");
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public void increaseCancel(Long accountId, BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) > 0) {
            throw new IllegalArgumentException("金额为负数");
        }
        int count = this.baseMapper.updateAmountWhenIncreaseCancel(accountId, amount);
        if(count <= 0) {
            throw new IllegalStateException("更新失败");
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public void decreasePrepare(Long accountId, BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) > 0) {
            throw new IllegalArgumentException("金额为负数");
        }
        int count = this.baseMapper.updateAmountWhenDecreasePrepare(accountId, amount);
        if(count <= 0) {
            throw new IllegalStateException("更新失败");
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public void decreaseConfirm(Long accountId, BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) > 0) {
            throw new IllegalArgumentException("金额为负数");
        }
        int count = this.baseMapper.updateAmountWhenDecreaseConfirm(accountId, amount);
        if(count <= 0) {
            throw new IllegalStateException("更新失败");
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public void decreaseCancel(Long accountId, BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) > 0) {
            throw new IllegalArgumentException("金额为负数");
        }
        int count = this.baseMapper.updateAmountWhenDecreaseCancel(accountId, amount);
        if(count <= 0) {
            throw new IllegalStateException("更新失败");
        }
    }
}
