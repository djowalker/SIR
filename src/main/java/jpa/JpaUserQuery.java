package jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import domain.*;

public class JpaUserQuery {

	private static String[] cmd = { "addPerson", "addHome", "addDevice", "getPerson", "getDevices","getAllPerson", "getAllHome","exit" };
	private boolean running = false;
	private Scanner reader;

	private EntityManager manager;

	public JpaUserQuery(EntityManager manager,Scanner reader) {
		this.manager = manager;
		this.reader = reader;
	}

	private void welcome() throws InterruptedException {
		System.out.println("Welcome - Commandes " + toStringCmd());
		String cmdTemp = reader.nextLine();
		execute(cmdTemp);
		while (running) {
			System.out.println("Commande :"+ toStringCmd());
			cmdTemp = reader.nextLine();
			execute(cmdTemp);
		}

	}

	private String toStringCmd() {
		String result = "";
		for (String str : cmd){
			result += str+" ";
		}
		return result;
	}

	private void execute(String cmdTemp) throws InterruptedException {
		int index = 0;
		for (String str : cmd) {
			if (str.equals(cmdTemp)) {
				executeCmd(index);
			}
			index++;
		}
	}

	private void executeCmd(int index) throws InterruptedException {
		switch (index){
			case 0 : 
				addPerson();
				break;
			case 1 : 
				addHome();
				break;
			case 2 :
				addDevice();
				break;
			case 3 : 
				getPerson();
				break;
			case 4 :
				getDevices();
				break;
			case 5 : 
				getAllPersonToString();
				break;
			case 6 :
				getAllHomeToString();
				break;
			case 7 :
				exit();
				break;
			default :
				//invalid();
				break;
		}
		
	}
	
	private void getAllPersonToString() {
		for(Person pers : getAllPerson()){
			System.out.println(pers.toString());
		}
	}
	private void getAllHomeToString() {
		for(Home hom : getAllHome()){
			System.out.println(hom.toString());
		}
	}

	private void getPerson() {
			System.out.println("Prenom de la personne :");
			String name = reader.nextLine();
			System.out.println("Nom de la personne :");
			String surname = reader.nextLine();
			Person own = getPerson(name,surname);
			System.out.println(own.toString());
	}

	private void getDevices() {
		System.out.println("Prenom de la personne :");
		String name = reader.nextLine();
		System.out.println("Nom de la personne :");
		String surname = reader.nextLine();
		Person own = getPerson(name,surname);
		for (IntelligentDevice dev : getDevices(own.getId())){
			System.out.println(dev.toString());
		}
	}

	private void addPerson(){
		System.out.println("Prenom :");
		String prenom = reader.nextLine();
		System.out.println("Nom :");
		String nom = reader.nextLine();
		System.out.println("Email :");
		String mail = reader.nextLine();
		Person p = new Person(prenom,nom,mail);
		manager.persist(p);	
	}
	
	private void addHome(){
		System.out.println("Ville :");
		String ville = reader.nextLine();
		System.out.println("Adresse :");
		String addr = reader.nextLine();
		System.out.println("Nombre de salle :");
		int rooms = reader.nextInt();
	    reader.nextLine();
		System.out.println("Surface (m²) :");
		int sur = reader.nextInt();
		reader.nextLine();
		System.out.println("Prenom du propriétaire :");
		String preown = reader.nextLine();
		System.out.println("Nom du propriétaire :");
		String nomown = reader.nextLine();
		Person own = getPerson(preown,nomown);
		Home h = new Home(addr,ville,rooms,sur,own);
		own.getHomes().add(h);
		manager.persist(h);
	}
	
