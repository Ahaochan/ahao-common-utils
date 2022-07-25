package moe.ahao.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import moe.ahao.util.commons.CloneHelper;

import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Ahaochan on 2017/6/5.
 * <p>
 * dao返回的entity父类, 也可用于Mybatis返回插入id
 * 返回插入id的Mybatis用法:
 * <pre>
 * int saveUser(@Param("baseDO") BaseDO baseDO, @Param("name") String name);
 * </pre>
 * <pre>
 * &lt;insert id="saveUser" useGeneratedKeys="true" keyProperty="baseDO.id" keyColumn="id"&gt;
 *     insert into user (name) values (#{name});
 * &lt;/insert&gt;
 * </pre>
 */
@MappedSuperclass
public class BaseDO {
    private Long createBy;
    private Long updateBy;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    public BaseDO() {
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getCreateTime() {
        return CloneHelper.clone(createTime);
    }

    public void setCreateTime(Date createTime) {
        this.createTime = CloneHelper.clone(createTime);
    }

    public Date getUpdateTime() {
        return CloneHelper.clone(updateTime);
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = CloneHelper.clone(updateTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseDO baseDO = (BaseDO) o;
        return Objects.equals(createBy, baseDO.createBy) &&
            Objects.equals(updateBy, baseDO.updateBy) &&
            Objects.equals(createTime, baseDO.createTime) &&
            Objects.equals(updateTime, baseDO.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createBy, updateBy, createTime, updateTime);
    }

    @Override
    public String toString() {
        return "BaseDO{" +
            "createBy=" + createBy +
            ", updateBy=" + updateBy +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            '}';
    }
}
