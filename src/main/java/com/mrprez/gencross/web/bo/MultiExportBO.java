package com.mrprez.gencross.web.bo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Property;

public class MultiExportBO {
	
	private LinkedHashMap<Personnage, MultiExportColumn> columns = new LinkedHashMap<Personnage, MultiExportColumn>();
	private List<MultiExportLine> lines = new ArrayList<MultiExportLine>();
	
	
	public MultiExportBO(Collection<Personnage> personnageList){
		super();
		int index = 0;
		for(Personnage personnage : personnageList){
			columns.put(personnage, new MultiExportColumn(index++));
		}
	}
	
	public void addFixProperty(String title, Property[] properties){
		MultiExportLine line = new MultiExportLine(title, properties.length);
		for(int i=0; i<properties.length; i++){
			String value = properties[i].getValue()!=null ? properties[i].getValue().getString() : null;
			line.setValue(i, value);
			MultiExportColumn column = columns.get(properties[i].getPersonnage());
			column.add(value, lines.size()-1);
		}
		lines.add(line);
	}
	
	public void addMovingProperty(Property property){
		String value = property.getValue().getString();
		MultiExportColumn column = columns.get(property.getPersonnage());
		column.add(value);
		
		if(lines.size()<column.size()){
			lines.add(new MultiExportLine(null, columns.size()));
		}
		MultiExportLine line = lines.get(column.size()-1);
		line.setValue(column.getIndex(), value);
	}
	
	
	public void addFixProperty(String absoluteName, List<Property> propertyList) {
		addFixProperty(absoluteName, propertyList.toArray(new Property[propertyList.size()]));
	}
	
	
	
	
	
	public static class MultiExportColumn extends ArrayList<String>{
		private static final long serialVersionUID = 1L;
		private final int index;
		
		public MultiExportColumn(int index) {
			super();
			this.index = index;
		}



		public void add(String value, int position){
			while(size()<position){
				add(null);
			}
			add(value);
		}

		public int getIndex() {
			return index;
		}
	}
	
	public static class MultiExportLine {
		private String title;
		private String[] values;
		
		public MultiExportLine(String title, int length) {
			super();
			this.title = title;
			this.values = new String[length];
		}
		
		public void setValue(int position, String value){
			values[position] = value;
		}

		public String getTitle() {
			return title;
		}

		public String[] getValues() {
			return values;
		}
		
		
	}

	
	
	

}
