package ru.basted.basteduitests.config;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:config.properties"})
public interface ProjectConfig extends Config {
    @Key("site.url")
    @DefaultValue("https://basted.ru")
    String siteUrl();
}
