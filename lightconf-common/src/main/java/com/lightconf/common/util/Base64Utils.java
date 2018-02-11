package com.lightconf.common.util;

import org.apache.commons.codec.binary.Base64;

import java.io.*;

/**
 * <p>
 * BASE64编码解码工具类.
 * </p>
 *
 * @author whf
 * @date Aug 20, 2017
 */
public class Base64Utils {

    /**
     * 文件读取缓冲区大小.
     */
    private static final int CACHE_SIZE = 1024;

    /**
     * BASE64字符串解码为二进制数据.
     *
     * @param base64 base64.
     * @return byte数组.
     * @throws Exception Exception
     */
    public static byte[] decode(String base64) throws Exception {
        return Base64.decodeBase64(base64);
    }

    /**
     * <p>
     * 二进制数据编码为BASE64字符串.
     * </p>
     *
     * @param bytes bytes.
     * @return 编码字符串.
     * @throws Exception Exception.
     */
    public static String encode(byte[] bytes) throws Exception {
        return Base64.encodeBase64String(bytes);
    }

    /**
     * <p>
     * 将文件编码为BASE64字符串.
     * </p>
     * <p>
     * 大文件慎用，可能会导致内存溢出.
     * </p>
     *
     * @param filePath 文件绝对路径.
     * @return 编码字符串.
     * @throws Exception Exception.
     */
    public static String encodeFile(String filePath) throws Exception {
        byte[] bytes = fileToByte(filePath);
        return encode(bytes);
    }

    /**
     * <p>
     * BASE64字符串转回文件.
     * </p>
     *
     * @param filePath 文件绝对路径.
     * @param base64 编码字符串.
     * @throws Exception Exception.
     */
    public static void decodeToFile(String filePath, String base64) throws Exception {
        byte[] bytes = decode(base64);
        byteArrayToFile(bytes, filePath);
    }

    /**
     * <p>
     * 文件转换为二进制数组.
     * </p>
     *
     * @param filePath 文件路径.
     * @return byte数组.
     * @throws Exception Exception.
     */
    public static byte[] fileToByte(String filePath) throws Exception {
        byte[] data = new byte[0];
        File file = new File(filePath);
        if (file.exists()) {
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
            byte[] cache = new byte[CACHE_SIZE];
            int fileRead = 0;
            while ((fileRead = in.read(cache)) != -1) {
                out.write(cache, 0, fileRead);
                out.flush();
            }
            out.close();
            in.close();
            data = out.toByteArray();
        }
        return data;
    }

    /**
     * <p>
     * 二进制数据写文件.
     * </p>
     *
     * @param bytes 二进制数据.
     * @param filePath 文件生成目录.
     */
    public static void byteArrayToFile(byte[] bytes, String filePath) throws Exception {
        InputStream in = new ByteArrayInputStream(bytes);
        File destFile = new File(filePath);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        destFile.createNewFile();
        OutputStream out = new FileOutputStream(destFile);
        byte[] cache = new byte[CACHE_SIZE];
        int fileRead = 0;
        while ((fileRead = in.read(cache)) != -1) {
            out.write(cache, 0, fileRead);
            out.flush();
        }
        out.close();
        in.close();
    }

}
