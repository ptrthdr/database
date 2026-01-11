package pl.edu.wypozyczalnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.wypozyczalnia.model.Car;
import pl.edu.wypozyczalnia.model.CarStatus;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByStatus(CarStatus status);

    List<Car> findByBrandContainingIgnoreCase(String brand);

    List<Car> findByStatusAndBrandContainingIgnoreCase(CarStatus status, String brand);
}
