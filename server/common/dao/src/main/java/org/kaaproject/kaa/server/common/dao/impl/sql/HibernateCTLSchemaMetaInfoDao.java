package org.kaaproject.kaa.server.common.dao.impl.sql;

import org.hibernate.criterion.Restrictions;
import org.kaaproject.kaa.common.dto.ctl.CTLSchemaScopeDto;
import org.kaaproject.kaa.server.common.dao.impl.CTLSchemaMetaInfoDao;
import org.kaaproject.kaa.server.common.dao.model.sql.CTLSchemaMetaInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.kaaproject.kaa.server.common.dao.DaoConstants.CTL_SCHEMA_META_INFO_FQN;
import static org.kaaproject.kaa.server.common.dao.DaoConstants.CTL_SCHEMA_META_INFO_SCOPE;
import static org.kaaproject.kaa.server.common.dao.DaoConstants.CTL_SCHEMA_META_INFO_VERSION;

@Repository
public class HibernateCTLSchemaMetaInfoDao extends HibernateAbstractDao<CTLSchemaMetaInfo> implements CTLSchemaMetaInfoDao<CTLSchemaMetaInfo> {

    private static final Logger LOG = LoggerFactory.getLogger(HibernateCTLSchemaMetaInfoDao.class);

    public HibernateCTLSchemaMetaInfoDao() {
    }

    @Override
    protected Class<CTLSchemaMetaInfo> getEntityClass() {
        return CTLSchemaMetaInfo.class;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public CTLSchemaMetaInfo save(CTLSchemaMetaInfo metaInfo) {
        LOG.debug("Try to save or find meta info with fqn [{}] and version [{}]", metaInfo.getFqn(), metaInfo.getVersion());
        CTLSchemaMetaInfo uniqueMetaInfo = findByFqnAndVersion(metaInfo.getFqn(), metaInfo.getVersion());
        if (uniqueMetaInfo == null) {
            uniqueMetaInfo = super.save(metaInfo, true);
            LOG.debug("Save result: {}", uniqueMetaInfo);
        } else {
            LOG.debug("Search result: {}", uniqueMetaInfo);
        }
        return uniqueMetaInfo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CTLSchemaMetaInfo incrementCount(CTLSchemaMetaInfo metaInfo) {
        CTLSchemaMetaInfo uniqueMetaInfo = findById(metaInfo.getStringId());
        if (uniqueMetaInfo != null) {
            uniqueMetaInfo.incrementCount();
        }
        return super.save(uniqueMetaInfo, true);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public CTLSchemaMetaInfo findByFqnAndVersion(String fqn, Integer version) {
        LOG.debug("Searching ctl metadata by fqn [{}] and version [{}]", fqn, version);
        CTLSchemaMetaInfo ctlSchema = findOneByCriterion(Restrictions.and(
                Restrictions.eq(CTL_SCHEMA_META_INFO_VERSION, version),
                Restrictions.eq(CTL_SCHEMA_META_INFO_FQN, fqn)));
        if (LOG.isTraceEnabled()) {
            LOG.trace("[{},{}] Search result: {}.", fqn, version, ctlSchema);
        } else {
            LOG.debug("[{},{}] Search result: {}.", fqn, version, ctlSchema != null);
        }
        return ctlSchema;
    }

    @Override
    public List<CTLSchemaMetaInfo> findSystemSchemaMetaInfo() {
        LOG.debug("Searching system ctl metadata");
        List<CTLSchemaMetaInfo> metaInfoList = findListByCriterion(Restrictions.eq(CTL_SCHEMA_META_INFO_SCOPE, CTLSchemaScopeDto.SYSTEM));
        if (LOG.isTraceEnabled()) {
            LOG.trace("Search result: {}.", Arrays.toString(metaInfoList.toArray()));
        } else {
            LOG.debug("Search result: {}.", metaInfoList.size());
        }
        return metaInfoList;
    }

    @Override
    public CTLSchemaMetaInfo updateScope(CTLSchemaMetaInfo ctlSchemaMetaInfo) {
        LOG.debug("Updating ctl meta info scope {}", ctlSchemaMetaInfo);
        CTLSchemaMetaInfo metaInfo = findById(ctlSchemaMetaInfo.getStringId());
        if (metaInfo != null) {
            metaInfo.setScope(ctlSchemaMetaInfo.getScope());
            metaInfo = super.save(metaInfo);
        }
        LOG.debug("Update result: {}", metaInfo != null);
        return metaInfo;
    }
}