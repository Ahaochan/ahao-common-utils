package moe.ahao.transaction.mybatis.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import moe.ahao.domain.entity.BaseDO;
import moe.ahao.transaction.mybatis.enums.Sex;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user")
public class User extends BaseDO implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String salt;
    private Sex sex;

    @TableField("is_locked")
    private Boolean locked;
    @TableField("is_disabled")
    private Boolean disabled;
    @TableField("is_deleted")
    @TableLogic(value = "0", delval = "1")
    private Boolean deleted;

    private Date expireTime;
}
