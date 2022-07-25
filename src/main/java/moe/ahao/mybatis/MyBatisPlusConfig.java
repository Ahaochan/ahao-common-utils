package moe.ahao.mybatis;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(MybatisSqlSessionFactoryBean.class)
public class MyBatisPlusConfig {
    /**
     * 自动填充字段, 配合 {@link TableField}
     * @see <a href="https://baomidou.com/pages/4c6bcf/">自动填充功能</a>
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                this.strictInsertFill(metaObject, "createTime", Date::new, Date.class);
                this.strictInsertFill(metaObject, "updateTime", Date::new, Date.class);
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                this.strictUpdateFill(metaObject, "updateTime", Date::new, Date.class);
            }
        };
    }

    /**
     * @see <a href="https://baomidou.com/pages/2976a3/#mybatisplusinterceptor">插件主体(必看!)(since 3.4.0)</a>
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();

        List<InnerInterceptor> innerInterceptors = Arrays.asList(
            // new TenantLineInnerInterceptor(), new DynamicTableNameInnerInterceptor(),
            new PaginationInnerInterceptor(), new OptimisticLockerInnerInterceptor(),
            // new IllegalSQLInnerInterceptor(),
            new BlockAttackInnerInterceptor() // update 和 delete 必须要有 where
        );

        mybatisPlusInterceptor.setInterceptors(innerInterceptors);
        return mybatisPlusInterceptor;
    }
}
