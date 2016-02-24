package com.mrprez.gencross.web.tmp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class TestWriter {
	private Class<?> clazz;
	private String instanceName;
	private Set<Method> setters;
	private Set<Method> getters;
	private Set<Class<?>> businessInterfaces;
	private BufferedWriter writer;
	
	
	public TestWriter(String className) throws ClassNotFoundException{
		super();
		clazz = Class.forName(className);
		instanceName = clazz.getSimpleName().substring(0,1).toLowerCase()+clazz.getSimpleName().substring(1);
	}
	
	public void writeTestFile() throws IOException{
		String testClassName = clazz.getSimpleName()+"Test";
		File file = new File("M:/workspace/gencross-web/src/test/java/com/mrprez/gencross/web/test/action", testClassName+".java");
		if(file.exists()){
			file.delete();
		}
		writer = new BufferedWriter(new FileWriter(file));
		try{
			writeTest();
		}finally{
			writer.close();
		}
	}
	
	private void writeTest() throws IOException{
		setters = extractSetters(clazz.getDeclaredMethods());
		getters = extractGetters(clazz.getDeclaredMethods());
		businessInterfaces = extractBusinessinterfaces(clazz.getDeclaredMethods());
		writeLine("package com.mrprez.gencross.web.test.action;");
		writeLine("");
		writeLine("import org.junit.Assert;");
		writeLine("import org.junit.runner.RunWith;");
		writeLine("import org.junit.Test;");
		writeLine("import org.mockito.InjectMocks;");
		writeLine("import org.mockito.Mock;");
		writeLine("import org.mockito.runners.MockitoJUnitRunner;");
		writeLine("");
		writeLine("import "+clazz.getName()+";");
		for(Class<?> businessInterface : businessInterfaces){
			writeLine("import "+businessInterface.getName()+";");
		}
		writeLine("");
		writeLine("@RunWith(MockitoJUnitRunner.class)");
		writeLine("public class "+clazz.getSimpleName()+"Test extends AbstractActionTest {");
		for(Class<?> businessInterface : businessInterfaces){
			writeLine("");
			writeLine("\t@Mock");
			String fieldName = businessInterface.getSimpleName().substring(1,2).toLowerCase()+businessInterface.getSimpleName().substring(2);
			writeLine("\tprivate "+businessInterface.getSimpleName()+" "+fieldName+";");
		}
		writeLine("");
		writeLine("\t@InjectMocks");
		writeLine("\tprivate "+clazz.getSimpleName()+" "+instanceName+";");
		writeLine("");
		for(Method method : clazz.getDeclaredMethods()){
			if(Modifier.isPublic(method.getModifiers()) && ! method.getName().startsWith("get") && ! method.getName().startsWith("set")){
				writeLine("");
				writeLine("");
				writeMethodTest(method);
			}
		}
		writeLine("}");
	}
	
	private void writeLine(String string) throws IOException {
		writer.write(string);
		writer.newLine();
	}
	
	
	private void writeMethodTest(Method method) throws IOException{
		String testMethodName = "test"+method.getName().substring(0, 1).toUpperCase()+method.getName().substring(1);
		writeLine("\t@Test");
		writeLine("\tpublic void "+testMethodName+"() throws Exception {");
		writeLine("\t\t// Prepare");
		writePrepare(method);
		
		writeLine("");
		writeLine("\t\t// Execute");
		writeLine("\t\tString result = "+instanceName+"."+method.getName()+"();");
		
		writeLine("");
		writeLine("\t\t// Check");
		writeCheck(method);
		writeLine("\t}");
		
	}
	
	private void writePrepare(Method method) throws IOException{
		int count = 1;
		for(Method setter : setters){
			Class<?> parameterType = setter.getParameterTypes()[0];
			if(parameterType.equals(Integer.TYPE) || parameterType.isAssignableFrom(Integer.class)){
				writeLine("\t\t"+instanceName+"."+setter.getName()+"("+(count++)+");");
			} else if(parameterType.isAssignableFrom(String.class)){
				writeLine("\t\t"+instanceName+"."+setter.getName()+"(\"string_"+(count++)+"\");");
			}
		}
	}
	
	
	private void writeCheck(Method method) throws IOException{
		writeLine("\t\tAssert.assertEquals(\"input\", result);");
		for(Method getter : getters){
			Class<?> parameterType = getter.getReturnType();
			if(parameterType.isPrimitive()){
				writeLine("\t\tAssert.assertEquals(10, "+instanceName+"."+getter.getName()+"());");
			}else{
				writeLine("\t\tAssert.assertEquals(\"failTest\", "+instanceName+"."+getter.getName()+"());");
			}
		}
	}
	
	
	private Set<Class<?>> extractBusinessinterfaces(Method[] methods){
		Set<Class<?>> result = new HashSet<Class<?>>();
		for(Method method : methods){
			if(Modifier.isPublic(method.getModifiers())){
				if(method.getName().startsWith("set") && method.getParameterTypes().length==1 && method.getReturnType().equals(Void.TYPE)
						&& method.getParameterTypes()[0].getName().startsWith("com.mrprez.gencross.web.bs.face.I")){
					result.add(method.getParameterTypes()[0]);
				}
				if(method.getName().startsWith("get") && method.getParameterTypes().length==0 && ! method.getReturnType().equals(Void.TYPE)
						&& method.getReturnType().getName().startsWith("com.mrprez.gencross.web.bs.face.I")){
					result.add(method.getReturnType());
				}	
			}
		}
		return result;
	}
	
	

	public Set<Method> extractSetters(Method[] methods){
		Set<Method> result = new HashSet<Method>();
		for(Method method : methods){
			if(method.getName().startsWith("set") && Modifier.isPublic(method.getModifiers())
					&& method.getParameterTypes().length==1 && method.getReturnType().equals(Void.TYPE)
					&& ! method.getParameterTypes()[0].getName().startsWith("com.mrprez.gencross.web.bs.face.I")){
				result.add(method);
			}
		}
		return result;
	}
	
	public Set<Method> extractGetters(Method[] methods){
		Set<Method> result = new HashSet<Method>();
		for(Method method : methods){
			if(method.getName().startsWith("get") && Modifier.isPublic(method.getModifiers())
					&& method.getParameterTypes().length==0 && ! method.getReturnType().equals(Void.TYPE)
					&& ! method.getReturnType().getName().startsWith("com.mrprez.gencross.web.bs.face.I")){
				result.add(method);
			}
		}
		return result;
	}
	
	

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		new TestWriter("com.mrprez.gencross.web.action.AttributePlayerAction").writeTestFile();
		new TestWriter("com.mrprez.gencross.web.action.BackgroundAction").writeTestFile();
		new TestWriter("com.mrprez.gencross.web.action.ChangeMailAction").writeTestFile();
		new TestWriter("com.mrprez.gencross.web.action.ChangePasswordAction").writeTestFile();
		new TestWriter("com.mrprez.gencross.web.action.CreatePersonnageAction").writeTestFile();
		new TestWriter("com.mrprez.gencross.web.action.DownloadAction").writeTestFile();
		new TestWriter("com.mrprez.gencross.web.action.EditPersonnageAction").writeTestFile();
		new TestWriter("com.mrprez.gencross.web.action.EditTableAction").writeTestFile();
		new TestWriter("com.mrprez.gencross.web.action.ExceptionAction").writeTestFile();
		new TestWriter("com.mrprez.gencross.web.action.ExportAction").writeTestFile();
		new TestWriter("com.mrprez.gencross.web.action.ForgottenPasswordAction").writeTestFile();
		new TestWriter("com.mrprez.gencross.web.action.HelpFileAction").writeTestFile();
		new TestWriter("com.mrprez.gencross.web.action.JobProcessingAction").writeTestFile();
		new TestWriter("com.mrprez.gencross.web.action.ListUserAction").writeTestFile();
		new TestWriter("com.mrprez.gencross.web.action.LoggerAction").writeTestFile();
		new TestWriter("com.mrprez.gencross.web.action.LoginAction").writeTestFile();
		new TestWriter("com.mrprez.gencross.web.action.MultiExportAction").writeTestFile();
		new TestWriter("com.mrprez.gencross.web.action.ParamsAction").writeTestFile();
		new TestWriter("com.mrprez.gencross.web.action.PersonnageListAction").writeTestFile();
		new TestWriter("com.mrprez.gencross.web.action.PlanGameAction").writeTestFile();
		new TestWriter("com.mrprez.gencross.web.action.SubscriptionAction").writeTestFile();
		new TestWriter("com.mrprez.gencross.web.action.TableListAction").writeTestFile();
		new TestWriter("com.mrprez.gencross.web.action.TablePointsPoolsAction").writeTestFile();
		new TestWriter("com.mrprez.gencross.web.action.UploadAction").writeTestFile();

	}

}
