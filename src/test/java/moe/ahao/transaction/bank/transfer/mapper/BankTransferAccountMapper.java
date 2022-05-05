package moe.ahao.transaction.bank.transfer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.ahao.transaction.bank.transfer.entity.BankTransferAccount;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

public interface BankTransferAccountMapper extends BaseMapper<BankTransferAccount> {
    @Update("update bank_transfer_account set amount = amount + #{amount} where account_id = #{accountId}")
    int updateAmountWhenIncrease(@Param("accountId") Long accountId, @Param("amount") BigDecimal amount);
    @Update("update bank_transfer_account set amount = amount - #{amount} where account_id = #{accountId} and amount >= #{amount}")
    int updateAmountWhenDecrease(@Param("accountId") Long accountId, @Param("amount") BigDecimal amount);

    @Update("update bank_transfer_account set in_buffer_amount = in_buffer_amount + #{amount} where account_id = #{accountId}")
    void updateAmountWhenIncreaseTry(@Param("accountId") Long accountId, @Param("amount") BigDecimal amount);
    @Update("update bank_transfer_account set amount = amount + #{amount}, in_buffer_amount = in_buffer_amount - #{amount} where account_id = #{accountId} and in_buffer_amount >= #{amount}")
    void updateAmountWhenIncreaseConfirm(@Param("accountId") Long accountId, @Param("amount") BigDecimal amount);
    @Update("update bank_transfer_account set in_buffer_amount = in_buffer_amount - #{amount} where account_id = #{accountId} and in_buffer_amount >= #{amount}")
    void updateAmountWhenIncreaseCancel(@Param("accountId") Long accountId, @Param("amount") BigDecimal amount);

    @Update("update bank_transfer_account set amount = amount - #{amount}, out_buffer_amount = out_buffer_amount + #{amount} where account_id = #{accountId} and amount >= #{amount}")
    void updateAmountWhenDecreaseTry(@Param("accountId") Long accountId, @Param("amount") BigDecimal amount);
    @Update("update bank_transfer_account set out_buffer_amount = out_buffer_amount - #{amount} where account_id = #{accountId} and out_buffer_amount >= #{amount}")
    void updateAmountWhenDecreaseConfirm(@Param("accountId") Long accountId, @Param("amount") BigDecimal amount);
    @Update("update bank_transfer_account set amount = amount + #{amount}, out_buffer_amount = out_buffer_amount - #{amount} where account_id = #{accountId} and out_buffer_amount >= #{amount}")
    void updateAmountWhenDecreaseCancel(@Param("accountId") Long accountId, @Param("amount") BigDecimal amount);
}
