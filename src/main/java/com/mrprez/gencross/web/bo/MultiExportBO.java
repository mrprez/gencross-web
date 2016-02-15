package com.mrprez.gencross.web.bo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.util.HtmlToText;
import com.mrprez.gencross.value.Value;

public class MultiExportBO {
	
	private LinkedHashMap<Personnage, MultiExportColumn> columns = new LinkedHashMap<Personnage, MultiExportColumn>();
	private List<MultiExportLine> lines = new ArrayList<MultiExportLine>();
	
	
	public MultiExportBO(Personnage[] personnageTab){
		super();
		for(int index=0; index<personnageTab.length; index++){
			columns.put(personnageTab[index], new MultiExportColumn(index));
		}
	}
	
	public void addFullLine(String title, Property[] properties) throws IOException{
		String[] propertyPath = title.split("#");
		int depth = propertyPath.length-1;
		title = propertyPath[propertyPath.length-1];
		MultiExportLine line = new MultiExportLine(title, depth, properties.length);
		for(int i=0; i<properties.length; i++){
			String value = getValueText(properties[i].getValue());
			line.setValue(i, value);
			MultiExportColumn column = columns.get(properties[i].getPersonnage());
			column.add(value, lines.size()-1);
		}
		lines.add(line);
	}
	
	public void addSimpleElement(Property property) throws IOException{
		String value = property.getName()+":"+getValueText(property.getValue());
		MultiExportColumn column = columns.get(property.getPersonnage());
		column.add(value);
		
		if(lines.size()<column.size()){
			lines.add(new MultiExportLine(null, property.getAbsoluteName().split("#").length-1, columns.size()));
		}
		MultiExportLine line = lines.get(column.size()-1);
		line.setValue(column.getIndex(), value);
	}
	
	private String getValueText(Value value) throws IOException{
		if(value==null){
			return null;
		}
		String text = value.getString();
		if(text.startsWith("<html>")){
			HtmlToText htmlToText = new HtmlToText();
			htmlToText.parse(text);
			text = htmlToText.getString();
		}
		return value.getString();
	}
	
	
	public List<MultiExportLine> getLines(){
		return lines;
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
		private int depth;
		private String[] values;
		
		public MultiExportLine(String title, int depth, int length) {
			super();
			this.title = title;
			this.depth = depth;
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

		public int getDepth() {
			return depth;
		}
		
		
	}

	
	
	

}
