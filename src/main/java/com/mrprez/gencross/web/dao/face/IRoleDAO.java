package com.mrprez.gencross.web.dao.face;

import com.mrprez.gencross.web.bo.RoleBO;

public interface IRoleDAO extends IAbstractDAO {
	
	public RoleBO getRole(String name) throws Exception;

}
