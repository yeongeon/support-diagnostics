package com.elastic.support.util;

import com.elastic.support.diagnostics.Constants;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class HashUtilTest {

    protected static final Logger logger = LoggerFactory.getLogger(HashUtilTest.class);

    static final LinkedList<String> NAMES = new LinkedList() {{
        add("Apple");
        add("Apricot");
        add("Avocado");
        add("Banana");
        add("Bilberry");
        add("Blackberry");
        add("Blackcurrant");
        add("Blueberry");
        add("Boysenberry");
        add("CrabApples");
    }};
    static {
        Random random = new Random(System.nanoTime());
        Collections.shuffle(NAMES, random);
    }

    static final List<String> HOSTS = new ArrayList() {{
        add("192.168.65.254");
        add("test-1.host");
        add("test-2.host");
        add("test-3.host");
        add("test-4.host");
        add("test-5.host");
        add("test-6.host");
        add("test-7.host");
        add("test-8.host");
    }};


    static Map<String, String> dic = new LinkedHashMap<>();

    @Test
    public void test1Hash(){
        for (String host: HOSTS) {
            logger.info("{}: {}", host, HashUtil.peak(host, dic, NAMES));
        }

        logger.info("{}", dic);
    }

    @Test
    public void test2Regex(){
        logger.info("{}", dic);

        final Pattern pattern = Pattern.compile(Constants.IP_REGEX);

        String sample = "--highest-ip 192.168.65.254 --log-destination asl";
        Matcher match = pattern.matcher(sample);
        if(match.find()){
            logger.info("match.group(0): {}, dic: {}", match.group(), dic.get(match.group()));
        }

    }

}