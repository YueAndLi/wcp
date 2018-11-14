package com.farm.doc.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Entity(name = "FarmDocfile")
@Table(name = "KNOW_FARM_DOCFILE")
public class FarmDocfile implements java.io.Serializable {
    private static final long serialVersionUID = 3911185369411888075L;

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "ID", length = 32, insertable = true, updatable = true, nullable = false)
    private String id;
    @Column(name = "PSTATE", length = 2, nullable = false)
    private String pstate;
    @Column(name = "EUSER", length = 32, nullable = false)
    private String euser;
    @Column(name = "PCONTENT", length = 128)
    private String pcontent;
    @Column(name = "EUSERNAME", length = 64, nullable = false)
    private String eusername;
    @Column(name = "CUSER", length = 32, nullable = false)
    private String cuser;
    @Column(name = "CUSERNAME", length = 64, nullable = false)
    private String cusername;
    @Column(name = "ETIME", length = 16, nullable = false)
    private String etime;
    @Column(name = "CTIME", length = 16, nullable = false)
    private String ctime;
    @Column(name = "FILENAME", length = 64, nullable = false)
    private String filename;
    @Column(name = "DIR", length = 256, nullable = false)
    private String dir;
    @Column(name = "LEN", length = 12, nullable = false)
    private Float len;
    @Column(name = "EXNAME", length = 16, nullable = false)
    private String exname;
    @Column(name = "NAME", length = 64, nullable = false)
    private String name;
    @Column(name = "TYPE", length = 2, nullable = false)
    private String type;
    @Column(name = "SERVERID", length = 32, nullable = false)
    private String serverid;
    @Column(name = "FAST_PATH",length = 256)
    private String fastPath;

    @Transient
    private File file;
    @Transient
    private String url;

    public FarmDocfile() {
    }

    public FarmDocfile(String dir, String serverid, String type, String name,
                       String filename, String ctime, String etime, String cusername,
                       String cuser, String eusername, String euser, String pstate) {
        this.dir = dir;
        this.serverid = serverid;
        this.type = type;
        this.name = name;
        this.filename = filename;
        this.ctime = ctime;
        this.etime = etime;
        this.cusername = cusername;
        this.cuser = cuser;
        this.eusername = eusername;
        this.euser = euser;
        this.pstate = pstate;
    }

    public FarmDocfile(String dir, String serverid, String type, String name,
                       String filename, String ctime, String etime, String cusername,
                       String cuser, String eusername, String euser, String pstate,
                       String pcontent, String exname, Float len) {
        this.dir = dir;
        this.serverid = serverid;
        this.type = type;
        this.name = name;
        this.filename = filename;
        this.ctime = ctime;
        this.etime = etime;
        this.cusername = cusername;
        this.cuser = cuser;
        this.eusername = eusername;
        this.euser = euser;
        this.pstate = pstate;
        this.exname = exname;
        this.len = len;
        this.pcontent = pcontent;
    }

    public String getPstate() {
        return this.pstate;
    }

    public void setPstate(String pstate) {
        this.pstate = pstate;
    }

    public String getEuser() {
        return this.euser;
    }

    public void setEuser(String euser) {
        this.euser = euser;
    }

    public String getPcontent() {
        return this.pcontent;
    }

    public void setPcontent(String pcontent) {
        this.pcontent = pcontent;
    }

    public String getEusername() {
        return this.eusername;
    }

    public void setEusername(String eusername) {
        this.eusername = eusername;
    }

    public String getCuser() {
        return this.cuser;
    }

    public void setCuser(String cuser) {
        this.cuser = cuser;
    }

    public String getCusername() {
        return this.cusername;
    }

    public void setCusername(String cusername) {
        this.cusername = cusername;
    }

    public String getEtime() {
        return this.etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public String getCtime() {
        return this.ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDir() {
        return this.dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public Float getLen() {
        return len;
    }

    public void setLen(Float len) {
        this.len = len;
    }

    public String getExname() {
        return this.exname;
    }

    public void setExname(String exname) {
        this.exname = exname;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getServerid() {
        return this.serverid;
    }

    public void setServerid(String serverid) {
        this.serverid = serverid;
    }

    public InputStream getInputStream() throws FileNotFoundException {
        if (!file.exists()) {
            return null;
        }

        return new FileInputStream(file);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFastPath() {
        return fastPath;
    }

    public void setFastPath(String fastPath) {
        this.fastPath = fastPath;
    }
}