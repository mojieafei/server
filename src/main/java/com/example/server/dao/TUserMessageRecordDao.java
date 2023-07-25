package com.example.server.dao;

import com.example.server.dao.model.TUserMessageRecord;
import com.example.server.dao.model.TUserMessageRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TUserMessageRecordDao {
    long countByExample(TUserMessageRecordExample example);

    int deleteByExample(TUserMessageRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TUserMessageRecord record);

    int insertSelective(TUserMessageRecord record);

    List<TUserMessageRecord> selectByExample(TUserMessageRecordExample example);

    TUserMessageRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TUserMessageRecord record, @Param("example") TUserMessageRecordExample example);

    int updateByExample(@Param("record") TUserMessageRecord record, @Param("example") TUserMessageRecordExample example);

    int updateByPrimaryKeySelective(TUserMessageRecord record);

    int updateByPrimaryKey(TUserMessageRecord record);
}