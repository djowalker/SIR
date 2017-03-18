package domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="home")
public class Home {

	private Long id;
	private String address;
	private String Town;
	private int rooms;
	private int surface;
	private List<Heater> heaters = new ArrayList<Heater>();
	private List<ElectronicDevice> devices = new ArrayList<ElectronicDevice>();
	
	public Home (){
	}

	public Home(String address, String town) {
		this.address = address;
		Town = town;
	}

	public Home(String address, String town, int rooms, int surface, List<Heater> heaters,
			List<ElectronicDevice> devices) {
		this.address = address;
		Town = town;
		this.rooms = rooms;
		this.surface = surface;
		this.heaters = heaters;
		this.devices = devices;
	}

	public Home(String town) {
		Town = town;
	}

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getTown() {
		return Town;
	}
	public void setTown(String town) {
		Town = town;
	}
	
	public int getRoom() {
		return rooms;
	}

	public void setRoom(int room) {
		this.rooms = room;
	}

	public int getSurface() {
		return surface;
	}

	public void setSurface(int surface) {
		this.surface = surface;
	}

	@OneToMany(mappedBy = "home", cascade = CascadeType.PERSIST)
	public List<Heater> getHeaters() {
		return heaters;
	}

	public void setHeaters(List<Heater> heaters) {
		this.heaters = heaters;
	}

	@OneToMany(mappedBy = "home", cascade = CascadeType.PERSIST)
	public List<ElectronicDevice> getDevices() {
		return devices;
	}

	public void setDevices(List<ElectronicDevice> devices) {
		this.devices = devices;
	}

	@Override
	public String toString() {
		return "Home [id=" + id + ", address=" + address + ", Town=" + Town + ", room=" + rooms + ", surface=" + surface
				+ ", heaters=" + heaters + ", devices=" + devices + "]";
	}
	
	
	
}
