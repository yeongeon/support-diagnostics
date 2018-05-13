package com.elastic.support.util;

import com.elastic.support.diagnostics.chain.DiagnosticContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public class AliasUtil {
    protected static final Logger logger = LogManager.getLogger();

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

    private static int position(final String k, final int availables){
        long hash = AliasUtil.hash(k);
        return (int) (hash%availables);
    }

    private static String name(final String k, final List<String> candidates){
        int ptr = position(k, candidates.size());
        return candidates.remove(ptr);
    }

    public static String peak(final String k, final Map<String, String> dic, final List<String> candidates){
        if(dic.containsKey(k)){
            return dic.get(k);
        }
        String n = String.format("%s", name(k, candidates));
        dic.put(k, n);

        return n;
    }

    public static String alias(DiagnosticContext ctx, String org) {
        logger.info("ctx: {}", ctx);
        logger.info("org: {}", org);
        return (!ctx.getInputParams().useAliases()||ctx==null?new String(org):peak(new String(org), ctx.getAliaseDic(), ctx.getAliases()));
    }
}
