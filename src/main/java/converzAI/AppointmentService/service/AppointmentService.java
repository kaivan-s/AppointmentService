package converzAI.AppointmentService.service;

import converzAI.AppointmentService.model.AppointmentModel;
import converzAI.AppointmentService.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public Optional<AppointmentModel> bookAppointment(LocalDateTime startTime, int operatorId) {
        if(appointmentRepository.findByOperatorIdAndStartTime(operatorId, startTime) == null) {
            AppointmentModel appointment = new AppointmentModel(startTime, operatorId);
            return Optional.of(appointmentRepository.save(appointment));
        }
        return Optional.empty();
    }

    public Optional<AppointmentModel> rescheduleAppointment(Long appointmentId, LocalDateTime startTime) {
        Optional<AppointmentModel> appointmentOpt = appointmentRepository.findById(appointmentId);
        if(appointmentOpt.isPresent()) {
            AppointmentModel appointment = appointmentOpt.get();
            if(appointmentRepository.findByOperatorIdAndStartTime(appointment.getOperatorId(), startTime) == null) {
                appointment.setStartTime(startTime);
                appointment.setEndTime(startTime.plusHours(1));
                return Optional.of(appointmentRepository.save(appointment));
            }
        }
        return Optional.empty();
    }

    public void cancelAppointment(Long appointmentId) {
        appointmentRepository.deleteById(appointmentId);
    }

    public List<AppointmentModel> getBookedAppointment(int operatorId) {
        return appointmentRepository.findAllByOperatorId(operatorId);
    }

    public List<LocalDateTime> getOpenSlots(int operatorId, LocalDateTime startTime, LocalDateTime endTime) {
        List<AppointmentModel> appointments = getBookedAppointment(operatorId);
        List<LocalDateTime> openSlots = new ArrayList<>();

        LocalDateTime current = startTime;
        while(current.isBefore(endTime)) {
            if(isSlotsAvailable(appointments, current)) {
                openSlots.add(current);
            }
            current = current.plusHours(1);
        }
        return openSlots;
    }

    private Boolean isSlotsAvailable(List<AppointmentModel> appointments, LocalDateTime current) {
        return appointments.stream().noneMatch(a -> a.getStartTime().equals(current));
    }

}
