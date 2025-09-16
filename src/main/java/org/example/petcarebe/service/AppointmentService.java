package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.appointment.ConfirmAppointmentRequest;
import org.example.petcarebe.dto.request.appointment.CreateAppointmentRequest;
import org.example.petcarebe.dto.request.appointment.UpdateAppointmentRequest;
import org.example.petcarebe.dto.response.appointment.AppointmentResponse;
import org.example.petcarebe.dto.response.appointment.CreateAppointmentResponse;
import org.example.petcarebe.enums.AppointmentStatus;
import org.example.petcarebe.model.Appointment;
import org.example.petcarebe.model.Customer;
import org.example.petcarebe.model.WorkSchedule;
import org.example.petcarebe.repository.AppointmentRepository;
import org.example.petcarebe.repository.CustomerRepository;
import org.example.petcarebe.repository.WorkScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository  appointmentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private WorkScheduleRepository workScheduleRepository;

    @Value("${expired.date}")
    private Long expiredDate;

    public CreateAppointmentResponse createAppointment(CreateAppointmentRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer Not Found"));

        Appointment appointment = new Appointment();
        appointment.setCustomer(customer);
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setQueueNumber(-1);
        appointment.setStatus(AppointmentStatus.PENDING);

        Appointment savedAppointment = appointmentRepository.save(appointment);

        return convertToCreateResponse(savedAppointment);

    }

    private CreateAppointmentResponse convertToCreateResponse(Appointment appointment) {
        return CreateAppointmentResponse.builder()
                .id(appointment.getId())
                .appointmentTime(appointment.getAppointmentTime())
                .appointmentDate(appointment.getAppointmentDate())
                .status(appointment.getStatus())
                .notes(appointment.getNotes())
                .message("Created new Appointment for" + appointment.getCustomer().getFullname())
                .customerName(appointment.getCustomer().getFullname())
                .customerEmail(appointment.getCustomer().getEmail())
                .customerPhone(appointment.getCustomer().getPhone())
                .customerAddress(appointment.getCustomer().getAddress())
                .build();

    }
    private CreateAppointmentResponse convertToCreateResponse(Appointment appointment, String message) {
        return CreateAppointmentResponse.builder()
                .id(appointment.getId())
                .appointmentTime(appointment.getAppointmentTime())
                .appointmentDate(appointment.getAppointmentDate())
                .status(appointment.getStatus())
                .notes(appointment.getNotes())
                .message(message)
                .customerName(appointment.getCustomer().getFullname())
                .customerEmail(appointment.getCustomer().getEmail())
                .customerPhone(appointment.getCustomer().getPhone())
                .customerAddress(appointment.getCustomer().getAddress())
                .build();

    }
    private AppointmentResponse convertToAppointmentResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .status(appointment.getStatus())
                .notes(appointment.getNotes())
                .queueNumber(appointment.getQueueNumber())
                .customerId(appointment.getCustomer().getId())
                .customerName(appointment.getCustomer().getFullname())
                .customerPhone(appointment.getCustomer().getPhone())
                .customerEmail(appointment.getCustomer().getEmail())
                .customerAddress(appointment.getCustomer().getAddress())
                .message("Created new Appointment for" + appointment.getCustomer().getFullname())
                .build();

    }
    private AppointmentResponse convertToAppointmentResponse(Appointment appointment, String message) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .status(appointment.getStatus())
                .notes(appointment.getNotes())
                .queueNumber(appointment.getQueueNumber())
                .customerId(appointment.getCustomer().getId())
                .customerName(appointment.getCustomer().getFullname())
                .customerPhone(appointment.getCustomer().getPhone())
                .customerEmail(appointment.getCustomer().getEmail())
                .customerAddress(appointment.getCustomer().getAddress())
                .message(message)
                .build();

    }
    public AppointmentResponse getAppointmentById(Long id) {
        return convertToAppointmentResponse(appointmentRepository.findById(id).orElse(new Appointment())) ;
    }

    public AppointmentResponse cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Appointment Not Found"));
