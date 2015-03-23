package com.mrprez.gencross.web.service;

import java.util.Collection;
import java.util.HashMap;

import javax.jws.WebService;

import org.springframework.web.context.ContextLoader;

import com.mrprez.gencross.disk.PluginDescriptor;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.RoleBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.ws.api.IPersonnageService;



@WebService(endpointInterface = "com.mrprez.gencross.ws.api.IPersonnageService" )
public class PersonnageService implements IPersonnageService {
	//private static String VALID_VERSION = "validVersion";
	
	
	@Override
	public PluginDescriptor[] getPluginList() throws Exception {
		IPersonnageBS personnageBS = (IPersonnageBS) ContextLoader.getCurrentWebApplicationContext().getBean("personnageBS");
		return personnageBS.getAvailablePersonnageTypes().toArray(new PluginDescriptor[0]);
	}
	
	
//	@Override
	public HashMap<Integer,String> getPersonnageLabels(PluginDescriptor pluginDescriptor) throws Exception{
		IPersonnageBS personnageBS = (IPersonnageBS) ContextLoader.getCurrentWebApplicationContext().getBean("personnageBS");
		Collection<PersonnageWorkBO> personnageWorkList = personnageBS.getPersonnageListFromType(pluginDescriptor.getName());
		
		UserBO user = AuthentificationService.localThreadUser.get();
		HashMap<Integer,String> result = new HashMap<Integer,String>();
		for(PersonnageWorkBO personnageWork : personnageWorkList){
			if(user.getRoles().contains(RoleBO.MANAGER) && user.equals(personnageWork.getGameMaster())){
				result.put(personnageWork.getId(), personnageWork.getName());
			}
		}
		
		return result;
	}

	
//	public byte[] loadPersonnage(Integer personnageId) throws Exception{
//		IPersonnageBS personnageBS = (IPersonnageBS) ContextLoader.getCurrentWebApplicationContext().getBean("personnageBS");
//		PersonnageWorkBO personnageWork = personnageBS.loadPersonnage(personnageId);
//		Personnage personnage = personnageWork.getPersonnageData().getPersonnage();
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		PersonnageSaver.savePersonnage(personnage, baos);
//		
//		return baos.toByteArray();
//	}
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
