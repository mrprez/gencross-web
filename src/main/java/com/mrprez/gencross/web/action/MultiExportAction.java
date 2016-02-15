package com.mrprez.gencross.web.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.mrprez.gencross.export.DrawerGenerator;
import com.mrprez.gencross.export.FileGenerator;
import com.mrprez.gencross.export.TemplatedFileGenerator;
import com.mrprez.gencross.web.action.util.ClassNameComparator;
import com.mrprez.gencross.web.bo.MultiExportBO;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IExportBS;
import com.mrprez.gencross.web.bs.face.ITableBS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class MultiExportAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private static String CSV_OUTPUT_FILE_NAME = "multiExport.csv";
	private static String ZIP_OUTPUT_FILE_NAME = "multiExport.zip";
	
	private Integer tableId;
	private TableBO table;
	private Map<Integer, String> pjList;
	private Map<Integer, String> pnjList;
	private String exportedPjList;
	private String exportedPnjList;
	private Map<Class<? extends TemplatedFileGenerator>, List<String>> templateFiles;
	private String fileGeneratorName = DrawerGenerator.class.getSimpleName();
	private String selectedTemplate;
	private File templateFile;
	
	private String fileName;
	private Integer fileSize;
	private InputStream inputStream;
	private MultiExportBO export;
	
	private ITableBS tableBS;
	private IExportBS exportBS;
	
	
	@Override
	public String execute() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		table = tableBS.getTableForGM(tableId, user);
		if(table==null){
			addActionError("Impossible de charger cette table");
			return ERROR;
		}
		
		pjList = new TreeMap<Integer, String>();
		pnjList = new TreeMap<Integer, String>();
		for(PersonnageWorkBO personnageWork : table.getPersonnages()){
			if(personnageWork.getPlayer()!=null){
				pjList.put(personnageWork.getId(), personnageWork.getName()+" ("+personnageWork.getPlayer().getUsername()+")");
			}else{
				pnjList.put(personnageWork.getId(), personnageWork.getName());
			}
		}
		
		templateFiles = new TreeMap<Class<? extends TemplatedFileGenerator>, List<String>>(new ClassNameComparator());
		Map<Class<? extends TemplatedFileGenerator>, List<String>> originTemplateFiles = exportBS.getTemplateFiles(table.getType());
		for(Class<? extends TemplatedFileGenerator> clazz : originTemplateFiles.keySet()){
			templateFiles.put(clazz, new ArrayList<String>(originTemplateFiles.get(clazz)));
			templateFiles.get(clazz).add("Uploader un fichier");
		}
		
		return INPUT;
	}
	
	public String exportCsv() throws Exception {
		String exportResult = export();
		if(!exportResult.equals(SUCCESS)){
			return exportResult;
		}
		
		StringBuilder resultBuilder = new StringBuilder();
		for(MultiExportBO.MultiExportLine line : export.getLines()){
			if(line.getTitle()!=null){
				resultBuilder.append(line.getTitle());
			}
			resultBuilder.append(";");
			for(String value : line.getValues()){
				if(value!=null){
					resultBuilder.append(value);
				}
				resultBuilder.append(";");
			}
			resultBuilder.append("\n");
		}
		byte csvContent[] = resultBuilder.toString().getBytes("ISO-8859-1");
		fileSize = csvContent.length;
		inputStream = new ByteArrayInputStream(csvContent);
		fileName = CSV_OUTPUT_FILE_NAME;
		
		return "file";
	}
	
	
	public String exportZip() throws Exception{
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		table = tableBS.getTableForGM(tableId, user);
		if(table==null){
			addActionError("Impossible de charger cette table");
			return ERROR;
		}
		
		if((exportedPjList==null || exportedPjList.isEmpty()) && (exportedPnjList==null || exportedPnjList.isEmpty())){
			addActionError("Vous devez selectionner au moins un personnage.");
			return execute();
		}
		
		List<Integer> personnageIdList = new ArrayList<Integer>();
		if(exportedPjList!=null && exportedPjList.length()>0){
			String exportedPjIdTab[] = exportedPjList.split(",");
			for(int i=0; i<exportedPjIdTab.length; i++){
				personnageIdList.add(Integer.valueOf(exportedPjIdTab[i].trim()));
			}
		}
		if(exportedPnjList!=null && exportedPnjList.length()>0){
			String exportedPnjIdTab[] = exportedPnjList.split(",");
			for(int i=0; i<exportedPnjIdTab.length; i++){
				personnageIdList.add(Integer.valueOf(exportedPnjIdTab[i].trim()));
			}
		}
		
		FileGenerator fileGenerator = exportBS.getGenerator(fileGeneratorName);
		if(fileGenerator==null){
			super.addActionError("Type d'export introuvable.");
			return ERROR;
		}
		
		byte export[];
		if(selectedTemplate!=null){
			if(selectedTemplate.equals("Uploader un fichier")){
				export = exportBS.multiExport(personnageIdList, user, (TemplatedFileGenerator)fileGenerator, templateFile);
			}else{
				export = exportBS.multiExport(personnageIdList, user, (TemplatedFileGenerator)fileGenerator, table.getType(), selectedTemplate);
			}
		}else{
			export = exportBS.multiExport(personnageIdList, user, fileGenerator);
		}
		
		fileSize = export.length;
		inputStream = new ByteArrayInputStream(export);
		fileName = ZIP_OUTPUT_FILE_NAME;
		
		return "file";
	}
	
	
	public String export() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		table = tableBS.getTableForGM(tableId, user);
		if(table==null){
			addActionError("Impossible de charger cette table");
			return ERROR;
		}
		
		if((exportedPjList==null || exportedPjList.isEmpty()) && (exportedPnjList==null || exportedPnjList.isEmpty())){
			addActionError("Vous devez selectionner au moins un personnage.");
			return execute();
		}
		
		List<Integer> personnageIdList = new ArrayList<Integer>();
		if(exportedPjList!=null && exportedPjList.length()>0){
			String exportedPjIdTab[] = exportedPjList.split(",");
			for(int i=0; i<exportedPjIdTab.length; i++){
				personnageIdList.add(Integer.valueOf(exportedPjIdTab[i].trim()));
			}
		}
		if(exportedPnjList!=null && exportedPnjList.length()>0){
			String exportedPnjIdTab[] = exportedPnjList.split(",");
			for(int i=0; i<exportedPnjIdTab.length; i++){
				personnageIdList.add(Integer.valueOf(exportedPnjIdTab[i].trim()));
			}
		}
		
		export = exportBS.multiExportInGrid(personnageIdList, user);
		
		return SUCCESS;
	}

	public Map<String, Class<? extends FileGenerator>> getGeneratorList() {
		return FileGenerator.getGeneratorList();
	}
	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	public TableBO getTable() {
		return table;
	}

	public void setTable(TableBO table) {
		this.table = table;
	}

	public Map<Integer, String> getPjList() {
		return pjList;
	}

	public void setPjList(Map<Integer, String> pjList) {
		this.pjList = pjList;
	}

	public Map<Integer, String> getPnjList() {
		return pnjList;
	}

	public void setPnjList(Map<Integer, String> pnjList) {
		this.pnjList = pnjList;
	}

	public String getExportedPjList() {
		return exportedPjList;
	}

	public void setExportedPjList(String exportedPjList) {
		this.exportedPjList = exportedPjList;
	}
	
	public String getExportedPnjList() {
		return exportedPnjList;
	}

	public void setExportedPnjList(String exportedPnjList) {
		this.exportedPnjList = exportedPnjList;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getFileSize() {
		return fileSize;
	}

	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public MultiExportBO getExport() {
		return export;
	}

	public ITableBS getTableBS() {
		return tableBS;
	}

	public void setTableBS(ITableBS tableBS) {
		this.tableBS = tableBS;
	}

	public IExportBS getExportBS() {
		return exportBS;
	}

	public void setExportBS(IExportBS exportBS) {
		this.exportBS = exportBS;
	}

	public String getFileGeneratorName() {
		return fileGeneratorName;
	}

	public void setFileGeneratorName(String fileGeneratorName) {
		this.fileGeneratorName = fileGeneratorName;
	}

	public File getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(File templateFile) {
		this.templateFile = templateFile;
	}
	
	public Map<Class<? extends TemplatedFileGenerator>, List<String>> getTemplateFiles(){
		return templateFiles;
	}

	public String getSelectedTemplate() {
		return selectedTemplate;
	}

	public void setSelectedTemplate(String selectedTemplate) {
		this.selectedTemplate = selectedTemplate;
	}
	

}
