package com.lzc.core.datasource.repository.impl;

import com.lzc.core.datasource.DataSourceEntry;
import com.lzc.core.datasource.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 公共基础Repository 实现类
 * Created by ziyu.zhang on 2017/6/30 19:48
 */
public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

	private final EntityManager em;

	public BaseRepositoryImpl(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.em = em;
	}
	
	/** (non-Javadoc)
	 * <p>Title: rollback</p>
	 * <p>Description: 事务回滚</p>
	 */
	@Override
	public void rollback(){
		em.getTransaction().rollback();
	}
	
	/** (non-Javadoc)
	* <p>Title: saveObject</p>
	* <p>Description: 数据保存</p>
	* @param entity
	* @return
	*/
	@Override
	public Object saveObject(Object entity) {
		return  em.merge(entity);
	}
	
	/**
	 * @Description: 查询：根据对象类型和主键
	 */
	public Object findOne(Class<?> entityClass, Object primaryKey) {
		return em.find(entityClass, primaryKey);
	}
	
	/** (non-Javadoc)
	* <p>Title: findAll</p>
	* <p>Description: 根据HQL语句获取查询结果集合</p>
	* @param queryString
	* @return
	*/
	@SuppressWarnings({ "rawtypes" })
	@Override
	public List findAll(String queryString) {
		return em.createQuery(queryString).getResultList();
	}


	@SuppressWarnings("rawtypes")
	@Override
	public List findBySql(String sqlString) {
		Query query=em.createNativeQuery(sqlString);
		return query.getResultList();
	}
	/**
	* <p>Title: findBySql</p>
	* <p>Description: 根据SQL语句、实体类获取查询结果集合</p>
	* @param sqlString
	* @param resultClass
	* @return
	*/
	@SuppressWarnings("rawtypes")
	@Override
	public List findBySql(String sqlString, Class resultClass){
		Query query=em.createNativeQuery(sqlString, resultClass);
		return query.getResultList();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List findBySql(String sqlString,String resultSetMapping){
		Query query=em.createNativeQuery(sqlString, resultSetMapping);
		return query.getResultList();
	}

	/* (non-Javadoc)
	* <p>Title: findBySql</p>
	* <p>Description: 根据SQL语句查询数据，并返回计数</p>
	*/
	@Override
	public Map<DataSourceEntry.QueryType, Object> findBySql(String sqlString, int start, int length){
		Map<DataSourceEntry.QueryType, Object> resultMap=new LinkedHashMap<>();
		//查询数据
		Query query=em.createNativeQuery(sqlString);
		query.setFirstResult(start).setMaxResults(length);
		List data = this.findBySql(sqlString+" limit "+start+","+length);
		resultMap.put(DataSourceEntry.QueryType.total, data.size()+start+length);
		//统计总数
		String fromSql = sqlString.substring(sqlString.indexOf("from", 0));
		boolean matches = Pattern.compile("^from\\s+(s_|bh_settlement|bh_basic_unit_group|bh_attention_rule).*", Pattern.CASE_INSENSITIVE).matcher(fromSql).find();
		if(matches){
			String countSQL="select count(*) "+fromSql;
			if(sqlString.indexOf("order by")!=-1) countSQL="select count(*) "+sqlString.substring(sqlString.indexOf("from", 0),sqlString.indexOf("order by", 0));
			BigInteger total=(BigInteger) em.createNativeQuery(countSQL).getResultList().get(0);
			resultMap.put(DataSourceEntry.QueryType.total, total);
		}else{
			resultMap.put(DataSourceEntry.QueryType.total, data.size()+start+length);
		}
		resultMap.put(DataSourceEntry.QueryType.data, data);
		return resultMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Page<T> findAll(String queryString, Pageable pageable){
		int pageSize=pageable.getPageSize();
		int pageNum=pageable.getPageNumber();
		int startIndex=pageNum*pageSize;
		Query query=em.createQuery(queryString);
		long total=(long) em.createQuery("select count(*) "+queryString.substring(queryString.indexOf("from"))).getResultList().get(0);
		query.setFirstResult(startIndex).setMaxResults(pageSize);
		List<T> content=query.getResultList();
		return new PageImpl<T>(content, pageable, total);
	}
	
	@Override
	public void clear() {
		em.clear();
	}
	
	public List<T> findAll(String queryString, Pageable pageable, Specification<T> spec){
		return null;
	}
	
	/**
	 * @Description: 执行SQL语句返回Query对象
	 */
	public Query createNativeQuery(String sqlString) {
		return em.createNativeQuery(sqlString);
	}
	
	/**
	 * @Description: 执行SQL
	 */
	public int execNativeQuery(String sqlString,Object...objects){
		Query query = em.createNativeQuery(sqlString);
		if(objects!=null&&objects.length>0){
		   for(int i=0;i<objects.length;i++){
			   query.setParameter(i+1,objects[i]);
		   }
		}
		return query.executeUpdate();
	}
	
	/**
	 * @Description: 执行SQL语句
	 */
	@Override
	public int execute(String sqlString){
		Query query=em.createNativeQuery(sqlString);
		return query.executeUpdate();
	}
	/**
	 * @Description: 执行HQL更新语句
	 */
	@Override
	public int updateQuery(String updateQuery){
		Query query=em.createQuery(updateQuery);
		return query.executeUpdate();
	}
	/**
	 * @Description: 存储过程调用
	 */
	public StoredProcedureQuery createStoredProcedureQuery(String procedureName){
		return em.createStoredProcedureQuery(procedureName);
	}

}
