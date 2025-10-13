package org.example.petcarebe.service;

import jakarta.mail.MessagingException;
import org.example.petcarebe.dto.request.appointment.ConfirmAppointmentRequest;
import org.example.petcarebe.dto.request.appointment.CreateAppointmentRequest;
import org.example.petcarebe.dto.request.appointment.GetAppointmentByStatusRequest;
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
import java.util.*;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository  appointmentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private WorkScheduleRepository workScheduleRepository;

    @Autowired
    private EmailService emailService;

    @Value("${expired.date}")
    private Long expiredDate;
    @Value("example.email")
    private String exampleEmail;

    public CreateAppointmentResponse createAppointment(CreateAppointmentRequest request) throws MessagingException {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer Not Found"));

        Appointment appointment = new Appointment();
        appointment.setCustomer(customer);
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setNotes(request.getNotes());
        appointment.setQueueNumber(-1);
        appointment.setStatus(AppointmentStatus.PENDING);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        Map<String, Object> data = new HashMap<>();
        data.put("customerName", customer.getFullname());
        data.put("customerEmail", customer.getEmail() != null ? customer.getEmail() : "");
        data.put("customerAddress", customer.getAddress());
        data.put("reason", request.getNotes());
        data.put("appointmentDate", request.getAppointmentDate());
        data.put("appointmentTime", request.getAppointmentTime());
        data.put("location", "Phòng khám thú cưng ABC");
        emailService.sendAppointmentPendingEmail(customer.getEmail() != null ? customer.getEmail() : exampleEmail, data);
        return convertToCreateResponse(savedAppointment, "Appointment Created Successfully");

    }


    public AppointmentResponse getAppointmentById(Long id) {
        return convertToAppointmentResponse(appointmentRepository.findById(id).orElse(new Appointment())) ;
    }

    public AppointmentResponse cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new RuntimeException("Appointment Not Found"));
