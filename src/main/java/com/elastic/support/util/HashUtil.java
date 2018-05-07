package com.elastic.support.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class HashUtil {

    private static MessageDigest md5Digest = null;

    static {
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supported", e);
        }
    }

    private static byte[] getKeyBytes(String k) {
        try {
            return k.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] computeMd5(String k) {
        MessageDigest md5;
        try {
            md5 = (MessageDigest) md5Digest.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("clone of MD5 not supported", e);
        }
        md5.update(getKeyBytes(k));
        return md5.digest();
    }

    private static long hash(final String k){
        byte[] bKey = computeMd5(k);
        return ((long) (bKey[3] & 0xFF) << 24)
                | ((long) (bKey[2] & 0xFF) << 16)
                | ((long) (bKey[1] & 0xFF) << 8)
                | (bKey[0] & 0xFF);
    }

    public static int position(final String k, final int availables){
        long hash = HashUtil.hash(k);
        return (int) (hash%availables);
    }

    public static String name(final String k, final List<String> names){
        int ptr = position(k, names.size());
        return names.remove(ptr);
    }
}
