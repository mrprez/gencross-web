package com.mrprez.gencross.web.test.bs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mrprez.gencross.disk.PersonnageFactory;
import com.mrprez.gencross.web.bs.AdminBS;
import com.mrprez.gencross.web.bs.AuthentificationBS;
import com.mrprez.gencross.web.bs.ExportBS;
import com.mrprez.gencross.web.bs.GcrFileBS;
import com.mrprez.gencross.web.bs.ParamsBS;
import com.mrprez.gencross.web.bs.PersonnageBS;
import com.mrprez.gencross.web.bs.PlanGameBS;
import com.mrprez.gencross.web.bs.TableBS;
import com.mrprez.gencross.web.dao.face.IMailResource;
import com.mrprez.gencross.web.dao.face.IPersonnageDAO;
import com.mrprez.gencross.web.dao.face.IUserDAO;

public class GetterSetterTest {

	@Test
	public void testAdminBS() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(AdminBS.class);
	}
	
	@Test
	public void testAuthentificationBS() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(AuthentificationBS.class);
	}
	
	
	@Test
	public void testExportBS() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(ExportBS.class);
	}
	
	
	@Test
	public void testGcrFileBS() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(GcrFileBS.class);
	}
	
	
	@Test
	public void testPersonnageBS() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testGetterSetter(PersonnageBS.class, "PersonnageFactory", PersonnageFactory.class);
		testGetterSetter(PersonnageBS.class, "PersonnageDAO", IPersonnageDAO.class);
		testGetterSetter(PersonnageBS.class, "MailResource", IMailResource.class);
		testGetterSetter(PersonnageBS.class, "UserDAO", IUserDAO.class);
	}
	
	
	@Test
	public void testParamsBS() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(ParamsBS.class);
	}
	
	
	@Test
	public void testPlanGameBS() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(PlanGameBS.class);
	}
	
	
	@Test
	public void testTableBS() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(TableBS.class);
	}
	
	
	private void testGetterSetter(Class<?> clazz, String propertyName, Class<?> propertyClass) throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException{
		Object object = clazz.newInstance();
		Method setterMethod = clazz.getMethod("set"+propertyName, propertyClass);
		Method getterMethod = clazz.getMethod("get"+propertyName);
		testGetterSetter(object, setterMethod, getterMethod);
	}
	
	
	private void testAllGetterSetter(Class<?> clazz) throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException{
		Object object = clazz.newInstance();
		for(Method method : clazz.getMethods()){
			if(method.getName().startsWith("set")){
				String propertyName = method.getName().substring(3);
				testGetterSetter(object, method, clazz.getMethod("get"+propertyName));
			}
			
		}
		
	}
	
	
	private void testGetterSetter(Object object, Method setterMethod, Method getterMethod) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Class<?> propertyClass = setterMethod.getParameterTypes()[0];
		Object parameter = Mockito.mock(propertyClass);
		setterMethod.invoke(object, parameter);
		Object result = getterMethod.invoke(object);
		Assert.assertSame(parameter, result);
	}
	
}
