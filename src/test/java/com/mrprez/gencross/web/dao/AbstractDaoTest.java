package com.mrprez.gencross.web.dao;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.cfg.Configuration;
import org.junit.Before;

public abstract class AbstractDaoTest {
	
	public abstract AbstractDAO getDao();
	
	public abstract String getDataSetFileName();
	
	
	@Before
	public void setUp() throws Exception{
		Configuration hibernateConfiguration = new Configuration();
        hibernateConfiguration = hibernateConfiguration.configure();
        getDao().setSessionFactory(hibernateConfiguration.buildSessionFactory());
        
        Connection connection = DriverManager.getConnection(System.getProperty(hibernateConfiguration.getProperty("connection.url")),
        		hibernateConfiguration.getProperty("connection.username"),
        		hibernateConfiguration.getProperty("connection.password"));
        
        try{
        	createDatabase(connection);
        	insertIntoDatabase(connection);
        }finally{
        	connection.close();
        }
        
	}
	
	private void createDatabase(Connection connection) throws SQLException, IOException{
		BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/datasets/createDatabase.sql"));
        try{
	        String sqlLine;
	        while((sqlLine=reader.readLine())!=null){
	        	if(sqlLine.isEmpty()){
		        	PreparedStatement statement = connection.prepareStatement(sqlLine);
		        	statement.execute();
	        	}
	        }
        }finally{
        	reader.close();
        }
        
	}
	
	
	protected void insertIntoDatabase(Connection connection) throws Exception {
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/resources/datasets/"+getDataSetFileName()+".xml"));
		DatabaseOperation.INSERT.execute(new DatabaseConnection(connection), dataSet);
	}

}
