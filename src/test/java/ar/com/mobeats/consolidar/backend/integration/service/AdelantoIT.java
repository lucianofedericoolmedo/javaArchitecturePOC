package ar.com.mobeats.consolidar.backend.integration.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ar.com.mobeats.consolidar.backend.factory.TestingDataInsertion;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration("classpath:spring/services.xml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AdelantoIT {

    @Autowired
    TestingDataInsertion testingData;
    
    @Before
    public void setup() {

    }

    @Test
    public void crearAdelantoIT() throws InterruptedException {
    }

}
