package br.com.diego.soares;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;
import java.util.Map;

public class PostgreSQLTestResource implements QuarkusTestResourceLifecycleManager {
    static final PostgreSQLContainer<?> CONTAINER = new PostgreSQLContainer<>("postgres:16");

    @Override
    public Map<String, String> start() {
        CONTAINER.start();
        return Map.of(
                "quarkus.datasource.jdbc.url", CONTAINER.getJdbcUrl(),
                "quarkus.datasource.username", CONTAINER.getUsername(),
                "quarkus.datasource.password", CONTAINER.getPassword()
        );
    }

    @Override
    public void stop() {
        if (CONTAINER.isRunning()) {
            CONTAINER.stop();
        }
    }
}
