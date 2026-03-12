package lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.service;

import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.model.Drone;
import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.repository.ServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Service
public class ServerService {

    public ServerService(){
        System.out.println("Service created");
    }

    @Autowired
    private ServerRepository serverRepository;
    boolean serverRunning = false;

    /**
     * Metodas, paleidžiantis duomenų serverį, komunikacijai su XML failu.
     * @throws IOException
     */
    public void launchServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        this.serverRunning = true;
        serverRepository.setFilePath("src/main/resources/data/drones.xml");
        System.out.println("Serveris paleistas, laukiamos uzklausos");

        while (serverRunning) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("serveris: Klientas prisijunge");

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Scanner input = new Scanner(socket.getInputStream());

                String request = input.nextLine();

                if (request.contains("add")){
                    String response = addDroneToXml(input);
                    sendData(response, out);
                } else if (request.contains("update")){
                    String[] requestParts = request.split("/");
                    String droneName = new String(requestParts[1]);
                    String response = updateDroneInXml(input, droneName);
                    sendData(response, out);
                } else if (request.contains("delete")) {
                    String[] requestParts = request.split("/");
                    String droneName = new String(requestParts[1]);
                    String response = deleteDroneFromXml(droneName);
                    sendData(response, out);
                } else if (request.contains("move")) {
                    List<Drone> response = moveDronesFromDbToXml(input);
                    sendData(response, out);
                } else if (request.contains("byname")) {
                    String[] requestParts = request.split("/");
                    String droneName = new String(requestParts[1]);
                    Drone drone = getDroneFromXmlByName(droneName);
                    sendData(drone, out);
                } else if (request.contains("byprice")) {
                    String [] requestParts = request.split("/");
                    String dronePrice = new String(requestParts[1]);
                    System.out.println(dronePrice);
                    List<Drone> response = getDroneFromXmlByPrice(dronePrice);
                    sendData(response, out);
                } else if (request.equals("all")) {
                    List<Drone> drones = serverRepository.getAllDrones().getDrones();
                    sendData(drones, out);
                } else {
                    out.println("Error");
                    serverSocket = new ServerSocket(1234);
                }

                socket.close();

            } catch (IOException e) {
                System.out.println("Service klaida");
            }
        }

        serverSocket.close();
    }

    /**
     * Metodas, per socket jugntį, siunčiantis duomenis į dronų valdymo failą(DroneService)
     * @param response siunčiami duomenys
     * @param out Duomenų išsiuntimo srautas, perduodantis duomenis iš serverio - klientui
     * @throws IOException
     */
    private void sendData(Object response, PrintWriter out) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(response);
        out.println(json);
        System.out.printf("Server service: Data sent ");
    }

    /**
     * Metodas įrašantis droną į xml failą.
     * @param input Duomenų įvesties srautas, gaunantis duomenis iš kliento - serveriui.
     * @return Tekstinį pranešimą: sėkmės atveju grąžinama patvirtinimo žinutė iš repozitorijos,
     * o jei dronas jau egzistuoja arba įvyksta klaida – pranešimas apie nesėkmę.
     */

    public String addDroneToXml(Scanner input){
        ObjectMapper objectMapper = new ObjectMapper();

        String droneJson = input.nextLine();
        Drone drone = objectMapper.readValue(droneJson, Drone.class);
        List<Drone> drones = serverRepository.getAllDrones().getDrones();
        if (drones == null)
        {
            drones = new ArrayList<>();
        }

        if (!drones.contains(drone))
        {
            drones.add(drone);

            return serverRepository.writeAllDronesToFile(drones);
        }

        return "Server service: drone was not added to xml";
    }


    /**
     * Metodas atnaujinantis drono duomenis XML faile
     * @param input Duomenų įvesties srautas, gaunantis duomenis iš kliento - serveriui.
     * @param droneName drono kuris turi būti atnaujinamas - pranešimas.
     * @return Tekstinį pranešimą: sėkmės atveju grąžinama patvirtinimo žinutė iš repozitorijos,
     * o jei dronas jau egzistuoja arba įvyksta klaida – pranešimas apie nesėkmę.
     */
    public String updateDroneInXml(Scanner input, String droneName) {
        ObjectMapper objectMapper = new ObjectMapper();

        String droneJson = input.nextLine();
        Drone responseDrone = objectMapper.readValue(droneJson, Drone.class);
        List<Drone> drones = serverRepository.getAllDrones().getDrones();
        int droneIndex = 0;

        for (Drone drone : drones){
            if(drone.getDroneName().equals(droneName)){
                drones.set(droneIndex, responseDrone);
                return serverRepository.writeAllDronesToFile(drones);
            }
            droneIndex++;
        }
        return "Server service: drone was not added to xml";
    }

    /**
     * Metodas, pašalinantis norimą droną iš XML failo
     * @param droneName norimo pašalinto drono pavadinimas
     * @return Tekstinį pranešimą: sėkmės atveju grąžinama patvirtinimo žinutė iš repozitorijos,
     * o jei dronas jau egzistuoja arba įvyksta klaida – pranešimas apie nesėkmę.
     */
    public String deleteDroneFromXml(String droneName){
        List<Drone> drones = serverRepository.getAllDrones().getDrones();

        for(Drone drone : drones){
            if(drone.getDroneName().equals(droneName)){
                drones.remove(drone);

                return serverRepository.writeAllDronesToFile(drones);
            }

        }
        return "Server service: drone was not deleted from xml";
    }

    /**
     * Perkelia dronus iš duomenų bazės į XML failą.
     * @param input Duomenų įvesties srautas, gaunantis duomenis iš kliento - serveriui.
     * @return Gražina pridėtų į XML failą dronų sąrašą.
     */
    public List<Drone> moveDronesFromDbToXml(Scanner input) {
        ObjectMapper objectMapper = new ObjectMapper();

        String dronesJson = input.nextLine();
        Drone[] requestDronesArray = objectMapper.readValue(dronesJson, Drone[].class);
        List<Drone> requestDrones = Arrays.asList(requestDronesArray);

        List<Drone> addedDrones = new ArrayList<>();
        List<Drone> dronesInXml = serverRepository.getAllDrones().getDrones();
        for(Drone drone : requestDrones) {
            if (!dronesInXml.contains(drone)){
                addedDrones.add(drone);
            }
        }
        dronesInXml.addAll(addedDrones);
        String response = serverRepository.writeAllDronesToFile(dronesInXml);
        return addedDrones;
    }

    /**
     * Metodas, surandantis droną pagal pavadinimą XML faile.
     * @param droneName norimo rasti drono pavadinimas.
     * @return Sėkmės atveju rasto drono objektas, nesėkmės - null reikšmė
     */
    public Drone getDroneFromXmlByName(String droneName){
        List<Drone> dronesInXml = serverRepository.getAllDrones().getDrones();
        for(Drone drone : dronesInXml) {
            if (drone.getDroneName().equals(droneName)) {
                return drone;
            }
        }
        return null;
    }

    /**
     * Metodas, visus dronus su didesne kaina nei nurodyta.
     * @param price norimo rasti drono kaina.
     * @return Sugąžina rastų dronų sąrašą.
     */
    public List<Drone> getDroneFromXmlByPrice(String price){
        List<Drone> dronesInXml = serverRepository.getAllDrones().getDrones();

        List<Drone> sortedDrones = new ArrayList<>();

        double dronePrice = Double.valueOf(price);
        for(Drone drone : dronesInXml) {
            if(drone.getPrice() >= dronePrice){
                sortedDrones.add(drone);
            }
        }
        return sortedDrones;
    }
}
