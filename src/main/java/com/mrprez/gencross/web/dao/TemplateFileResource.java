package com.mrprez.gencross.web.dao;

import java.io.File;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.web.context.ServletContextAware;

import com.mrprez.gencross.export.TemplatedFileGenerator;
import com.mrprez.gencross.web.dao.face.ITemplateFileResource;

public class TemplateFileResource implements ITemplateFileResource, ServletContextAware {
	private File templatesRepository;
	
	public TemplateFileResource(){
		super();
	}
	
	@Override
	public File[] getTemplateFiles(Class<? extends TemplatedFileGenerator> clazz) throws Exception{
		File repository = new File(templatesRepository, clazz.getSimpleName());
		return repository.listFiles();
	}
	
	@Override
	public File getTemplateFile(Class<? extends TemplatedFileGenerator> clazz, String fileName) throws Exception {
		File repository = new File(templatesRepository, clazz.getSimpleName());
		return new File(repository, fileName);
	}
	
	@Override
    public void setServletContext(ServletContext servletContext) {
		templatesRepository = new File(servletContext.getInitParameter("templateRepository"));
		Logger.getLogger(getClass()).info("Templates repository "+templatesRepository.getAbsolutePath()+" loaded" );
	}

	
	
	
	
}
