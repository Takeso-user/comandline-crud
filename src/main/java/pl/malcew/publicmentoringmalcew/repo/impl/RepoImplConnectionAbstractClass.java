package pl.malcew.publicmentoringmalcew.repo.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class RepoImplConnectionAbstractClass {
    @Value("${spring.datasource.url}")
    private String URL;

    @Value("${spring.datasource.username}")
    private String USERNAME;

    @Value("${spring.datasource.password}")
    private String PASSWORD;
private final Logger LOG = LoggerFactory.getLogger(RepoImplConnectionAbstractClass.class);
    protected Connection getConnection() throws SQLException {
        var connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        LOG.info("Connection established: {}", connection);
        return connection;
    }

}
