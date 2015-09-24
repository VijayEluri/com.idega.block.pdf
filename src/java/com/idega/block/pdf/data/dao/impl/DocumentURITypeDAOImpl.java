/**
 * @(#)DocumentURITypeDAOImpl.java    1.0.0 14:03:13
 *
 * Idega Software hf. Source Code Licence Agreement x
 *
 * This agreement, made this 10th of February 2006 by and between 
 * Idega Software hf., a business formed and operating under laws 
 * of Iceland, having its principal place of business in Reykjavik, 
 * Iceland, hereinafter after referred to as "Manufacturer" and Agura 
 * IT hereinafter referred to as "Licensee".
 * 1.  License Grant: Upon completion of this agreement, the source 
 *     code that may be made available according to the documentation for 
 *     a particular software product (Software) from Manufacturer 
 *     (Source Code) shall be provided to Licensee, provided that 
 *     (1) funds have been received for payment of the License for Software and 
 *     (2) the appropriate License has been purchased as stated in the 
 *     documentation for Software. As used in this License Agreement, 
 *     Licensee shall also mean the individual using or installing 
 *     the source code together with any individual or entity, including 
 *     but not limited to your employer, on whose behalf you are acting 
 *     in using or installing the Source Code. By completing this agreement, 
 *     Licensee agrees to be bound by the terms and conditions of this Source 
 *     Code License Agreement. This Source Code License Agreement shall 
 *     be an extension of the Software License Agreement for the associated 
 *     product. No additional amendment or modification shall be made 
 *     to this Agreement except in writing signed by Licensee and 
 *     Manufacturer. This Agreement is effective indefinitely and once
 *     completed, cannot be terminated. Manufacturer hereby grants to 
 *     Licensee a non-transferable, worldwide license during the term of 
 *     this Agreement to use the Source Code for the associated product 
 *     purchased. In the event the Software License Agreement to the 
 *     associated product is terminated; (1) Licensee's rights to use 
 *     the Source Code are revoked and (2) Licensee shall destroy all 
 *     copies of the Source Code including any Source Code used in 
 *     Licensee's applications.
 * 2.  License Limitations
 *     2.1 Licensee may not resell, rent, lease or distribute the 
 *         Source Code alone, it shall only be distributed as a 
 *         compiled component of an application.
 *     2.2 Licensee shall protect and keep secure all Source Code 
 *         provided by this this Source Code License Agreement. 
 *         All Source Code provided by this Agreement that is used 
 *         with an application that is distributed or accessible outside
 *         Licensee's organization (including use from the Internet), 
 *         must be protected to the extent that it cannot be easily 
 *         extracted or decompiled.
 *     2.3 The Licensee shall not resell, rent, lease or distribute 
 *         the products created from the Source Code in any way that 
 *         would compete with Idega Software.
 *     2.4 Manufacturer's copyright notices may not be removed from 
 *         the Source Code.
 *     2.5 All modifications on the source code by Licencee must 
 *         be submitted to or provided to Manufacturer.
 * 3.  Copyright: Manufacturer's source code is copyrighted and contains 
 *     proprietary information. Licensee shall not distribute or 
 *     reveal the Source Code to anyone other than the software 
 *     developers of Licensee's organization. Licensee may be held 
 *     legally responsible for any infringement of intellectual property 
 *     rights that is caused or encouraged by Licensee's failure to abide 
 *     by the terms of this Agreement. Licensee may make copies of the 
 *     Source Code provided the copyright and trademark notices are 
 *     reproduced in their entirety on the copy. Manufacturer reserves 
 *     all rights not specifically granted to Licensee.
 *
 * 4.  Warranty & Risks: Although efforts have been made to assure that the 
 *     Source Code is correct, reliable, date compliant, and technically 
 *     accurate, the Source Code is licensed to Licensee as is and without 
 *     warranties as to performance of merchantability, fitness for a 
 *     particular purpose or use, or any other warranties whether 
 *     expressed or implied. Licensee's organization and all users 
 *     of the source code assume all risks when using it. The manufacturers, 
 *     distributors and resellers of the Source Code shall not be liable 
 *     for any consequential, incidental, punitive or special damages 
 *     arising out of the use of or inability to use the source code or 
 *     the provision of or failure to provide support services, even if we 
 *     have been advised of the possibility of such damages. In any case, 
 *     the entire liability under any provision of this agreement shall be 
 *     limited to the greater of the amount actually paid by Licensee for the 
 *     Software or 5.00 USD. No returns will be provided for the associated 
 *     License that was purchased to become eligible to receive the Source 
 *     Code after Licensee receives the source code. 
 */
