package org.simpleframework.mvc.bean;

import java.io.InputStream;

/**
 * 封装表单文件数据
 * Created by Why on 2017/3/9.
 */
public final class FileParam {
    private String fileName;
    private long fileSize;
    private String contentType;
    private InputStream inputStream;

    public FileParam(String fileName, long fileSize, String contentType, InputStream inputStream) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.inputStream = inputStream;
    }


    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
