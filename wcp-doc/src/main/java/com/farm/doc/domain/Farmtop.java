package com.farm.doc.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/* *
 *功能：置顶文档类
 *详细：
 *
 *版本：v2.1
 *作者：FarmCode代码工程
 *日期：20150707114057
 *说明：
 */
@Entity(name = "FarmTop")
@Table(name = "KNOW_FARM_TOP")
public class Farmtop implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "ID", length = 32, insertable = true, updatable = true, nullable = false)
    private String id;
    @Column(name = "SORT", length = 10, nullable = false)
    private Integer sort;
    @Column(name = "DOCID", length = 32, nullable = false)
    private String docid;
    @Column(name = "PCONTENT", length = 128)
    private String pcontent;
    @Column(name = "PSTATE", length = 2, nullable = false)
    private String pstate;
    @Column(name = "CUSER", length = 32, nullable = false)
    private String cuser;
    @Column(name = "CTIME", length = 16, nullable = false)
    private String ctime;
    @Column(name = "CUSERNAME", length = 64, nullable = false)
    private String cusername;

    public Integer getSort() {
        return this.sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getDocid() {
        return this.docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getPcontent() {
        return this.pcontent;
    }

    public void setPcontent(String pcontent) {
        this.pcontent = pcontent;
    }

    public String getPstate() {
        return this.pstate;
    }

    public void setPstate(String pstate) {
        this.pstate = pstate;
    }

    public String getCuser() {
        return this.cuser;
    }

    public void setCuser(String cuser) {
        this.cuser = cuser;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCtime() {
        return this.ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getCusername() {
        return this.cusername;
    }

    public void setCusername(String cusername) {
        this.cusername = cusername;
    }
}