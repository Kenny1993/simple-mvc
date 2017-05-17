package org.simpleframework.mvc.helper;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.simpleframework.mvc.bean.FileParam;
import org.simpleframework.mvc.bean.Param;
import org.simpleframework.util.CollectionUtil;
import org.simpleframework.util.FileUtil;
import org.simpleframework.util.StreamUtil;
import org.simpleframework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件上传助手类
 */
public final class UploadHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadHelper.class);

    /**
     * Apache Commons FileUpload 提供的 Servlet 文件上传对象
     */
    private static ServletFileUpload servletFileUpload;

    /**
     * 初始化
     */
    public static void init(ServletContext servletContext) {
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        servletFileUpload = new ServletFileUpload(new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository));
        servletFileUpload.setHeaderEncoding(ConfigHelper.getAppEncoding());
        servletFileUpload.setFileSizeMax(ConfigHelper.getAppUploadMaxSizeLimit() * 1024 * 1024);
    }

    /**
     * 判断请求是否为 multipart 类型
     */
    public static boolean isMultipart(HttpServletRequest request) {
        return servletFileUpload.isMultipartContent(request);
    }

    /**
     * 创建请求对象
     */
    public static Param createParam(HttpServletRequest request) throws IOException {
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        Map<String, FileParam> fileMap = new HashMap<String, FileParam>();
        try {
            Map<String, List<FileItem>> fileItemListMap = servletFileUpload.parseParameterMap(request);
            if (CollectionUtil.isNotEmpty(fileItemListMap)) {
                for (Map.Entry<String, List<FileItem>> fileItemListEntry : fileItemListMap.entrySet()) {
                    String fieldName = fileItemListEntry.getKey();
                    List<FileItem> fileItemList = fileItemListEntry.getValue();
                    if (CollectionUtil.isNotEmpty(fileItemList)) {
                        for (FileItem fileItem : fileItemList) {
                            if (fileItem.isFormField()) {
                                String fieldValue = fileItem.getString(ConfigHelper.getAppEncoding());
                                if (fieldMap.containsKey(fieldName)) {
                                    Object o = fieldMap.get(fieldName);
                                    if (o instanceof ArrayList) {
                                        ((ArrayList) o).add(fieldValue);
                                        fieldMap.put(fieldName, o);
                                    } else {
                                        List<Object> list = new ArrayList<Object>();
                                        list.add(o);
                                        list.add(fieldValue);
                                        fieldMap.put(fieldName, list);
                                    }
                                } else {
                                    fieldMap.put(fieldName, fieldValue);
                                }
                            } else {
                                String fileName = FileUtil.getRealFileName(new String(fileItem.getName().getBytes(), ConfigHelper.getAppEncoding()));
                                if (StringUtil.isNotEmpty(fileName)) {
                                    long fileSize = fileItem.getSize();
                                    String contentType = fileItem.getContentType();
                                    InputStream inputStream = fileItem.getInputStream();
                                    fileMap.put(fieldName, new FileParam(fileName, fileSize, contentType, inputStream));
                                }
                            }
                        }
                    }
                }
            }
        } catch (FileUploadException e) {
            LOGGER.debug("create param failure", e);
            throw new RuntimeException(e);
        }
        return new Param(fieldMap, fileMap);
    }

    /**
     * 上传文件
     */
    public static void uploadFile(String basePath, FileParam fileParam) {
        if (fileParam != null) {
            String fileName = fileParam.getFileName();
            if (isAllowUpload(fileName)) {
                String filePath = basePath + fileName;
                FileUtil.createFile(filePath);
                try {
                    InputStream inputStream = new BufferedInputStream(fileParam.getInputStream());
                    OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
                    StreamUtil.copyStream(inputStream, outputStream);
                } catch (Exception e) {
                    LOGGER.debug("upload file failure: " + filePath);
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 批量上传文件
     */
    public static void uploadFile(String basePath, List<FileParam> fileParamList) {
        if (CollectionUtil.isNotEmpty(fileParamList)) {
            for (FileParam fileParam : fileParamList) {
                uploadFile(basePath, fileParam);
            }
        }
    }

    private static boolean isAllowUpload(String filename) {
        String s = ConfigHelper.getAppUploadAllowExt();
        if (StringUtil.isNotEmpty(s)) {
            String[] exts = StringUtil.splitString(s, StringUtil.SEPARATOR);
            for (String ext : exts) {
                if (filename.endsWith(ext)) {
                    return true;
                }
            }
        }
        LOGGER.debug(filename + " is not allow to be uploaded");
        return false;
    }
}
