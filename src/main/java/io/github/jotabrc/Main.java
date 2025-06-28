package io.github.jotabrc;

import io.github.jotabrc.config.DatabaseConfig;
import io.github.jotabrc.util.DependencySelectorImpl;
import io.github.jotabrc.util.LoadEnvironmentVariables;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;

import java.io.IOException;

@Slf4j
public class Main extends Application {

    private static final DatabaseConfig databaseConfig = DatabaseConfig.getInstance();
    private static final LoadEnvironmentVariables loadEnv = DependencySelectorImpl.getInstance().select(LoadEnvironmentVariables.class);

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void init() throws Exception {
        super.init();
        loadEnv.loadDatabaseVariables();
        migrateDb();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader mainLoader = new FXMLLoader(Main.class.getResource("/main.fxml"));
        Scene scene = new Scene(mainLoader.load());
        stage.setMaximized(true);

        stage.setTitle("Financial Management Application");
        stage.setScene(scene);
        stage.show();
    }

    private static void migrateDb() {
        try {
            var flyway = Flyway.configure()
                    .locations("classpath:db/migration")
                    .dataSource("jdbc:" +
                                    databaseConfig.getDB_TYPE() + "://" +
                                    databaseConfig.getDB_HOST() + ":" +
                                    databaseConfig.getDB_PORT() + "/" +
                                    databaseConfig.getDB_SCHEMA(),
                            databaseConfig.getDB_USER(), databaseConfig.getDB_PASSWORD())
                    .load();
            flyway.migrate();
        } catch (FlywayException e) {
            log.error(e.getMessage());
        }
    }
}
