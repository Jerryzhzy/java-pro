package com.lzc.core.datasource.repository;

import com.lzc.core.datasource.DataSourceEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 公共基础Repository，继承此Repository可使用该接口定义的公共方法
 * Created by ziyu.zhang on 2017/6/30 19:53
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
	
	public Object saveObject(Object entity);
	
	public T findOne(Specification<T> spec);
	
	public Object findOne(Class<?> entityClass, Object primaryKey);

	public Page<T> findAll(Specification<T> spec, Pageable pageable);

	public List<T> findAll(Specification<T> spec);

	public List<T> findAll(Specification<T> spec, Sort sort);

	public long count(Specification<T> spec);

	public List findAll(String queryString);
	
	public List findBySql(String sqlString);
	
	public List findBySql(String sqlString, Class resultClass);
	
	public List findBySql(String sqlString, String resultSetMapping);

	public Map<DataSourceEntry.QueryType, Object> findBySql(String sqlString, int start, int length);

	public Page<T> findAll(String queryString, Pageable pageable);

	public void clear();
	
	public void rollback();
	
	public Query createNativeQuery(String sqlString);
	
	public int execute(String sqlString);
	
	public int updateQuery(String updateQuery);
	
	public StoredProcedureQuery createStoredProcedureQuery(String procedureName);
	
	public int execNativeQuery(String sqlString, Object... objects);
	

}
