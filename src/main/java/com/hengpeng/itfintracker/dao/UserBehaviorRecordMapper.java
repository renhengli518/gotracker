package com.hengpeng.itfintracker.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Options;

import com.hengpeng.itfintracker.commons.page.Page;
import com.hengpeng.itfintracker.entity.UserBehaviorRecord;

public interface UserBehaviorRecordMapper {

	int insert(UserBehaviorRecord record);

	int insertSelective(UserBehaviorRecord record);

	List<UserBehaviorRecord> getUserBehaviorByIp(String ip);

	//@Options(useCache = false, timeout = 10000, flushCache = true)
	List<UserBehaviorRecord> getUserBehaviorRecordPageList(Page page);

	List<UserBehaviorRecord> getUserBehaviorRecordList(Map<String, Object> map);
}