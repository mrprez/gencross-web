package com.mrprez.gencross.web.dao;

import java.io.File;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.web.context.ServletContextAware;

import com.mrprez.gencross.export.TemplatedFileGenerator;
import com.mrprez.gencross.web.dao.face.ITemplateFileResource;
import com.mrprez.gencross.disk.PluginDescriptor;

public class TemplateFileResource implements ITemplateFileResource, ServletContextAware {
	private File templatesRepository;
	
	public TemplateFileResource(){
		super();
	}
	
	@Override
	public File[] getTemplateFiles(Class<? extends TemplatedFileGenerator> generatorClazz, PluginDescriptor pluginDescriptor) throws Exception{
		File generatorRepository = new File(templatesRepository, generatorClazz.getSimpleName());
		File templateRepository = new File(generatorRepository, pluginDescriptor.getName());
		return templateRepository.listFiles();
	}
	
	@Override
	public File getTemplateFile(Class<? extends TemplatedFileGenerator> generatorClazz, PluginDescriptor pluginDescriptor, String fileName) throws Exception {
		File generatorRepository = new File(templatesRepository, generatorClazz.getSimpleName());
		File templateRepository = new File(generatorRepository, pluginDescriptor.getName());
		return new File(templateRepository, fileName);
	}
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		templatesRepository = new File(servletContext.getInitParameter("templateRepository"));
		Logger.getLogger(getClass()).info("Templates repository "+templatesRepository.getAbsolutePath()+" loaded" );
	}

	
	
	
	
}
