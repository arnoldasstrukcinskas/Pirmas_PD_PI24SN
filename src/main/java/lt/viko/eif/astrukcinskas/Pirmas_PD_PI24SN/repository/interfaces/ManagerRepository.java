package lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.repository.interfaces;

import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Integer> {

    Manager findByUsername(String username);
}
