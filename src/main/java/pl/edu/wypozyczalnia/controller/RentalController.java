package pl.edu.wypozyczalnia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import pl.edu.wypozyczalnia.model.*;
import pl.edu.wypozyczalnia.repository.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/rentals")
public class RentalController {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;

    public RentalController(
            RentalRepository rentalRepository,
            UserRepository userRepository,
            CarRepository carRepository) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.carRepository = carRepository;
    }

    // ================= WYPOŻYCZ =================
    @PostMapping("/rent")
    @Transactional
    public String rentCar(
            @RequestParam Long userId,
            @RequestParam Long carId,
            @RequestParam LocalDate endDate) {

        // sprawdzamy czy auto jest dostępne
        if (rentalRepository.existsByCarIdAndActiveTrue(carId)) {
            throw new RuntimeException("Car is already rented");
        }

        User user = userRepository.findById(userId).orElseThrow();
        Car car = carRepository.findById(carId).orElseThrow();

        // tworzymy wypożyczenie
        Rental rental = new Rental();
        rental.setUser(user);
        rental.setCar(car);
        rental.setStartDate(LocalDate.now());
        rental.setEndDate(endDate);
        rental.setActive(true);

        // zmieniamy status auta
        car.setStatus(CarStatus.RENTED);

        rentalRepository.save(rental);
        carRepository.save(car);

        return "redirect:/view/cars";
    }

    // ================= ZWRÓĆ =================
    @PostMapping("/return/{carId}")
    @Transactional
    public String returnCar(@PathVariable Long carId) {

        Rental rental = rentalRepository
                .findByCarIdAndActiveTrue(carId)
                .orElseThrow();

        rental.setActive(false);
        rentalRepository.save(rental);

        Car car = rental.getCar();
        car.setStatus(CarStatus.AVAILABLE);
        carRepository.save(car);

        return "redirect:/view/cars";
    }
}
