package com.farm.doc.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * FarmRfDoctype entity. @author MyEclipse Persistence Tools
 */

@Entity(name = "FarmRfDoctype")
@Table(name = "KNOW_FARM_RF_DOCTYPE")
public class FarmRfDoctype implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "ID", length = 32, insertable = true, updatable = true, nullable = false)
    private String id;
    @Column(name = "TYPEID", length = 32, nullable = false)
    private String typeid;
    @Column(name = "DOCID", length = 32, nullable = false)
    private String docid;

    // Constructors

    /**
     * default constructor
     */
    public FarmRfDoctype() {
    }

    /**
     * full constructor
     */
    public FarmRfDoctype(String typeid, String docid) {
        this.typeid = typeid;
        this.docid = docid;
    }

    // Property accessors

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeid() {
        return this.typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getDocid() {
        return this.docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

}