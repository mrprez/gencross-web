package com.mrprez.gencross.web.bs;

import java.util.Arrays;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.value.IntValue;
import com.mrprez.gencross.value.Value;

public class PersonnageComparatorBSTest {
	
	
	@Test
	public void testFindPropertiesDifferences_haveTheSameAttributes() throws Exception{
		PersonnageComparatorBS personnageComparatorBS = new PersonnageComparatorBS();
		Personnage personnage1 = PersonnageWorkBSTest.buildPersonnageWork().getPersonnage();
		Personnage personnage2 = PersonnageWorkBSTest.buildPersonnageWork().getPersonnage();
		personnage1.getProperty("Compétences#Religion").getSubProperties().add(new Property("Protestant", personnage1.getProperty("Compétences#Religion")));
		personnage2.getProperty("Compétences#Religion").getSubProperties().add(new Property("Protestant", personnage2.getProperty("Compétences#Religion")));
		
		personnage1.getProperty("Attributs#Carrure").setValue(new IntValue(2));
		personnage1.getProperty("Attributs#Erudition").setEditable(false);
		personnage1.getProperty("Attributs#Perception").setMax(new IntValue(4));
		personnage1.getProperty("Attributs#Pouvoir").setMin(new IntValue(-2));
		personnage1.getProperty("Compétences#Religion#Protestant").setRemovable(false);
		personnage1.getProperty("Compétences#Cartographie#Apprentissage").setOptions(Arrays.<Value>asList(new IntValue(0)));
		personnage2.getProperty("Compétences#Commerce#Apprentissage").setOptions(Arrays.<Value>asList(new IntValue(0)));
		personnage1.getProperty("Compétences#Herboristerie#Apprentissage").setOptions(Arrays.<Value>asList(new IntValue(0)));
		personnage2.getProperty("Compétences#Herboristerie#Apprentissage").setOptions(Arrays.<Value>asList(new IntValue(0), new IntValue(1)));
		personnage1.getProperty("Compétences#Ingénierie navale#Apprentissage").setOptions(Arrays.<Value>asList(new IntValue(0)));
		personnage2.getProperty("Compétences#Ingénierie navale#Apprentissage").setOptions(Arrays.<Value>asList(new IntValue(1)));
		personnage1.getProperty("Compétences#Intendance#Apprentissage").setOptions(Arrays.<Value>asList(new IntValue(0)));
		personnage2.getProperty("Compétences#Intendance#Apprentissage").setOptions(Arrays.<Value>asList(new IntValue(0)));
		
		
		Set<String> result = personnageComparatorBS.findPropertiesDifferences(personnage1, personnage2);
		
		Assert.assertEquals(9, result.size());
		Assert.assertTrue(result.contains("Attributs#Carrure"));
		Assert.assertTrue(result.contains("Attributs#Erudition"));
		Assert.assertTrue(result.contains("Attributs#Perception"));
		Assert.assertTrue(result.contains("Attributs#Pouvoir"));
		Assert.assertTrue(result.contains("Compétences#Religion#Protestant"));
		Assert.assertTrue(result.contains("Compétences#Cartographie#Apprentissage"));
		Assert.assertTrue(result.contains("Compétences#Commerce#Apprentissage"));
		Assert.assertTrue(result.contains("Compétences#Herboristerie#Apprentissage"));
		Assert.assertTrue(result.contains("Compétences#Ingénierie navale#Apprentissage"));
	}
	
