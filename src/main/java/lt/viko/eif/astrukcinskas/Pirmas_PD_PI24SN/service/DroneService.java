package lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.service;

import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.model.Drone;
import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.repository.interfaces.DroneRepository;
import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.service.DTO.DroneDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

/**
 * Servisas, atsakingas už drono operacijas duomenų bazėje.
 */
@Service
public class DroneService {

    @Autowired
    private DroneRepository droneRepository;

    /**
     * Metodas pridedantis droną į duomenų bazę.
     * @param droneDTO Drono duomenų perdavimo objektas
     * @param managerName Vartotojo vardas, kuris pridėjo droną.
     * @return Tekstas su pridėto drono pavadinimu
     */
    public String createDroneInDb(DroneDTO droneDTO, String managerName) {
        Drone drone = new Drone();
        drone.setDroneName(droneDTO.getDroneName());
        drone.setBatteryCapacity(droneDTO.getBatteryCapacity());
        drone.setPrice(droneDTO.getPrice());
        drone.setDroneNew(droneDTO.getDroneNew());
        drone.setFramsShape(droneDTO.getFramsShape());
        drone.setAutonomus(droneDTO.isAutonomus());
        drone.setLastUpdateDate(LocalDateTime.now());
        drone.setManagerName(managerName);

        droneRepository.save(drone);

        return "Drone added to db: " + drone.getDroneName();
    }

    /**
     * Metodas atnaujinantis drono informaciją duomenų bazėje.
     * @param droneName Norimo atnaujinti drono pavadinimas
     * @param droneDTO Drono duomenų perdavimo objektas
     * @param managerName Vartotojo vardas, kurit atnaujino droną
     * @return Tekstas su atnaujinto drono pavadinimu
     */
    public String updateDroneInDb(String droneName, DroneDTO droneDTO, String managerName) {
        Drone drone = droneRepository.findByDroneName(droneName);
        drone.setDroneName(droneDTO.getDroneName());
        drone.setBatteryCapacity(droneDTO.getBatteryCapacity());
        drone.setPrice(droneDTO.getPrice());
        drone.setDroneNew(droneDTO.getDroneNew());
        drone.setFramsShape(droneDTO.getFramsShape());
        drone.setAutonomus(droneDTO.isAutonomus());
        drone.setLastUpdateDate(LocalDateTime.now());
        drone.setManagerName(managerName);

        droneRepository.save(drone);
        return "Updated drone with name: " + drone.getDroneName();
    }

    /**
     * Metodas ištrinantis droną iš duomenų bazės
     * @param droneName Norimo ištrinti drono pavadinimas
     * @return Tekstas su pašalinto drono pavadinimu
     */
    public String deleteDroneFromDb(String droneName) {
        Drone drone = droneRepository.findByDroneName(droneName);
        droneRepository.delete(drone);

        return "Deleted drone with name: " + drone.getDroneName();
    }


    /**
     * Metodas paimantis visus dronus iš duomenų bazės.
     * @return Grąžinamas dronų sąrašas
     */
    public List<Drone> getAllDronesFromDB() {
        return droneRepository.findAll();
    }

    /**
     * Metodas surandantis droną duomenų bazėje pagal pavadinimą.
     * @param droneName norimo surasti drono pavadinimas
     * @return Grąžinamas drono objektas
     */
    public Drone getDroneFromDbByName(String droneName) {
        return droneRepository.findByDroneName(droneName);
    }

    /**
     * Metodas paimantis visus dronis, kurių kaina didesnė nei nurodyta
     * @param price Norimų surasti dronų minimali kaina
     * @return Grąžinamas dronų sąrašas
     */
    public List<Drone> getDroneFromDbByPrice(double price) {
        return droneRepository.findAllByPriceGreaterThan(price);
    }

