package com.mrprez.gencross.web.bo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.util.Date;

import org.hibernate.Hibernate;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.disk.PersonnageFactory;
import com.mrprez.gencross.disk.PersonnageSaver;

public class PersonnageDataBO {
	private static PersonnageFactory personnageFactory;
	private Personnage personnage;
	private Integer id;
	private Date lastUpdateDate;
	
	
	@SuppressWarnings("deprecation")
	public Blob getData() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PersonnageSaver.savePersonnage(personnage, baos);
		return Hibernate.createBlob(baos.toByteArray());
	}
	public void setData(Blob data) throws Exception {
		personnage = getPersonnageFactory().loadPersonnage(data.getBinaryStream());
	}
	
	public Personnage getPersonnage() {
		return personnage;
	}
	public void setPersonnage(Personnage personnage) {
		this.personnage = personnage;
	}
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
	
	
	public static PersonnageFactory getPersonnageFactory() throws IOException{
		if(personnageFactory == null){
			initPersonnageFactory();
		}
		return personnageFactory;
	}
	public static synchronized void initPersonnageFactory() throws IOException{
		if(personnageFactory == null){
			personnageFactory = new PersonnageFactory();
		}
	}
	
}
