package com.farm.wcp.know.service.impl;

import com.farm.authority.domain.User;
import com.farm.core.auth.domain.LoginUser;
import com.farm.core.sql.query.DBRule;
import com.farm.core.sql.query.DBSort;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.time.TimeTool;
import com.farm.doc.domain.Doc;
import com.farm.doc.domain.FarmDoctype;
import com.farm.doc.domain.ex.DocEntire;
import com.farm.doc.exception.CanNoWriteException;
import com.farm.doc.server.FarmDocManagerInter;
import com.farm.doc.server.FarmDocOperateRightInter;
import com.farm.doc.server.FarmDocOperateRightInter.POP_TYPE;
import com.farm.doc.server.FarmDocTypeInter;
import com.farm.doc.server.FarmFileManagerInter;
import com.farm.lucene.face.WordAnalyzerFace;
import com.farm.wcp.know.service.KnowServiceInter;
import com.farm.wcp.know.service.WebDocServiceInter;
import com.farm.wcp.know.util.HttpImgDownloadHandle;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * 文档管理
 *
 * @author MAC_wd
 */
@Service
public class KnowServiceImpl implements KnowServiceInter {
    @Resource
    private FarmDocManagerInter farmDocManagerImpl;
    @Resource
    private FarmFileManagerInter farmFileManagerImpl;
    @Resource
    private FarmDocOperateRightInter farmDocOperateRightImpl;
    @Resource
    private FarmDocTypeInter farmDocTypeManagerImpl;
    private static final Logger log = Logger.getLogger(KnowServiceImpl.class);

    @Override
    @Transactional
    public DocEntire creatKnow(String knowtitle, String knowtypeId, String text, String knowtag, POP_TYPE pop_type_edit,
                               POP_TYPE pop_type_read, String groupId, String istop, LoginUser currentUser) {
        DocEntire doc = new DocEntire(new Doc());
        doc.getDoc().setTitle(knowtitle);
        doc.setTexts(text, currentUser);
        doc.getDoc().setWritepop(pop_type_edit.getValue());
        doc.getDoc().setDocgroupid(groupId);
        doc.getDoc().setReadpop(pop_type_read.getValue());
        doc.getDoc().setTagkey(knowtag);
        doc.getDoc().setDomtype("1");
        doc.getDoc().setState("1");
        doc.getDoc().setPubtime(TimeTool.getTimeDate14());
        doc.getDoc().setIstop(Integer.parseInt(istop));
        doc.setType(farmDocTypeManagerImpl.getType(knowtypeId));
        doc = farmDocManagerImpl.createDoc(doc, currentUser);

        return doc;
    }

    @Override
    @Transactional
    public DocEntire editKnow(String docid, String text, String knowtag, String istop, LoginUser currentUser, String editNote)
            throws CanNoWriteException {
        Doc doc = farmDocManagerImpl.getDocOnlyBean(docid);
        return editKnow(docid, doc.getTitle(), null, text, knowtag, POP_TYPE.getEnum(doc.getWritepop()),
                POP_TYPE.getEnum(doc.getReadpop()), doc.getDocgroupid(), istop, currentUser, editNote);
    }

    @Override
    @Transactional
    public DocEntire editKnow(String id, String knowtitle, String knowtype, String text, String knowtag,
                              POP_TYPE pop_type_edit, POP_TYPE pop_type_read, String groupId, String istop, LoginUser currentUser, String editNote)
            throws CanNoWriteException {
        DocEntire entity = farmDocManagerImpl.getDoc(id);
        if (groupId != null && !groupId.isEmpty()) {
            entity.getDoc().setDocgroupid(groupId);
        }
        entity.getDoc().setTitle(knowtitle);
        entity.setTexts(text, currentUser);
        entity.getDoc().setTagkey(knowtag);
        entity.getDoc().setWritepop(pop_type_edit.getValue());
        entity.getDoc().setReadpop(pop_type_read.getValue());
        FarmDoctype type = farmDocTypeManagerImpl.getType(knowtype);
        entity.setType(type);
        entity.getDoc().setIstop(Integer.parseInt(istop));
        entity = farmDocManagerImpl.editDocByUser(entity, editNote, currentUser);
        return entity;
    }

    @Override
    @Transactional
    public DocEntire getDocByWeb(String url, LoginUser user) {
        DocEntire doc = new DocEntire(new Doc());
        try {
            String[] webdocs = WebDocServiceImpl.instance().crawlerWebDocTempFileId(new URL(url),
                    WebDocServiceInter.DOC_TYPE.HTML, new HttpImgDownloadHandle(farmFileManagerImpl));
            doc.getDoc().setTitle(webdocs[1]);
            String tag = null;
            List<Object[]> taglist = WordAnalyzerFace.parseHtmlWordCaseForSortList(webdocs[0]);
            for (Object[] Object : taglist.size() > 10 ? taglist.subList(0, 10) : taglist) {
                if (tag != null) {
                    tag = tag + ",";
                } else {
                    tag = "";
                }
                tag = tag + Object[0];
            }
            doc.getDoc().setTagkey(tag);
            doc.setTexts(webdocs[2], user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return doc;
    }


    @Override
    public DataQuery getMyDocQuery(DataQuery query, User user) {
        query = DataQuery
                .init(
                        query,
                        "KNOW_FARM_DOC a LEFT JOIN KNOW_FARM_DOCRUNINFO b ON a.RUNINFOID=b.ID LEFT JOIN KNOW_FARM_RF_DOCTYPE c ON c.DOCID=a.ID LEFT JOIN KNOW_FARM_DOCTYPE d ON d.ID=c.TYPEID",
                        "a.ID as ID,a.STATE as STATE,a.TITLE AS title,a.DOCDESCRIBE AS DOCDESCRIBE,a.AUTHOR AS AUTHOR,a.PUBTIME AS PUBTIME,a.TAGKEY AS TAGKEY ,a.IMGID AS IMGID,b.VISITNUM AS VISITNUM,b.PRAISEYES AS PRAISEYES,b.PRAISENO AS PRAISENO,b.HOTNUM AS HOTNUM,b.EVALUATE as EVALUATE,b.ANSWERINGNUM as ANSWERINGNUM,d.NAME AS TYPENAME");
        query.addRule(new DBRule("a.STATE", "0", "!="));
        query.addRule(new DBRule("a.CUSER", user.getId(), "="));
        query.addSort(new DBSort("a.ctime", "desc"));
        return query;
    }
}
