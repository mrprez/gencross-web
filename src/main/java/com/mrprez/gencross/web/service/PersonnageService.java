package com.mrprez.gencross.web.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jws.WebService;

import org.springframework.web.context.ContextLoader;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.disk.PersonnageFactory;
import com.mrprez.gencross.disk.PersonnageSaver;
import com.mrprez.gencross.disk.PluginDescriptor;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.RoleBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.ws.api.IPersonnageService;
import com.mrprez.gencross.ws.api.bo.PersonnageLabel;



@WebService(endpointInterface = "com.mrprez.gencross.ws.api.IPersonnageService" )
public class PersonnageService implements IPersonnageService {
	//private static String VALID_VERSION = "validVersion";
	
	
	@Override
	public PluginDescriptor[] getPluginList() throws Exception {
		IPersonnageBS personnageBS = (IPersonnageBS) ContextLoader.getCurrentWebApplicationContext().getBean("personnageBS");
		return personnageBS.getAvailablePersonnageTypes().toArray(new PluginDescriptor[0]);
	}
	
	
	@Override
	public PersonnageLabel[] getPersonnageLabels(PluginDescriptor pluginDescriptor) throws Exception{
		IPersonnageBS personnageBS = (IPersonnageBS) ContextLoader.getCurrentWebApplicationContext().getBean("personnageBS");
		Collection<PersonnageWorkBO> personnageWorkList = personnageBS.getPersonnageListFromType(pluginDescriptor.getName());
		
		UserBO user = AuthentificationService.localThreadUser.get();
		List<PersonnageLabel> result = new ArrayList<PersonnageLabel>();
		for(PersonnageWorkBO personnageWork : personnageWorkList){
			if(user.getRoles().contains(new RoleBO(RoleBO.MANAGER)) || user.equals(personnageWork.getGameMaster())){
				result.add(new PersonnageLabel(personnageWork.getId(), personnageWork.getName()));
			}
		}
		
		return result.toArray(new PersonnageLabel[result.size()]);
	}

	
	@Override
	public byte[] getPersonnage(int id) throws Exception {
		IPersonnageBS personnageBS = (IPersonnageBS) ContextLoader.getCurrentWebApplicationContext().getBean("personnageBS");
		PersonnageWorkBO personnageWork = personnageBS.loadPersonnage(id);
		Personnage personnage = personnageWork.getPersonnageData().getPersonnage();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PersonnageSaver.savePersonnage(personnage, baos);
		return baos.toByteArray();
	}


	@Override
	public void savePersonnage(int personnageId, byte[] xml) throws Exception {
		PersonnageFactory personnageFactory = new PersonnageFactory();
		ByteArrayInputStream bais = new ByteArrayInputStream(xml);
		Personnage personnage = personnageFactory.loadPersonnage(bais);
		
		IPersonnageBS personnageBS = (IPersonnageBS) ContextLoader.getCurrentWebApplicationContext().getBean("personnageBS");
		PersonnageWorkBO personnageWork = personnageBS.loadPersonnage(personnageId);
		personnageWork.getPersonnageData().setPersonnage(personnage);
		
		personnageBS.savePersonnage(personnageWork);
	}


	
	
//	
//	public void savePersonnage(Integer personnageId, byte[] personnageContent) throws Exception{
//		IPersonnageBS personnageBS = (IPersonnageBS) ContextLoader.getCurrentWebApplicationContext().getBean("personnageBS");
//		PersonnageFactory personnageFactory = (PersonnageFactory) ContextLoader.getCurrentWebApplicationContext().getBean("personnageFactory");
//		Personnage personnage = personnageFactory.loadPersonnage(new ByteArrayInputStream(personnageContent));
//		PersonnageWorkBO personnageWork = personnageBS.loadPersonnage(personnageId);
//		personnageWork.getPersonnageData().setPersonnage(personnage);
//		personnageBS.savePersonnage(personnageWork);
//	}
	

}
