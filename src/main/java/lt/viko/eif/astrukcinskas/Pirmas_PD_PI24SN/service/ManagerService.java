package lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.service;

import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.model.Drone;
import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.model.Manager;
import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.repository.interfaces.ManagerRepository;
import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.service.DTO.DroneDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

@Service
public class ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private DroneService droneService;

    private String host = "localhost";
    private int port = 1234;
    private InetSocketAddress address = new InetSocketAddress(host, port);
    private Socket socket = new Socket();
    /**
     * Metodas užregistruojantis vartotoją duomenų bazėje
     * @param manager Vartotojo objektas
     * @return Grąžinamas užregistruoto vartotojo slapyvardį
     */
    public String register(Manager manager){
        Manager response = managerRepository.save(manager);

        return response.getUsername();
    }

    /**
     * Metodas, prijungiantis vartotoją prie sistemos
     * @param username Vartotojo slapyvardis
     * @param password Vartotojo slaptažodis
     * @return Grąžinamas prijungto vartotojo slaptažodis
     * @throws LoginException
     */
    public String login(String username, String password) throws LoginException, IOException {
        Manager manager = managerRepository.findByUsername(username);

        if (password.equals(manager.getPassword())){
            socket.connect(address);
            return "Logged in: " + manager.getUsername();
        } else {
            throw new LoginException("Failed to login");
        }
    }

    /**
     * Metodas kontaktuojantis su DronoServcie, kuriantis naują droną duomenų bazėje
     * @param droneDTO Drono duomenų perdavimo objektas
     * @param managerUsername Vartotojo slapyvardis
     * @return Grąžinamas tekstas apie operacijos būseną
     */
    public String createDroneInDb(DroneDTO droneDTO, String managerUsername){
        return droneService.createDroneInDb(droneDTO, managerUsername);
    }

    /**
     * Metodas kontaktuojantis su DronoService, atnaujinantis drono informaciją duomenų bazėje
     * @param droneName Drono pavadinimas
     * @param droneDTO Drono duomenų perdavimo objektas
     * @param managerName Vartotojo slapyvardis
     * @return Grąžinamas tekstas apie operacijos būseną
     */
    public String updateDroneInDb(String droneName, DroneDTO droneDTO, String managerName){
        return droneService.updateDroneInDb(droneName, droneDTO, managerName);
    }

    /**
     * Metodas kontaktuojantis su DronoService, pašalinantis droną iš duomenų bazės
     * @param droneName Drono pavadinimas
     * @return Grąžinamas tekstas apie operacijos būseną
     */
    public String deleteDroneFromDb(String droneName)
    {
        return droneService.deleteDroneFromDb(droneName);
    }

    /**
     * Metodas kontaktuojantis su DronoService, gaunantis visus dronus iš duomenų bazės
     * @return Grąžinamas tekstas apie operacijos būseną
     */
    public List<Drone> getAllDronesFromDB(){
        return droneService.getAllDronesFromDB();
    }

    /**
     * Metodas kontaktuojantis su DronoService, perkeliantis visus dronų duomenis
     * iš XML failo į duomenų bazę
     * @param managerUserName Vartotojo slapyvardis
     * @return Grąžinamas tekstas apie operacijos būseną
     * @throws IOException
     */
    public String moveDronesFromXmlToDB(String managerUserName) throws IOException {
        Socket socket = new Socket();
        socket.connect(address);
        return droneService.moveDronesFromXmlToDB(managerUserName, socket);
    }

    /**
     * Metodas kontaktuojantis su DronoService, surandantis droną duomenų bazėje pagal pavadinimą
     * @param droneName Drono pavadinimas
     * @return Grąžinamas drono objektas
     */
    public Drone getFromDbByName(String droneName){
        return droneService.getDroneFromDbByName(droneName);
    }

    /**
     * Metodas kontaktuojantis su DronoService, surandantis visus dronus, kurių kaina
     * aukštesnė nei nurodyta.
     * @param price Drono kaina
     * @return Gražinamas dronų sąrašas
     */
    public List<Drone> getFromDbByPrice(double price){
        return droneService.getDroneFromDbByPrice(price);
    }

//    XML FILE METHODS
//    --------------------------------------------------------------------

    /**
     * Metodas kontaktuojantis su DronoService, įrašantis naują droną į XML failą
     * @param droneDto Drono duomenų perdavimo objetkas
     * @return Grąžinamas tekstas apie operacijos būseną
     * @throws IOException
     */
    public String createDroneInXml(DroneDTO droneDto) throws IOException {
        return droneService.addDroneToXML(droneDto, this.socket);
    }

    /**
     * Metodas kontaktuojantis su DronoService, atnaujinantis norimo drono
     * informaciją XML faile
     * @param droneName Drono pavadinimas
     * @param droneDTO Drono duomenų perdavimo objetkas
     * @return Grąžinamas tekstas apie operacijos būseną
     * @throws IOException
     */
    public String updateDroneInXml(String droneName, DroneDTO droneDTO) throws IOException {
        return droneService.updateDroneInXML(droneName, droneDTO, this.socket);
    }

    /**
     * Metodas kontaktuojantis su DronoService, pašalinantis droną iš XML failo
     * @param droneName Drono pavadinimas
     * @return Grąžinamas tekstas apie operacijos būseną
     * @throws IOException
     */
    public String deleteDroneFromXml(String droneName) throws IOException{
        return droneService.deleteDroneInXML(droneName, this.socket);
    }

    /**
     * Metodas kontaktuojantis su DronoService, gaunantis visus dronus iš XML failo
     * @return Grąžinamas dronų sąrašas
     * @throws IOException
     */
    public List<Drone> getAllDronesFromXml() throws IOException {
        return droneService.getAllDronesFromXML(this.socket);
    }

    /**
     * Metodas kontaktuojantis su DronoService, perkeliantis visus dronus iš
     * duomenų bazės į XML failą
     * @return Grąžinamas dronų sąrašas
     * @throws IOException
     */
    public List<Drone> moveDronesFromDbToXml() throws IOException {
        return droneService.moveDronesFromDbToXml(this.socket);
    }

    /**
     * Metodas kontaktuojantis su DronoService, surandantis droną XML faile
     * pagal pavadinimą
     * @param name Drono pavadinimas
     * @return Grąžinamas drono objektas
     * @throws IOException
     */
    public Drone getDroneFromXMlByName(String name) throws IOException {
        Drone response = droneService.getDroneFromXmlByName(name, this.socket);

        if (response == null) {
            throw new RuntimeException("Manager service: failed to get drone by name: " + name);
        }

        return response;
    }

    /**
     * Metodas kontaktuojantis su DronoService, surandantis visus dronus XML faile,
     * kurių kaina didesnė nei nurodyta
     * @param price Drono kaina
     * @return Grąžinamas dronų sąrašas
     * @throws IOException
     */
    public List<Drone> getDroneFromXmlByPrice(double price) throws IOException {
        List<Drone> response = droneService.getDrobeFromXmlByPrice(price, this.socket);

        if (response == null){
            throw new RuntimeException("Manager  service: failed to get drone by price: " + String.valueOf(price));
        }
        return response;
    }

//    Helpers
//    Metodas kontroleriui

    /**
     * Metodas surandantis vartotoją pagal slapyvardį
     * @param username Vartotojo slapyvardis
     * @return Grąžinamas vartotojo objektas
     */
    public Manager findManagerByUserName(String username) {
        Manager manager = managerRepository.findByUsername(username);

        if (manager == null) {
            return null;
        }
        return manager;
    }

}

