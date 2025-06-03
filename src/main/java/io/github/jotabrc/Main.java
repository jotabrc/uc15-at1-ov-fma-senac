package io.github.jotabrc;

import io.github.jotabrc.config.DatabaseConfig;
import io.github.jotabrc.dto.PaymentDto;
import io.github.jotabrc.dto.RecurringReceiptDto;
import io.github.jotabrc.dto.RoleDto;
import io.github.jotabrc.dto.UserRegisterDto;
import io.github.jotabrc.service.*;
import io.github.jotabrc.util.DependencySelectorImpl;
import io.github.jotabrc.util.LoadEnvironmentVariables;
import io.github.jotabrc.util.RoleName;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;

import java.time.LocalDate;

@Slf4j
public class Main {

    private static final DatabaseConfig databaseConfig = DatabaseConfig.getInstance();
    private static final LoadEnvironmentVariables loadEnv = DependencySelectorImpl.getInstance().select(LoadEnvironmentVariables.class);

    public static void main(String[] args) {
        try {
            loadEnv.loadDatabaseVariables();
            migrateDb();
//            addRole();
//            addUser();
//            UserService userService = new UserServiceImpl();
//            userService.auth(
//                    UserAuthDto
//                            .builder()
//                            .email("email@email")
//                            .password("password1234")
//                            .build()
//            );
//            System.out.println(ApplicationContext.getInstance().getUserUuid());
//            addPayment();
//            addRecurringReceipt();
//            get();

        } catch (Exception e) {
            log.info(e.getMessage());
        }
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

    /**
     * AT 1 requirement.
     */
    private static void addRole() {
        RoleService roleService = new RoleServiceImpl();
        try {
            roleService.add(
                    RoleDto
                            .builder()
                            .name(RoleName.USER)
                            .description("User")
                            .build()
            );
            roleService.add(
                    RoleDto
                            .builder()
                            .name(RoleName.ADMIN)
                            .description("Admin")
                            .build()
            );
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    /**
     * AT 1 requirement.
     */
    private static void addUser() {
        UserService userService = new UserServiceImpl();
        try {
            userService.add(
                    UserRegisterDto
                            .builder()
                            .username("example")
                            .email("email@email")
                            .name("John Doe")
                            .password("password1234")
                            .build()
            );
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    private static void addPayment() {
        FinancialService financialService = new FinancialServiceImpl();
        PaymentDto p = new PaymentDto(LocalDate.now(), 100.55, "desc", "payee");
        financialService.save(p);
    }

    private static void addRecurringReceipt() {
        FinancialService financialService = new FinancialServiceImpl();
        RecurringReceiptDto dto =
                new RecurringReceiptDto(LocalDate.now(), 99.99, "receipt rr", LocalDate.now().plusMonths(2), "vendor");
        financialService.save(dto);
    }

    private static void get() {
        FinanceService financeService = new FinanceServiceImpl();
        var r = financeService.get("67c7ab49-e36a-412f-a9cc-0b96a71b3bc6", 0, 10, "due_date", LocalDate.now(), LocalDate.now());
        r.getContent().getFirst().getFinancialItems().forEach(e -> System.out.println(e.getDescription()));
    }
}
