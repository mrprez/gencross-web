package com.mrprez.gencross.web.bs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.mrprez.gencross.disk.PersonnageFactory;
import com.mrprez.gencross.disk.PluginDescriptor;
import com.mrprez.gencross.web.bs.face.IGencrossUiPackagerBS;

public class GencrossUiPackagerBS implements IGencrossUiPackagerBS {
	
	private static final String GENCROSS_UI_SETUP = "GencrossUI.zip";
	
	private PersonnageFactory personnageFactory;
	
	
	
	@Override
	public byte[] buildGencrossUiPackage() throws Exception {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
		
		try{
			ZipInputStream zipInputStream = new ZipInputStream(getClass().getClassLoader().getResourceAsStream(GENCROSS_UI_SETUP));
			try{
				ZipEntry zipEntry;
				while( (zipEntry = zipInputStream.getNextEntry()) !=null ){
					if(zipEntry.isDirectory() || !zipEntry.getName().startsWith("GenCross/repository/")){
						writeZipEntry(zipEntry, zipInputStream, zipOutputStream);
					}
				}
			}finally{
				zipInputStream.close();
			}
			
			for(File jarFile : loadPersonnageJarList()){
				writeJarFile(jarFile, zipOutputStream);
			}
			
			
		}finally{
			zipOutputStream.close();
		}
		return byteArrayOutputStream.toByteArray();
		
	}
	
	private void writeZipEntry(ZipEntry zipEntry, ZipInputStream zipInputStream, ZipOutputStream zipOutputStream) throws IOException{
		zipOutputStream.putNextEntry(new ZipEntry(zipEntry));
		int length;
		byte[] buffer = new byte[1024];
		while( (length = zipInputStream.read(buffer)) > 0 ){
			zipOutputStream.write(buffer, 0, length);
		}
		zipOutputStream.closeEntry();
	}
	
	
	private void writeJarFile(File jarFile, ZipOutputStream zipOutputStream) throws IOException{
		zipOutputStream.putNextEntry(new ZipEntry("GenCross/repository/"+jarFile.getName()));
		FileInputStream fileInputStream = new FileInputStream(jarFile);
		try{
			int length;
			byte[] buffer = new byte[1024];
			while( (length = fileInputStream.read(buffer)) > 0 ){
				zipOutputStream.write(buffer, 0, length);
			}
			zipOutputStream.closeEntry();
		}finally{
			fileInputStream.close();
		}
	}
		
	
	
	private Collection<File> loadPersonnageJarList() throws ClassNotFoundException, URISyntaxException{
		Collection<File> result = new ArrayList<File>();
		for(PluginDescriptor pluginDescriptor : personnageFactory.getPluginList()){
			Class<?> pluginClass = getClass().getClassLoader().loadClass(pluginDescriptor.getClassName());
			URL jarUrl = pluginClass.getProtectionDomain().getCodeSource().getLocation();
			File jarFile = new File(jarUrl.toURI());
			result.add(jarFile);
		}
		return result;
	}

	public PersonnageFactory getPersonnageFactory() {
		return personnageFactory;
	}

	public void setPersonnageFactory(PersonnageFactory personnageFactory) {
		this.personnageFactory = personnageFactory;
	}

}
