package jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Parameter;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SetAttribute;

import domain.*;

public class JpaUserQuery {

	private static String[] cmd = { "addPerson", "addHome", "addDevice", "getPerson", "getDevices","getAllPerson", "getAllHome","exit" };
	private boolean running = false;
	private Scanner reader;

	private EntityManager manager;
	private CriteriaBuilder cb;
	
	public JpaUserQuery(EntityManager manager,Scanner reader) {
		this.manager = manager;
		this.reader = reader;
		this.cb = this.manager.getCriteriaBuilder();
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
		System.out.println("Surface (m�) :");
		int sur = reader.nextInt();
		reader.nextLine();
		System.out.println("Prenom du propri�taire :");
		String preown = reader.nextLine();
		System.out.println("Nom du propri�taire :");
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
		System.out.println("R�f�rence :");
		String ref = reader.nextLine();
		System.out.println("Consommation moyenne (watt/heure):");
		int con = reader.nextInt();
		reader.nextLine();
		if (type.equals("chauffage")){
			System.out.println("Source d'�n�rgie :");
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

	private void addFriend(){
		String name= "",surname = "";
		Person person = getPerson(name,surname);
		Person friend = getPerson(name,surname);
		friend.getFriends().add(person);
		person.getFriends().add(friend);
	}
	
	private Home getHomeByAdd(String add) {
//		return manager.createQuery("Select h From Home h where h.adresse=:a ", Home.class).
//				setParameter("a", add).getSingleResult();
		 CriteriaQuery<Home> q = cb.createQuery(Home.class);
		 Root<Home> c = q.from(Home.class);
		 Expression<String>path = c.get("adresse");
		 Predicate condition = cb.like(path, add);
		 q.where(condition);
		 TypedQuery<Home> qT = manager.createQuery(q);
		 return qT.getSingleResult();
	}

	private void exit(){
		running = false;
		reader.close();
	}
	
	private int numberOfPerson() {
//		return manager.createQuery("Select a From Person a", Person.class).getResultList().size();
		 CriteriaQuery<Person> q = cb.createQuery(Person.class);
		 Root<Person> c = q.from(Person.class);
		 q.select(c);
		 TypedQuery<Person> qT = manager.createQuery(q);
		 return qT.getResultList().size();
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
//		return manager.createQuery("Select a From Person a", Person.class).getResultList();
		 CriteriaQuery<Person> q = cb.createQuery(Person.class);
		 Root<Person> c = q.from(Person.class);
		 q.select(c);
		 TypedQuery<Person> qT = manager.createQuery(q);
		 return qT.getResultList();
	}
	
	private List<Home> getAllHome() {
//		return manager.createQuery("Select h From Home h", Home.class).getResultList();
		 CriteriaQuery<Home> q = cb.createQuery(Home.class);
		 Root<Home> c = q.from(Home.class);
		 q.select(c);
		 TypedQuery<Home> qT = manager.createQuery(q);
		 return qT.getResultList();
	}

	private Person getPerson(String prenom,String nom){
//		return manager.createQuery("Select a From Person a where a.name=:p and a.surname=:n", Person.class).
		 CriteriaQuery<Person> q = cb.createQuery(Person.class);
		 Root<Person> c = q.from(Person.class);
		 ParameterExpression<String> n = cb.parameter(String.class);
		 ParameterExpression<String> p = cb.parameter(String.class);
		 Expression<String>pathP = c.get("name");
		 Expression<String>pathN = c.get("surname");
		 Predicate condition = cb.like(pathN, n);
		 Predicate condition2 = cb.like(pathP,p);
		 q.select(c).where(condition,condition2);
		 TypedQuery<Person> qT = manager.createQuery(q);
		 return qT.setParameter(n, nom).setParameter(p, prenom).getSingleResult();
	}
	
	private void listPerson() {
		List<Person>list = getAllPerson();
		System.out.println("Nombre de personne : " + list);
		for (Person pers : list) {
			System.out.println(pers.toString());
		}
	}

	@SuppressWarnings("unchecked")
	private List<Home> getHomes(Long id) {
//		return manager.createQuery("SELECT h From Person p Join p.homes h Where p.id=:longId").setParameter("longId", id).getResultList();
		 CriteriaQuery<Home> q = cb.createQuery(Home.class);
		 Metamodel m = manager.getMetamodel();
		 EntityType<Person> Person_ = m.entity(Person.class);

		 Root<Person> c = q.from(Person.class);
		 Join<Person, Home> homes = c.joinList("homes");
		 ParameterExpression<Long> p = cb.parameter(Long.class);
		 Expression<Long>pathP = c.get("id");
		 Predicate condition = cb.equal(pathP,p);
		 q.select(homes).where(condition);
		 TypedQuery<Home> qT = manager.createQuery(q);
		 return qT.setParameter(p, id).getResultList();
	}

	private List<IntelligentDevice> getDevices(Long id) {
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
