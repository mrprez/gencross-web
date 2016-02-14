package com.mrprez.gencross.web.bs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.export.FileGenerator;
import com.mrprez.gencross.export.TemplatedFileGenerator;
import com.mrprez.gencross.web.bo.MultiExportBO;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IExportBS;
import com.mrprez.gencross.web.dao.face.IPersonnageDAO;
import com.mrprez.gencross.web.dao.face.ITemplateFileResource;

public class ExportBS implements IExportBS {
	private static String GENERATOR_PACKAGE = "com.mrprez.gencross.export";
	
	private ITemplateFileResource templateFileResource;
	private IPersonnageDAO personnageDAO;
	
	
	
	@Override
	public byte[] export(PersonnageWorkBO personnageWork, FileGenerator fileGenerator) throws Exception{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		fileGenerator.write(personnageWork.getPersonnage(), baos);
		return baos.toByteArray();
	}
	
	@Override
	public byte[] export(PersonnageWorkBO personnageWork, TemplatedFileGenerator fileGenerator, String templateName) throws Exception {
		File templateFile = templateFileResource.getTemplate(fileGenerator.getClass(), personnageWork.getPluginName(), templateName);
		return export(personnageWork, fileGenerator, templateFile);
	}
	
	@Override
	public byte[] export(PersonnageWorkBO personnageWork, TemplatedFileGenerator fileGenerator, File templateFile) throws Exception{
		fileGenerator.setTemplate(templateFile);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		fileGenerator.write(personnageWork.getPersonnage(), baos);
		return baos.toByteArray();
	}
	
	@Override
	public FileGenerator getGenerator(String className) throws Exception {
		if(className.startsWith(GENERATOR_PACKAGE)){
			try {
				return Class.forName(className).asSubclass(FileGenerator.class).newInstance();
			} catch (ClassNotFoundException e) {
				return null;
			}
		}else{
			try {
				return Class.forName(GENERATOR_PACKAGE+"."+className).asSubclass(FileGenerator.class).newInstance();
			} catch (ClassNotFoundException e) {
				return null;
			}
		}
	}
	
	
	@Override
	public byte[] multiExport(Collection<Integer> personnageIdList, UserBO user, TemplatedFileGenerator fileGenerator, String pluginName, String selectedTemplate) throws Exception {
		File template = templateFileResource.getTemplate(fileGenerator.getClass(), pluginName, selectedTemplate);
		return multiExport(personnageIdList, user, fileGenerator, template);
	}
	
	
	@Override
	public byte[] multiExport(Collection<Integer> personnageIdList, UserBO user, TemplatedFileGenerator fileGenerator,  File templateFile) throws Exception {
		fileGenerator.setTemplate(templateFile);
		return multiExport(personnageIdList, user, fileGenerator);
	}

	
	@Override
	public byte[] multiExport(Collection<Integer> personnageIdList, UserBO user, FileGenerator fileGenerator) throws Exception {
		List<PersonnageWorkBO> personnageList = new ArrayList<PersonnageWorkBO>(personnageIdList.size());
		for(Integer personnageId : personnageIdList){
			PersonnageWorkBO personnageWork = personnageDAO.loadPersonnageWork(personnageId);
			if( user.equals(personnageWork.getPlayer()) || user.equals(personnageWork.getGameMaster())){
				personnageList.add(personnageWork);
			}
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		
		try{
			for(PersonnageWorkBO personnageWork : personnageList){
				zos.putNextEntry(new ZipEntry(personnageWork.getName()+"_"+personnageWork.getId()+"."+fileGenerator.getOutputExtension()));
				fileGenerator.write(personnageWork.getPersonnage(), zos);
				zos.closeEntry();
			}
		}finally{
			zos.close();
		}
		
		return baos.toByteArray();
	}
	
	@Override
	public MultiExportBO multiExportInGrid(Collection<Integer> personnageIdList, UserBO user) throws Exception{
		List<Personnage> personnageList = new ArrayList<Personnage>(personnageIdList.size());
		for(Integer personnageId : personnageIdList){
			PersonnageWorkBO personnageWork = personnageDAO.loadPersonnageWork(personnageId);
			if( user.equals(personnageWork.getPlayer()) || user.equals(personnageWork.getGameMaster())){
				personnageList.add(personnageWork.getPersonnage());
			}
		}
		MultiExportBO multiExportResult = new MultiExportBO(personnageList);
		
		
		for(Property property : personnageList.get(0).getProperties()){
			List<Property> propertyList = new ArrayList<Property>();
			for(Personnage personnage : personnageList){
				propertyList.add(personnage.getProperty(property.getAbsoluteName()));
			}
			multiExportResult.addFixProperty(property.getAbsoluteName(), propertyList);
		}
		
		return multiExportResult;
	}
	
	

	
	
	@Override
	public Map<Class<? extends TemplatedFileGenerator>, List<String>> getTemplateFiles(String pluginName) throws Exception {
		return templateFileResource.getTemplates(pluginName);
	}

	public IPersonnageDAO getPersonnageDAO() {
		return personnageDAO;
	}

	public void setPersonnageDAO(IPersonnageDAO personnageDAO) {
		this.personnageDAO = personnageDAO;
	}

	public ITemplateFileResource getTemplateFileResource() {
		return templateFileResource;
	}

	public void setTemplateFileResource(ITemplateFileResource templateFileResource) {
		this.templateFileResource = templateFileResource;
	}

		

}
