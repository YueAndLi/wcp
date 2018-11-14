package com.farm.wcp.webfile.server;

import com.farm.core.auth.domain.LoginUser;
import com.farm.doc.domain.ex.DocEntire;
import com.farm.doc.server.FarmDocOperateRightInter.POP_TYPE;

import java.util.List;

public interface WcpWebFileManagerInter {
    public static final String LUCENE_DIR = "WEBFILE";

    /**
     * 创建一个网络资源文件
     *
     * @param fileid   文件id
     * @param typeId   分类
     * @param fileName 名称
     * @param tag      标签
     * @param groupId  小组id
     * @param editPop  修改权限类型
     * @param readPop  阅读权限类型
     * @param isTop    是否置顶
     * @return 资源文件ID
     */
    public DocEntire creatWebFile(List<String> fileid, String typeId, String fileName, String tag, String groupId, String text,
                                  POP_TYPE editPop, POP_TYPE readPop, String isTop, LoginUser currentUser);

    /**
     * 修改一个网络资源文件
     *
     * @param docid    资源文件ID
     * @param fileid   文件id
     * @param typeId   分类
     * @param fileName 名称
     * @param tag      标签
     * @param groupId  小组id
     * @param editPop  修改权限类型
     * @param readPop  阅读权限类型
     * @return
     */
    public DocEntire editWebFile(String docid, List<String> fileid, String typeId, String fileName, String tag, String groupId,
                                 String text, POP_TYPE editPop, POP_TYPE readPop, String editNote, String istop, LoginUser currentUser);

}
