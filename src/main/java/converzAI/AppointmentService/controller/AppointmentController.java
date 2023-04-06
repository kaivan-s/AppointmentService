package converzAI.AppointmentService.controller;

import converzAI.AppointmentService.model.AppointmentModel;
import converzAI.AppointmentService.service.AppointmentService;
import converzAI.AppointmentService.utilities.AppointmentUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/book")
    public ResponseEntity<AppointmentModel> bookAppointment(@RequestBody AppointmentModel appointment) {
        Optional<AppointmentModel> bookedAppointment = appointmentService
                .bookAppointment(appointment.getStartTime(), appointment.getOperatorId());
        return bookedAppointment
                .map(value -> new ResponseEntity<>(value, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentModel> rescheduleAppointment(@PathVariable("id") Long appointmentId,
                                                                  @RequestParam LocalDateTime newStartTime) {
        Optional<AppointmentModel> rescheduledAppointment = appointmentService
                .rescheduleAppointment(appointmentId, newStartTime);
        return rescheduledAppointment
                .map(appointment -> new ResponseEntity<>(appointment, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable("id") Long appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{operatorId}/booked")
    public ResponseEntity<List<String>> getBookedAppointments(@PathVariable("operatorId") int operatorId) {
        List<AppointmentModel> appointments = appointmentService.getBookedAppointment(operatorId);
        List<String> hours = appointments.stream().map(appointment -> {
            int startHour = appointment.getStartTime().getHour();
            int endHour = appointment.getEndTime().getHour();
            return String.format(startHour + " - " + endHour);
        }).toList();
        return new ResponseEntity<>(hours, HttpStatus.OK);
    }

    @GetMapping("/{operatorId}/openSlots")
    public ResponseEntity<List<String>> getOpenSlots(@PathVariable("operatorId")int operatorId,
                                                            @RequestParam("start") LocalDateTime startTime,
                                                            @RequestParam("end") LocalDateTime endTime) {
        List<LocalDateTime> openSlots = appointmentService.getOpenSlots(operatorId, startTime, endTime);
        List<String> result = AppointmentUtility.getMergedIntervalsForOpenSlots(openSlots);
        return new ResponseEntity<>(result, HttpStatus.OK);
        }

    }
