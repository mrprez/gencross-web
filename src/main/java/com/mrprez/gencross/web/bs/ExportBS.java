package com.mrprez.gencross.web.bs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.export.FileGenerator;
import com.mrprez.gencross.export.TemplatedFileGenerator;
import com.mrprez.gencross.util.HtmlToText;
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
	public List<String[]> multiExportInGrid(Collection<Integer> personnageIdList, UserBO user) throws Exception{
		List<Personnage> personnageList = new ArrayList<Personnage>(personnageIdList.size());
		for(Integer personnageId : personnageIdList){
			PersonnageWorkBO personnageWork = personnageDAO.loadPersonnageWork(personnageId);
			if( user.equals(personnageWork.getPlayer()) || user.equals(personnageWork.getGameMaster())){
				personnageList.add(personnageWork.getPersonnage());
			}
		}
		
		List<String[]> result = new ArrayList<String[]>();
		for(Property property : personnageList.get(0).getProperties()){
			List<Property> propertyList = new ArrayList<Property>();
			for(Personnage personnage : personnageList){
				propertyList.add(personnage.getProperty(property.getAbsoluteName()));
			}
			result.addAll(treatFixProperty(propertyList, 0));
		}
		
		return result;
	}
	
	private List<String[]> treatFixProperty(List<Property> propertyList, int depth) throws IOException{
		List<String[]> result = new ArrayList<String[]>();
		String line[] = new String[propertyList.size() + 1];
		
		line[0] = StringUtils.repeat("  ", depth) + propertyList.get(0).getFullName();
		
		int index = 0;
		for(Property property : propertyList){
			index++;
			line[index] = property.getRenderer().displayValue(property);
			if(line[index].startsWith("<html>")){
				HtmlToText htmlToText = new HtmlToText();
				htmlToText.parse(line[index]);
				line[index] = htmlToText.getString();
			}
		}
		result.add(line);
		
		int maxPropertiesListSize = 0;
		for(Property property : propertyList){
			if(property.getSubProperties()!=null && property.getSubProperties().size()>maxPropertiesListSize){
				maxPropertiesListSize = property.getSubProperties().size();
			}
		}
		
		if(maxPropertiesListSize>0){
			if(hasFixSubProperties(propertyList)){
				for(int i=0; i<maxPropertiesListSize; i++){
					List<Property> subPropertyList = new ArrayList<Property>();
					for(Property property : propertyList){
						subPropertyList.add(property.getSubProperties().get(i));
					}
					result.addAll(treatFixProperty(subPropertyList, depth+1));
				}
			}else{
				for(int i=0; i<maxPropertiesListSize; i++){
					List<Property> subPropertyList = new ArrayList<Property>();
					for(Property property : propertyList){
						if(property!=null && property.getSubProperties()!=null && i<property.getSubProperties().size()){
							subPropertyList.add(property.getSubProperties().get(i));
						}else{
							subPropertyList.add(null);
						}
					}
					result.addAll(treatMovingProperties(subPropertyList, 0));
				}
			}
		}
		
		return result;
	}
	
	
	/**
	 * Compare subPropertiesList of all Property in propertyList argument.
	 * If each subPropertiesList contains the same property (if they have the same name in the same order), return true,
	 * otherwise return false; 
	 * @param propertyList
	 * @return true if all subPropertiesList contains the same properties, false otherwise.
	 */
	private boolean hasFixSubProperties(List<Property> propertyList){
		int maxPropertiesListSize = 0;
		for(Property property : propertyList){
			if(property.getSubProperties() == null || property.getSubProperties().isEmpty()){
				return false;
			}
			if(maxPropertiesListSize == 0){
				maxPropertiesListSize = property.getSubProperties().size();
			}else if(property.getSubProperties().size() != maxPropertiesListSize){
				return false;
			}
		}
		
		for(int i=0; i<maxPropertiesListSize; i++){
			String propertyName = null;
			for(Property property : propertyList){
				if(propertyName==null){
					propertyName = property.getSubProperties().get(i).getAbsoluteName();
				}else if(!propertyName.equals(property.getSubProperties().get(i).getAbsoluteName())){
					return false;
				}
			}
		}
		return true;
	}
	
	private List<String[]> treatMovingProperties(List<Property> propertyList, int depth) throws IOException{
		List<String[]> result = new ArrayList<String[]>();
		String line[] = new String[propertyList.size() + 1];
		
		if(hasSameNameOrNull(propertyList)){
			// TODO line[0] = StringUtils.repeat("  ", depth) + ....getFullName();
			int index = 0;
			for(Property property : propertyList){
				index++;
				line[index] = property.getRenderer().displayValue(property);
				if(line[index].startsWith("<html>")){
					HtmlToText htmlToText = new HtmlToText();
					htmlToText.parse(line[index]);
					line[index] = htmlToText.getString();
				}
			}
			result.add(line);
		}else{
			int index = 0;
			for(Property property : propertyList){
				index++;
				if(property != null){
					line[index] = property.getText();
				}
			}
			result.add(line);
		}
		
		int maxPropertiesListSize = 0;
		for(Property property : propertyList){
			if(property!=null && property.getSubProperties()!=null && property.getSubProperties().size()>maxPropertiesListSize){
				maxPropertiesListSize = property.getSubProperties().size();
			}
		}
		
		if(maxPropertiesListSize > 0){
			for(int i=0; i<maxPropertiesListSize; i++){
				List<Property> subPropertyList = new ArrayList<Property>();
				for(Property property : propertyList){
					if(property!=null && property.getSubProperties()!=null && i<property.getSubProperties().size()){
						subPropertyList.add(property.getSubProperties().get(i));
					}else{
						subPropertyList.add(null);
					}
				}
				result.addAll(treatMovingProperties(subPropertyList, depth+1));
			}
		}
		
		return result;
	}
	
	
	private boolean hasSameNameOrNull(List<Property> propertyList){
		String name = null;
		for(Property property : propertyList){
			if(property!=null){
				if(name==null){
					name = property.getFullName();
				}else if( ! name.equals(property.getFullName())){
					return false;
				}
			}
		}
		return true;
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
