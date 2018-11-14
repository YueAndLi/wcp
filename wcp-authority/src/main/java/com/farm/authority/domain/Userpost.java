package com.farm.authority.domain;

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
@Entity(name = "UserPost")
@Table(name = "KNOW_ALONE_AUTH_USERPOST")
public class Userpost implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "ID", length = 32, insertable = true, updatable = true, nullable = false)
    private String id;
    @Column(name = "POSTID", length = 32, nullable = false)
    private String postid;
    @Column(name = "USERID", length = 32, nullable = false)
    private String userid;

    public Userpost() {
    }

    public Userpost(String id, String postid, String userid) {
        super();
        this.id = id;
        this.postid = postid;
        this.userid = userid;
    }

    public String getPostid() {
        return this.postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}