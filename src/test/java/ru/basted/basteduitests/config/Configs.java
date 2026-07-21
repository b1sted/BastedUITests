package ru.basted.basteduitests.config;

import org.aeonbits.owner.ConfigFactory;

/**
 * Единая точка доступа к конфигурации проекта.
 * <p>
 * Инкапсулирует создание {@link ProjectConfig} через Owner, чтобы избежать
 * дублирования {@code ConfigFactory.create(...)} в разных классах.
 */
public final class Configs {
    private static final ProjectConfig PROJECT_CONFIG = ConfigFactory.create(
            ProjectConfig.class, System.getProperties()
    );

    private Configs() {
        throw new UnsupportedOperationException("Запрещено создание экземпляра утилитарного класса");
    }

    public static ProjectConfig project() {
        return PROJECT_CONFIG;
    }
}
