package pl.edu.wypozyczalnia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import pl.edu.wypozyczalnia.model.Car;
import pl.edu.wypozyczalnia.model.CarStatus;
import pl.edu.wypozyczalnia.repository.CarRepository;
import pl.edu.wypozyczalnia.repository.RentalRepository;
import pl.edu.wypozyczalnia.view.CarView;

@Controller
@Transactional
public class ViewController {

    private final CarRepository carRepository;
    private final RentalRepository rentalRepository;

    public ViewController(CarRepository carRepository,
            RentalRepository rentalRepository) {
        this.carRepository = carRepository;
        this.rentalRepository = rentalRepository;
    }

    // ================= POMOCNICZA METODA =================
    private CarView toView(Car car) {

        CarView view = new CarView();
        view.setCarId(car.getId());
        view.setBrand(car.getBrand());
        view.setModel(car.getModel());
        view.setStatus(car.getStatus().name());

        rentalRepository.findByCarIdAndActiveTrue(car.getId())
                .ifPresent(rental -> {
                    view.setRentedBy(rental.getUser().getUsername());
                    view.setStartDate(rental.getStartDate());
                    view.setEndDate(rental.getEndDate());
                });

        return view;
    }

    // ================= READ =================
    @GetMapping("/view/cars")
    public String cars(Model model) {

        List<CarView> cars = carRepository.findAll()
                .stream()
                .map(this::toView)
                .toList();

        model.addAttribute("cars", cars);
        return "cars";
    }

    // ================= CREATE =================
    @PostMapping("/view/cars/add")
    public String addCar(@RequestParam String brand,
            @RequestParam String modelName) {

        Car car = new Car();
        car.setBrand(brand);
        car.setModel(modelName);
        car.setStatus(CarStatus.AVAILABLE);

        carRepository.save(car);
        return "redirect:/view/cars";
    }

    // ================= DELETE =================
    @PostMapping("/view/cars/delete/{id}")
    public String deleteCar(@PathVariable Long id, Model model) {

        // ❌ NIE POZWALAMY USUWAĆ AKTYWNIE WYPOŻYCZONEGO AUTA
        if (rentalRepository.existsByCarIdAndActiveTrue(id)) {
            model.addAttribute("error",
                    "Cannot delete car that is currently rented.");
            return cars(model);
        }

        carRepository.deleteById(id);
        return "redirect:/view/cars";
    }

    // ================= EDIT FORM =================
    @GetMapping("/view/cars/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute(
                "car",
                carRepository.findById(id).orElseThrow());
        return "edit-car";
    }

    // ================= UPDATE =================
    @PostMapping("/view/cars/edit")
    public String updateCar(@RequestParam Long id,
            @RequestParam String brand,
            @RequestParam String modelName,
            @RequestParam CarStatus status) {

        Car car = carRepository.findById(id).orElseThrow();
        car.setBrand(brand);
        car.setModel(modelName);
        car.setStatus(status);

        carRepository.save(car);
        return "redirect:/view/cars";
    }

    // ================= SEARCH (BRAND + STATUS) =================
    @GetMapping("/view/cars/search")
    public String searchCars(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String status,
            Model model) {

        List<Car> cars;

        boolean hasBrand = brand != null && !brand.isBlank();
        boolean hasStatus = status != null && !status.isBlank();

        if (hasBrand && hasStatus) {
            cars = carRepository.findByStatusAndBrandContainingIgnoreCase(
                    CarStatus.valueOf(status),
                    brand);
        } else if (hasBrand) {
            cars = carRepository.findByBrandContainingIgnoreCase(brand);
        } else if (hasStatus) {
            cars = carRepository.findByStatus(CarStatus.valueOf(status));
        } else {
            cars = carRepository.findAll();
        }

        List<CarView> views = cars.stream()
                .map(this::toView)
                .toList();

        model.addAttribute("cars", views);
        return "cars";
    }
}
