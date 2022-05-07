package moe.ahao.transaction.bank.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDTO {
    private String transferNo;
    private Long accountId;
    private BigDecimal amount;
}
