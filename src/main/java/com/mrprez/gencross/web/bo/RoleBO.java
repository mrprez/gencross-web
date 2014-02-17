package com.mrprez.gencross.web.bo;

public class RoleBO {
	public static final String USER = "user";
	
	
	private String name;

	
	public RoleBO() {
		super();
	}
	public RoleBO(String name) {
		super();
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object object) {
		if(object instanceof RoleBO){
			if(name==null){
				return ((RoleBO)object).getName()==null;
			}
			return name.equals(((RoleBO)object).getName());
		}
		return false;
	}
	@Override
	public int hashCode() {
		if(name==null){
			return 0;
		}
		return name.hashCode();
	}
	
	

}
