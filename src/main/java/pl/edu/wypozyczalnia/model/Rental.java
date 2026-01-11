package pl.edu.wypozyczalnia.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== POWIĄZANIA =====

    @ManyToOne(optional = false)
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    // ===== DANE WYPOŻYCZENIA =====

    private LocalDate startDate;
    private LocalDate endDate;

    private boolean active = true;

    // ===== GETTERY / SETTERY =====

    public Long getId() {
        return id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
