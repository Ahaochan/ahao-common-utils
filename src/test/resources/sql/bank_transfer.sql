DROP TABLE IF EXISTS bank_transfer_account;
CREATE TABLE bank_transfer_account (
account_id          BIGINT(20) AUTO_INCREMENT NOT NULL COMMENT '主键ID',
account_name        VARCHAR(50) NOT NULL COMMENT '用户名',
amount              DECIMAL(18, 4) COMMENT '余额',
in_buffer_amount    DECIMAL(18, 4) COMMENT '转入金额缓冲',
out_buffer_amount   DECIMAL(18, 4) COMMENT '转出金额缓冲',
create_by           BIGINT(20) DEFAULT 0 COMMENT '创建人',
update_by           BIGINT(20) DEFAULT 0 COMMENT '更新人',
create_time         DATETIME COMMENT '创建时间',
update_time         DATETIME COMMENT '更新时间',
PRIMARY KEY (account_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='银行转账表';

INSERT INTO bank_transfer_account(account_id, account_name, amount, in_buffer_amount, out_buffer_amount, create_by, update_by, create_time, update_time) values
(1, 'admin1', 100, 0, 0, 1, 1, now(), now()),
(2, 'admin2', 100, 0, 0, 1, 1, now(), now());
