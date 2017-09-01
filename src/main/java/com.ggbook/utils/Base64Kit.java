package com.ggbook.utils;

import com.jfinal.kit.StrKit;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;

/**
 * Created by Administrator on 2017/4/19.
 */
public class Base64Kit {

    public static String encode(String content) throws Exception {
        return new String(new BASE64Encoder().encode(content.getBytes("UTF-8")));
    }

    public static String decode(String content) throws Exception {
        byte [] byte_content= new BASE64Decoder().decodeBuffer(content);
        return new String(byte_content,"UTF-8");
    }
    /**
     * 将文件转成base64 字符串
     * @param path 文件路径
     * @return  *
     * @throws Exception
     */
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);;
        if (!file.exists()) return null;
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return new BASE64Encoder().encode(buffer);

    }

    /**
     * 将base64字符解码保存文件
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */
    public static void decoderBase64File(String base64Code, String targetPath)
            throws Exception {
        if (StrKit.isBlank(base64Code)) return;
        File file = new File(targetPath.substring(0, targetPath.lastIndexOf("/")));
        file.mkdirs();
        byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.flush();
        out.close();
    }

    /**
     * 将base64字符保存文本文件
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */
    public static void toFile(String base64Code, String targetPath)
            throws Exception {

        byte[] buffer = base64Code.getBytes();
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }
}
