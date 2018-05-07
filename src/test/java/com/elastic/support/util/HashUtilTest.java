package com.elastic.support.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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
        add("test-1.host");
        add("test-2.host");
        add("test-3.host");
        add("test-4.host");
        add("test-5.host");
        add("test-6.host");
        add("test-7.host");
        add("test-8.host");
    }};

    @Test
    public void testHash(){
        Map<String, String> dic = new LinkedHashMap<>();

        for (String host: HOSTS) {
            dic.put(host, HashUtil.name(host, NAMES));
        }

        logger.info("dic: {}", dic);
    }

}