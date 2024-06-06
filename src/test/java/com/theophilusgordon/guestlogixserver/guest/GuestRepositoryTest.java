package com.theophilusgordon.guestlogixserver.guest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class GuestRepositoryTest {
	@Autowired
	private GuestRepository guestRepository;

	@Autowired
	private TestEntityManager testEntityManager;

}
