package com.eastcom.social.pos.core.orm.entity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table DOCUMENT.
 */
public class Document {

    private String id;
    private String fileName;
    private String fkFileId;
    private Integer fileNo;
    private String fkSignboardId;
    private Integer isDownload;
    private Integer idRead;
    private String createDate;
    private String download;
    private String readDate;

    public Document() {
    }

    public Document(String id) {
        this.id = id;
    }

    public Document(String id, String fileName, String fkFileId, Integer fileNo, String fkSignboardId, Integer isDownload, Integer idRead, String createDate, String download, String readDate) {
        this.id = id;
        this.fileName = fileName;
        this.fkFileId = fkFileId;
        this.fileNo = fileNo;
        this.fkSignboardId = fkSignboardId;
        this.isDownload = isDownload;
        this.idRead = idRead;
        this.createDate = createDate;
        this.download = download;
        this.readDate = readDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFkFileId() {
        return fkFileId;
    }

    public void setFkFileId(String fkFileId) {
        this.fkFileId = fkFileId;
    }

    public Integer getFileNo() {
        return fileNo;
    }

    public void setFileNo(Integer fileNo) {
        this.fileNo = fileNo;
    }

    public String getFkSignboardId() {
        return fkSignboardId;
    }

    public void setFkSignboardId(String fkSignboardId) {
        this.fkSignboardId = fkSignboardId;
    }

    public Integer getIsDownload() {
        return isDownload;
    }

    public void setIsDownload(Integer isDownload) {
        this.isDownload = isDownload;
    }

    public Integer getIdRead() {
        return idRead;
    }

    public void setIdRead(Integer idRead) {
        this.idRead = idRead;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getReadDate() {
        return readDate;
    }

    public void setReadDate(String readDate) {
        this.readDate = readDate;
    }}
