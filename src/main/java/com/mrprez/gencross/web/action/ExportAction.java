package com.mrprez.gencross.web.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mrprez.gencross.export.FileGenerator;
import com.mrprez.gencross.export.TemplatedFileGenerator;
import com.mrprez.gencross.web.action.util.SessionUtil;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bs.face.IExportBS;
import com.opensymphony.xwork2.ActionSupport;

public class ExportAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private static String OUTPUT_FILE_NAME = "export";
	
	private File templateFile;
	private String fileGeneratorName;
	private String selectedTemplate;
	private Integer personnageId;
	private String fileName;
	private Integer fileSize;
	private InputStream inputStream;
	private Map<Class<? extends TemplatedFileGenerator>, List<String>> templateFiles;
	
	private IExportBS exportBS;
	
	
	public String execute() throws Exception {
		templateFiles = new HashMap<Class<? extends TemplatedFileGenerator>, List<String>>();
		Map<Class<? extends TemplatedFileGenerator>, List<String>> originTemplateFiles = exportBS.getTemplateFiles();
		for(Class<? extends TemplatedFileGenerator> clazz : originTemplateFiles.keySet()){
			templateFiles.put(clazz, new ArrayList<String>(originTemplateFiles.get(clazz)));
			templateFiles.get(clazz).add("Uploader un fichier");
		}
		
		return INPUT;
	}
	
	public String export() throws Exception {
		PersonnageWorkBO personnageWork = SessionUtil.getPersonnageInSession(personnageId);
		if(personnageWork==null){
			return ERROR;
		}
		FileGenerator fileGenerator = exportBS.getGenerator(fileGeneratorName);
		if(fileGenerator==null){
			super.addActionError("Type d'export introuvable.");
			return ERROR;
		}
		
		byte export[];
		if(selectedTemplate!=null){
			if(selectedTemplate.equals("Uploader un fichier")){
				export = exportBS.export(personnageWork, (TemplatedFileGenerator)fileGenerator, templateFile);
			}else{
				export = exportBS.export(personnageWork, (TemplatedFileGenerator)fileGenerator, selectedTemplate);
			}
		}else{
			export = exportBS.export(personnageWork, fileGenerator);
		}
		
		fileSize = export.length;
		inputStream = new ByteArrayInputStream(export);
		fileName = OUTPUT_FILE_NAME+"."+fileGenerator.getOutputExtension();
		
		return SUCCESS;
	}


	public Map<String, Class<? extends FileGenerator>> getGeneratorList() {
		return FileGenerator.getGeneratorList();
	}
	public File getTemplateFile() {
		return templateFile;
	}
	public void setTemplateFile(File templateFile) {
		this.templateFile = templateFile;
	}
	public String getFileGeneratorName() {
		return fileGeneratorName;
	}
	public void setFileGeneratorName(String fileGeneratorName) {
		this.fileGeneratorName = fileGeneratorName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public Integer getFileSize() {
		return fileSize;
	}
	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}
	public Map<Class<? extends TemplatedFileGenerator>, List<String>> getTemplateFiles() {
		return templateFiles;
	}
	public String getSelectedTemplate() {
		return selectedTemplate;
	}
	public void setSelectedTemplate(String selectedTemplate) {
		this.selectedTemplate = selectedTemplate;
	}
	public Integer getPersonnageId() {
		return personnageId;
	}
	public void setPersonnageId(Integer personnageId) {
		this.personnageId = personnageId;
	}

	public IExportBS getExportBS() {
		return exportBS;
	}

	public void setExportBS(IExportBS exportBS) {
		this.exportBS = exportBS;
	}
	
	
	

}
