package com.mrprez.gencross.web.dao;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.springframework.orm.hibernate3.HibernateTransactionManager;

public abstract class AbstractDaoTest {
	
	private IDataSet setupDataSet;
	
	private Connection connection;
	
	
	public abstract AbstractDAO getDao();
	
	public abstract String getDataSetFileName();
	
	
	@Before
	public void setUp() throws Exception{
		Configuration hibernateConfiguration = new Configuration();
        hibernateConfiguration = hibernateConfiguration.configure();
        HibernateTransactionManager transactionManager = new HibernateTransactionManager(hibernateConfiguration.buildSessionFactory());
        getDao().setTransactionManager(transactionManager);
        
        connection = DriverManager.getConnection(hibernateConfiguration.getProperty("connection.url"),
        		hibernateConfiguration.getProperty("connection.username"),
        		hibernateConfiguration.getProperty("connection.password"));
        
        try{
        	createDatabase(connection);
        	insertIntoDatabase(connection);
        }finally{
        	connection.commit();
        }
	}
	
	@After
	public void tearDown() throws SQLException{
		connection.close();
	}
	
	private void createDatabase(Connection connection) throws SQLException, IOException{
		BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/datasets/createDatabase.sql"));
        try{
	        String sqlLine;
	        while((sqlLine=reader.readLine())!=null){
	        	if( ! sqlLine.isEmpty() ){
		        	PreparedStatement statement = connection.prepareStatement(sqlLine);
		        	statement.execute();
	        	}
	        }
        }finally{
        	reader.close();
        }
        
	}
	
	
	protected void insertIntoDatabase(Connection connection) throws Exception {
		setupDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/resources/datasets/"+getDataSetFileName()));
		DatabaseOperation.INSERT.execute(new DatabaseConnection(connection), setupDataSet);
	}

	public IDataSet getSetupDataSet() {
		return setupDataSet;
	}
	
	public ITable getTable(String tableName) throws DatabaseUnitException, SQLException{
		DatabaseConnection databaseConnection = new DatabaseConnection(connection);
		IDataSet databaseDataSet = databaseConnection.createDataSet();
        return databaseDataSet.getTable(tableName);
	}

}
