package lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.controller;

import jakarta.security.auth.message.AuthException;
import jdk.javadoc.doclet.Reporter;
import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.model.Drone;
import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.model.Manager;
import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.service.DTO.DroneDTO;
import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.service.DroneService;
import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.service.ManagerService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private DroneService droneService;

    private Manager loggedManager;

    /**
     * Metodas vykdantis vartotojo registraciją sistemoje
     * @param manager Vartotojo objektas
     * @returns Grąžinama statuso žinutė
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Manager manager) {
        String response = managerService.register(manager);
        return ResponseEntity.ok(response);
    }

    /**
     * Metodas vykdantis vartotojo prijungimą prie sistemos
     * @param username Vartotojo slapyvardis
     * @param password Vartotojo slaptažodis
     * @return Grąžinama statuso žinutė
     * @throws LoginException
     */
    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam(name = "username") String username,
                                        @RequestParam(name = "password") String password) throws LoginException {
        String response = "";
        try {
            response = managerService.login(username, password);
            loggedManager = managerService.findManagerByUserName(username);
        } catch (AuthException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Metodas vykdantis drono sukūrimą duomenų bazėje
     * @param droneDTO Duomenų perdavimo objektas
     * @return Grąžinama statuso žinutė
     * @throws AuthException
     */
    @PostMapping("/createDroneInDb")
    public ResponseEntity<String> createDroneInDb(@RequestBody DroneDTO droneDTO) throws AuthException {
        if (loggedManager == null)
        {
            throw new AuthException("Manager controller: Need to log in");
        }

        String response = "";
        try {
            response = managerService.createDroneInDb(droneDTO, loggedManager.getUsername());
        } catch (UnsupportedOperationException e)
        {
            return ResponseEntity.badRequest().body("Manager Controller, failed to creade" + e.getMessage());
        }

        return ResponseEntity.ok(response);

    }

    /**
     * Metodas atnaujinantis drono informaciją duomenų bazėje
     * @param droneName Drono pavadinimas
     * @param droneDTO Drono duomenų perdavimo objektas
     * @returnGrąžinama statuso žinutė
     * @throws AuthException
     */
    @PutMapping("/updateDroneInDb")
    public ResponseEntity<String> updateDroneInDb(@RequestParam String droneName,
                                             @RequestBody DroneDTO droneDTO) throws AuthException {
        if (loggedManager == null)
        {
            throw new AuthException("Manager controller: Need to log in");
        }

        String response = "";

        try {
            response = managerService.updateDroneInDb(droneName, droneDTO, loggedManager.getUsername());
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.badRequest().body("Manager controller: Failed to update drone." + e.getMessage());
        }
         return ResponseEntity.ok(response);
    }

    /**
     * Metodas vykdantis drono pašalinimą iš duomenų bazės
     * @param droneName Drono pavadinimas
     * @return Grąžinama statuso žinutė
     * @throws AuthException
     */
    @DeleteMapping("/deleteDroneInDb")
    public ResponseEntity<String> deleteDroneInDb(@RequestParam String droneName) throws AuthException {
        if (loggedManager == null)
        {
            throw new AuthException("Manager controller: Need to log in");
        }

        String response = "";

        try {
            response = managerService.deleteDroneFromDb(droneName);
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.badRequest().body("Manager controller: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Metodas vykdantis visų dronų grąžinimą iš duomenų bazės
     * @return Grąžinama statuso žinutė ir dronų sąrašas
     * @throws AuthException
     */
    @GetMapping("/getAllFromDB")
    public ResponseEntity<List<Drone>> getAllFromDB() throws AuthException {
        if (loggedManager == null)
        {
            throw new AuthException("Manager controller: Need to log in");
        }

        List<Drone> drones = new ArrayList<>();

        try {
            drones = managerService.getAllDronesFromDB();
        } catch (UnsupportedOperationException e){
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(drones);

    }

    /**
     * Metodas vykdantis visų dronų suradimą pagal pavadinimą duomenų bazėje
     * @param droneName Drono pavadinimas
     * @return Grąžinama statuso žinutė ir drono objektas
     * @throws AuthException
     */
    @GetMapping("/getDroneFromDbByName")
    public ResponseEntity<Drone> getDroneFromDbByName(@RequestParam String droneName) throws AuthException {
        if (loggedManager == null)
        {
            throw new AuthException("Manager controller: Need to log in");
        }

        Drone response = new Drone();

        try {
            response = managerService.getFromDbByName(droneName);
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Metodas surandantis visus dronus, kurių kaina didesnė nurodyta
     * @param price Drono kaina
     * @return Grąžinama statuso žinutė ir dronų sąrašas
     * @throws AuthException
     */
    @GetMapping("/getDroneFromDbByPrice")
    public ResponseEntity<List<Drone>> getDroneFromDbByPrice(@RequestParam double price) throws AuthException {
        if (loggedManager == null)
        {
            throw new AuthException("Manager controller: Need to log in");
        }

        List<Drone> response = new ArrayList<>();

        try {
            response = managerService.getFromDbByPrice(price);
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Metodas vykdantis visų dronų perkėlimą iš XMl failo į duomenų bazę
     * @return Grąžinama statuso žinutė
     * @throws AuthException
     * @throws IOException
     */
    @PostMapping("/moveFromXmlToDb")
    public ResponseEntity<String> moveFromXmlToDb() throws AuthException, IOException {
        if (loggedManager == null)
        {
            throw new AuthException("Manager controller: Need to log in");
        }

        String response = "";

        try {
            managerService.moveDronesFromXmlToDB(loggedManager.getUsername());
        } catch (UnsupportedOperationException e){
            return ResponseEntity.badRequest().body("Manager controller: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }


//    XML FILE METHODS
//    -------------------------------------------------------------------------

    /**
     * Metodas vykdantis drono įrašymą į XML failą
     * @param droneDTO
     * @return Grąžinama statuso žinutė
     * @throws AuthException
     */
    @PostMapping("/addDroneToXml")
    public ResponseEntity<String> CreateDroneInXml(@RequestBody DroneDTO droneDTO) throws AuthException {
        if (loggedManager == null)
        {
            throw new AuthException("Manager controller: Need to log in");
        }

        String response = "";

        try {
            response = managerService.createDroneInXml(droneDTO);
        } catch (UnsupportedOperationException | IOException e) {
            return ResponseEntity.badRequest().body("Manager controller: failed to add drone to xml" + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Metodas vykdantis drono duomenų atnaujinimą XML faile
     * @param droneName Drono pavadinimas
     * @param droneDTO Drono duomenų perdavimo objektas
     * @return Grąžinama statuso žinutė
     * @throws AuthException
     */
    @PutMapping("/updateDroneInXml")
    public ResponseEntity<String> UpdateDroneInXml(@RequestParam String droneName,
                                                   @RequestBody DroneDTO droneDTO) throws AuthException {
        if (loggedManager == null)
        {
            throw new AuthException("Manager controller: Need to log in");
        }

        String response = "";

        try {
            response = managerService.updateDroneInXml(droneName ,droneDTO);
        } catch (UnsupportedOperationException | IOException e) {
            return ResponseEntity.badRequest().body("Manager controller: failed to update drone in xml" + e.getMessage());
        }

        return ResponseEntity.ok(response);

    }

    /**
     * Metodas vykdantis drono pašalinimą iš XMl failo
     * @param droneName Drono pavadinimas
     * @return Grąžinama statuso žinutė
     * @throws AuthException
     */
    @DeleteMapping("/deleteDroneFromXml")
    public ResponseEntity<String> DeleteDroneFromXml(String droneName) throws AuthException {
        if (loggedManager == null)
        {
            throw new AuthException("Manager controller: Need to log in");
        }

        String response = "";

        try {
            response = managerService.deleteDroneFromXml(droneName);
        } catch (UnsupportedOperationException | IOException e) {
            return ResponseEntity.badRequest().body("Manager controller: failed to delete drone from xml" + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Metodas vykdantis visų dronų nuskaitymą iš XML failo
     * @return Grąžinama statuso žinutė ir dronų sąrašas
     * @throws IOException
     */
    @GetMapping("/getAllFromXML")
    public ResponseEntity<List<Drone>> getAll() throws IOException {
        List<Drone> response = new ArrayList<>();
        try {
            response = managerService.getAllDronesFromXml();
        } catch (IOException e){
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Metodas vykdantis dronų paeišką pagal pavadinimą XML faile
     * @param droneName Drono pavadinimas
     * @return Grąžinama statuso žinutė ir drono objektas
     */
    @GetMapping("/getFromXmlByName")
    public ResponseEntity<Drone> getDroneFromXmlByName(@RequestParam String droneName){
        Drone response = new Drone();

        try {
            response = managerService.getDroneFromXMlByName(droneName);
        } catch (IOException e){
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Metodas vykdantis dronų paiešką XML faile, kurių kaina didesnė nei nurodyta
     * @param price Drono kaina
     * @return Grąžinama statuso žinutė ir dronų sąrašas
     */
    @GetMapping("/getFromXmlByPrice")
    public ResponseEntity<List<Drone>> getDroneFromXmlByPrice(@RequestParam double price){
        List<Drone> response = new ArrayList<>();

        try {
            response = managerService.getDroneFromXmlByPrice(price);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Metodas vykdantis visų dronų perkėlimą iš duomenų bazės i XML failą
     * @return Grąžinama statuso žinutė ir dronų sąrašas
     * @throws AuthException
     */
    @PostMapping("/moveAllFromDbToXml")
    public ResponseEntity<List<Drone>> MoveAllFromDbToXml() throws AuthException {
        if (loggedManager == null)
        {
            throw new AuthException("Manager controller: Need to log in");
        }

        List<Drone> response = new ArrayList<>();
        try {
            response = managerService.moveDronesFromDbToXml();
        } catch (UnsupportedOperationException | IOException e) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(response);
    }
}
