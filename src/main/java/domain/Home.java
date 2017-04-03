package domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
public class Home {

	private Long id;
	private String adresse;
	private String ville;
	private int rooms;
	private int surface;
	private List<IntelligentDevice> devices = new ArrayList<IntelligentDevice>();
	private Person owner;
	
	public Home (){
	}

	public Home(String ad, String vi,int ro,int su,Person own) {
		adresse = ad;
		ville = vi;
		rooms = ro;
		surface = su;
		owner = own;
	}

	@Id
	@GeneratedValue (strategy= GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	
	@Column (name ="ville")
	public String getVille() {
		return ville;
	}
	public void setVille(String town) {
		ville = town;
	}
	
	@Column (name ="rooms")
	public int getRooms() {
		return rooms;
	}

	public void setRooms(int room) {
		this.rooms = room;
	}

	@ManyToOne
	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}
	
	@Column (name ="surf")
	public int getSurface() {
		return surface;
	}

	public void setSurface(int surface) {
		this.surface = surface;
	}

	@OneToMany(cascade = CascadeType.ALL)
	public List<IntelligentDevice> getDevices() {
		return devices;
	}

	public void setDevices(List<IntelligentDevice> devices) {
		this.devices = devices;
	}

	@Override
	public String toString() {
		return "Home [id=" + id + ", address=" + adresse + ", Town=" + ville + ", room=" + rooms + ", surface=" + surface
			 + ", owner=" + owner.getName() +" "+owner.getSurname()+ "]";
	}
	
	
	
}
