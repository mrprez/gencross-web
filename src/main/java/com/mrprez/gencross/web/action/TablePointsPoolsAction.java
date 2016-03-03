package com.mrprez.gencross.web.action;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.mrprez.gencross.PoolPoint;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.ITableBS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class TablePointsPoolsAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private TableBO table;
	private SortedSet<PersonnageWorkBO> pjList;
	private Collection<String> pointPoolList;
	private String pointPoolName;
	private String pointPoolModification;
	
	private ITableBS tableBS;
	
	
	@Override
	public String execute() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		table = tableBS.getTableForGM(id, user);
		if(table==null){
			addActionError("Impossible de charger cette table");
			return ERROR;
		}
		
		pjList = new TreeSet<PersonnageWorkBO>(new PersonnageWorkComparator());
		pjList.addAll(tableBS.getPjList(id));
		pointPoolList = tableBS.getPointPoolList(id);
		
		return INPUT;
	}
	
	public Map<String,int[]> findMinMaxPjPoints(){
		LinkedHashMap<String,int[]> result = new LinkedHashMap<String,int[]>();
		for(String poolName : pointPoolList){
			result.put(poolName, new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE});
		}
		for(PersonnageWorkBO pj : pjList){
			for(PoolPoint poolPoint : pj.getPersonnage().getPointPools().values()){
				if(result.get(poolPoint.getName())[0] > poolPoint.getTotal()){
					result.get(poolPoint.getName())[0] = poolPoint.getTotal();
				}
				if(result.get(poolPoint.getName())[1] < poolPoint.getTotal()){
					result.get(poolPoint.getName())[1] = poolPoint.getTotal();
				}
			}
		}
		return result;
	}
	
	public String addPoints() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		try{
			int addedPoints = Integer.parseInt(pointPoolModification.trim());
			String error = tableBS.addPointsToPj(id, user, pointPoolName, addedPoints);
			if(error!=null){
				addActionError(error);
				return execute();
			}
		}catch (NumberFormatException nfe) {
			addActionError("Nombre invalide: \""+pointPoolModification+"\"");
			return execute();
		}
		
		return SUCCESS;
	}
	
	

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public TableBO getTable() {
		return table;
	}
	public void setTable(TableBO table) {
		this.table = table;
	}
	public Collection<PersonnageWorkBO> getPjList() {
		return pjList;
	}
	public void setPjList(SortedSet<PersonnageWorkBO> pjList) {
		this.pjList = pjList;
	}
	public String getPointPoolName() {
		return pointPoolName;
	}
	public void setPointPoolName(String pointPoolName) {
		this.pointPoolName = pointPoolName;
	}
	public String getPointPoolModification() {
		return pointPoolModification;
	}
	public void setPointPoolModification(String pointPoolModification) {
		this.pointPoolModification = pointPoolModification;
	}
	public Collection<String> getPointPoolList() {
		return pointPoolList;
	}
	public void setPointPoolList(Collection<String> pointPoolList) {
		this.pointPoolList = pointPoolList;
	}
	
	public ITableBS getTableBS() {
		return tableBS;
	}

	public void setTableBS(ITableBS tableBS) {
		this.tableBS = tableBS;
	}


	private class PersonnageWorkComparator implements Comparator<PersonnageWorkBO> {
		@Override
		public int compare(PersonnageWorkBO arg0, PersonnageWorkBO arg1) {
			return arg0.getName().compareTo(arg1.getName());
		}
	}



}
