package converzAI.AppointmentService.repository;

import converzAI.AppointmentService.model.AppointmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentModel, Long> {

    List<AppointmentModel> findAllByOperatorId(int operatorId);

    @Query("SELECT a FROM AppointmentModel a WHERE a.operatorId=:operatorId AND a.startTime = :startTime")
    AppointmentModel findByOperatorIdAndStartTime(@Param("operatorId") int operatorId,
                                                  @Param("startTime") LocalDateTime startTime);

    @Query("SELECT a FROM AppointmentModel a WHERE a.operatorId=:operatorId AND a.startTime >= :startTime " +
            "AND a.endTime<:endTime")
    List<AppointmentModel> findByOperatorIdAndTimeRange(@Param("operatorId") int operatorId,
                                                   @Param("startTime")LocalDateTime startTime,
                                                   @Param("endTime")LocalDateTime endTime);

}
