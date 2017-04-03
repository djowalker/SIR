package domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.OptimisticLock;

@Entity
@Table(name="person")
public class Person {
	
	private Long id;
	private String name;
	private String surname;
	private String mail;
	private List<Person> friends = new ArrayList<Person>();
	private List<Home> homes = new ArrayList<Home>();
	
	public Person(){
	}
	
	public Person(String name, String surname,String m) {
		this.name = name;
		this.surname = surname;
		this.mail = m;
	}

	
	@Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

	public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	@ManyToMany 
	@OptimisticLock(excluded = false) 
	public List<Person> getFriends() {
		return friends;
	}

	public void setFriends(List<Person> friend) {
		this.friends = friend;
	}
	
	
	@OneToMany(cascade = {CascadeType.ALL})
	@JoinColumn(name="HOME_ID")
	public List<Home> getHomes() {
		return homes;
	}

	public void setHomes(List<Home> homes) {
		this.homes = homes;
	}
	
	@Override
    public String toString() {
        return "Person [ " + this.id+ " : "+ this.name + "  " + this.surname + " , " + this.mail 
        		+ " , nombre de maison : " + this.homes.size() + " , nombre d'amis :"+this.friends.size()
        		+" ]";
    }
}