	@Test
	public void testFindPropertiesDifferences_compareSubpropertiesAttributes() throws Exception{
		PersonnageComparatorBS personnageComparatorBS = new PersonnageComparatorBS();
		Personnage personnage1 = PersonnageWorkBSTest.buildPersonnageWork().getPersonnage();
		Personnage personnage2 = PersonnageWorkBSTest.buildPersonnageWork().getPersonnage();
		personnage1.getProperty("Compétences#Armes blanches").getSubProperties().setOptions(null);
		personnage2.getProperty("Compétences#Armes blanches").getSubProperties().setOptions(null);
		
		personnage1.getProperty("Compétences#Religion").removeSubProperties();
		personnage2.getProperty("Compétences#Connaissance spécialisée").removeSubProperties();
		personnage1.getProperty("Compétences#Art").getSubProperties().setFixe(true);
		personnage1.getProperty("Compétences#Artisanat").getSubProperties().setOpen(false);
		personnage1.getProperty("Compétences#Langue étrangère").getSubProperties().setCanRemoveElement(false);
		personnage1.getProperty("Compétences#Armes à feu").getSubProperties().setOptions(null);
		personnage2.getProperty("Compétences#Armes de jet").getSubProperties().setOptions(null);
		personnage1.getProperty("Faiblesses").getSubProperties().getOptions().get("Borgne").setName("Other name");
		personnage1.getProperty("Avantages").getSubProperties().getOptions().put("Sorcier", new Property("Sorcier", personnage1.getProperty("Avantages")));
		personnage2.getProperty("Compétences#Balistique").getSubProperties().getOptions().put("Apprentissage2", new Property("Apprentissage2", personnage2.getProperty("Compétences#Balistique")));
		
		Set<String> result = personnageComparatorBS.findPropertiesDifferences(personnage1, personnage2);
		
		Assert.assertEquals(10, result.size());
		Assert.assertTrue(result.contains("Compétences#Religion"));
		Assert.assertTrue(result.contains("Compétences#Connaissance spécialisée"));
		Assert.assertTrue(result.contains("Compétences#Art"));
		Assert.assertTrue(result.contains("Compétences#Artisanat"));
		Assert.assertTrue(result.contains("Compétences#Langue étrangère"));
		Assert.assertFalse(result.contains("Compétences#Armes blanches"));
		Assert.assertTrue(result.contains("Compétences#Armes à feu"));
		Assert.assertTrue(result.contains("Compétences#Armes de jet"));
		Assert.assertTrue(result.contains("Faiblesses"));
		Assert.assertTrue(result.contains("Avantages"));
		Assert.assertTrue(result.contains("Compétences#Balistique"));
	}

	
	@Test
	public void testFindPropertiesDifferences_compareObject() throws Exception{
		PersonnageComparatorBS personnageComparatorBS = new PersonnageComparatorBS();
		Personnage personnage1 = PersonnageWorkBSTest.buildPersonnageWork().getPersonnage();
		Personnage personnage2 = PersonnageWorkBSTest.buildPersonnageWork().getPersonnage();
		personnage1.getProperty("Esquive").setValue(null);
		personnage1.getProperty("Avantages").setValue(new IntValue(0));
		
		Set<String> result = personnageComparatorBS.findPropertiesDifferences(personnage1, personnage2);
		
		Assert.assertEquals(2, result.size());
		Assert.assertTrue(result.contains("Esquive"));
		Assert.assertTrue(result.contains("Avantages"));
	}
	

	@Test
	public void testFindPropertiesDifferences_compare() throws Exception{
		PersonnageComparatorBS personnageComparatorBS = new PersonnageComparatorBS();
		Personnage personnage1 = PersonnageWorkBSTest.buildPersonnageWork().getPersonnage();
		Personnage personnage2 = PersonnageWorkBSTest.buildPersonnageWork().getPersonnage();
		personnage1.getProperty("Avantages").getSubProperties().add(personnage1.getProperty("Avantages").getSubProperties().getOptions().get("Commission"));
		personnage2.getProperty("Faiblesses").getSubProperties().add(personnage2.getProperty("Faiblesses").getSubProperties().getOptions().get("Manchot"));
		personnage2.getProperty("Compétences#Balistique").setName("Tir au canon");
				
		Set<String> result = personnageComparatorBS.findPropertiesDifferences(personnage1, personnage2);
		
		Assert.assertEquals(3, result.size());
		Assert.assertTrue(result.contains("Avantages"));
		Assert.assertTrue(result.contains("Faiblesses"));
		Assert.assertTrue(result.contains("Compétences"));
	}
	
	
	@Test
	public void testHasTheSameErrors_FailSize() throws Exception {
		PersonnageComparatorBS personnageComparatorBS = new PersonnageComparatorBS();
		Personnage personnage1 = new Personnage();
		Personnage personnage2 = new Personnage();
		
		personnage1.getErrors().add("error1");
		personnage1.getErrors().add("error2");
		personnage2.getErrors().add("error1");
		
		boolean result = personnageComparatorBS.hasTheSameErrors(personnage1, personnage2);
		
		Assert.assertFalse(result);
	}
	
