package com.mrprez.gencross.web.dao.face;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.mrprez.gencross.export.TemplatedFileGenerator;

public interface ITemplateFileResource {

	public Map<Class<? extends TemplatedFileGenerator>, List<String>> getTemplates(String pluginName) throws Exception;
	
	public File getTemplate(Class<? extends TemplatedFileGenerator> generatorClass, String pluginName, String templateName) throws Exception;
	
}
