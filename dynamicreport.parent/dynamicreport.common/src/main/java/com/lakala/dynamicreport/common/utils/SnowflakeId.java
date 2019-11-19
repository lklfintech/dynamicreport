/*
 * Copyright (c) 2019-2021, LaKaLa.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.lakala.dynamicreport.common.utils;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

/**
 * <p>
 * 分布式唯一ID工具类（雪花算法）
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class SnowflakeId extends IdentityGenerator {

    private final static Logger log = LoggerFactory.getLogger(SnowflakeId.class);

    // ==============================Fields===========================================
    /**
     * 开始时间截 (2019-01-01)
     */
    private static final long TWEPOCH = 1546272000000L;

    /**
     * 机器id所占的位数
     */
    private static final long WORKER_ID_BITS = 5L;

    /**
     * 数据标识id所占的位数
     */
    private static final long DATA_CENTER_ID_BITS = 5L;

    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private static final long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS);

    /**
     * 支持的最大数据标识id，结果是31
     */
    private static final long MAX_DATA_CENTER_ID = -1L ^ (-1L << DATA_CENTER_ID_BITS);

    /**
     * 序列在id中占的位数
     */
    private static final long SEQUENCE_BITS = 12L;

    /**
     * 机器ID向左移12位
     */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

    /**
     * 数据标识id向左移17位(12+5)
     */
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    /**
     * 时间截向左移22位(5+5+12)
     */
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private static final long SEQUENCE_MASK = -1L ^ (-1L << SEQUENCE_BITS);

    /**
     * 工作机器ID(0~31)
     */
    private static long workerId;

    /**
     * 数据中心ID(0~31)
     */
    private static long datacenterId;

    /**
     * 毫秒内序列(0~4095)
     */
    private static long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private static long lastTimestamp = -1L;

    // ==============================Constructors=====================================
    public SnowflakeId() {
        // 获取配置文件路径
        StringBuilder propertyFilePath = new StringBuilder();
        try {
            // 获取lib包目录和部署目录
            String libPath = this.getClass().getResource("").getPath();
            int libIndex = libPath.lastIndexOf("/lib/");
            String deployPath = null;
            if (libIndex > -1) {
                deployPath = libPath.substring(0, libIndex).replace("file:", "");
            } else {
                deployPath = libPath;
            }

            if (log.isDebugEnabled()) {
                log.debug("libPath: {}, deployPath: {}", libPath, deployPath);
            }
            // 获取当前运行环境，eg: dev/vsit/vuat/product。默认product
            Map<String, String> envMap = System.getenv();
            String springActiveProfile = envMap.get("APP_ACTIVE_PROFILE");
            if (StringUtils.isBlank(springActiveProfile)) {
                springActiveProfile = "product";
            }
            if (log.isDebugEnabled()) {
                log.debug("springActiveProfile: {}", springActiveProfile);
            }
            // 获取配置文件路径
            propertyFilePath.append(deployPath).append("/config/").append(springActiveProfile).append("/application.properties");
            if (log.isDebugEnabled()) {
                log.debug("propertyFilePath: {}", propertyFilePath);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        File file = new File(propertyFilePath.toString());
        if (file.exists()) {
            // 获取workerId、datacenterId配置并赋值
            try (FileInputStream in = new FileInputStream(propertyFilePath.toString())) {
                Properties properties = new Properties();
                properties.load(in);

                int configWorkerId = Integer.valueOf(properties.getProperty("cluster.worker.id"));
                int configDatacenterId = Integer.valueOf(properties.getProperty("cluster.datacenter.id"));
                if (log.isDebugEnabled()) {
                    log.debug("configWorkerId: {}, configDatacenterId: {}", configWorkerId, configDatacenterId);
                }
                if (configWorkerId > 0) {
                    SnowflakeId.workerId = configWorkerId;
                }
                if (configDatacenterId > 0) {
                    SnowflakeId.datacenterId = configDatacenterId;
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    public static SnowflakeId getInstance() {
        int workerId = new Random().nextInt(30);
        int datacenterId = new Random().nextInt(30);
        // 获取配置文件路径
        StringBuilder propertyFilePath = new StringBuilder();
        try {
            // 获取lib包目录和部署目录
            String libPath = SnowflakeId.class.getResource("").getPath();
            String deployPath = libPath.substring(0, libPath.lastIndexOf("/lib/")).replace("file:", "");
            if (log.isDebugEnabled()) {
                log.debug("libPath: {}, deployPath: {}", libPath, deployPath);
            }
            // 获取当前运行环境，eg: dev/vsit/vuat/product。默认product
            Map<String, String> envMap = System.getenv();
            String springActiveProfile = envMap.get("APP_ACTIVE_PROFILE");
            if (StringUtils.isBlank(springActiveProfile)) {
                springActiveProfile = "product";
            }
            if (log.isDebugEnabled()) {
                log.debug("springActiveProfile: {}", springActiveProfile);
            }
            // 获取配置文件路径
            propertyFilePath.append(deployPath).append("/config/").append(springActiveProfile).append("/application.properties");
            if (log.isDebugEnabled()) {
                log.debug("propertyFilePath: {}", propertyFilePath);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        // 获取workerId、datacenterId配置并赋值
        try (FileInputStream in = new FileInputStream(propertyFilePath.toString())) {
            Properties properties = new Properties();
            properties.load(in);

            int configWorkerId = Integer.valueOf(properties.getProperty("cluster.worker.id"));
            int configDatacenterId = Integer.valueOf(properties.getProperty("cluster.datacenter.id"));
            if (log.isInfoEnabled()) {
                log.info("configWorkerId: {}", configWorkerId);
                log.info("configDatacenterId: {}", configDatacenterId);
            }
            if (configWorkerId > 0) {
                workerId = configWorkerId;
            }
            if (configDatacenterId > 0) {
                datacenterId = configDatacenterId;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new SnowflakeId(workerId, datacenterId);
    }

    /**
     * 构造函数
     *
     * @param workerId     工作ID (0~31)
     * @param datacenterId 数据中心ID (0~31)
     */
    private SnowflakeId(long workerId, long datacenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        if (datacenterId > MAX_DATA_CENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", MAX_DATA_CENTER_ID));
        }
        SnowflakeId.workerId = workerId;
        SnowflakeId.datacenterId = datacenterId;
    }

    // ==============================Methods==========================================

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public static synchronized long getId() {
        long timestamp = timeGen();

        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        // 如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            // 毫秒内序列溢出
            if (sequence == 0) {
                // 阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        // 时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        // 上次生成ID的时间截
        lastTimestamp = timestamp;

        // 移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - TWEPOCH) << TIMESTAMP_LEFT_SHIFT) | (datacenterId << DATA_CENTER_ID_SHIFT) | (workerId << WORKER_ID_SHIFT) | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected static long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    protected static long timeGen() {
        return System.currentTimeMillis();
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        long id = getId();
        if (log.isDebugEnabled()) {
            log.debug("snowflake generate id:{}", id);
        }
        return id;
    }

}