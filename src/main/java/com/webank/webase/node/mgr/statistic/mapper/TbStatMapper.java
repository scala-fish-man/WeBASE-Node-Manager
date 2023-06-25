package com.webank.webase.node.mgr.statistic.mapper;

import com.webank.webase.node.mgr.statistic.entity.TbStat;
import java.math.BigInteger;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

public interface TbStatMapper {

    @Delete({ "delete from tb_stat", "where group_id = #{groupId,jdbcType=INTEGER}" })
    int deleteByGroupId(String groupId);

    /**
     * Delete block height.
     */
    @Delete({
        "delete tb from tb_stat as tb,",
        "(SELECT max(block_number) maxBlock FROM tb_stat where group_id = #{groupId}) AS tmp",
        "where tb.group_id = #{groupId}",
        " and tb.block_number <= tmp.maxBlock - ${blockRetainMax}"})
    Integer remove(@Param("groupId") String groupId, @Param("blockRetainMax") BigInteger blockRetainMax);

    @Select({"select ", TbStatSqlProvider.ALL_COLUMN_FIELDS,
        " from tb_stat where group_id = #{groupId} ",
        " and stat_timestamp between #{startTimestamp} and #{endTimestamp} order by id"})
    List<TbStat> findByTimeBetween(@Param("groupId") String groupId, @Param("startTimestamp") String startTimestamp,
        @Param("endTimestamp") String endTimestamp);

    @Delete({"delete from tb_stat where stat_timestamp < #{timestamp}"})
    int deleteTimeAgo(String timestamp);

    @Select({ "select", TbStatSqlProvider.ALL_COLUMN_FIELDS, "from tb_stat where id = ",
            "(select max(id) from tb_stat where group_id = #{groupId}) " })
    TbStat getMaxByGroupId(@Param("groupId") String groupId);

    @Select({ "select", TbStatSqlProvider.ALL_COLUMN_FIELDS, "from tb_stat where ",
            " group_id = #{groupId} ", "and block_number = #{blockNumber}"})
    TbStat findByGroupAndBlockNum(@Param("groupId") String groupId, @Param("blockNumber") Integer blockNumber);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_stat
     *
     * @mbg.generated
     */
    @Delete({ "delete from tb_stat", "where id = #{id,jdbcType=INTEGER}" })
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_stat
     *
     * @mbg.generated
     */
    @InsertProvider(type = TbStatSqlProvider.class, method = "insertSelective")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
    int insertSelective(TbStat record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_stat
     *
     * @mbg.generated
     */
    @Select({ "select", "id, group_id, block_cycle, tps, block_number, block_size, stat_timestamp, create_time, ", "modify_time", "from tb_stat", "where id = #{id,jdbcType=INTEGER}" })
    @Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true), @Result(column = "group_id", property = "groupId", jdbcType = JdbcType.INTEGER), @Result(column = "block_cycle", property = "blockCycle", jdbcType = JdbcType.DOUBLE), @Result(column = "tps", property = "tps", jdbcType = JdbcType.INTEGER), @Result(column = "block_number", property = "blockNumber", jdbcType = JdbcType.INTEGER), @Result(column = "block_size", property = "blockSize", jdbcType = JdbcType.INTEGER), @Result(column = "stat_timestamp", property = "statTimestamp", jdbcType = JdbcType.VARCHAR), @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP), @Result(column = "modify_time", property = "modifyTime", jdbcType = JdbcType.TIMESTAMP) })
    TbStat selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_stat
     *
     * @mbg.generated
     */
    @UpdateProvider(type = TbStatSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(TbStat record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_stat
     *
     * @mbg.generated
     */
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert({ "<script>", "insert into tb_stat (group_id, ", "block_cycle, tps, ", "block_number, block_size, ", "stat_timestamp, create_time, ", "modify_time)", "values<foreach collection=\"list\" item=\"detail\" index=\"index\" separator=\",\">(#{detail.groupId,jdbcType=INTEGER}, ", "#{detail.blockCycle,jdbcType=DOUBLE}, #{detail.tps,jdbcType=INTEGER}, ", "#{detail.blockNumber,jdbcType=INTEGER}, #{detail.blockSize,jdbcType=INTEGER}, ", "#{detail.statTimestamp,jdbcType=VARCHAR}, #{detail.createTime,jdbcType=TIMESTAMP}, ", "#{detail.modifyTime,jdbcType=TIMESTAMP})</foreach></script>" })
    int batchInsert(java.util.List<TbStat> list);
}
