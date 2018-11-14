package com.farm.authority.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/* *
 *功能：岗位权限类
 *详细：
 *
 *版本：v0.1
 *作者：FarmCode代码工程
 *日期：20141204174206
 *说明：
 */
@Entity(name = "Postaction")
@Table(name = "KNOW_ALONE_AUTH_POSTACTION")
public class Postaction implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "POSTID", length = 32, nullable = false)
    private String postid;
    @Column(name = "MENUID", length = 32, nullable = false)
    private String menuid;
    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "ID", length = 32, insertable = true, updatable = true, nullable = false)
    private String id;


    public String getPostid() {
        return this.postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getMenuid() {
        return this.menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}