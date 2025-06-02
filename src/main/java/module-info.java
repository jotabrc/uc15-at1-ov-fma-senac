import io.github.jotabrc.service.FinancialService;
import io.github.jotabrc.service.FinancialServiceImpl;

open module uc.fifteen.at.one.ov.fma.senac {
    requires flyway.core;
    requires transitive java.sql;
    requires static lombok;
    requires mysql.connector.j;
    requires java.naming;
    requires java.management;
    requires org.slf4j;
    requires com.fasterxml.jackson.annotation;
    requires java.base;
    requires java.desktop;

    uses io.github.jotabrc.repository.util.SqlBuilder;
    provides io.github.jotabrc.repository.util.SqlBuilder
            with io.github.jotabrc.repository.util.SqlBuilderImpl;

    uses io.github.jotabrc.repository.RoleRepository;
    provides io.github.jotabrc.repository.RoleRepository
            with io.github.jotabrc.repository.RoleRepositoryImpl;

    uses io.github.jotabrc.repository.UserRepository;
    provides io.github.jotabrc.repository.UserRepository
            with io.github.jotabrc.repository.UserRepositoryImpl;

    uses io.github.jotabrc.repository.FinanceRepository;
    provides io.github.jotabrc.repository.FinanceRepository
            with io.github.jotabrc.repository.FinanceRepositoryImpl;

    uses io.github.jotabrc.repository.util.PrepareStatement;
    provides io.github.jotabrc.repository.util.PrepareStatement
            with io.github.jotabrc.repository.util.PrepareStatementImpl;

    uses io.github.jotabrc.security.ApplicationContextHolder;
    provides io.github.jotabrc.security.ApplicationContextHolder
            with io.github.jotabrc.security.ApplicationContextHolderImpl;

    uses io.github.jotabrc.service.RoleService;
    provides io.github.jotabrc.service.RoleService
            with io.github.jotabrc.service.RoleServiceImpl;

    uses io.github.jotabrc.service.UserService;
    provides io.github.jotabrc.service.UserService
            with io.github.jotabrc.service.UserServiceImpl;

    uses io.github.jotabrc.service.FinanceService;
    provides io.github.jotabrc.service.FinanceService
            with io.github.jotabrc.service.FinanceServiceImpl;

    uses FinancialService;
    provides FinancialService
            with FinancialServiceImpl;

    uses io.github.jotabrc.service.ReceiptService;
    provides io.github.jotabrc.service.ReceiptService
            with io.github.jotabrc.service.ReceiptServiceImpl;

    uses io.github.jotabrc.service.RecurringReceiptService;
    provides io.github.jotabrc.service.RecurringReceiptService
            with io.github.jotabrc.service.RecurringReceiptServiceImpl;

    uses io.github.jotabrc.service.RecurringPaymentService;
    provides io.github.jotabrc.service.RecurringPaymentService
            with io.github.jotabrc.service.RecurringPaymentServiceImpl;

    uses io.github.jotabrc.util.ConnectionUtil;
    provides io.github.jotabrc.util.ConnectionUtil
            with io.github.jotabrc.util.ConnectionUtilImpl;

    uses io.github.jotabrc.util.LoadEnvironmentVariables;
    provides io.github.jotabrc.util.LoadEnvironmentVariables
            with io.github.jotabrc.util.LoadEnvironmentVariablesImpl;

    uses io.github.jotabrc.util.PropertiesLoader;
    provides io.github.jotabrc.util.PropertiesLoader
            with io.github.jotabrc.util.PropertiesLoaderImpl;
}