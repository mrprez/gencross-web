package com.mrprez.gencross.web.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.springframework.web.context.ContextLoader;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.disk.PersonnageFactory;
import com.mrprez.gencross.disk.PersonnageSaver;
import com.mrprez.gencross.disk.PluginDescriptor;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;


@WebService(endpointInterface = "com.mrprez.gencross.web.service")
@SOAPBinding(style = Style.RPC)
public class PersonnageService {
	private static String VALID_VERSION = "validVersion";
	
	
	@WebMethod
	public Collection<PluginDescriptor> getPluginList() throws Exception{
		IPersonnageBS personnageBS = (IPersonnageBS) ContextLoader.getCurrentWebApplicationContext().getBean("personnageBS");
		return personnageBS.getAvailablePersonnageTypes();
	}
	
	public Map<String, Integer> findPersonnageList(PluginDescriptor pluginDescriptor) throws Exception{
		IPersonnageBS personnageBS = (IPersonnageBS) ContextLoader.getCurrentWebApplicationContext().getBean("personnageBS");
		Collection<PersonnageWorkBO> personnageWorkList = personnageBS.getPersonnageListFromType(pluginDescriptor.getName());
		
		Map<String, Integer> result = new HashMap<String, Integer>();
		for(PersonnageWorkBO personnageWork : personnageWorkList){
			result.put(personnageWork.getName(), personnageWork.getId());
		}
		
		return result;
	}
	
	public byte[] loadPersonnage(Integer personnageId) throws Exception{
		IPersonnageBS personnageBS = (IPersonnageBS) ContextLoader.getCurrentWebApplicationContext().getBean("personnageBS");
		PersonnageWorkBO personnageWork = personnageBS.loadPersonnage(personnageId);
		Personnage personnage = personnageWork.getPersonnageData().getPersonnage();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PersonnageSaver.savePersonnage(personnage, baos);
		
		return baos.toByteArray();
	}
	
	public void savePersonnage(Integer personnageId, byte[] personnageContent) throws Exception{
		IPersonnageBS personnageBS = (IPersonnageBS) ContextLoader.getCurrentWebApplicationContext().getBean("personnageBS");
		PersonnageFactory personnageFactory = (PersonnageFactory) ContextLoader.getCurrentWebApplicationContext().getBean("personnageFactory");
		Personnage personnage = personnageFactory.loadPersonnage(new ByteArrayInputStream(personnageContent));
		PersonnageWorkBO personnageWork = personnageBS.loadPersonnage(personnageId);
		personnageWork.getPersonnageData().setPersonnage(personnage);
		personnageBS.savePersonnage(personnageWork);
	}
	

}
