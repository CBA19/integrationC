package tn.esprit.spring.services;

import static org.junit.Assert.assertEquals;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.repository.EmployeRepository;


public class EmployeServiceImplTest {
	
	@Autowired
	EmployeServiceImpl employeServiceImpl;
	
	private static final Logger l = LogManager.getLogger(EmployeServiceImpl.class);
	
	@Test
	public void testAuthenticate() throws ParseException {
		l.info("In  authenticate : "); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date d = dateFormat.parse("2020-10-19");
		Employe emp=new Employe("benalaya", "chams", "chamesseddine.benalaya@esprit.tn",  "20205203",false, Role.ADMINISTRATEUR);
		Employe e=employeServiceImpl.authenticate(emp.getEmail(), emp.getPassword());
		assertEquals(emp.getPrenom(), e.getPrenom());
		
		l.info("Out of  authenticate : "); 
		 
	}


}
