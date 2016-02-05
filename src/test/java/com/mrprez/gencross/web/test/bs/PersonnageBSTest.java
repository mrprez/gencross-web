package com.mrprez.gencross.web.test.bs;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.PoolPoint;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.history.HistoryItem;
import com.mrprez.gencross.value.DoubleValue;
import com.mrprez.gencross.web.bo.PersonnageDataBO;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bs.PersonnageBS;
import com.mrprez.gencross.web.dao.PersonnageDAO;
import com.mrprez.gencross.web.dao.face.IUserDAO;

@RunWith(MockitoJUnitRunner.class)
public class PersonnageBSTest {
	
	@InjectMocks
	private PersonnageBS personnageBS;
	
	@Mock
	private PersonnageDAO personnageDao;
	
	@Mock
	private IUserDAO userDao;
	
	
	
	@Test
	public void testModifyHistory_Success() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		Personnage personnage = personnageWork.getPersonnage();
		personnage.setNewValue("Attributs#Erudition", 3);
		
		// Execute
		personnageBS.modifyHistory(personnageWork, "Expérience", 8, 0);
		
		// Check
		Assert.assertEquals(13, personnage.getPointPools().get("Attributs").getRemaining());
		Assert.assertEquals(-8, personnage.getPointPools().get("Expérience").getRemaining());
		Assert.assertEquals("Attributs#Erudition", personnage.getHistory().get(0).getAbsoluteName());
		Assert.assertEquals(HistoryItem.UPDATE, personnage.getHistory().get(0).getAction());
		Assert.assertEquals("Expérience", personnage.getHistory().get(0).getPointPool());
		Assert.assertEquals(8, personnage.getHistory().get(0).getCost());
	}
	
	@Test
	public void testModifyHistory_Success_NoPointPool() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		Personnage personnage = personnageWork.getPersonnage();
		personnage.setNewValue("Attributs#Erudition", 3);
		
		// Execute
		personnageBS.modifyHistory(personnageWork, "Toto", 8, 0);
		
		// Check
		Assert.assertEquals(13, personnage.getPointPools().get("Attributs").getRemaining());
		Assert.assertEquals(0, personnage.getPointPools().get("Expérience").getRemaining());
		Assert.assertEquals("Attributs#Erudition", personnage.getHistory().get(0).getAbsoluteName());
		Assert.assertEquals(HistoryItem.UPDATE, personnage.getHistory().get(0).getAction());
		Assert.assertEquals("Toto", personnage.getHistory().get(0).getPointPool());
		Assert.assertEquals(8, personnage.getHistory().get(0).getCost());
	}
	
	@Test
	public void testModifyHistory_Success_NoNewPointPool() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		Personnage personnage = personnageWork.getPersonnage();
		personnage.setNewValue("Nom", "Loic");
		
		// Execute
		personnageBS.modifyHistory(personnageWork, "Expérience", 8, 0);
		
		// Check
		Assert.assertEquals(13, personnage.getPointPools().get("Attributs").getRemaining());
		Assert.assertEquals(-8, personnage.getPointPools().get("Expérience").getRemaining());
		Assert.assertEquals("Nom", personnage.getHistory().get(0).getAbsoluteName());
		Assert.assertEquals(HistoryItem.UPDATE, personnage.getHistory().get(0).getAction());
		Assert.assertEquals("Expérience", personnage.getHistory().get(0).getPointPool());
		Assert.assertEquals(8, personnage.getHistory().get(0).getCost());
	}
	
	@Test
	public void testModifyPointPool() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		
		// Execute
		personnageBS.modifyPointPool(personnageWork, "Expérience", 10);
		
		// Check
		Mockito.verify(personnageDao, Mockito.atLeastOnce()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertEquals(personnageWork.getPersonnage().getPointPools().get("Expérience").getRemaining(), 10);
	}
	
	@Test
	public void testModifyPointPool_Fail() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		Map<String, Integer> pointPoolCopy = new HashMap<String, Integer>();
		for(Entry<String, PoolPoint> pointPoolEntry : personnageWork.getPersonnage().getPointPools().entrySet()){
			pointPoolCopy.put(pointPoolEntry.getKey(), pointPoolEntry.getValue().getTotal());
		}
		
		// Execute
		personnageBS.modifyPointPool(personnageWork, "PA", 10);
		
		// Check
		for(Entry<String, PoolPoint> pointPoolEntry : personnageWork.getPersonnage().getPointPools().entrySet()){
			Assert.assertEquals(pointPoolCopy.get(pointPoolEntry.getKey()).intValue(), pointPoolEntry.getValue().getTotal());
		}
	}
	
	@Test
	public void testNextPhase() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPersonnageData(new PersonnageDataBO());
		personnageWork.getPersonnageData().setPersonnage(Mockito.mock(Personnage.class));
		
		// Execute
		personnageBS.nextPhase(personnageWork);
		
		// Check
		Mockito.verify(personnageDao, Mockito.atLeastOnce()).savePersonnage(personnageWork.getPersonnageData());
		Mockito.verify(personnageWork.getPersonnage()).passToNextPhase();
	}
	
	@Test
	public void testRemoveProperty() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		Property motherProperty = personnageWork.getPersonnage().getProperty("Compétences#Connaissance spécialisée");
		personnageWork.getPersonnage().addPropertyToMotherProperty(motherProperty.getSubProperties().getOptions().get("Droit"));
		
		// Execute
		personnageBS.removeProperty(personnageWork, "Compétences#Connaissance spécialisée#Droit");
		
		// Check
		Mockito.verify(personnageDao, Mockito.atLeastOnce()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertNull(personnageWork.getPersonnage().getProperty("Compétences#Connaissance spécialisée#Droit"));
	}
	
	
	@Test
	public void testRemoveProperty_Fail() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		Property motherProperty = personnageWork.getPersonnage().getProperty("Compétences#Connaissance spécialisée");
		personnageWork.getPersonnage().addPropertyToMotherProperty(motherProperty.getSubProperties().getOptions().get("Droit"));
		
		// Execute
		personnageBS.removeProperty(personnageWork, "Compétences#Commerce");
		
		// Check
		Mockito.verify(personnageDao, Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
	}
	
	@Test
	public void testSetNewValue_Success() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");

		// Execute
		boolean result = personnageBS.setNewValue(personnageWork, "4", "Attributs#Erudition");
		
		// Check
		Assert.assertTrue(result);
		Mockito.verify(personnageDao, Mockito.atLeastOnce()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertEquals(4, personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().getInt());
		Assert.assertEquals("Attributs#Erudition", personnageWork.getPersonnage().getHistory().get(0).getAbsoluteName());
	}
	
	@Test
	public void testSetNewValue_Fail() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");

		// Execute
		boolean result = personnageBS.setNewValue(personnageWork, "7", "Attributs#Erudition");
		
		// Check
		Assert.assertFalse(result);
		Mockito.verify(personnageDao, Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertEquals(1, personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().getInt());
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
	}
	
	@Test
	public void testSetNewValue_Success_Options() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		personnageWork.getPersonnage().getProperty("Attributs#Erudition").setOptions(new int[]{1,2,3,4,5});

		// Execute
		boolean result = personnageBS.setNewValue(personnageWork, "4", "Attributs#Erudition");
		
		// Check
		Assert.assertTrue(result);
		Mockito.verify(personnageDao, Mockito.atLeastOnce()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertEquals(4, personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().getInt());
		Assert.assertEquals("Attributs#Erudition", personnageWork.getPersonnage().getHistory().get(0).getAbsoluteName());
	}
	
	@Test
	public void testSetNewValue_Fail_Options() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		personnageWork.getPersonnage().getProperty("Attributs#Erudition").setOptions(new int[]{1,3,5});

		// Execute
		boolean result = personnageBS.setNewValue(personnageWork, "4", "Attributs#Erudition");
		
		// Check
		Assert.assertFalse(result);
		Mockito.verify(personnageDao, Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertEquals(1, personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().getInt());
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
	}
	
	@Test
	public void testSetNewValue_Success_IntOffset() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().setOffset(new Integer(2));

		// Execute
		boolean result = personnageBS.setNewValue(personnageWork, "3", "Attributs#Erudition");
		
		// Check
		Assert.assertTrue(result);
		Mockito.verify(personnageDao, Mockito.atLeastOnce()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertEquals(3, personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().getInt());
		Assert.assertEquals("Attributs#Erudition", personnageWork.getPersonnage().getHistory().get(0).getAbsoluteName());
	}
	
	@Test
	public void testSetNewValue_Fail_IntOffset() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().setOffset(new Integer(2));

		// Execute
		boolean result = personnageBS.setNewValue(personnageWork, "4", "Attributs#Erudition");
		
		// Check
		Assert.assertFalse(result);
		Mockito.verify(personnageDao, Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertEquals(1, personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().getInt());
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
	}
	
	@Test
	public void testSetNewValue_Success_StringOffset() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		personnageWork.getPersonnage().getProperty("Nom").getValue().setOffset("a");

		// Execute
		boolean result = personnageBS.setNewValue(personnageWork, "aaaa", "Nom");
		
		// Check
		Assert.assertTrue(result);
		Mockito.verify(personnageDao, Mockito.atLeastOnce()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertEquals("aaaa", personnageWork.getPersonnage().getProperty("Nom").getValue().getString());
		Assert.assertEquals("Nom", personnageWork.getPersonnage().getHistory().get(0).getAbsoluteName());
	}
	
	@Test
	public void testSetNewValue_Fail_StringOffset() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		personnageWork.getPersonnage().getProperty("Nom").getValue().setOffset("a");

		// Execute
		boolean result = personnageBS.setNewValue(personnageWork, "tata", "Nom");
		
		// Check
		Assert.assertFalse(result);
		Mockito.verify(personnageDao, Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertEquals("", personnageWork.getPersonnage().getProperty("Nom").getValue().getString());
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
	}
	
	@Test
	public void testSetNewValue_Success_DoubleOffset() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		personnageWork.getPersonnage().getProperty("Compétences#Cartographie").setValue(new DoubleValue(0.0));
		personnageWork.getPersonnage().getProperty("Compétences#Cartographie").getValue().setOffset(new Double(2.0));
		personnageWork.getPersonnage().getProperty("Compétences#Cartographie").setMax(null);
		personnageWork.getPersonnage().getProperty("Compétences#Cartographie").setMin(null);

		// Execute
		boolean result = personnageBS.setNewValue(personnageWork, "4", "Compétences#Cartographie");
		
		// Check
		Assert.assertTrue(result);
		Mockito.verify(personnageDao, Mockito.atLeastOnce()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertEquals(4.0, personnageWork.getPersonnage().getProperty("Compétences#Cartographie").getValue().getDouble(), 0.1);
		Assert.assertEquals("Compétences#Cartographie", personnageWork.getPersonnage().getHistory().get(0).getAbsoluteName());
	}
	
	@Test
	public void testSetNewValue_Fail_DoubletOffset() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		personnageWork.getPersonnage().getProperty("Attributs#Erudition").setValue(new DoubleValue(1.0));
		personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().setOffset(new Double(2.0));

		// Execute
		boolean result = personnageBS.setNewValue(personnageWork, "4", "Attributs#Erudition");
		
		// Check
		Assert.assertFalse(result);
		Mockito.verify(personnageDao, Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertEquals(1, personnageWork.getPersonnage().getProperty("Attributs#Erudition").getValue().getInt());
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
	}
	
	@Test
	public void testSetNewValue_Success_NullOffset() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		personnageWork.getPersonnage().getProperty("Nom").getValue();

		// Execute
		boolean result = personnageBS.setNewValue(personnageWork, "Loïc", "Nom");
		
		// Check
		Assert.assertTrue(result);
		Mockito.verify(personnageDao, Mockito.atLeastOnce()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertEquals("Loïc", personnageWork.getPersonnage().getProperty("Nom").getValue().getString());
		Assert.assertEquals("Nom", personnageWork.getPersonnage().getHistory().get(0).getAbsoluteName());
	}
	
	@Test
	public void testAddPropertyFromOption_Success() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		
		// Execute
		boolean result = personnageBS.addPropertyFromOption(personnageWork, "Compétences#Connaissance spécialisée", "Droit", null);
		
		// Check
		Assert.assertTrue(result);
		Mockito.verify(personnageDao, Mockito.atLeastOnce()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertNotNull(personnageWork.getPersonnage().getProperty("Compétences#Connaissance spécialisée#Droit"));
		Assert.assertEquals("Compétences#Connaissance spécialisée#Droit", personnageWork.getPersonnage().getHistory().get(0).getAbsoluteName());
		
	}
	
	@Test
	public void testAddPropertyFromOption_Success_Specification() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		
		// Execute
		boolean result = personnageBS.addPropertyFromOption(personnageWork, "Faiblesses", "Ennemi - ", "Barbe noire");
		
		// Check
		Assert.assertTrue(result);
		Mockito.verify(personnageDao, Mockito.atLeastOnce()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertNotNull(personnageWork.getPersonnage().getProperty("Faiblesses#Ennemi - Barbe noire"));
		Assert.assertEquals("Faiblesses#Ennemi - Barbe noire", personnageWork.getPersonnage().getHistory().get(0).getAbsoluteName());
	}
	
	@Test
	public void testAddPropertyFromOption_Fail_MotherNull() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		
		// Execute
		boolean result = personnageBS.addPropertyFromOption(personnageWork, "Toto", "Droit", null);
		
		// Check
		Assert.assertFalse(result);
		Mockito.verify(personnageDao, Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertNull(personnageWork.getPersonnage().getProperty("Toto#Droit"));
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
	}
	
	@Test
	public void testAddPropertyFromOption_Fail_NoSubList() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		
		// Execute
		boolean result = personnageBS.addPropertyFromOption(personnageWork, "Attributs#Erudition", "Droit", null);
		
		// Check
		Assert.assertFalse(result);
		Mockito.verify(personnageDao, Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertNull(personnageWork.getPersonnage().getProperty("Attributs#Eruditio#Droit"));
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
	}
	
	@Test
	public void testAddPropertyFromOption_Fail_BadOption() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		
		// Execute
		boolean result = personnageBS.addPropertyFromOption(personnageWork, "Compétences#Connaissance spécialisée", "Toto", null);
		
		// Check
		Assert.assertFalse(result);
		Mockito.verify(personnageDao, Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertNull(personnageWork.getPersonnage().getProperty("Compétences#Connaissance spécialisée#Toto"));
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
	}
	
	@Test
	public void testAddPropertyFromOption_Fail_NullSpecification() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		
		// Execute
		boolean result = personnageBS.addPropertyFromOption(personnageWork, "Faiblesses", "Ennemi - ", null);
		
		// Check
		Assert.assertFalse(result);
		Mockito.verify(personnageDao, Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertTrue(personnageWork.getPersonnage().getProperty("Faiblesses").getSubProperties().isEmpty());
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
		
	}
	
	@Test
	public void testAddPropertyFromOption_Fail_EmptySpecification() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		
		// Execute
		boolean result = personnageBS.addPropertyFromOption(personnageWork, "Faiblesses", "Ennemi - ", "");
		
		// Check
		Assert.assertFalse(result);
		Mockito.verify(personnageDao, Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertTrue(personnageWork.getPersonnage().getProperty("Faiblesses").getSubProperties().isEmpty());
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
		
	}
	
	@Test
	public void testAddPropertyFromOption_Fail() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		
		// Execute
		boolean result = personnageBS.addPropertyFromOption(personnageWork, "Faiblesses", "Ennemi - ", "Barbe#noire");
		
		// Check
		Assert.assertFalse(result);
		Mockito.verify(personnageDao, Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertTrue(personnageWork.getPersonnage().getProperty("Faiblesses").getSubProperties().isEmpty());
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
	}
	
	@Test
	public void testAddFreeProperty_Success() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		
		// Execute
		boolean result = personnageBS.addFreeProperty(personnageWork, "Compétences#Religion", "Catholique");
		
		// Check
		Assert.assertTrue(result);
		Mockito.verify(personnageDao, Mockito.atLeastOnce()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertNotNull(personnageWork.getPersonnage().getProperty("Compétences#Religion#Catholique"));
		Assert.assertEquals("Compétences#Religion#Catholique", personnageWork.getPersonnage().getHistory().get(0).getAbsoluteName());
	}
	
	@Test
	public void testAddFreeProperty_Fail_NoMother() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		
		// Execute
		boolean result = personnageBS.addFreeProperty(personnageWork, "Compétences#NotExist", "Catholique");
		
		// Check
		Assert.assertFalse(result);
		Mockito.verify(personnageDao, Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertNull(personnageWork.getPersonnage().getProperty("Compétences#NotExist#Catholique"));
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
	}
	
	@Test
	public void testAddFreeProperty_Fail_NoSubProperties() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		personnageWork.getPersonnage().getProperty("Compétences#Religion").removeSubProperties();
		
		// Execute
		boolean result = personnageBS.addFreeProperty(personnageWork, "Compétences#Religion", "Catholique");
		
		// Check
		Assert.assertFalse(result);
		Mockito.verify(personnageDao, Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertNull(personnageWork.getPersonnage().getProperty("Compétences#Religion#Catholique"));
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
	}
	
	@Test
	public void testAddFreeProperty_Fail_NoDefaultProperty() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		personnageWork.getPersonnage().getProperty("Compétences#Religion").getSubProperties().setDefaultProperty(null);
		
		// Execute
		boolean result = personnageBS.addFreeProperty(personnageWork, "Compétences#Religion", "Catholique");
		
		// Check
		Assert.assertFalse(result);
		Mockito.verify(personnageDao, Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertNull(personnageWork.getPersonnage().getProperty("Compétences#Religion#Catholique"));
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
	}
	
	@Test
	public void testAddFreeProperty_Fail() throws Exception{
		// Prepare
		PersonnageWorkBO personnageWork = PersonnageWorkBSTest.buildPersonnageWork("Pavillon Noir");
		
		// Execute
		boolean result = personnageBS.addFreeProperty(personnageWork, "Compétences#Religion", "Catholique#");
		
		// Check
		Assert.assertFalse(result);
		Mockito.verify(personnageDao, Mockito.never()).savePersonnage(personnageWork.getPersonnageData());
		Assert.assertNull(personnageWork.getPersonnage().getProperty("Compétences#Religion#Catholique"));
		Assert.assertTrue(personnageWork.getPersonnage().getHistory().isEmpty());
	}
	
	
}
