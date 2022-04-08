package moe.ahao.tree;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName(TreeNode.TABLE_NAME)
@Data
public class TreeNode {
    public static final String TABLE_NAME = "ahao_tree";

    private long id;
    private long parentId;
    private long left;
    private long right;
    private int level;
}
