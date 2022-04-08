package moe.ahao.tree;

import org.apache.ibatis.annotations.*;

import java.util.List;

public interface TreeRepository<T extends TreeNode> {

    @Select("select * from " + TreeNode.TABLE_NAME + " where id = #{id}")
    @Results(id = "TreeNodeMap", value = {
        @Result(property = "id", column = "id", id = true),
        @Result(property = "parentId", column = "parent_id"),
        @Result(property = "left", column = "left_val"),
        @Result(property = "right", column = "right_val"),
        @Result(property = "level", column = "level"),
    })
    T selectOneById(@Param("id") long id);

    @ResultMap("TreeNodeMap")
    @Select({"select * from " + TreeNode.TABLE_NAME + " where left_val > #{left} and right_val < #{right} and level = #{level} order by left_val asc"})
    List<T> selectChildren(@Param("left") long left, @Param("right") long right, @Param("level") int level);
    @ResultMap("TreeNodeMap")
    @Select("select * from " + TreeNode.TABLE_NAME + " where left_val > #{left} and left_val < #{right} order by left_val asc")
    List<T> selectDescendantList(@Param("left") long left, @Param("right") long right);
    @ResultMap("TreeNodeMap")
    @Select("select * from " + TreeNode.TABLE_NAME + " where left_val < #{left} and right_val > #{right} order by left_val asc")
    List<T> selectAncestor(@Param("left") long left, @Param("right") long right);

    @Update("update " + TreeNode.TABLE_NAME + " set left_val = left_val + 2 where left_val > #{val}")
    int updateLeftWhenAdd(long val);
    @Update("update " + TreeNode.TABLE_NAME + " set right_val = right_val + 2 where right_val >= #{val}")
    int updateRightWhenAdd(long val);
    @Insert("insert into " + TreeNode.TABLE_NAME + "(parent_id, left_val, right_val, level) values(#{parentId}, #{left}, #{right}, #{level})")
    int insertOne(T node);

    @Update("update " + TreeNode.TABLE_NAME + " set left_val = left_val - #{decrease} where left_val > #{val}")
    int updateLeftWhenDelete(@Param("decrease") long decrease, @Param("val") long val);
    @Update("update " + TreeNode.TABLE_NAME + " set right_val = right_val - #{decrease} where right_val > #{val}")
    int updateRightWhenDelete(@Param("decrease") long decrease, @Param("val") long val);
    @Update("delete from " + TreeNode.TABLE_NAME + " where left_val >= #{left} and right_val <= #{right}")
    int delete(@Param("left") long left, @Param("right") long right);
}