//        appointment.setStatus(AppointmentStatus.CANCELLED);
        switch (appointment.getStatus()) {
            case PENDING, CONFIRMED, RESCHEDULED, NOSHOW:
                appointment.setStatus(AppointmentStatus.CANCELLED);
                break;
            case CANCELLED:
                throw new RuntimeException("Appointment is already Cancelled");
            case INPROGRESS, COMPLETED:
                throw new RuntimeException("Appointment is already In Progress or Completed");
            default:
                throw new RuntimeException("Appointment Status Not Found");
        }

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertToAppointmentResponse(savedAppointment, "Appointment has been cancelled");
    }

    public AppointmentResponse rescheduleAppointment(Long id, UpdateAppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Appointment Not Found"));
        if(appointment.getAppointmentDate().isAfter(request.getRequestDate().minusDays(expiredDate))) {
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

    public AppointmentResponse updateToInProgressAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new RuntimeException("Appointment Not Found"));
        System.out.println("To INprogress, appointment id: " + appointment.getId());
        if (Objects.requireNonNull(appointment.getStatus()) == AppointmentStatus.CONFIRMED) {
            appointment.setStatus(AppointmentStatus.INPROGRESS);
        }
        else throw new RuntimeException("Appointment Status is not Confirmed");
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertToAppointmentResponse(savedAppointment, "Appointment status has been updated to In Progress");
    }

    public AppointmentResponse updateToCompleteAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new RuntimeException("Appointment Not Found"));
        if (Objects.requireNonNull(appointment.getStatus()) == AppointmentStatus.INPROGRESS) {
            appointment.setStatus(AppointmentStatus.COMPLETED);
        }
        else throw new RuntimeException("Appointment Status is not In Progress");
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertToAppointmentResponse(savedAppointment, "Appointment status has been updated to Complete");
    }

    public AppointmentResponse updateToNoShowAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new RuntimeException("Appointment Not Found"));
        if (Objects.requireNonNull(appointment.getStatus()) == AppointmentStatus.CONFIRMED) {
            appointment.setStatus(AppointmentStatus.NOSHOW);
        }
        else throw new RuntimeException("Appointment Status is not CONFIRMED");
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertToAppointmentResponse(savedAppointment, "Appointment status has been updated to No show");
    }

    public AppointmentResponse confirmAppointment(Long appointmentId, ConfirmAppointmentRequest request) throws MessagingException {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new RuntimeException("Appointment Not Found"));
        WorkSchedule workSchedule = workScheduleRepository.findById(request.getWorkScheduleId()).orElseThrow(() -> new RuntimeException("Work Schedule Not Found"));
        appointment.setQueueNumber(createQueueNumber(workSchedule));
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointment.setWorkSchedule(workSchedule);

        Appointment savedAppointment = appointmentRepository.save(appointment);

        Map<String, Object> data = new HashMap<>();
        data.put("customerName", appointment.getCustomer().getFullname());
        data.put("customerEmail", appointment.getCustomer().getEmail());
        data.put("customerPhone", appointment.getCustomer().getPhone());
        data.put("reason", appointment.getNotes());
        data.put("appointmentDate", appointment.getAppointmentDate());
        data.put("appointmentTime", appointment.getAppointmentTime());
        data.put("doctorName", appointment.getWorkSchedule().getDoctor().getFullname());
        data.put("room", "Phong 01");
        data.put("queueNumber", appointment.getQueueNumber());

        emailService.sendAppointmentConfirmationEmail(appointment.getCustomer() != null ? appointment.getCustomer().getEmail() : exampleEmail, data);
        return convertToAppointmentResponse(savedAppointment, "Appointment has been confirmed");
    }

    public List<AppointmentResponse> getAppointmentsByClientId(String clientId) {
        Customer customer =  customerRepository.findByClientId(clientId)
                .orElseThrow(() -> new RuntimeException("Customer Not Found"));
        List<Appointment> appointmentList = appointmentRepository.findAllByCustomer(customer);
        return appointmentList.stream().map(this::convertToAppointmentResponse).toList();
    }

    public List<AppointmentResponse> getAppointmentByStatus(GetAppointmentByStatusRequest request) {
        Optional<Customer> customerOptional = customerRepository.findById(request.getCustomerId());
        Customer customer = customerOptional.orElse(null);
        List<Appointment> appointmentList = new ArrayList<>();
        if(customer != null) {
            appointmentList = appointmentRepository.findAllByCustomer(customer);
        }
        else{
            appointmentList = appointmentRepository.findAllByStatus(request.getStatus());
        }
        return appointmentList.stream().map(this::convertToAppointmentResponse).toList();
    }

    public List<AppointmentResponse> getAppointmentsByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer Not Found"));
        List<Appointment> appointmentList = appointmentRepository.findAllByCustomer(customer);
        return appointmentList.stream().map(this::convertToAppointmentResponse).toList();
    }

    public List<AppointmentResponse> getAllAppointments() {
        List<Appointment> appointmentList = appointmentRepository.findAll();
        return appointmentList.stream().map(this::convertToAppointmentResponse).toList();
    }
    public void deleteAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new RuntimeException("Appointment Not Found"));
        appointmentRepository.delete(appointment);
    }


    private CreateAppointmentResponse convertToCreateResponse(Appointment appointment) {
        return CreateAppointmentResponse.builder()
                .id(appointment.getId())
                .appointmentTime(appointment.getAppointmentTime())
                .appointmentDate(appointment.getAppointmentDate())
                .status(appointment.getStatus())
                .notes(appointment.getNotes())
                .message("Created new Appointment for" + (appointment.getCustomer() != null ? appointment.getCustomer().getFullname(): null))
                .customerName(appointment.getCustomer() != null ? appointment.getCustomer().getFullname(): null)
                .customerEmail(appointment.getCustomer() != null ? appointment.getCustomer().getEmail(): null)
                .customerPhone(appointment.getCustomer() != null ? appointment.getCustomer().getPhone(): null)
                .customerAddress(appointment.getCustomer() != null ? appointment.getCustomer().getAddress(): null)
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
                .customerName(appointment.getCustomer() != null ? appointment.getCustomer().getFullname(): null)
                .customerEmail(appointment.getCustomer() != null ? appointment.getCustomer().getEmail(): null)
                .customerPhone(appointment.getCustomer() != null ? appointment.getCustomer().getPhone(): null)
                .customerAddress(appointment.getCustomer() != null ? appointment.getCustomer().getAddress(): null)
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
                .customerId(appointment.getCustomer() != null ? appointment.getCustomer().getId() : null)
                .customerName(appointment.getCustomer() != null ? appointment.getCustomer().getFullname(): null)
                .customerEmail(appointment.getCustomer() != null ? appointment.getCustomer().getEmail(): null)
                .customerPhone(appointment.getCustomer() != null ? appointment.getCustomer().getPhone(): null)
                .customerAddress(appointment.getCustomer() != null ? appointment.getCustomer().getAddress(): null)
                .message("Created new Appointment for" + (appointment.getCustomer() != null ? appointment.getCustomer().getFullname(): null))
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
                .customerId(appointment.getCustomer() != null ? appointment.getCustomer().getId() : null)
                .customerName(appointment.getCustomer() != null ? appointment.getCustomer().getFullname(): null)
                .customerEmail(appointment.getCustomer() != null ? appointment.getCustomer().getEmail(): null)
                .customerPhone(appointment.getCustomer() != null ? appointment.getCustomer().getPhone(): null)
                .customerAddress(appointment.getCustomer() != null ? appointment.getCustomer().getAddress(): null)
                .message(message)
                .build();

    }


    public List<AppointmentResponse> getAppointmentByDoctor(Long doctorId) {
        List<Appointment> responses = appointmentRepository.findAllByDoctorId(doctorId);
        return responses.stream()
                .map(this::convertToAppointmentResponse)
                .toList();
    }


}

