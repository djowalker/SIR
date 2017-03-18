package jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import domain.ElectronicDevice;
import domain.Heater;
import domain.Home;
import domain.Person;


public class JpaTest {

	private EntityManager manager;

	public JpaTest(EntityManager manager) {
		this.manager = manager;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("example");
		EntityManager manager = factory.createEntityManager();
		JpaTest test = new JpaTest(manager);

		EntityTransaction tx = manager.getTransaction();
		tx.begin();
			try{
				test.createPerson();
			} catch(Exception e){
				e.printStackTrace();
			}		
				
		tx.commit();
		
		test.listPerson();

		manager.close();
		System.out.println("done");
	}
	
	private void createPerson(){
		int nomOfPerson = manager.createQuery("Select a From Person a",Person.class).getResultList().size();
		if(nomOfPerson == 0){
			Home h = new Home("rue de Vaugirard", "Brest");
			
			Heater he = new Heater("24A", 35);
			List<Heater> hel = new ArrayList<Heater>();
			List<ElectronicDevice> edl = new ArrayList<ElectronicDevice>();
			
			ElectronicDevice ed = new ElectronicDevice("TV", "SAMSUNG12", 12);
			edl.add(ed);
			
			Home h2 = new Home("rue de test","Lannion",4,100,hel,edl);
			manager.persist(h);
			manager.persist(h2);
			manager.persist(he);
			manager.persist(ed);
			
			manager.persist(new Person("Guy","Ferdinand"));
			manager.persist(new Person("Louis","Ferdinand"));
			manager.persist(new Person("Andy","Ferdinand"));
		}
	}
	
	private void listPerson(){
		List<Person> resultList = manager.createQuery("Select a From Person a", Person.class).getResultList();
		System.out.println("num of person : " + resultList.size());
		for(Person pers : resultList){
			System.out.println("next PErson : " + pers.getName() + " " + pers.getSurname());
		}
	}

	


}
