package spring.petproject.domain;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;


public class TestAuditorium {
	
	@Test(expected = IllegalStateException.class)
	public void testCountVipsIllegalState() {
		Auditorium a = new Auditorium();
		a.setVipSeats(Stream.of(1L,2L,3L).collect(Collectors.toSet()));
		assertEquals(0, a.countVipSeats(Arrays.asList(10L, 20L, 30L)));
		assertEquals(1, a.countVipSeats(Arrays.asList(10L, 2L, 30L)));
		assertEquals(2, a.countVipSeats(Arrays.asList(10L, 2L, 3L, 4L, 5L, 6L)));
	}


	@Test
	public void testCountVipsWithAll() {
		Auditorium a = new Auditorium();
		//Vip seats must be sub set of all seats
		a.setNumberOfSeats(5);
		a.setVipSeats(Stream.of(1L,2L,3L).collect(Collectors.toSet()));
		assertEquals(0, a.countVipSeats(Arrays.asList(10L, 20L, 30L)));
		assertEquals(1, a.countVipSeats(Arrays.asList(10L, 2L, 30L)));
		assertEquals(2, a.countVipSeats(Arrays.asList(10L, 2L, 3L, 4L, 5L, 6L)));
	}

	public void testGetAllSeats() {
	    Auditorium a = new Auditorium();
	    a.setNumberOfSeats(10);
	    assertEquals(10, a.getAllSeats().size());
	}

}
