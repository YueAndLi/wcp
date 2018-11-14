package com.farm.parameter.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/* *
 *功能：用户个性化参数类
 *详细：
 *
 *版本：v0.1
 *作者：王东
 *日期：20141204174206
 *说明：
 */
@Entity(name = "Aloneparameterlocal")
@Table(name = "KNOW_ALONE_PARAMETER_LOCAL")
public class Aloneparameterlocal implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "PVALUE", length = 32, nullable = false)
    private String pvalue;
    @Column(name = "EUSER", length = 32, nullable = false)
    private String euser;
    @Column(name = "PARAMETERID", length = 32, nullable = false)
    private String parameterid;
    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "ID", length = 32, insertable = true, updatable = true, nullable = false)
    private String id;

    public String getPvalue() {
        return this.pvalue;
    }

    public void setPvalue(String pvalue) {
        this.pvalue = pvalue;
    }

    public String getEuser() {
        return this.euser;
    }

    public void setEuser(String euser) {
        this.euser = euser;
    }

    public String getParameterid() {
        return this.parameterid;
    }

    public void setParameterid(String parameterid) {
        this.parameterid = parameterid;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}