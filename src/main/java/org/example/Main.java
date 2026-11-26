package org.example;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Environment;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main extends Application<AppConfiguration> {
    public static void main(String[] args) throws Exception {
        new Main().run(args);
    }

    @Override
    public void run(AppConfiguration configuration, Environment environment) {
        IncidentService service = new IncidentService();
        environment.jersey().register(new IncidentResource(service));
        log.info("Registered POST /api/echo");
    }
}