//        appointment.setStatus(AppointmentStatus.CANCELLED);
        switch (appointment.getStatus()) {
            case PENDING, CONFIRMED, RESCHEDULED, NO_SHOW:
                appointment.setStatus(AppointmentStatus.CANCELLED);
                break;
            case CANCELLED:
                throw new RuntimeException("Appointment is already Cancelled");
            case IN_PROGRESS, COMPLETED:
                throw new RuntimeException("Appointment is already In Progress or Completed");
            default:
                throw new RuntimeException("Appointment Status Not Found");
        }

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertToAppointmentResponse(savedAppointment, "Appointment has been cancelled");
    }

    public AppointmentResponse rescheduleAppointment(Long id, UpdateAppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Appointment Not Found"));
        if(appointment.getAppointmentDate().isAfter(LocalDate.now().minusDays(expiredDate))) {
            // if appointment date is before visit date within 3 day or after now
            throw new RuntimeException("Appointment Date is After Now");
        }
        switch (appointment.getStatus()) {
            case PENDING, CONFIRMED:
                appointment.setStatus(AppointmentStatus.RESCHEDULED);
                appointment.setAppointmentDate(request.getAppointmentDate());
                appointment.setAppointmentTime(request.getAppointmentTime());
                appointment.setNotes(request.getNotes());
                break;
            case RESCHEDULED:
                throw new RuntimeException("Your appointment has been rescheduled before");
            case CANCELLED:
                throw new RuntimeException("Appointment is already cancelled");
            default:
                throw new RuntimeException("Appointment Status Not Found");
        }
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertToAppointmentResponse(savedAppointment, "Appointment has been rescheduled to another time");
    }

    private int createQueueNumber(WorkSchedule workSchedule) {
        return appointmentRepository.getAppointmentNumberByWorkSchedule(workSchedule.getId()) + 1;
    }

    public AppointmentResponse updateToInProgressAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Appointment Not Found"));
        if (Objects.requireNonNull(appointment.getStatus()) == AppointmentStatus.CONFIRMED) {
            appointment.setStatus(AppointmentStatus.IN_PROGRESS);
        }
        else throw new RuntimeException("Appointment Status is not Confirmed");
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertToAppointmentResponse(savedAppointment, "Appointment status has been updated to In Progress");
    }

    public AppointmentResponse updateToCompleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Appointment Not Found"));
        if (Objects.requireNonNull(appointment.getStatus()) == AppointmentStatus.IN_PROGRESS) {
            appointment.setStatus(AppointmentStatus.COMPLETED);
        }
        else throw new RuntimeException("Appointment Status is not In Progress");
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertToAppointmentResponse(savedAppointment, "Appointment status has been updated to Complete");
    }

    public AppointmentResponse updateToNoShowAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Appointment Not Found"));
        if (Objects.requireNonNull(appointment.getStatus()) == AppointmentStatus.CONFIRMED) {
            appointment.setStatus(AppointmentStatus.NO_SHOW);
        }
        else throw new RuntimeException("Appointment Status is not CONFIRMED");
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertToAppointmentResponse(savedAppointment, "Appointment status has been updated to No show");
    }

    public AppointmentResponse confirmAppointment(Long id, ConfirmAppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Appointment Not Found"));
        WorkSchedule workSchedule = workScheduleRepository.findById(request.getWorkScheduleId()).orElseThrow(() -> new RuntimeException("Work Schedule Not Found"));
        appointment.setQueueNumber(createQueueNumber(workSchedule));
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointment.setWorkSchedule(workSchedule);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertToAppointmentResponse(savedAppointment, "Appointment has been confirmed");
    }

    public List<AppointmentResponse> getAppointmentsByClientId(String clientId) {
        Customer customer =  customerRepository.findByClientId(clientId)
                .orElseThrow(() -> new RuntimeException("Customer Not Found"));
        List<Appointment> appointmentList = appointmentRepository.findAllByCustomer(customer);
        return appointmentList.stream().map(this::convertToAppointmentResponse).toList();
    }
}

