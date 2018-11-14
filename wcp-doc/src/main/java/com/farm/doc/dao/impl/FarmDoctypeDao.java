package com.farm.doc.dao.impl;

import com.farm.core.sql.query.DBRule;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import com.farm.core.sql.utils.HibernateSQLTools;
import com.farm.doc.dao.FarmDoctypeDaoInter;
import com.farm.doc.domain.FarmDoctype;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 文档分类
 *
 * @author MAC_wd
 */
@Repository
public class FarmDoctypeDao extends HibernateSQLTools<FarmDoctype> implements FarmDoctypeDaoInter {
    @Resource(name = "sessionFactory")
    private SessionFactory sessionFatory;

    public void deleteEntity(FarmDoctype entity) {
        Session session = sessionFatory.getCurrentSession();
        session.delete(entity);
    }

    public int getAllListNum() {
        Session session = sessionFatory.getCurrentSession();
        SQLQuery sqlquery = session.createSQLQuery("select count(*) from KNOW_FARM_DOCTYPE");

        return Integer.parseInt(sqlquery.list().get(0).toString());
    }

    public FarmDoctype getEntity(String id) {
        Session session = sessionFatory.getCurrentSession();
        return (FarmDoctype) session.get(FarmDoctype.class, id);
    }

    public FarmDoctype insertEntity(FarmDoctype entity) {
        Session session = sessionFatory.getCurrentSession();
        session.save(entity);
        return entity;
    }

    public void editEntity(FarmDoctype entity) {
        Session session = sessionFatory.getCurrentSession();
        session.update(entity);
    }

    @Override
    public Session getSession() {
        return sessionFatory.getCurrentSession();
    }

    public DataResult runSqlQuery(DataQuery query) {
        try {
            return query.search(sessionFatory.getCurrentSession());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void deleteEntitys(List<DBRule> rules) {
        deleteSqlFromFunction(sessionFatory.getCurrentSession(), rules);
    }

    @Override
    public List<FarmDoctype> selectEntitys(List<DBRule> rules) {
        return selectSqlFromFunction(sessionFatory.getCurrentSession(), rules);
    }

    @Override
    public void updataEntitys(Map<String, Object> values, List<DBRule> rules) {
        updataSqlFromFunction(sessionFatory.getCurrentSession(), values, rules);
    }

    public SessionFactory getSessionFatory() {
        return sessionFatory;
    }

    public void setSessionFatory(SessionFactory sessionFatory) {
        this.sessionFatory = sessionFatory;
    }

    @Override
    protected SessionFactory getSessionFactory() {
        return sessionFatory;
    }

    @Override
    protected Class<FarmDoctype> getTypeClass() {
        return FarmDoctype.class;
    }

    @Override
    public Integer getTypesNum() {
        Session session = sessionFatory.getCurrentSession();
        SQLQuery sqlquery = session.createSQLQuery("select count(*) from KNOW_FARM_DOCTYPE where PSTATE=1");
        return Integer.parseInt(sqlquery.list().get(0).toString());
    }
}
