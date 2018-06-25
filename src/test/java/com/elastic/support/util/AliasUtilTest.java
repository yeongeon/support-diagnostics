package com.elastic.support.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class AliasUtilTest {

    protected static final Logger logger = LoggerFactory.getLogger(AliasUtilTest.class);

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

    static Map<String, String> dic = new LinkedHashMap<>();

    @Test
    public void test1Hash(){
        for (String host: HOSTS) {
            logger.info("{}: {}", host, AliasUtil.peak(host, dic, NAMES));
        }

        logger.info("{}", dic);
    }

}