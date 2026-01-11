package pl.edu.wypozyczalnia.controller;

import org.springframework.web.bind.annotation.*;
import pl.edu.wypozyczalnia.model.Car;
import pl.edu.wypozyczalnia.repository.CarRepository;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarRepository carRepository;

    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @PostMapping
    public Car addCar(@RequestBody Car car) {
        return carRepository.save(car);
    }

    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable Long id) {
        carRepository.deleteById(id);
    }

    @GetMapping
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @PutMapping("/{id}")
    public Car updateCar(@PathVariable Long id, @RequestBody Car updatedCar) {
        return carRepository.findById(id)
                .map(car -> {
                    car.setBrand(updatedCar.getBrand());
                    car.setModel(updatedCar.getModel());
                    return carRepository.save(car);
                })
                .orElseThrow(() -> new RuntimeException("Car not found"));
    }

}
