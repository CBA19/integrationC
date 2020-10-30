package tn.esprit.spring.services;

import java.util.ArrayList;


import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.repository.ContratRepository;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.TimesheetRepository;

@Service
public class EmployeServiceImpl implements IEmployeService {

	@Autowired
	EmployeRepository employeRepository;
	@Autowired
	DepartementRepository deptRepoistory;
	@Autowired
	ContratRepository contratRepoistory;
	@Autowired
	TimesheetRepository timesheetRepository;

	private static final Logger l = LogManager.getLogger(EmployeServiceImpl.class);
	 
	@Override
	public Employe authenticate(String login, String password) {
		l.info("In  authenticate : "); 
		Employe e=employeRepository.getEmployeByEmailAndPassword(login, password);
		l.info("Out of  authenticate. "); 
		return e;
		 
	}

	@Override
	public int addOrUpdateEmploye(Employe employe) {
		l.info("In  addOrUpdateEmploye : "); 
		employeRepository.save(employe);
		l.info("Out of  addOrUpdateEmploye."); 
		return employe.getId();
	}


	public void mettreAjourEmailByEmployeId(String email, int employeId) {
		l.info("In  mettreAjourEmailByEmployeId : "); 
		Employe employe = employeRepository.findById(employeId).get();
		l.debug("Je vais  lancer la modification de l'email.");
		employe.setEmail(email);
		employeRepository.save(employe);
		l.info("Out of mettreAjourEmailByEmployeId." ); 

	}

	@Transactional	
	public void affecterEmployeADepartement(int employeId, int depId) {
		l.info("In affecterEmployeADepartement() : ");
		Departement depManagedEntity = deptRepoistory.findById(depId).get();
		Employe employeManagedEntity = employeRepository.findById(employeId).get();
		l.debug("Je vais  lancer une condition.");
		if(depManagedEntity.getEmployes() == null){
			l.debug("Department empty.");
			List<Employe> employes = new ArrayList<>();
			employes.add(employeManagedEntity);
			depManagedEntity.setEmployes(employes);
			l.debug("Employe ajouter au departement vide.");
		}else{
			l.debug("Department not empty.");
			depManagedEntity.getEmployes().add(employeManagedEntity);
			l.debug("Employe ajouter au departement.");
		}

		// à ajouter? 
		deptRepoistory.save(depManagedEntity); 
		l.info("Out of affecterEmployeADepartement.");

	}
	@Transactional
	public void desaffecterEmployeDuDepartement(int employeId, int depId)
	{
		l.info("In desaffecterEmployeDuDepartement() : ");
		Departement dep = deptRepoistory.findById(depId).get();

		l.debug("Je vais  lancer une boucle des employes du dep");
		int employeNb = dep.getEmployes().size();
		for(int index = 0; index < employeNb; index++){
			l.debug("Je viens de lancer une boucle des employes du dep");
			l.debug("Je vais tester si l'employe existe dans cet dep");
			if(dep.getEmployes().get(index).getId() == employeId){
				l.debug("Il existe");
				dep.getEmployes().remove(index);
				l.debug("Employe desaffecte du dep");
				break;//a revoir
			}
		}
		l.info("Out of desaffecterEmployeDuDepartement.");
	} 
	
	// Tablesapce (espace disque) 

	public int ajouterContrat(Contrat contrat) {
		l.info("In ajouterContrat() : ");
		contratRepoistory.save(contrat);
		int ref =contrat.getReference();
		l.info("Out of ajouterContrat() : ");
		return ref;
		
	}

	public void affecterContratAEmploye(int contratId, int employeId) {
		l.info("In affecterContratAEmploye() : ");

		Contrat contratManagedEntity = contratRepoistory.findById(contratId).get();
		Employe employeManagedEntity = employeRepository.findById(employeId).get();

		contratManagedEntity.setEmploye(employeManagedEntity);
		l.debug("Contrat affecte a l'employe.");
		contratRepoistory.save(contratManagedEntity);

		l.info("Out of affecterContratAEmploye() : ");
	}

	public String getEmployePrenomById(int employeId) {
		l.info("In getEmployePrenomById() : ");
		Employe employeManagedEntity = employeRepository.findById(employeId).get();
		String prenom= employeManagedEntity.getPrenom();
		l.info("Out of getEmployePrenomById() : ");
		return prenom;
	}
	 
	public void deleteEmployeById(int employeId)
	{
		Employe employe = employeRepository.findById(employeId).get();

		//Desaffecter l'employe de tous les departements
		//c'est le bout master qui permet de mettre a jour
		//la table d'association
		for(Departement dep : employe.getDepartements()){
			dep.getEmployes().remove(employe);
		}

		employeRepository.delete(employe);
	}

	public void deleteContratById(int contratId) {
		Contrat contratManagedEntity = contratRepoistory.findById(contratId).get();
		contratRepoistory.delete(contratManagedEntity);

	}

	public int getNombreEmployeJPQL() {
		return employeRepository.countemp();
	}

	public List<String> getAllEmployeNamesJPQL() {
		return employeRepository.employeNames();

	}

	public List<Employe> getAllEmployeByEntreprise(Entreprise entreprise) {
		return employeRepository.getAllEmployeByEntreprisec(entreprise);
	}

	public void mettreAjourEmailByEmployeIdJPQL(String email, int employeId) {
		employeRepository.mettreAjourEmailByEmployeIdJPQL(email, employeId);

	}
	public void deleteAllContratJPQL() {
		employeRepository.deleteAllContratJPQL();
	}

	public float getSalaireByEmployeIdJPQL(int employeId) {
		return employeRepository.getSalaireByEmployeIdJPQL(employeId);
	}

	public Double getSalaireMoyenByDepartementId(int departementId) {
		return employeRepository.getSalaireMoyenByDepartementId(departementId);
	}

	public List<Timesheet> getTimesheetsByMissionAndDate(Employe employe, Mission mission, Date dateDebut,
			Date dateFin) {
		return timesheetRepository.getTimesheetsByMissionAndDate(employe, mission, dateDebut, dateFin);
	}

	public List<Employe> getAllEmployes() {
		return (List<Employe>) employeRepository.findAll();
	}

}
