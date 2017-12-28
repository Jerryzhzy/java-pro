package com.lzc.core.datasource.repository;

import com.lzc.core.datasource.repository.impl.BaseRepositoryImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * 自定义BaseRepositoryBean实现，通过此实现持久层自动转换
 * Created by ziyu.zhang on 2017/6/30 19:48
 */
public class BaseRepositoryBean<R extends JpaRepository<T, I>, T, I extends Serializable>
		extends JpaRepositoryFactoryBean<R, T, I> {
	@SuppressWarnings("rawtypes")
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager em) {
		return new BaseRepositoryFactory(em);
	}

	private static class BaseRepositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory {
		private final EntityManager em;

		public BaseRepositoryFactory(EntityManager em) {
			super(em);
			this.em = em;
		}

		@SuppressWarnings("unchecked")
		protected Object getTargetRepository(RepositoryMetadata metadata) {
			return new BaseRepositoryImpl<T, I>((Class<T>) metadata.getDomainType(), em);
		}

		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			return BaseRepositoryImpl.class;
		}
	}
}
