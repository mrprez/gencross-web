package com.mrprez.gencross.web.test.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mrprez.gencross.web.action.AttributeGameMasterAction;
import com.mrprez.gencross.web.action.AttributePlayerAction;
import com.mrprez.gencross.web.action.BackgroundAction;
import com.mrprez.gencross.web.action.ChangeMailAction;
import com.mrprez.gencross.web.action.ChangePasswordAction;
import com.mrprez.gencross.web.action.CreatePersonnageAction;
import com.mrprez.gencross.web.action.DownloadAction;
import com.mrprez.gencross.web.action.EditPersonnageAction;
import com.mrprez.gencross.web.action.EditTableAction;
import com.mrprez.gencross.web.action.ExceptionAction;
import com.mrprez.gencross.web.action.ExportAction;
import com.mrprez.gencross.web.action.ForgottenPasswordAction;
import com.mrprez.gencross.web.action.HelpFileAction;
import com.mrprez.gencross.web.action.JobProcessingAction;
import com.mrprez.gencross.web.action.ListUserAction;
import com.mrprez.gencross.web.action.LoggerAction;
import com.mrprez.gencross.web.action.LoginAction;
import com.mrprez.gencross.web.action.MultiExportAction;
import com.mrprez.gencross.web.action.ParamsAction;
import com.mrprez.gencross.web.action.PersonnageListAction;
import com.mrprez.gencross.web.action.PlanGameAction;
import com.mrprez.gencross.web.action.SubscriptionAction;
import com.mrprez.gencross.web.action.TableListAction;
import com.mrprez.gencross.web.action.TablePointsPoolsAction;
import com.mrprez.gencross.web.action.UploadAction;


public class GetterSetterTest {

	@Test
	public void testAttributeGameMasterAction() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(AttributeGameMasterAction.class);
	}


	@Test
	public void testAttributePlayerAction() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(AttributePlayerAction.class);
	}


	@Test
	public void testBackgroundAction() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(BackgroundAction.class);
	}


	@Test
	public void testChangeMailAction() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(ChangeMailAction.class);
	}


	@Test
	public void testChangePasswordAction() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(ChangePasswordAction.class);
	}


	@Test
	public void testCreatePersonnageAction() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(CreatePersonnageAction.class);
	}


	@Test
	public void testDownloadAction() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(DownloadAction.class);
	}


	@Test
	public void testEditPersonnageAction() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(EditPersonnageAction.class);
	}


	@Test
	public void testEditTableAction() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(EditTableAction.class);
	}


	@Test
	public void testExceptionAction() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(ExceptionAction.class);
	}


	@Test
	public void testExportAction() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(ExportAction.class);
	}


	@Test
	public void testForgottenPasswordAction() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(ForgottenPasswordAction.class);
	}


	@Test
	public void testHelpFileAction() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(HelpFileAction.class);
	}


	@Test
	public void testJobProcessingAction() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(JobProcessingAction.class);
	}


	@Test
	public void testListUserAction() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(ListUserAction.class);
	}


	@Test
	public void testLoggerAction() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(LoggerAction.class);
	}


	@Test
	public void testLoginAction() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(LoginAction.class);
	}


	@Test
	public void testMultiExportAction() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(MultiExportAction.class);
	}


	@Test
	public void testParamsAction() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(ParamsAction.class);
	}


	@Test
	public void testPersonnageListAction() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(PersonnageListAction.class);
	}


	@Test
	public void testPlanGameAction() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(PlanGameAction.class);
	}


	@Test
	public void testSubscriptionAction() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(SubscriptionAction.class);
	}


	@Test
	public void testTableListAction() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(TableListAction.class);
	}


	@Test
	public void testTablePointsPoolsAction() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(TablePointsPoolsAction.class);
	}


	@Test
	public void testUploadAction() throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		testAllGetterSetter(UploadAction.class);
	}
	
	
	private void testAllGetterSetter(Class<?> clazz) throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException{
		Object object = clazz.newInstance();
		for(Method method : clazz.getDeclaredMethods()){
			if(method.getName().startsWith("set") && method.getParameterTypes().length==1){
				String propertyName = method.getName().substring(3);
				if(methodExist(clazz, "get"+propertyName, method.getParameterTypes()[0])){
					testGetterSetter(object, method, clazz.getMethod("get"+propertyName));
				}
			}
			
		}
		
	}
	
	private boolean methodExist(Class<?> clazz, String methodName, Class<?> returnedType, Class<?>... parameterTypes){
		try{
			return clazz.getMethod(methodName, parameterTypes).getReturnType().isAssignableFrom(returnedType);
		}catch(NoSuchMethodException nsme){
			return false;
		}
	}
	
	
	private void testGetterSetter(Object object, Method setterMethod, Method getterMethod) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Class<?> propertyClass = setterMethod.getParameterTypes()[0];
		Object parameter;
		if(propertyClass.isAssignableFrom(Integer.class)){
			parameter = new Integer(10);
		} else if(propertyClass.isAssignableFrom(String.class)){
			parameter = "a simple string";
		}else{
			parameter = Mockito.mock(propertyClass);
		}
		setterMethod.invoke(object, parameter);
		Object result = getterMethod.invoke(object);
		Assert.assertSame(parameter, result);
	}
	
}
