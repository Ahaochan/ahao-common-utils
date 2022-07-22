package moe.ahao.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页查询结果
 */
@Data
@NoArgsConstructor
public class PagingInfo<T> {
    /**
     * 页码
     */
    private Integer pageNo;
    /**
     * 每页条数
     */
    private Integer pageSize;
    /**
     * 总条数
     */
    private Long total;
    /**
     * 起始位置
     */
    // private Integer startPos = (pageNo - 1) * pageSize;

    private List<T> list;

    public PagingInfo(Long total, List<T> list) {
        this.total = total;
        this.list = list;
    }
}
