package org.ibase4j.provider.sys;

import java.util.Map;

import org.ibase4j.core.config.Resources;
import org.ibase4j.core.support.dubbo.BaseProviderImpl;
import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.ibase4j.dao.generator.SysDeptMapper;
import org.ibase4j.dao.sys.SysDeptExpandMapper;
import org.ibase4j.model.generator.SysDept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.github.pagehelper.PageInfo;

/**
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:19:19
 */
@CacheConfig(cacheNames = "sysDept")
@DubboService(interfaceClass = SysDeptProvider.class)
public class SysDeptProviderImpl extends BaseProviderImpl<SysDept> implements SysDeptProvider {
	@Autowired
	private SysDeptMapper sysDeptMapper;
	@Autowired
	private SysDeptExpandMapper syDeptExpandMapper;

	@CachePut
	@Transactional
	public SysDept update(SysDept record) {
		if (record.getId() == null) {
			record.setEnable(1);
			sysDeptMapper.insert(record);
		} else {
			sysDeptMapper.updateByPrimaryKey(record);
		}
		return record;
	}

	@CachePut
	@Transactional
	public void delete(Integer id) {
		SysDept record = queryById(id);
		Assert.notNull(record, String.format(Resources.getMessage("USER_IS_NULL"), id));
		record.setEnable(0);
		update(record);
	}

	@Cacheable
	public SysDept queryById(Integer id) {
		return sysDeptMapper.selectByPrimaryKey(id);
	}

	public PageInfo<SysDept> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(syDeptExpandMapper.query(params));
	}
}
