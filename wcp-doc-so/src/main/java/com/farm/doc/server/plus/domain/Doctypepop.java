package com.farm.doc.server.plus.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity(name = "DocTypePop")
@Table(name = "KNOW_FARM_DOCTYPE_POP")
public class Doctypepop implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "ID", length = 32, insertable = true, updatable = true, nullable = false)
    private String id;
    @Column(name = "TYPEID", length = 32, nullable = false)
    private String typeid;
    @Column(name = "ONAME", length = 64, nullable = false)
    private String oname;
    @Column(name = "POPTYPE", length = 1, nullable = false)
    private String poptype;
    @Column(name = "FUNTYPE", length = 1, nullable = false)
    private String funtype;
    @Column(name = "OID", length = 32, nullable = false)
    private String oid;

    public String getTypeid() {
        return this.typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getOname() {
        return this.oname;
    }

    public void setOname(String oname) {
        this.oname = oname;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoptype() {
        return this.poptype;
    }

    public void setPoptype(String poptype) {
        this.poptype = poptype;
    }

    public String getFuntype() {
        return this.funtype;
    }

    public void setFuntype(String funtype) {
        this.funtype = funtype;
    }

    public String getOid() {
        return this.oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }
}