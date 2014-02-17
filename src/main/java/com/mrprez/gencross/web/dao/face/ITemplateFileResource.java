package com.mrprez.gencross.web.dao.face;

import java.io.File;

import com.mrprez.gencross.export.TemplatedFileGenerator;

public interface ITemplateFileResource {

	public File[] getTemplateFiles(Class<? extends TemplatedFileGenerator> clazz) throws Exception;

	public File getTemplateFile(Class<? extends TemplatedFileGenerator> clazz, String fileName) throws Exception;
}
