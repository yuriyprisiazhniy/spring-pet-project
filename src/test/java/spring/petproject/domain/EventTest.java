package spring.petproject.domain;


import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;


public class EventTest {

	private Event event;

	@BeforeMethod
	public void initEvent() {
		event = new Event("aaa", 1.1);
		event.setRating(EventRating.HIGH);
	
		LocalDateTime now = LocalDateTime.now();
		
		event.addAirDateTime(now);
		event.addAirDateTime(now.plusDays(1));
		event.addAirDateTime(now.plusDays(2));
	}
	
	@Test
	public void testAddRemoveAirDates() {
		int size = event.getAirDates().size();
		
		LocalDateTime date = LocalDateTime.now().plusDays(5).truncatedTo(ChronoUnit.MINUTES);
		
		event.addAirDateTime(date);
		
		assertEquals(size+1, event.getAirDates().size());
		assertTrue(event.getAirDates().contains(date));
		
		event.removeAirDateTime(date);
		
		assertEquals(size, event.getAirDates().size());
		assertFalse(event.getAirDates().contains(date));		
	}
	
	@Test
	public void testCheckAirDates() {
		assertTrue(event.airsOnDate(LocalDate.now()));
		assertTrue(event.airsOnDate(LocalDate.now().plusDays(1)));
		assertFalse(event.airsOnDate(LocalDate.now().minusDays(10)));
		
		assertTrue(event.airsOnDates(LocalDate.now(), LocalDate.now().plusDays(10)));
		assertTrue(event.airsOnDates(LocalDate.now().minusDays(10), LocalDate.now().plusDays(10)));
		assertTrue(event.airsOnDates(LocalDate.now().plusDays(1), LocalDate.now().plusDays(1)));
		assertFalse(event.airsOnDates(LocalDate.now().minusDays(10), LocalDate.now().minusDays(5)));
		
		LocalDateTime time = LocalDateTime.now().plusHours(4);
		event.addAirDateTime(time);
		assertTrue(event.airsOnDateTime(time));
		time = time.plusHours(30);
		assertFalse(event.airsOnDateTime(time));
	}
	
	@Test
	public void testAddRemoveAuditoriums() {
		LocalDateTime time = event.getAirDates().first();
		
		assertTrue(event.getAuditoriums().isEmpty());
		
		event.assignAuditorium(time, new Auditorium());
		
		assertFalse(event.getAuditoriums().isEmpty());
		
		event.removeAuditoriumAssignment(time);
		
		assertTrue(event.getAuditoriums().isEmpty());
	}
	
	@Test
	public void testAddRemoveAuditoriumsWithAirDates() {
		LocalDateTime time = LocalDateTime.now().plusDays(10);
		
		assertTrue(event.getAuditoriums().isEmpty());
		
		event.addAirDateTime(time, new Auditorium());
		
		assertFalse(event.getAuditoriums().isEmpty());
		
		event.removeAirDateTime(time);
		
		assertTrue(event.getAuditoriums().isEmpty());
	}
	
	@Test
	public void testNotAddAuditoriumWithoutCorrectDate() {
		LocalDateTime time = LocalDateTime.now().plusDays(10);
		
		boolean result = event.assignAuditorium(time, new Auditorium());
		
		assertFalse(result);
		assertTrue(event.getAuditoriums().isEmpty());
		
		result = event.removeAirDateTime(time);
		assertFalse(result);
		
		assertTrue(event.getAuditoriums().isEmpty());
	}

}
