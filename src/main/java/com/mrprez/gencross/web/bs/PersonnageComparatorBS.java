package com.mrprez.gencross.web.bs;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.PoolPoint;
import com.mrprez.gencross.PropertiesList;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.value.Value;
import com.mrprez.gencross.web.bs.face.IPersonnageComparatorBS;

public class PersonnageComparatorBS implements IPersonnageComparatorBS{

	@Override
	public Set<String> findPropertiesDifferences(Personnage personnage1, Personnage personnage2) throws Exception {
		Set<String> result = new HashSet<String>();
		for(Property property : personnage1.getProperties()){
			result.addAll(compare(property, personnage2.getProperty(property.getAbsoluteName())));
		}
		return result;
	}
	
	private Set<String> compare(Property property1, Property property2){
		Set<String> result = new HashSet<String>();
		if(!haveTheSameAttributes(property1, property2)){
			result.add(property1.getAbsoluteName());
			return result;
		}
		if(!compareSubpropertiesAttributes(property1.getSubProperties(), property2.getSubProperties())){
			result.add(property1.getAbsoluteName());
			return result;
		}
		if(property1.getSubProperties()!=null){
			Iterator<Property> it1 = property1.getSubProperties().iterator();
			Iterator<Property> it2 = property2.getSubProperties().iterator();
			while(it1.hasNext() && it2.hasNext()){
				Property subProperty1 = it1.next();
				Property supProperty2 = it2.next();
				if(!subProperty1.getFullName().equals(supProperty2.getFullName())){
					result.add(property1.getAbsoluteName());
					return result;
				}
			}
			if(it1.hasNext() || it2.hasNext()){
				result.add(property1.getAbsoluteName());
				return result;
			}
			for(Property property : property1.getSubProperties()){
				result.addAll(compare(property, property2.getSubProperty(property.getFullName())));
			}
		}
		return result;
	}
	
	private boolean haveTheSameAttributes(Property property1, Property property2){
		if(!compareObject(property1.getValue(), property2.getValue())){
			return false;
		}else if(property1.isEditable()!=property2.isEditable()){
			return false;
		}else if(property1.isRemovable()!=property2.isRemovable()){
			return false;
		}else if(!property1.getFullName().equals(property2.getFullName())){
			return false;
		}else if(!compareObject(property1.getMax(), property2.getMax())){
			return false;
		}else if(!compareObject(property1.getMin(), property2.getMin())){
			return false;
		}
		if(property1.getOptions()==null && property2.getOptions()==null){
			return true;
		}else if(property1.getOptions()==null || property2.getOptions()==null){
			return false;
		}else if(property1.getOptions().size()!=property2.getOptions().size()){
			return false;
		}
		Iterator<Value> it1 = property1.getOptions().iterator();
		Iterator<Value> it2 = property2.getOptions().iterator();
		while(it1.hasNext()){
			if(!it1.next().equals(it2.next())){
				return false;
			}
		}
		
		return true;
	}
	
	private boolean compareSubpropertiesAttributes(PropertiesList propertiesList1, PropertiesList propertiesList2){
		if(propertiesList1==null && propertiesList2==null){
			return true;
		}else if(propertiesList1==null || propertiesList2==null){
			return false;
		}else if(propertiesList1.isFixe()!=propertiesList2.isFixe()){
			return false;
		}else if(propertiesList1.isOpen()!=propertiesList2.isOpen()){
			return false;
		}else if(propertiesList1.canRemoveElement()!=propertiesList2.canRemoveElement()){
			return false;
		}
		
		if(propertiesList1.getOptions()==null && propertiesList2.getOptions()==null){
			return true;
		}else if(propertiesList1.getOptions()==null || propertiesList2.getOptions()==null){
			return false;
		}
		Iterator<Property> it1 = propertiesList1.getOptions().values().iterator();
		Iterator<Property> it2 = propertiesList2.getOptions().values().iterator();
		while(it1.hasNext() && it2.hasNext()){
			if(!compare(it1.next(), it2.next()).isEmpty()){
				return false;
			}
		}
		if(it1.hasNext() || it2.hasNext()){
			return false;
		}
		return true;
	}
	
	private boolean compareObject(Object s1, Object s2){
		if(s1==null && s2==null){
			return true;
		}else if(s1==null || s2==null){
			return false;
		}
		return s1.equals(s2);
	}

	@Override
	public boolean hasTheSameErrors(Personnage personnage1, Personnage personnage2) throws Exception {
		if(personnage1.getErrors().size()!=personnage2.getErrors().size()){
			return false;
		}
		for(String error1 : personnage1.getErrors()){
			if(!personnage2.getErrors().contains(error1)){
				return false;
			}
		}
		return true;
	}

	@Override
	public Set<Integer> findPointPoolDifferences(Personnage personnage1, Personnage personnage2) throws Exception {
		HashSet<Integer> result = new HashSet<Integer>();
		int index = 0;
		for(PoolPoint poolPoint1 : personnage1.getPointPools().values()){
			PoolPoint poolPoint2 = personnage2.getPointPools().get(poolPoint1.getName());
			if(poolPoint1.getRemaining()!=poolPoint2.getRemaining() || poolPoint1.getTotal()!=poolPoint2.getTotal()){
				result.add(index);
			}
			index++;
		}
		
		return result;
	}


	@Override
	public boolean hasTheSameNextPhaseAvaibility(Personnage personnage1, Personnage personnage2) throws Exception {
		return personnage1.phaseFinished()==personnage2.phaseFinished();
	}

	
	
	

}
