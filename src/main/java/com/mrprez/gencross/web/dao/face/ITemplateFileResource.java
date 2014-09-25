package com.mrprez.gencross.web.dao.face;

import java.io.File;

import com.mrprez.gencross.export.TemplatedFileGenerator;
import com.mrprez.gencross.disk.PluginDescriptor;

public interface ITemplateFileResource {

	public File[] getTemplateFiles(Class<? extends TemplatedFileGenerator> generatorClazz, PluginDescriptor pluginDescriptor) throws Exception;

	public File getTemplateFile(Class<? extends TemplatedFileGenerator> generatorClazz, PluginDescriptor pluginDescriptor, String fileName) throws Exception;
}
