package com.farm.parameter.dao.impl;

import com.farm.parameter.dao.DictionaryTypeDaoInter;
import com.farm.parameter.domain.AloneDictionaryType;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class DictionaryTypeDao implements DictionaryTypeDaoInter {
    @Resource(name = "sessionFactory")
    private SessionFactory sessionFatory;

    public void deleteEntity(AloneDictionaryType entity) {
        Session session = sessionFatory.getCurrentSession();
        session.delete(entity);
    }

    @Override
    public void deleteEntityByTreecode(String entityId) {
        //String hql = "update AloneDictionaryType a set a.state = '2' "
        //		+ "where a.treecode like ?";
        String hql = "delete from AloneDictionaryType a where a.treecode like ?";
        Query query = sessionFatory.getCurrentSession().createQuery(hql);
        query.setString(0, "%" + entityId + "%");
        query.executeUpdate();
    }

    public int getAllListNum() {
        Session session = sessionFatory.getCurrentSession();
        SQLQuery sqlquery = session
                .createSQLQuery("select count(*) from KNOW_ALONE_DICTIONARY_TYPE");

        return Integer.parseInt(sqlquery.list().get(0).toString());
    }

    public AloneDictionaryType getEntity(String id) {
        Session session = sessionFatory.getCurrentSession();
        return (AloneDictionaryType) session.get(AloneDictionaryType.class, id);
    }

    public void insertEntity(AloneDictionaryType entity) {
        Session session = sessionFatory.getCurrentSession();
        session.save(entity);
    }

    public SessionFactory getSessionFatory() {
        return sessionFatory;
    }

    public void setSessionFatory(SessionFactory sessionFatory) {
        this.sessionFatory = sessionFatory;
    }

    public void editEntity(AloneDictionaryType entity) {
        Session session = sessionFatory.getCurrentSession();
        session.update(entity);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AloneDictionaryType> getListByEntityId(String entityId) {
        Session session = sessionFatory.getCurrentSession();
        Query query = session.createQuery("from AloneDictionaryType where entity=?")
                .setString(0, entityId);
        List<AloneDictionaryType> list = query.list();
        return list;
    }
}
