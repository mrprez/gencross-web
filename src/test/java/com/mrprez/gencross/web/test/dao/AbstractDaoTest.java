package com.mrprez.gencross.web.test.dao;

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
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.springframework.orm.hibernate3.HibernateTransactionManager;

import com.mrprez.gencross.web.dao.AbstractDAO;

public abstract class AbstractDaoTest {
	
	private IDataSet setupDataSet;
	
	private Connection connection;
	
	private Session session;
	
	public abstract AbstractDAO getDao();
	
	public abstract String getDataSetFileName();
	
	
	@Before
	public void setUp() throws Exception{
		Configuration hibernateConfiguration = new Configuration();
        hibernateConfiguration = hibernateConfiguration.configure();
        SessionFactory sessionFactory = hibernateConfiguration.buildSessionFactory();
        HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
        getDao().setTransactionManager(transactionManager);
        session = sessionFactory.getCurrentSession();
		
        
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
	
	public Integer checkTableRow(ITable table, String columnName, Object value) throws DatabaseUnitException, SQLException{
		Integer row = findTableRow(table, columnName, value);
		if(row==null){
			Assert.fail("No row found in "+table.getTableMetaData().getTableName()+" with "+columnName+"="+value);
		}
		return row;
	}
	
	public Integer findTableRow(ITable table, String columnName, Object value) throws DatabaseUnitException, SQLException{
		for(int row=0; row<table.getRowCount(); row++){
			if(value.equals(table.getValue(row, columnName))){
				return row;
			}
		}
		return null;
	}
	
	public int count(ITable table, String column, Object criteria) throws DataSetException{
		int count = 0;
		for(int row=0; row<table.getRowCount(); row++){
			if(criteria.equals(table.getValue(row, column))){
				count++;
			}
		}
		return count;
	}
	
	protected Session getSession(){
		Transaction transaction = session.getTransaction();
		if(!transaction.isActive()){
			transaction.begin();
		}
		return session;
	}

	public Transaction getTransaction() {
		return session.getTransaction();
	}

}
