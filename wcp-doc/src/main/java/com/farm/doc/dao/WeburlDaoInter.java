package com.farm.doc.dao;

import com.farm.core.sql.query.DBRule;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import com.farm.doc.domain.Weburl;
import org.hibernate.Session;

import java.util.List;
import java.util.Map;


/* *
 *功能：推荐服务数据库持久层接口
 *详细：
 *
 *版本：v0.1
 *作者：Farm代码工程自动生成
 *日期：20150707114057
 *说明：
 */
public interface WeburlDaoInter {
    /**
     * 删除一个推荐服务实体
     *
     * @param entity 实体
     */
    public void deleteEntity(Weburl weburl);

    /**
     * 由推荐服务id获得一个推荐服务实体
     *
     * @param id
     * @return
     */
    public Weburl getEntity(String weburlid);

    /**
     * 插入一条推荐服务数据
     *
     * @param entity
     */
    public Weburl insertEntity(Weburl weburl);

    /**
     * 获得记录数量
     *
     * @return
     */
    public int getAllListNum();

    /**
     * 修改一个推荐服务记录
     *
     * @param entity
     */
    public void editEntity(Weburl weburl);

    /**
     * 获得一个session
     */
    public Session getSession();

    /**
     * 执行一条推荐服务查询语句
     */
    public DataResult runSqlQuery(DataQuery query);

    /**
     * 条件删除推荐服务实体，依据对象字段值(一般不建议使用该方法)
     *
     * @param rules 删除条件
     */
    public void deleteEntitys(List<DBRule> rules);

    /**
     * 条件查询推荐服务实体，依据对象字段值,当rules为空时查询全部(一般不建议使用该方法)
     *
     * @param rules 查询条件
     * @return
     */
    public List<Weburl> selectEntitys(List<DBRule> rules);

    /**
     * 条件修改推荐服务实体，依据对象字段值(一般不建议使用该方法)
     *
     * @param values 被修改的键值对
     * @param rules  修改条件
     */
    public void updataEntitys(Map<String, Object> values, List<DBRule> rules);

    /**
     * 条件合计推荐服务:count(*)
     *
     * @param rules 统计条件
     */
    public int countEntitys(List<DBRule> rules);
}