	@Test
	public void testHasTheSameErrors_FailTextDifference() throws Exception {
		PersonnageComparatorBS personnageComparatorBS = new PersonnageComparatorBS();
		Personnage personnage1 = new Personnage();
		Personnage personnage2 = new Personnage();
		
		personnage1.getErrors().add("error1");
		personnage1.getErrors().add("error2");
		personnage2.getErrors().add("error1");
		personnage2.getErrors().add("errorX");
		
		boolean result = personnageComparatorBS.hasTheSameErrors(personnage1, personnage2);
		
		Assert.assertFalse(result);
	}
	
	@Test
	public void testHasTheSameErrors_Success() throws Exception {
		PersonnageComparatorBS personnageComparatorBS = new PersonnageComparatorBS();
		Personnage personnage1 = new Personnage();
		Personnage personnage2 = new Personnage();
		
		personnage1.getErrors().add("error1");
		personnage1.getErrors().add("error2");
		personnage2.getErrors().add("error1");
		personnage2.getErrors().add("error2");
		
		boolean result = personnageComparatorBS.hasTheSameErrors(personnage1, personnage2);
		
		Assert.assertTrue(result);
	}
	
	
	@Test
	public void testFindPointPoolDifferences_FailPoolValue() throws Exception {
		PersonnageComparatorBS personnageComparatorBS = new PersonnageComparatorBS();
		Personnage personnage1 = new Personnage();
		Personnage personnage2 = new Personnage();
		personnage1.putPoolPoint("PP1", 10);
		personnage1.putPoolPoint("PP2", 20);
		personnage2.putPoolPoint("PP1", 10);
		personnage2.putPoolPoint("PP2", 20);
		
		personnage1.getPointPools().get("PP1").spend(5);
		
		Set<Integer> result = personnageComparatorBS.findPointPoolDifferences(personnage1, personnage2);
		
		Assert.assertEquals(1, result.size());
		Assert.assertTrue(result.contains(0));
	}
	
	@Test
	public void testFindPointPoolDifferences_FailPoolTotal() throws Exception {
		PersonnageComparatorBS personnageComparatorBS = new PersonnageComparatorBS();
		Personnage personnage1 = new Personnage();
		Personnage personnage2 = new Personnage();
		personnage1.putPoolPoint("PP1", 10);
		personnage1.putPoolPoint("PP2", 20);
		personnage2.putPoolPoint("PP1", 10);
		personnage2.putPoolPoint("PP2", 20);
		
		personnage1.getPointPools().get("PP2").setTotal(25);
		
		Set<Integer> result = personnageComparatorBS.findPointPoolDifferences(personnage1, personnage2);
		
		Assert.assertEquals(1, result.size());
		Assert.assertTrue(result.contains(1));
	}
	
	
	@Test
	public void testHasTheSameNextPhaseAvaibility_Fail() throws Exception {
		PersonnageComparatorBS personnageComparatorBS = new PersonnageComparatorBS();
		Personnage personnage1 = new Personnage();
		Personnage personnage2 = new Personnage();
		personnage1.getPhaseList().add("Phase1");
		personnage1.getPhaseList().add("Phase2");
		personnage1.passToNextPhase();
		personnage2.getPhaseList().add("Phase1");
		personnage2.getPhaseList().add("Phase2");
		personnage2.passToNextPhase();
		
		boolean result = personnageComparatorBS.hasTheSameNextPhaseAvaibility(personnage1, personnage2);
		
		Assert.assertTrue(result);
	}
	
	@Test
	public void testHasTheSameNextPhaseAvaibility_Success() throws Exception {
		PersonnageComparatorBS personnageComparatorBS = new PersonnageComparatorBS();
		Personnage personnage1 = new Personnage();
		Personnage personnage2 = new Personnage();
		personnage1.getPhaseList().add("Phase1");
		personnage1.getPhaseList().add("Phase2");
		personnage1.passToNextPhase();
		personnage2.getPhaseList().add("Phase1");
		personnage2.passToNextPhase();
		
		boolean result = personnageComparatorBS.hasTheSameNextPhaseAvaibility(personnage1, personnage2);
		
		Assert.assertFalse(result);
	}
}
