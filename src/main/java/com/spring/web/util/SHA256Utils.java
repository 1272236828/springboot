package com.spring.web.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

// 获取文件的SHA256加密的代码
public class SHA256Utils {
    private static final Logger logger = LoggerFactory.getLogger(SHA256Utils.class);

    public static String gatSHA256(File file) {
        String SHA256Hex = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            SHA256Hex = DigestUtils.sha256Hex(fileInputStream);
            return SHA256Hex;
        } catch (IOException e) {
            logger.error("SHA256文件完整性检查失败：", e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Boolean checkSHA256(File file, String SHA256) {
        String fileSHA256Hex = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            fileSHA256Hex = DigestUtils.sha256Hex(fileInputStream);
            if (fileSHA256Hex.equals(SHA256)) {
                return true;
            }
        } catch (IOException e) {
            logger.error("SHA256文件完整性检查失败：", e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
