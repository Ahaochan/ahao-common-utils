package moe.ahao.transaction.user.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.ahao.transaction.user.mybatis.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    void insertSQL(User user);
    void insertBatchSQL(@Param("users") List<User> users);
    void truncate();
}
