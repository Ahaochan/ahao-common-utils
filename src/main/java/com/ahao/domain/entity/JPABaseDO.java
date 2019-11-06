package com.ahao.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Ahaochan on 2017/6/5.
 */
@MappedSuperclass
public class JPABaseDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long createBy;
    private Long updateBy;
    private Date createTime;
    private Date updateTime;

    public JPABaseDO() {
    }

    public JPABaseDO(Long id) {
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
        JPABaseDO baseDO = (JPABaseDO) o;
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
