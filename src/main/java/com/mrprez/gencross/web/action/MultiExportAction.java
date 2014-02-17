package com.mrprez.gencross.web.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.web.context.ContextLoader;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IExportBS;
import com.mrprez.gencross.web.bs.face.ITableBS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class MultiExportAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private static String OUTPUT_FILE_NAME = "multiExport.csv";
	
	private Integer tableId;
	private TableBO table;
	private Map<Integer, String> pjList;
	private Map<Integer, String> pnjList;
	private String exportedPjList;
	private String exportedPnjList;
	private String fileName;
	private Integer fileSize;
	private InputStream inputStream;
	private List<String[]> export;
	
	
	@Override
	public String execute() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		ITableBS tableBS = (ITableBS)ContextLoader.getCurrentWebApplicationContext().getBean("tableBS");
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
		
		return INPUT;
	}
	
	public String exportCsv() throws Exception {
		String exportResult = export();
		if(!exportResult.equals(SUCCESS)){
			return exportResult;
		}
		
		StringBuilder resultBuilder = new StringBuilder();
		for(String line[] : export){
			for(int i=0; i<line.length; i++){
				if(line[i]!=null){
					resultBuilder.append(line[i]);
				}
				resultBuilder.append(";");
			}
			resultBuilder.append("\n");
		}
		byte csvContent[] = resultBuilder.toString().getBytes("ISO-8859-1");
		fileSize = csvContent.length;
		inputStream = new ByteArrayInputStream(csvContent);
		fileName = OUTPUT_FILE_NAME;
		
		return "file";
	}
	
	
	public String export() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		IExportBS exportBS = (IExportBS)ContextLoader.getCurrentWebApplicationContext().getBean("exportBS");
		ITableBS tableBS = (ITableBS)ContextLoader.getCurrentWebApplicationContext().getBean("tableBS");
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
		
		export = exportBS.multiExport(personnageIdList, user);
		
		return SUCCESS;
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

	public List<String[]> getExport() {
		return export;
	}
	
	

}