package com.idega.block.pdf.data.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idega.block.pdf.data.DocumentURIGroupEntity;
import com.idega.block.pdf.data.DocumentURITypeEntity;
import com.idega.block.pdf.data.dao.DocumentURIGroupDAO;
import com.idega.block.pdf.data.dao.DocumentURITypeDAO;
import com.idega.core.persistence.impl.GenericDaoImpl;
import com.idega.data.SimpleQuerier;
import com.idega.util.ArrayUtil;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;
import com.idega.util.expression.ELUtil;

/**
 * <p>Implementation of {@link DocumentURITypeDAO}</p>
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.is">Martynas Stakė</a></p>
 *
 * @version 1.0.0 2015 vas. 26
 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
 */

/*
 * Spring
 */
@Repository(DocumentURITypeDAO.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(readOnly = false)
@EnableAspectJAutoProxy(proxyTargetClass=true)

/*
 * DWR
 */
@RemoteProxy(
		name=DocumentURITypeDAO.JAVASCRIPT_CLASS_NAME,
		creator=SpringCreator.class, 
		creatorParams={
			@Param(
					name="beanName", 
					value=DocumentURITypeDAO.BEAN_NAME),
			@Param(
					name="javascript", 
					value=DocumentURITypeDAO.JAVASCRIPT_CLASS_NAME)
		}
)
public class DocumentURITypeDAOImpl extends GenericDaoImpl implements
		DocumentURITypeDAO {

	@Autowired
	private DocumentURIGroupDAO documentURIGroupDAO;

	protected DocumentURIGroupDAO getDocumentURIGroupDAO() {
		if (this.documentURIGroupDAO == null) {
			ELUtil.getInstance().autowire(this);
		}

		return this.documentURIGroupDAO;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.pdf.data.dao.DocumentURITypeDAO#findById(java.lang.Long, java.lang.Class)
	 */
	@Override
	public <T extends DocumentURITypeEntity> T findById(Long id, Class<T> clazz) {
		if (id != null) {
			return getSingleResult(
					DocumentURITypeEntity.FIND_BY_ID,
					clazz, 
					new com.idega.core.persistence.Param(DocumentURITypeEntity.idProp, id));
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.pdf.data.dao.DocumentURITypeDAO#findById(java.lang.Long)
	 */
	@Override
	@RemoteMethod
	public DocumentURITypeEntity findById(Long id) {
		return findById(id, DocumentURITypeEntity.class);
	}

	@Override
	public List<DocumentURITypeEntity> findByIds(Collection<Long> ids) {
		if (!ListUtil.isEmpty(ids)) {
			return getResultList(
					DocumentURITypeEntity.FIND_BY_IDS,
					DocumentURITypeEntity.class, 
					new com.idega.core.persistence.Param(DocumentURITypeEntity.idProp, ids));
		}

		return null;
	}

	/**
	 * 
	 * @param externalIds
	 * @return
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	protected String[] getIDsByExternalIds(Collection<String> externalIds) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ID FROM ").append(DocumentURITypeEntity.ENTITY_NAME)
		.append(" WHERE ").append(DocumentURITypeEntity.COLUMN_EXTERNAL_ID)
		.append(" IN ('");

		for (Iterator<String> iterator = externalIds.iterator(); iterator.hasNext();) {
			String externalId = iterator.next();
			if (!StringUtil.isEmpty(externalId)) {
				sb.append(externalId);
				if (iterator.hasNext()) {
					sb.append("', '");
				}
			}
		}

		sb.append("')");

		try {
			return SimpleQuerier.executeStringQuery(sb.toString());
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Failed to execute query' " + 
					sb.toString() + "' cause of: ", e);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.pdf.data.dao.DocumentURITypeDAO#findByExternalIds(java.util.Collection)
	 */
	@Override
	public List<DocumentURITypeEntity> findByExternalIds(Collection<String> externalIds) {
		if (!ListUtil.isEmpty(externalIds)) {
			ArrayList<Long> primaryKeys = new ArrayList<Long>();

			String[] results = getIDsByExternalIds(externalIds);
			if (!ArrayUtil.isEmpty(results)) {
				for (String result: results) {
					primaryKeys.add(Long.valueOf(result));
				}
			}

			return findByIds(primaryKeys);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.pdf.data.dao.DocumentURITypeDAO#findByExternalId(java.lang.String, java.lang.String)
	 */
	@Override
	public DocumentURITypeEntity findByExternalId(String externalId,
			String groupIdentifier) {
		return getSingleResult(
			DocumentURITypeEntity.FIND_BY_EXTERNAL_ID,
			DocumentURITypeEntity.class, 
			new com.idega.core.persistence.Param(DocumentURITypeEntity.externalIdProp, externalId),
			new com.idega.core.persistence.Param(DocumentURIGroupEntity.identifierProp, groupIdentifier));
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.pdf.data.dao.DocumentURITypeDAO#findByName(java.lang.String)
	 */
	@Override
	@RemoteMethod
	public DocumentURITypeEntity findByName(String name) {
		if (!StringUtil.isEmpty(name)) {
			return getSingleResult(
					DocumentURITypeEntity.FIND_BY_NAME,
					DocumentURITypeEntity.class, 
					new com.idega.core.persistence.Param(DocumentURITypeEntity.nameProp, name));
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.pdf.data.dao.DocumentURITypeDAO#findByName(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T extends DocumentURITypeEntity> T findByName(String name, Class<T> clazz) {
		if (!StringUtil.isEmpty(name)) {
			return getSingleResult(
					DocumentURITypeEntity.FIND_BY_NAME,
					clazz, 
					new com.idega.core.persistence.Param(DocumentURITypeEntity.nameProp, name));
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.pdf.data.dao.DocumentURITypeDAO#findAll()
	 */
	@Override
	@RemoteMethod
	public List<DocumentURITypeEntity> findAll() {
		return getResultList(
				DocumentURITypeEntity.FIND_ALL,
				DocumentURITypeEntity.class);
	}

	/* (non-Javadoc)
	 * @see com.idega.block.pdf.data.dao.DocumentURITypeDAO#update(com.idega.block.pdf.data.DocumentURITypeEntity)
	 */
	@Override
	public <T extends DocumentURITypeEntity> T update(T entity) {
		if (entity != null) {
			if (findById(entity.getId()) == null) {
				persist(entity);
				if (entity.getId() != null) {
					getLogger().fine("Entity: " + entity + " created!");
					return entity;
				}
			} else {
				entity = merge(entity);
				if (entity != null) {
					getLogger().fine("Entity: " + entity + " updated");
					return entity;
				}
			}		
		}

		getLogger().warning("Failed to create/update entity: " + entity);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.pdf.data.dao.DocumentURITypeDAO#update(java.lang.Long, java.lang.String, java.lang.Class)
	 */
	@Override
	public <T extends DocumentURITypeEntity> T update(
			Long id, 
			String name,
			String externalId, 
			DocumentURIGroupEntity group, 
			Class<T> clazz) {
		T entityToUpdate = null;
		if (id != null) {
			entityToUpdate = findById(id, clazz);
		}

		if (entityToUpdate == null && !StringUtil.isEmpty(name)) {
			entityToUpdate = findByName(name, clazz);
		}

		if (entityToUpdate == null) {
			try {
				entityToUpdate = clazz.newInstance();
			} catch (InstantiationException e) {
				getLogger().log(Level.WARNING, 
						"Unable to find constructor for: " + clazz, e);
				return null;
			} catch (IllegalAccessException e) {
				getLogger().log(Level.WARNING, 
						"Constructor of: " + clazz + " can't be accessed: " , e);
				return null;
			}
		}

		if (!StringUtil.isEmpty(name)) {
			entityToUpdate.setName(name);
		}

		if (!StringUtil.isEmpty(externalId)) {
			entityToUpdate.setExternalId(externalId);
		}

		if (group != null) {
			entityToUpdate.setGroup(group);
		}

		return update(entityToUpdate);
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.pdf.data.dao.DocumentURITypeDAO#update(java.lang.Long, java.lang.String)
	 */
	@Override
	@RemoteMethod
	public DocumentURITypeEntity update(Long id, String name, String externalId, Long groupId) {
		return update(id, name, externalId, 
				getDocumentURIGroupDAO().findById(groupId), 
				DocumentURITypeEntity.class);
	}


	/* (non-Javadoc)
	 * @see com.idega.block.pdf.data.dao.DocumentURITypeDAO#remove(com.idega.block.pdf.data.DocumentURITypeEntity)
	 */
	@Override
	public void removeEntity(DocumentURITypeEntity entity) {
		if (entity != null) {
			super.remove(entity);
		}
	}

	/* (non-Javadoc)
	 * @see com.idega.block.pdf.data.dao.DocumentURITypeDAO#remove(java.lang.Long)
	 */
	@Override
	@RemoteMethod
	public void remove(Long id) {
		removeEntity(findById(id));
	}
}
