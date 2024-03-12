package com.orion.ops.framework.common.file;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件客户端
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/30 16:51
 */
public interface FileClient {

    /**
     * 上传文件
     *
     * @param path    文件路径
     * @param content 文件内容
     * @return 路径
     * @throws Exception Exception
     */
    String upload(String path, byte[] content) throws Exception;

    /**
     * 上传文件
     *
     * @param path            文件路径
     * @param content         文件内容
     * @param overrideIfExist 文件存在是否覆盖
     * @return 路径
     * @throws Exception Exception
     */
    String upload(String path, byte[] content, boolean overrideIfExist) throws Exception;

    /**
     * 上传文件
     *
     * @param path 文件路径
     * @param in   in
     * @return 路径
     * @throws Exception Exception
     */
    String upload(String path, InputStream in) throws Exception;

    /**
     * 上传文件
     *
     * @param path      文件路径
     * @param in        in
     * @param autoClose autoClose
     * @return 路径
     * @throws Exception Exception
     */
    String upload(String path, InputStream in, boolean autoClose) throws Exception;

    /**
     * 上传文件
     *
     * @param path            文件路径
     * @param in              in
     * @param autoClose       autoClose
     * @param overrideIfExist 文件存在是否覆盖
     * @return 路径
     * @throws Exception Exception
     */
    String upload(String path, InputStream in, boolean autoClose, boolean overrideIfExist) throws Exception;

    /**
     * 检查文件是否存在
     *
     * @param path path
     * @return 是否存在
     */
    boolean isExists(String path);

    /**
     * 删除文件
     *
     * @param path 路径
     * @return 是否删除
     * @throws Exception Exception
     */
    boolean delete(String path) throws Exception;

    /**
     * 获取文件内容
     *
     * @param path path
     * @return bytes
     * @throws Exception Exception
     */
    byte[] getContent(String path) throws Exception;

    /**
     * 获取文件输入流
     *
     * @param path path
     * @return stream
     * @throws Exception Exception
     */
    InputStream getContentInputStream(String path) throws Exception;

    /**
     * 获取文件输出流
     *
     * @param path path
     * @return stream
     * @throws Exception Exception
     */
    OutputStream getContentOutputStream(String path) throws Exception;

    /**
     * 获取文件输出流
     *
     * @param path   path
     * @param append append
     * @return stream
     * @throws Exception Exception
     */
    OutputStream getContentOutputStream(String path, boolean append) throws Exception;

}
