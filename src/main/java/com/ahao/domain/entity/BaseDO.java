package com.ahao.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Ahaochan on 2017/6/5.
 * dao返回的entity父类, 也可用于Mybatis返回插入id
 * 返回插入id的Mybatis用法:
 * int saveUser(@Param("baseDO") BaseDO baseDO, @Param("name") String name);
 * <insert id="saveUser" useGeneratedKeys="true" keyProperty="baseDO.id" keyColumn="id">
 * insert into user (name) values (#{name});
 * </insert>
 */
@MappedSuperclass
public class BaseDO {
    @TableId(type = IdType.AUTO)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long createBy;
    private Long updateBy;
    private Date createTime;
    private Date updateTime;

    public BaseDO() {
    }

    public BaseDO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseDO baseDO = (BaseDO) o;
        return Objects.equals(id, baseDO.id) &&
            Objects.equals(createBy, baseDO.createBy) &&
            Objects.equals(updateBy, baseDO.updateBy) &&
            Objects.equals(createTime, baseDO.createTime) &&
            Objects.equals(updateTime, baseDO.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createBy, updateBy, createTime, updateTime);
    }

    @Override
    public String toString() {
        return "BaseDO{" +
            "id=" + id +
            ", createBy=" + createBy +
            ", updateBy=" + updateBy +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            '}';
    }
}
