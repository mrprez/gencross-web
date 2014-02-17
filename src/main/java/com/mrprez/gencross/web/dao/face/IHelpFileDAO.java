package com.mrprez.gencross.web.dao.face;

import java.io.InputStream;

public interface IHelpFileDAO {
	
	
	public InputStream getHelpFileInputStream(String helpFileName) throws Exception;
	
	public long getHelpFileLength(String helpFileName) throws Exception;

}
