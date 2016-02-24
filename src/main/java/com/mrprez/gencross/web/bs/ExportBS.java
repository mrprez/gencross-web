package com.mrprez.gencross.web.bs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.PropertyOwner;
import com.mrprez.gencross.export.FileGenerator;
import com.mrprez.gencross.export.TemplatedFileGenerator;
import com.mrprez.gencross.web.bo.MultiExportBO;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IExportBS;
import com.mrprez.gencross.web.dao.face.IPersonnageDAO;
import com.mrprez.gencross.web.dao.face.ITemplateFileResource;

import edu.emory.mathcs.backport.java.util.Arrays;

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
	
	
	/**
	 * Exporte dans une tableau synthétique les propriétés de tous les personnages. 
	 */
	@Override
	public MultiExportBO multiExportInGrid(Collection<Integer> personnageIdList, UserBO user) throws Exception{
		List<Personnage> personnageList = new ArrayList<Personnage>(personnageIdList.size());
		for(Integer personnageId : personnageIdList){
			PersonnageWorkBO personnageWork = personnageDAO.loadPersonnageWork(personnageId);
			if( user.equals(personnageWork.getPlayer()) || user.equals(personnageWork.getGameMaster())){
				personnageList.add(personnageWork.getPersonnage());
			}
		}
		Personnage[] personnageTab = personnageList.toArray(new Personnage[personnageList.size()]);
		
		MultiExportBO multiExportResult = new MultiExportBO(personnageTab);
		
		Iterator<Property>[] iterators = getSubPropertiesIterators(personnageTab);
		Property[] emptyTab = new Property[personnageTab.length];
		Property[] subPropertyTab;
		while( ! Arrays.equals(subPropertyTab=nextPropertyTab(iterators), emptyTab) ){
			addFixProperty(subPropertyTab, multiExportResult);
		}
		
		return multiExportResult;
	}
	
	
	/**
	 * Ajoute au tableau synthétique la propriété commune à tous les personnages et les éventuelles sous propriétés de celle-ci.
	 * Ces dernières peuvent ne pas être des propriétés communes.
	 * La méthode ouvre un tableau d'itérateurs sur les sous propriétés et itère dessus. Pour chaque itération:
	 * - soit toutes les propriétés sont !=null et ont le même nom, et dans ce cas on rappelle cette méthode avec un nouveau tableau de propriétés issu de cette itération
	 * - sinon on ajoute unitairement toutes les propriétés non null de cette itération
	 * @param propertyTab
	 * @param multiExportResult
	 * @throws IOException
	 */
	private void addFixProperty(Property[] propertyTab, MultiExportBO multiExportResult) throws IOException{
		multiExportResult.addFullLine(propertyTab[0].getAbsoluteName(), propertyTab);
		
		Iterator<Property>[] iterators = getSubPropertiesIterators(propertyTab);
		while(hasNext(iterators)){
			Property[] subPropertyTab = nextPropertyTab(iterators);
			if(isFixProperty(subPropertyTab)){
				addFixProperty(subPropertyTab, multiExportResult);
			}else{
				for(Property subProperty : subPropertyTab){
					if(subProperty!=null){
						addOptionnalProperty(subProperty, multiExportResult);
					}
				}
			}
		}
	}
	
	
	private boolean hasNext(Iterator<Property>[] iterators){
		for(Iterator<Property> iterator : iterators){
			if(iterator!=null && iterator.hasNext()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Appelle la méthode next pour chaque itérateur et construit un tableau à partir de ces résultats.
	 * Pour les itérateur null ou ayant atteint leur limite, on laisse l'élément correspondant du tableau à null.
	 * @param iterators
	 * @return le tableau de résultat de l'appelle de la méthode next() pour chaque itérateur.
	 */
	private Property[] nextPropertyTab(Iterator<Property>[] iterators){
		Property[] propertyTab = new Property[iterators.length];
		for(int i=0; i<iterators.length; i++){
			if(iterators[i]!=null && iterators[i].hasNext()){
				propertyTab[i] = iterators[i].next();
			}
		}
		return propertyTab;
	}
	
	/**
	 * @param propertyTab tableau des Property à comparer
	 * @return true si aucune Property du tableau n'est null et que chaque Property à la même nom (fullName). False sinon.
	 */
	private boolean isFixProperty(Property[] propertyTab){
		String propertyName = null;
		for(Property property : propertyTab){
			if(property==null){
				return false;
			}
			if(propertyName==null){
				propertyName = property.getFullName();
			}
			if( ! propertyName.equals(property.getFullName()) ){
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * Construit un tableau d'itérateurs sur les sous propriétés des PropertyOwner passé en paramêtre.
	 * Attention, les Property (qui implémentent PropertyOwner) renvoie un itérateur null s'il n'a pas de de liste de sous propriétés.
	 * @param propertyOwnerTab
	 * @return
	 */
	private Iterator<Property>[] getSubPropertiesIterators(PropertyOwner[] propertyOwnerTab){
		@SuppressWarnings("unchecked")
		Iterator<Property>[] iterators = new Iterator[propertyOwnerTab.length];
		for(int i=0; i<propertyOwnerTab.length; i++){
			iterators[i] = propertyOwnerTab[i].iterator();
		}
		
		return iterators;
	}
	
	/**
	 * Ajoute une propriété non commune au tableau synthétique d'export des personnages.
	 * @param property la propriété à ajouter
	 * @param multiExportResult
	 * @throws IOException
	 */
	private void addOptionnalProperty(Property property, MultiExportBO multiExportResult) throws IOException{
		multiExportResult.addSimpleElement(property);
		if(property.getSubProperties()!=null){
			for(Property subProperty : property.getSubProperties()){
				addOptionnalProperty(subProperty, multiExportResult);
			}
		}
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
