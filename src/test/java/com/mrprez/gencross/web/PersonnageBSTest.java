package com.mrprez.gencross.web;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mrprez.gencross.disk.PersonnageFactory;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.PersonnageBS;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.dao.PersonnageDAO;
import com.mrprez.gencross.web.dao.face.IMailResource;
import com.mrprez.gencross.web.dao.face.IUserDAO;

public class PersonnageBSTest {
	
	
	private PersonnageBS buildPeronnageBS() throws Exception{
		PersonnageBS personnageBS = new PersonnageBS();
		PersonnageDAO personnageDAO = Mockito.mock(PersonnageDAO.class);
		personnageBS.setPersonnageDAO(personnageDAO);
		
		personnageBS.setUserDAO(Mockito.mock(IUserDAO.class));
		
		personnageBS.setPersonnageFactory(new PersonnageFactory());
		
		personnageBS.setMailResource(Mockito.mock(IMailResource.class));
		
		return personnageBS;
	}

	@Test
	public void testCreatePersonnageAsPlayer() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		UserBO mrprez = new UserBO();
		mrprez.setUsername("mrprez");
		mrprez.setMail("mrprez@gmail.com");
		Mockito.when(personnageBS.getUserDAO().getUser("mrprez")).thenReturn(mrprez);
		
		UserBO user = new UserBO();
		user.setUsername("GrosBill");
		user.setMail("grosbill@gmail.com");
		
		PersonnageWorkBO personnageWork = personnageBS.createPersonnageAsPlayer("Pavillon Noir", "Robert", "mrprez", user);
		Assert.assertEquals(personnageWork.getPluginName(), "Pavillon Noir");
		Assert.assertEquals(personnageWork.getGameMaster().getUsername(), "mrprez");
		Mockito.verify(personnageBS.getPersonnageDAO()).savePersonnage(personnageWork.getPersonnageData());
		Mockito.verify(personnageBS.getPersonnageDAO()).savePersonnage(personnageWork.getValidPersonnageData());
		Mockito.verify(personnageBS.getPersonnageDAO(), Mockito.atLeastOnce()).savePersonnageWork(personnageWork);
		
	}
	
	public void testCreatePersonnageAsGameMaster() throws Exception{
		PersonnageBS personnageBS = buildPeronnageBS();
		
		
	}
	
}
