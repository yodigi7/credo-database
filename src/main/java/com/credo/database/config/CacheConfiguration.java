package com.credo.database.config;

import java.time.Duration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.credo.database.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.credo.database.domain.User.class.getName());
            createCache(cm, com.credo.database.domain.Authority.class.getName());
            createCache(cm, com.credo.database.domain.User.class.getName() + ".authorities");
            createCache(cm, com.credo.database.domain.Person.class.getName());
            createCache(cm, com.credo.database.domain.Person.class.getName() + ".organizations");
            createCache(cm, com.credo.database.domain.Person.class.getName() + ".phones");
            createCache(cm, com.credo.database.domain.Person.class.getName() + ".emails");
            createCache(cm, com.credo.database.domain.Person.class.getName() + ".personsInHouses");
            createCache(cm, com.credo.database.domain.Person.class.getName() + ".tickets");
            createCache(cm, com.credo.database.domain.PersonNotes.class.getName());
            createCache(cm, com.credo.database.domain.Organization.class.getName());
            createCache(cm, com.credo.database.domain.Organization.class.getName() + ".addresses");
            createCache(cm, com.credo.database.domain.Organization.class.getName() + ".phones");
            createCache(cm, com.credo.database.domain.Organization.class.getName() + ".emails");
            createCache(cm, com.credo.database.domain.Organization.class.getName() + ".persons");
            createCache(cm, com.credo.database.domain.OrganizationNotes.class.getName());
            createCache(cm, com.credo.database.domain.PersonPhone.class.getName());
            createCache(cm, com.credo.database.domain.OrganizationPhone.class.getName());
            createCache(cm, com.credo.database.domain.ParishPhone.class.getName());
            createCache(cm, com.credo.database.domain.Parish.class.getName());
            createCache(cm, com.credo.database.domain.Parish.class.getName() + ".organizations");
            createCache(cm, com.credo.database.domain.Parish.class.getName() + ".phones");
            createCache(cm, com.credo.database.domain.Parish.class.getName() + ".people");
            createCache(cm, com.credo.database.domain.Parish.class.getName() + ".emails");
            createCache(cm, com.credo.database.domain.Event.class.getName());
            createCache(cm, com.credo.database.domain.Ticket.class.getName());
            createCache(cm, com.credo.database.domain.Ticket.class.getName() + ".events");
            createCache(cm, com.credo.database.domain.PersonEmail.class.getName());
            createCache(cm, com.credo.database.domain.OrganizationEmail.class.getName());
            createCache(cm, com.credo.database.domain.ParishEmail.class.getName());
            createCache(cm, com.credo.database.domain.HouseDetails.class.getName());
            createCache(cm, com.credo.database.domain.HouseDetails.class.getName() + ".addresses");
            createCache(cm, com.credo.database.domain.HouseAddress.class.getName());
            createCache(cm, com.credo.database.domain.OrganizationAddress.class.getName());
            createCache(cm, com.credo.database.domain.MembershipLevel.class.getName());
            createCache(cm, com.credo.database.domain.MembershipLevel.class.getName() + ".people");
            createCache(cm, com.credo.database.domain.Person.class.getName() + ".transactions");
            createCache(cm, com.credo.database.domain.Event.class.getName() + ".tickets");
            createCache(cm, com.credo.database.domain.Transaction.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
