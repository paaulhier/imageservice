package de.paulkokot.imageservice.imageserviceserver.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.logging.Logger;

@ShellComponent
@RequiredArgsConstructor
public class StopCommand {
    private static final Logger logger = Logger.getLogger(StopCommand.class.getName());
    private final ConfigurableApplicationContext applicationContext;

    @ShellMethod("Stop the application")
    public void stop() {
        logger.info("Stopping application");
        System.exit(SpringApplication.exit(
                applicationContext,
                () -> 0
        ));
    }
}