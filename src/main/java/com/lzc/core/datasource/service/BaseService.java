package com.lzc.core.datasource.service;

import com.lzc.core.datasource.DataSourceEntry;
import com.lzc.core.datasource.repository.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.StoredProcedureQuery;
import java.io.Serializable;
import java.util.*;

/**
 * 基础Service
 * Created by ziyu.zhang on 2017/6/30 19:54
 */
@Component
@Transactional
public abstract class BaseService<T, ID extends Serializable> {
	
	@Autowired
	protected BaseRepository<T, ID> repository;
	/**
	 * @Description: 查询：根据ID获取T对象
	 */
	public T findOne(ID id) {
		return repository.findOne(id);
	}

	/**
	 * @Description: 查询：根据JPA规范获取T对象
	 */
	public T findOne(Specification<T> spec) {
		return repository.findOne(spec);
	}
	
	/**
	 * @Description: 查询：根据对象类型和主键
	 */
	public Object findOne(Class<?> entityClass, Object primaryKey) {
		return repository.findOne(entityClass, primaryKey);
	}

	/**
	 * @Description: 删除：根据ID物理删除
	 */
	public void delete(ID id) {
		repository.delete(id);
	}

	/**
	 * @Description: 删除：删除T对象
	 */
	public void delete(T entity) {
		repository.delete(entity);
	}

	/**
	 * @Description: 删除：删除T集合
	 */
	public void delete(Iterable<? extends T> entities) {
		repository.delete(entities);
	}

	/**
	 * @Description: 删除：对T映射的数据表进行清空
	 */
	public void deleteAll() {
		repository.deleteAll();
	}

	/**
	 * @Description: 删除：批量删除T集合
	 */
	public void deleteInBatch(Iterable<T> entities) {
		repository.deleteInBatch(entities);
	}

	/**
	 * @Description: 删除：批量删除T映射的数据表所有数据
	 */
	public void deleteAllInBatch() {
		repository.deleteAllInBatch();
	}

	/**
	 * @Description: 计数：获取符合JPA规范的数据数量
	 */
	public long count(Specification<T> spec) {
		return repository.count(spec);
	}

	/**
	 * @Description: 保存：保存并刷新S对象
	 */
	public <S extends T> S saveAndFlush(S entity) {
		return repository.saveAndFlush(entity);
	}

	/**
	 * @Description: 保存：保存S集合
	 */
	public <S extends T> List<S> save(Iterable<S> entities) {
		return repository.save(entities);
	}

	/**
	 * @Description: 保存：保存T对象
	 */
	public T save(T entity) {
		return repository.save(entity);
	}

	/**
	 * @Description: 保存：保存Object对象
	 */
	public Object saveObject(Object entity){
		return repository.saveObject(entity);
	}

	/**
	 * @Description: 执行HQL更新语句
	 */
	public int updateQuery(String updateQuery){
		return repository.updateQuery(updateQuery);
	}

	/**
	 * @Description: 存储过程调用
	 */
	public StoredProcedureQuery createStoredProcedureQuery(String procedureName){
		return repository.createStoredProcedureQuery(procedureName);
	}

	/**
	 * @Description: 缓存：将修改的数据发送到数据库服务器端的数据高速缓存中，而不是数据文件中
	 * <p/>
	 * 注：此方法只同步持久化对象到缓存中，像execute()这种执行不返回持久化对象的操作是不会同步到缓存的,<BR/>
	 * 	     此时要精准读取数据库中数据的话需先执行clear()方法，将持久化对象改变为游离态对象,也就是清除缓存
	 */
	public void flush() {
		repository.flush();
	}

	/**
	 * @Description: 清除：把实体管理器中所有的实体对象变成游离状态
	 */
	public void clear() {
		repository.clear();
	}

	/**
	 * @Description: 事务回滚：回滚未提交的数据
	 */
	public void rollback() {
		repository.rollback();
	}

	/**
	 * @Description: 操作：执行SQL语句操作, 返回Query对象
	 */
	public javax.persistence.Query createNativeQuery(String sqlString) {
		return repository.createNativeQuery(sqlString);
	}
	
    /**
     * @Description:执行
     */
    public int execNativeQuery(String sqlString,Object...objects){
    	return repository.execNativeQuery(sqlString, objects);
    }

	/**
	 * @Description: 操作：执行SQL语句操作
	 */
	public int execute(String sqlString){
		return repository.execute(sqlString);
	}
	/**
	 * @Description: 查询：分页查询
	 * @param pageNumber	页码
	 * @param pageSize	每页个数
	 * @param sortType	排序
	 */
	public Page<T> findAll(int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		return repository.findAll(pageRequest);
	}

	/**
	 * @Description: 查询：分页查询
	 * @param pageable	分页信息对象
	 */
	public Page<T> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	/**
	 * @Description: 查询：分页查询
	 * @param spec	JPA规范
	 * @param pageable	分页信息对象
	 */
	public Page<T> findAll(Specification<T> spec, Pageable pageable) {
		return repository.findAll(spec, pageable);
	}

	/**
	 * @Description: 查询：根据JPA规范查询T集合
	 * @param spec	JPA规范
	 */
	public List<T> findAll(Specification<T> spec) {
		return repository.findAll(spec);
	}

	/**
	 * @Description: 查询：根据JPA规范和排序规范查询T集合
	 * @param spec	JPA规范
	 * @param sort	排序规范
	 */
	public List<T> findAll(Specification<T> spec, Sort sort) {
		return repository.findAll(spec, sort);
	}

	public List findAll(String queryString) {
		return repository.findAll(queryString);
	}

	public List findBySql(String sqlString) {
		return repository.findBySql(sqlString);
	}

	public Map<DataSourceEntry.QueryType, Object> findBySql(String sqlString, int start, int length) {
		return repository.findBySql(sqlString, start, length);
	}

	public List findBySql(String sqlString, Class resultClass) {
		return repository.findBySql(sqlString, resultClass);
	}

	public List findBySql(String sqlString, String resultSetMapping) {
		return repository.findBySql(sqlString, resultSetMapping);
	}

	public Page<T> findAll(String queryString, Pageable pageable) {
		return repository.findAll(queryString, pageable);
	}

	public List findMasterBySql(String sqlString) {
		return repository.findBySql(sqlString);
	}


	/**
	 * @Description: 创建分页请求
	 * @param pageNumber
	 * @param pagzSize
	 * @param sortType
	 */
	protected PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		List<Order> orders = new ArrayList<>();
		if ("auto".equals(sortType)) {
			orders.add(new Order(Direction.DESC, "id"));
		} else if (!"".equals(sortType)) {
			if (sortType.contains(",")) {
				String[] args = sortType.split(",");
				for (String arg : args) {
					if (arg.contains(" ")) {
						String[] strs = arg.split(" ");
						orders.add(new Order("desc".equals(strs[1].toLowerCase()) ? Direction.DESC : Direction.ASC,
								strs[0]));
					} else {
						orders.add(new Order(Direction.ASC, arg));
					}
				}
			} else {
				if (sortType.contains(" ")) {
					String[] strs = sortType.split(" ");
					orders.add(
							new Order("desc".equals(strs[1].toLowerCase()) ? Direction.DESC : Direction.ASC, strs[0]));
				} else {
					orders.add(new Order(Direction.ASC, sortType));
				}
			}
		}
		return new PageRequest(pageNumber - 1, pagzSize, new Sort(orders));
	}

}