    /**
     * Metodas perkeliantis visus dronus iš XML failo į duomenų bazę
     * @param managerName Vartotojo atliekančio šį veiksmą pavadinimas
     * @param socket Socket jungtis, per kurią perduodami duomenys
     * @return Grąžinama žinutė apie veiksmo statusą.
     * @throws IOException asdasd
     */
    public String moveDronesFromXmlToDB(String managerName, Socket socket) throws IOException {
        List<Drone> dronesInXml = getAllDronesFromXML(socket);
        List<Drone> dronesInDb = getAllDronesFromDB();
        try {
            for (Drone droneInXml : dronesInXml) {
                if (!dronesInDb.contains(droneInXml)) {
                    DroneDTO droneDTO = fromDroneToDto(droneInXml);
                    var response = createDroneInDb(droneDTO, managerName);
                }
            }
        } catch (Exception e) {
            return "Drone service: Faield to move drone to databas";
        }

        return "Drones moved from xml to database";
    }

//    XML FILE METHODS
//    --------------------------------------------------------------------

    /**
     * Metodas įrašantis droną į XML failą
     * @param droneDTO Drono duomenų perdavimo objektas
     * @param socket Socket jungtis, per kurią perduodami duomenys
     * @return Grąžina tekstą su įrašyto drono pavadinimu
     * @throws IOException asdasd
     */
    public String addDroneToXML(DroneDTO droneDTO, Socket socket) throws IOException {
        String request = "add";

        Drone drone = fromDtoToDrone(droneDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        Scanner input = new Scanner(socket.getInputStream());
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        String droneJson = objectMapper.writeValueAsString(drone);


        out.println(request);
        out.println(droneJson);


        String json = input.nextLine();

        String response = objectMapper.readValue(json, new TypeReference<String>() {
        });

        return "Drone service: drone added to xml ->" + response;
    }

    /**
     * Metodas atnajinantis droną XML faile
     * @param droneName Norimo atnaujinti drono pavadinimas
     * @param droneDTO Drono duomenų perdavimo objektas
     * @param socket Socket jungtis, per kurią perduodami duomenys
     * @return Grąžina tekstą su atnaujinto drono pavadinimu
     * @throws IOException asdasdasda
     */
    public String updateDroneInXML(String droneName, DroneDTO droneDTO, Socket socket) throws IOException {
        String request = "update/";

        Drone drone = fromDtoToDrone(droneDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        Scanner input = new Scanner(socket.getInputStream());
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        String droneJson = objectMapper.writeValueAsString(drone);

        out.println(request + droneName);
        out.println(droneJson);

        String json = input.nextLine();

        String response = objectMapper.readValue(json, new TypeReference<String>() {
        });

        return "Drone service: drone updated in xml ->" + response;
    }

    /**
     * Metidas pašalinantis droną iš XML failo
     * @param droneName Norimo pašalinti drono pavadinimas
     * @param socket Socket jungtis, per kurią perduodami duomenys
     * @return Tekstas su pašalinto drono pavadinimu
     * @throws IOException
     */
    public String deleteDroneInXML(String droneName, Socket socket) throws IOException {
        String request = "delete/";

        ObjectMapper objectMapper = new ObjectMapper();
        Scanner input = new Scanner(socket.getInputStream());
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        out.println(request + droneName);

        String json = input.nextLine();

        String response = objectMapper.readValue(json, new TypeReference<String>() {
        });

        return "Drone service: drone deleted from xml ->" + droneName;
    }

    /**
     * Metodas perkeliantis visus dronus iš duomenų bazės į XML failą
     * @param socket Socket jungtis, per kurią perduodami duomenys
     * @return Grąžinamas perkeltų dronų sąrašas.
     * @throws IOException
     */
    public List<Drone> moveDronesFromDbToXml(Socket socket) throws IOException {
        String request = "move";

        List<Drone> dronesInDb = getAllDronesFromDB();

        ObjectMapper objectMapper = new ObjectMapper();
        Scanner input = new Scanner(socket.getInputStream());
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        String dronesJson = objectMapper.writeValueAsString(dronesInDb);

        output.println(request);
        output.println(dronesJson);

        String json = input.nextLine();

        List<Drone> response = objectMapper.readValue(json, new TypeReference<List<Drone>>() {
        });

        return response;
    }

    /**
     * Metodas surandantis drona XML faile pagal pateiktą drono pavadinimą
     * @param droneName Norimo surasti drono pavadinimas
     * @param socket Socket jungtis, per kurią perduodami duomenys
     * @return Gražinamas rastas drono objektas
     * @throws IOException
     */
    public Drone getDroneFromXmlByName(String droneName, Socket socket) throws IOException {
        String request = "byname/";

        ObjectMapper objectMapper = new ObjectMapper();
        Scanner input = new Scanner(socket.getInputStream());
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

        output.println(request + droneName);

        String json = input.nextLine();

        Drone response = objectMapper.readValue(json, new TypeReference<Drone>() {
        });

        return response;
    }

    /**
     * Metodas surandantis visus dronus XML faile, kurių kaina didesnė nei nurodyta.
     * @param price Minimali norimų rasti dronų kaina
     * @param socket Socket jungtis, per kurią perduodami duomenys
     * @return Gražinamas rastų dronų sąrašas
     * @throws IOException
     */
    public List<Drone> getDrobeFromXmlByPrice(double price, Socket socket) throws IOException {
        String request = "byprice/";

        ObjectMapper objectMapper = new ObjectMapper();
        Scanner input = new Scanner(socket.getInputStream());
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

        output.println(request + String.valueOf(price));

        String json = input.nextLine();

        List<Drone> response = objectMapper.readValue(json, new TypeReference<List<Drone>>() {
        });

        return response;
    }

    /**
     * Metodas paimantis visus dronus iš XML failo
     * @param socket Socket jungtis, per kurią perduodami duomenys
     * @return Grąžinamas dronų sąrašas
     * @throws IOException
     */
    public List<Drone> getAllDronesFromXML(Socket socket) throws IOException {
        String request = "all";
        ObjectMapper objectMapper = new ObjectMapper();
        Scanner input = new Scanner(socket.getInputStream());
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        System.out.println("klientas: issiunte");
        out.println(request);
        System.out.println("klientas: gauna");
        String json = input.nextLine();
        System.out.println("klientas: gavo");

        List<Drone> drones = objectMapper.readValue(json, new TypeReference<List<Drone>>() {
        });

        return drones;
    }


    //    Helper classes for conversion.

    /**
     * Pagalbinis metodas konvertuojantis drono duomenų perdavimo objektą į drono objektą
     * @param droneDTO Drono duomenų pervadimo objektas
     * @return Grąžinama drono klasė
     */
    public Drone fromDtoToDrone(DroneDTO droneDTO) {
        Drone drone = new Drone();
        drone.setDroneName(droneDTO.getDroneName());
        drone.setBatteryCapacity(droneDTO.getBatteryCapacity());
        drone.setPrice(droneDTO.getPrice());
        drone.setDroneNew(droneDTO.getDroneNew());
        drone.setFramsShape(droneDTO.getFramsShape());
        drone.setAutonomus(droneDTO.isAutonomus());
        drone.setLastUpdateDate(droneDTO.getLastUpdateDate());
        drone.setManagerName(droneDTO.getManagerName());
        return drone;
    }

    /**
     * Pagalbinis metodas konvertuojantis drono objektą į duomenų perdavomi objektą.
     * @param drone Drono objektas
     * @return Grąžinamas drono duomenų perdavimo objektas
     */
    public DroneDTO fromDroneToDto(Drone drone) {
        DroneDTO droneDto = new DroneDTO();
        droneDto.setDroneName(drone.getDroneName());
        droneDto.setBatteryCapacity(drone.getBatteryCapacity());
        droneDto.setPrice(drone.getPrice());
        droneDto.setDroneNew(drone.getDroneNew());
        droneDto.setFramsShape(drone.getFramsShape());
        droneDto.setAutonomus(drone.isAutonomus());
        droneDto.setLastUpdateDate(drone.getLastUpdateDate());
        droneDto.setManagerName(drone.getManagerName());
        return droneDto;
    }

}
