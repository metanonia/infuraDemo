package com.metanonia.infura.utils;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Mysql {
    Logger logger = Logger.getLogger(Mysql.class.getName());
    private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://localhost:/metanonia?useSSL=false";

    private Connection conn = null;
    private Statement statement = null;

    public Mysql(String user, String password) {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, user, password);
        }
        catch (Exception e) {
            logger.info(e.toString());
            System.exit(1);
        }
    }

    public void close() {
        try {
            if(statement != null) statement.close();
            if(conn != null) conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.info("mysql closed");
    }

    public Connection getConn() {
        return conn;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }
}
