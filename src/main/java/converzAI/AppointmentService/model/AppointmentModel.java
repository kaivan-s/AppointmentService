package converzAI.AppointmentService.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class AppointmentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private int operatorId;

    public AppointmentModel() {};
    public AppointmentModel(LocalDateTime startTime, int operatorId) {
        this.startTime = startTime;
        this.operatorId = operatorId;
        this.endTime = startTime.plusHours(1);
    }

}
