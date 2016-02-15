package com.mrprez.gencross.web.bs.face;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.mrprez.gencross.export.FileGenerator;
import com.mrprez.gencross.export.TemplatedFileGenerator;
import com.mrprez.gencross.web.bo.MultiExportBO;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;

public interface IExportBS {

	
	byte[] export(PersonnageWorkBO personnageWork, FileGenerator fileGenerator) throws Exception;

	byte[] export(PersonnageWorkBO personnageWork, TemplatedFileGenerator fileGenerator, String templateName) throws Exception;
	
	byte[] export(PersonnageWorkBO personnageWork, TemplatedFileGenerator fileGenerator, File templateFile) throws Exception;
	
	Map<Class<? extends TemplatedFileGenerator>, List<String>> getTemplateFiles(String pluginName) throws Exception;

	FileGenerator getGenerator(String className) throws Exception;

	MultiExportBO multiExportInGrid(Collection<Integer> personnageIdList, UserBO user)throws Exception;
	
	byte[] multiExport(Collection<Integer> personnageIdList, UserBO user, FileGenerator fileGenerator)throws Exception;

	byte[] multiExport(Collection<Integer> personnageIdList, UserBO user, TemplatedFileGenerator fileGenerator, File templateFile)throws Exception;

	byte[] multiExport(Collection<Integer> personnageIdList, UserBO user, TemplatedFileGenerator fileGenerator, String pluginName, String selectedTemplate)throws Exception;
	

}
