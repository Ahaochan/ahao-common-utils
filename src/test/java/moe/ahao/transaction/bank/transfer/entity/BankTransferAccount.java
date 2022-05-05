package moe.ahao.transaction.bank.transfer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import moe.ahao.domain.entity.BaseDO;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bank_transfer_account")
public class BankTransferAccount extends BaseDO {
    @TableId(type = IdType.AUTO)
    private Long accountId;
    private String accountName;
    private BigDecimal amount;
    private BigDecimal outBufferAmount;
    private BigDecimal inBufferAmount;
}
