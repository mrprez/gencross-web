package com.mrprez.gencross.web.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.web.context.ServletContextAware;

import com.mrprez.gencross.export.FileGenerator;
import com.mrprez.gencross.export.TemplatedFileGenerator;
import com.mrprez.gencross.web.dao.face.ITemplateFileResource;

public class TemplateFileResource implements ITemplateFileResource, ServletContextAware {
	private File templatesRepository;
	
	
	@Override
	public Map<Class<? extends TemplatedFileGenerator>, List<String>> getTemplates(String pluginName) throws Exception {
		Map<Class<? extends TemplatedFileGenerator>, List<String>> templateMap = new HashMap<Class<? extends TemplatedFileGenerator>, List<String>>();
		
		for(Class<? extends FileGenerator> generatorClass : FileGenerator.getGeneratorList().values()){
			if(TemplatedFileGenerator.class.isAssignableFrom(generatorClass)){
				Class<? extends TemplatedFileGenerator> templatedGeneratorClass = generatorClass.asSubclass(TemplatedFileGenerator.class);
				List<String> templateList = new ArrayList<String>();
				templateMap.put(templatedGeneratorClass, templateList);
				File generatorRep = new File(templatesRepository, generatorClass.getSimpleName());
				File pluginRepository = new File(generatorRep, pluginName);
				if(pluginRepository.exists()){
					for(File templateFile : pluginRepository.listFiles()){
						templateList.add(templateFile.getName());
					}
				}
			}
		}
		
		return templateMap;
	}
	
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		templatesRepository = new File(servletContext.getInitParameter("templateRepository"));
		Logger.getLogger(getClass()).info("Templates repository "+templatesRepository.getAbsolutePath()+" loaded" );
	}


	@Override
	public File getTemplate(Class<? extends TemplatedFileGenerator> generatorClass, String pluginName, String templateName) throws Exception {
		File generatorRep = new File(templatesRepository, generatorClass.getSimpleName());
		File pluginRep = new File(generatorRep, pluginName);
		return new File(pluginRep, templateName);
	}


	
	
}