	private void addDevice() throws InterruptedException{
		System.out.println("Type : (chauffage ou electronique)");
		String type = reader.nextLine().trim();
		boolean valid = (type.equals("chauffage") || type.equals("electronique"));
		while (!valid){
			System.out.println("Type invalide -> (chauffage ou electronique) :");
			type = reader.nextLine().trim();
			valid = (type.equals("chauffage") || type.equals("electronique"));
		}
		System.out.println("Référence :");
		String ref = reader.nextLine();
		System.out.println("Consommation moyenne (watt/heure):");
		int con = reader.nextInt();
		reader.nextLine();
		if (type.equals("chauffage")){
			System.out.println("Source d'énérgie :");
			String source = reader.nextLine();
			System.out.println("Adresse du domicile :");
			String add = reader.nextLine();
			Home h = getHomeByAdd(add);
			IntelligentDevice dev = new Heater(ref,h,con,source);
			h.getDevices().add(dev);
			manager.persist(dev);
		}
		else {
			System.out.println("Nom du device :");
			String name = reader.nextLine();
			System.out.println("Adresse du domicile :");
			String add = reader.nextLine();
			Home h = getHomeByAdd(add);
			IntelligentDevice dev = new Heater(ref,h,con,name);
			h.getDevices().add(dev);
			manager.persist(dev);
		}
		
	}

	
	private Home getHomeByAdd(String add) {
		return manager.createQuery("Select h From Home h where h.adresse=:a ", Home.class).
				setParameter("a", add).getSingleResult();
	}

	private void exit(){
		running = false;
		reader.close();
	}
	
	private int numberOfPerson() {
		return manager.createQuery("Select a From Person a", Person.class).getResultList().size();
	}

	private void createPersonAuto() {
		if (numberOfPerson() == 0) {
			Person p1 = new Person("Guy", "Ferdinand","guy@email");
			Person p2 = new Person("Louis", "Ferdinand","louis@email");
			Person p3 = new Person("Andy", "Ferdinand","andyemail");
			Home h = new Home("rue de Vaugirard", "Brest",3,75,p1);
			Home h2 = new Home("rue de la mer", "Lannion",1,30,p2);
			IntelligentDevice he1 = new Heater("123A", h, 10, "electrique");
			IntelligentDevice he2 = new Heater("123B", h2, 10, "gaz");
			IntelligentDevice ed = new ElectronicDevice("123V", h, 12, "SAMSUNG12");
			manager.persist(p1);
			manager.persist(p2);
			manager.persist(p3);
		} else {
			System.out.println("already created");
		}
	}

	private List<Person> getAllPerson() {
		return manager.createQuery("Select a From Person a", Person.class).getResultList();
	}
	
	private List<Home> getAllHome() {
		return manager.createQuery("Select h From Home h", Home.class).getResultList();
	}

	private Person getPerson(String prenom,String nom){
		return manager.createQuery("Select a From Person a where a.name=:p and a.surname=:n", Person.class).
				setParameter("n", nom).setParameter("p", prenom).getSingleResult();
	}
	
	private void listPerson() {
		List<Person>list = getAllPerson();
		System.out.println("Nombre de personne : " + list);
		for (Person pers : list) {
			System.out.println(pers.toString());
		}
	}

	@SuppressWarnings("unchecked")
	private List<Home> getHomes(long id) {
		return manager.createQuery("SELECT h From Person p Join p.homes h Where p.id=:longId")
				.setParameter("longId", id).getResultList();
	}

	private List<IntelligentDevice> getDevices(long id) {
		List<IntelligentDevice> result = new ArrayList<IntelligentDevice>();
		List<Home> listHome = getHomes(id);
		for (Home hom : listHome) {
			List<IntelligentDevice> listDev = hom.getDevices();
			for (IntelligentDevice dev : listDev) {
				result.add(dev);
			}
		}
		return result;
	}

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("example");
		EntityManager manager = factory.createEntityManager();
		EntityTransaction tx = manager.getTransaction();
		Scanner reader = new Scanner(System.in);
		JpaUserQuery test = new JpaUserQuery(manager,reader);
		test.running = true;
		while (test.running) {
			tx.begin();
			test.welcome();
			tx.commit();
		}
		manager.close();
		factory.close();
		System.out.println("done");
	}

}
