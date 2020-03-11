package com.server.configuration;

import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * EhCache配置文件，可以替代ehcache.xml 文件
 */

@Configuration
@EnableCaching
public class EhCacheConfiguration implements CachingConfigurer {

    @Bean(destroyMethod="shutdown")
    public net.sf.ehcache.CacheManager ehCacheManager() {
    	System.out.println("==================================ehcacheconfiguration=========================");
        CacheConfiguration cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setName("MachinesTypeBean");
        cacheConfiguration.setMemoryStoreEvictionPolicy("LRU");
        cacheConfiguration.setMaxEntriesLocalHeap(1000);
        
        
        
        CacheConfiguration sonCompanysForSql = new CacheConfiguration();
        sonCompanysForSql.setName("findAllSonCompanyIdForInSql");
        sonCompanysForSql.setMemoryStoreEvictionPolicy("LRU");
        sonCompanysForSql.setMaxEntriesLocalHeap(100);
        sonCompanysForSql.setTimeToLiveSeconds(1000);
        
        CacheConfiguration stateInfoList = new CacheConfiguration();
        stateInfoList.setName("stateInfoDtoList");
        stateInfoList.setMemoryStoreEvictionPolicy("LRU");
        stateInfoList.setMaxEntriesLocalHeap(100);
        stateInfoList.setTimeToLiveSeconds(1000);
        
        CacheConfiguration stateInfoName = new CacheConfiguration();
        stateInfoName.setName("stateInfoName");
        stateInfoName.setMemoryStoreEvictionPolicy("LRU");
        stateInfoName.setMaxEntriesLocalHeap(100);
        stateInfoName.setTimeToLiveSeconds(1000);
        
        CacheConfiguration vmCodeByFactoryNumber = new CacheConfiguration();
        vmCodeByFactoryNumber.setName("vmCodeByFactoryNumber");
        vmCodeByFactoryNumber.setMemoryStoreEvictionPolicy("LRU");
        vmCodeByFactoryNumber.setMaxEntriesLocalHeap(100);
        vmCodeByFactoryNumber.setTimeToLiveSeconds(1000);
        
        
        CacheConfiguration factoryNumberByVmCode = new CacheConfiguration();
        factoryNumberByVmCode.setName("factoryNumberByVmCode");
        factoryNumberByVmCode.setMemoryStoreEvictionPolicy("LRU");
        factoryNumberByVmCode.setMaxEntriesLocalHeap(100);
        factoryNumberByVmCode.setTimeToLiveSeconds(1000);
        
        
        
        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
        //可以创建多个cacheConfiguration，都添加到Config中
        config.addCache(cacheConfiguration);
        config.addCache(sonCompanysForSql);
        config.addCache(stateInfoList);
        config.addCache(stateInfoName);
        config.addCache(vmCodeByFactoryNumber);
        config.addCache(factoryNumberByVmCode);
        return net.sf.ehcache.CacheManager.newInstance(config);
    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheManager());
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }

    @Override
    public CacheResolver cacheResolver() { return null; }

    @Override
    public CacheErrorHandler errorHandler() {
        return null;
    }

}