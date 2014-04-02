package com.mrprez.gencross.web.bs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.export.FileGenerator;
import com.mrprez.gencross.export.TemplatedFileGenerator;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IExportBS;
import com.mrprez.gencross.web.dao.face.IPersonnageDAO;
import com.mrprez.gencross.web.dao.face.ITemplateFileResource;

public class ExportBS implements IExportBS {
	private static String GENERATOR_PACKAGE = "com.mrprez.gencross.export";
	
	private Set<Class<? extends TemplatedFileGenerator>> templatedFileGeneratorList = new HashSet<Class<? extends TemplatedFileGenerator>>();
	private Map<Class<? extends TemplatedFileGenerator>, List<String>> templateFiles = new HashMap<Class<? extends TemplatedFileGenerator>, List<String>>();
	
	private ITemplateFileResource templateFileResource;
	private IPersonnageDAO personnageDAO;
	
	
	public ExportBS(ITemplateFileResource templateFileResource) throws Exception{
		this.templateFileResource = templateFileResource;
		
		for(Class<? extends FileGenerator> fileGeneratorClass : FileGenerator.getGeneratorList().values()){
			if(TemplatedFileGenerator.class.isAssignableFrom(fileGeneratorClass)){
				templatedFileGeneratorList.add(fileGeneratorClass.asSubclass(TemplatedFileGenerator.class));
				
				List<String> templateFileList = new ArrayList<String>();
				File fileTab[] = templateFileResource.getTemplateFiles(fileGeneratorClass.asSubclass(TemplatedFileGenerator.class));
				if(fileTab!=null){
					for(int i=0; i<fileTab.length; i++){
						templateFileList.add(fileTab[i].getName());
					}
				}
				templateFiles.put(fileGeneratorClass.asSubclass(TemplatedFileGenerator.class), templateFileList);
			}
		}
	}
	
	
	@Override
	public byte[] export(PersonnageWorkBO personnageWork, FileGenerator fileGenerator) throws Exception{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		fileGenerator.write(personnageWork.getPersonnage(), baos);
		return baos.toByteArray();
	}
	
	@Override
	public byte[] export(PersonnageWorkBO personnageWork, TemplatedFileGenerator fileGenerator, String templateName) throws Exception{
		File templateFile = templateFileResource.getTemplateFile(fileGenerator.getClass(), templateName);
		fileGenerator.setTemplate(templateFile);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		fileGenerator.write(personnageWork.getPersonnage(), baos);
		return baos.toByteArray();
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
	public List<String[]> multiExport(Collection<Integer> personnageIdList, UserBO user) throws Exception{
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
	
	private List<String[]> treatFixProperty(List<Property> propertyList, int depth){
		List<String[]> result = new ArrayList<String[]>();
		String line[] = new String[propertyList.size() + 1];
		
		line[0] = StringUtils.repeat("  ", depth) + propertyList.get(0).getFullName();
		
		int index = 0;
		for(Property property : propertyList){
			index++;
			line[index] = property.getRenderer().displayValue(property);
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
	
	private List<String[]> treatMovingProperties(List<Property> propertyList, int depth){
		List<String[]> result = new ArrayList<String[]>();
		String line[] = new String[propertyList.size() + 1];
		
		int index = 0;
		for(Property property : propertyList){
			index++;
			if(property != null){
				line[index] = property.getText();
			}
		}
		result.add(line);
		
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
	
	
	@Override
	public Set<Class<? extends TemplatedFileGenerator>> getTemplatedFileGeneratorList() {
		return templatedFileGeneratorList;
	}

	@Override
	public Map<Class<? extends TemplatedFileGenerator>, List<String>> getTemplateFiles() {
		return templateFiles;
	}

	public IPersonnageDAO getPersonnageDAO() {
		return personnageDAO;
	}

	public void setPersonnageDAO(IPersonnageDAO personnageDAO) {
		this.personnageDAO = personnageDAO;
	}
	
	
	
	

}
