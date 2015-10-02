package com.mrprez.gencross.web.bo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;

import org.hibernate.Hibernate;

public class PersonnageXmlBO {
	private Integer id;
	private Date lastUpdateDate;
	private byte xml[];
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public byte[] getXml() {
		return xml;
	}
	public void setXml(byte xml[]) {
		this.xml = xml;
	}
	@SuppressWarnings("deprecation")
	public Blob getData() throws IOException {
		return Hibernate.createBlob(xml);
	}
	public void setData(Blob data) throws SQLException, IOException {
		InputStream is = data.getBinaryStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream((int) data.length());
		try{
			int i;
			while((i=is.read())>=0){
				baos.write(i);
			}
		}finally{
			is.close();
		}
		xml = baos.toByteArray();
	}
	
	
	
	
	
	
	
}
