package com.farm.doc.dao.impl;

import com.farm.core.sql.query.DBRule;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import com.farm.core.sql.utils.HibernateSQLTools;
import com.farm.doc.dao.WeburlDaoInter;
import com.farm.doc.domain.Weburl;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/* *
 *功能：推荐服务持久层实现
 *详细：
 *
 *版本：v0.1
 *作者：FarmCode代码工程
 *日期：20150707114057
 *说明：
 */
@Repository
public class WeburlDaoImpl extends HibernateSQLTools<Weburl> implements WeburlDaoInter {
    @Resource(name = "sessionFactory")
    private SessionFactory sessionFatory;

    @Override
    public void deleteEntity(Weburl weburl) {
        // TODO 自动生成代码,修改后请去除本注释
        Session session = sessionFatory.getCurrentSession();
        session.delete(weburl);
    }

    @Override
    public int getAllListNum() {
        // TODO 自动生成代码,修改后请去除本注释
        Session session = sessionFatory.getCurrentSession();
        SQLQuery sqlquery = session.createSQLQuery("select count(*) from farm_code_field");
        return Integer.parseInt(sqlquery.list().get(0).toString());
    }

    @Override
    public Weburl getEntity(String weburlid) {
        // TODO 自动生成代码,修改后请去除本注释
        Session session = sessionFatory.getCurrentSession();
        return (Weburl) session.get(Weburl.class, weburlid);
    }

    @Override
    public Weburl insertEntity(Weburl weburl) {
        // TODO 自动生成代码,修改后请去除本注释
        Session session = sessionFatory.getCurrentSession();
        session.save(weburl);
        return weburl;
    }

    @Override
    public void editEntity(Weburl weburl) {
        // TODO 自动生成代码,修改后请去除本注释
        Session session = sessionFatory.getCurrentSession();
        session.update(weburl);
    }

    @Override
    public Session getSession() {
        // TODO 自动生成代码,修改后请去除本注释
        return sessionFatory.getCurrentSession();
    }

    @Override
    public DataResult runSqlQuery(DataQuery query) {
        // TODO 自动生成代码,修改后请去除本注释
        try {
            return query.search(sessionFatory.getCurrentSession());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void deleteEntitys(List<DBRule> rules) {
        // TODO 自动生成代码,修改后请去除本注释
        deleteSqlFromFunction(sessionFatory.getCurrentSession(), rules);
    }

    @Override
    public List<Weburl> selectEntitys(List<DBRule> rules) {
        // TODO 自动生成代码,修改后请去除本注释
        return selectSqlFromFunction(sessionFatory.getCurrentSession(), rules);
    }

    @Override
    public void updataEntitys(Map<String, Object> values, List<DBRule> rules) {
        // TODO 自动生成代码,修改后请去除本注释
        updataSqlFromFunction(sessionFatory.getCurrentSession(), values, rules);
    }

    @Override
    public int countEntitys(List<DBRule> rules) {
        // TODO 自动生成代码,修改后请去除本注释
        return countSqlFromFunction(sessionFatory.getCurrentSession(), rules);
    }

    public SessionFactory getSessionFatory() {
        return sessionFatory;
    }

    public void setSessionFatory(SessionFactory sessionFatory) {
        this.sessionFatory = sessionFatory;
    }

    @Override
    protected Class<?> getTypeClass() {
        return Weburl.class;
    }

    @Override
    protected SessionFactory getSessionFactory() {
        return sessionFatory;
    }
}
