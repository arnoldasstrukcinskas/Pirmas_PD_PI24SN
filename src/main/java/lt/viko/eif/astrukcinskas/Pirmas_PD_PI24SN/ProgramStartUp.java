package lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN;

import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.service.ManagerService;
import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProgramStartUp {

    @Autowired
    private ServerService server;

    @Autowired
    private ManagerService client;

    /**
     * Metodas paleidžiantis duomenų serverį
     * @return Grąžinama statuso žinutė
     */
    @GetMapping("/server_start")
    public ResponseEntity<String> startServer() {
        Thread serverThread = new Thread(){
            @Override
            public void run() {
                try {
                    server.launchServer();
                }
                catch (Exception e){
                    System.out.printf("Failed with launching server: %s%n", e);
                }
            }
        };
        System.out.println(serverThread.toString());
        serverThread.start();
        return ResponseEntity.ok("Server launched");
    }
}
