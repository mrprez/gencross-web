package com.mrprez.gencross.web.dao;

import java.io.File;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.disk.PersonnageSaver;
import com.mrprez.gencross.web.dao.face.ISaveRepositoryResource;

public class SaveRepositoryResource implements ServletContextAware, ISaveRepositoryResource {
	private static final String OLD_REPOSITORY_NAME = "old";
	
	private File repository;
	private File oldRepository;
	
	
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		repository = new File(servletContext.getInitParameter("saveRepository"));
		oldRepository = new File(repository, OLD_REPOSITORY_NAME);
	}


	@Override
	public void cleanOldRepository() throws Exception {
		File oldFiles[] = oldRepository.listFiles();
		for(int i=0; i<oldFiles.length; i++){
			oldFiles[i].delete();
		}
	}


	@Override
	public void moveFilesToOldRepository() throws Exception {
		File fileTab[] = repository.listFiles();
		for(int i=0; i<fileTab.length; i++){
			if(fileTab[i].isFile()){
				fileTab[i].renameTo(new File(oldRepository, fileTab[i].getName()));
			}
		}
	}


	@Override
	public void savePersonnage(Personnage personnage, String name) throws Exception {
		File file = new File(repository, name+".gcr");
		PersonnageSaver.savePersonnageGcr(personnage, file);
	}
	
	

}
