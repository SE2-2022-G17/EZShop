package it.polito.ezshop.teamTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.model.Dummy;
import it.polito.ezshop.model.PersistencyManager;
import it.polito.ezshop.model.Storable;
import it.polito.ezshop.model.UserImpl;

public class TestPersistency {
	
	@Test
	public void testStoreNull() {
		PersistencyManager persistencyManager = new PersistencyManager();
		assertFalse(persistencyManager.store(null));
	}
	
	@Test 
	public void testStoreNotExistingFile() {
		PersistencyManager persistencyManager = new PersistencyManager();
		Storable s = new Storable() {
			
			@Override
			public String getCSV() {
				return null;
			}
		};
		assertNotNull(s);
		assertFalse(persistencyManager.store(s));
	}
	
	@Test
	public void testStoreSuccessful() {
		PersistencyManager persistencyManager = new PersistencyManager();
		Storable dummy = new Dummy();
		assertTrue(persistencyManager.store(dummy));
	}
	
	@Test 
	public void testDeleteNull() {
		PersistencyManager persistencyManager = new PersistencyManager();
		assertFalse(persistencyManager.delete(null));		
	}
	
	@Test
	public void testDeleteNotExistingFile() {
		PersistencyManager persistencyManager = new PersistencyManager();
		Storable s = new Storable() {
			
			@Override
			public String getCSV() {
				return null;
			}
		};
		assertNotNull(s);
		assertFalse(persistencyManager.delete(s));	
	}
	
	@Test 
	public void testDeleteSuccessful() {
		PersistencyManager persistencyManager = new PersistencyManager();
		Storable dummy = new Dummy();
		assertTrue(persistencyManager.store(dummy));
		
		assertTrue(persistencyManager.delete(dummy));
	}
	
	@Test
	public void testUpdateNull() {
		PersistencyManager persistencyManager = new PersistencyManager();
		assertFalse(persistencyManager.update(null));			
	}
	
	@Test
	public void testUpdateNotExistingFile() {
		PersistencyManager persistencyManager = new PersistencyManager();
		Storable s = new Storable() {
			
			@Override
			public String getCSV() {
				return null;
			}
		};
		assertNotNull(s);
		assertFalse(persistencyManager.update(s));	
	}
	
	@Test 
	public void testUpdateSuccessful() {
		PersistencyManager persistencyManager = new PersistencyManager();
		Storable dummy = new Dummy();
		assertTrue(persistencyManager.store(dummy));
		
		assertTrue(persistencyManager.update(dummy));
	}
	
	@Test
	public void testGetFileNull() {
		PersistencyManager persistencyManager = new PersistencyManager();
		assertEquals("", persistencyManager.getFile(null));			
	}
	
	
	@Test
	public void testGetFileNotExisting() {
		PersistencyManager persistencyManager = new PersistencyManager();
		Storable s = new Storable() {
			
			@Override
			public String getCSV() {
				return null;
			}
		};
		assertNotNull(s);
		assertEquals("", persistencyManager.getFile(s));	
	}
	
	
	@Test 
	public void testGetFileSuccessful() {
		PersistencyManager persistencyManager = new PersistencyManager();
		Storable dummy = new Dummy();
		
		assertEquals("dataCSV/dummy.csv", persistencyManager.getFile(dummy));
	}
	
	
	
	

}
