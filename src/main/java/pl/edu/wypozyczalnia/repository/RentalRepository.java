package pl.edu.wypozyczalnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.wypozyczalnia.model.Rental;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    boolean existsByCarId(Long carId);

    // ===== SPRAWDZANIE DOSTĘPNOŚCI AUTA =====

    boolean existsByCarIdAndActiveTrue(Long carId);

    // ===== AKTYWNE WYPOŻYCZENIA =====

    List<Rental> findByActiveTrue();

    // ===== HISTORIA WYPOŻYCZEŃ AUTA =====

    List<Rental> findByCarId(Long carId);

    // ===== HISTORIA WYPOŻYCZEŃ UŻYTKOWNIKA =====

    List<Rental> findByUserId(Long userId);

    // ===== AKTUALNE WYPOŻYCZENIE AUTA =====

    Optional<Rental> findByCarIdAndActiveTrue(Long carId);

    // ===== PRZYKŁAD Z DATĄ (to co miałeś) =====

    List<Rental> findByCarIdAndEndDateAfter(Long carId, LocalDate date);
}
