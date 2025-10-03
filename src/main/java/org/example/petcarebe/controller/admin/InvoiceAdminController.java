package org.example.petcarebe.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.petcarebe.dto.request.invoice.*;
import org.example.petcarebe.dto.response.invoice.*;
import org.example.petcarebe.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/v1/invoices")
@Tag(name = "👥 Invoice Management", description = "APIs for managing Invoices in the pet care system")
public class InvoiceAdminController {
    // Your controller methods will go here
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private ServicePackageService servicePackageService;
    @Autowired
    private ProductService  productService;
    @Autowired
    private PrescriptionService prescriptionService;
    @Autowired
    private DiscountService discountService;
    @Autowired
    private VaccineService vaccineService;
    @Autowired
    private PaymentService paymentService;

    @Operation(
            summary = "Taoj moiws hoa ddown danjg DRAFT",
            description = "Add new Invoice"
    )
    @PostMapping
    public ResponseEntity<CreateInvoiceResponse> createInvoice(@RequestBody CreateInvoiceRequest request){
        try{
            CreateInvoiceResponse response = invoiceService.createInvoice(request);
            return  ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            CreateInvoiceResponse error = new CreateInvoiceResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (Exception e) {
            CreateInvoiceResponse error = new CreateInvoiceResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

    }
    @GetMapping("/getlist")
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices(){
        try{
            List<InvoiceResponse> response = invoiceService.getAllInvoice();
            return  ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }
        catch (RuntimeException e){
            List<InvoiceResponse> error = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
        catch (Exception e){
            List<InvoiceResponse> error = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    //Caap nhat thong tin hoa don
    @PostMapping("/{invoiceId}")
    public ResponseEntity<UpdateInvoiceResponse> updateInvoice(@PathVariable("invoiceId") Long invoiceId, @RequestBody UpdateInvoiceRequest request){
        try{
            UpdateInvoiceResponse response = invoiceService.updateInvoice(invoiceId, request);
            return  ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            UpdateInvoiceResponse error = new UpdateInvoiceResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (Exception e) {
            UpdateInvoiceResponse error = new UpdateInvoiceResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Hủy hóa đơn
    @GetMapping("/cancel/{invoiceId}")
    public ResponseEntity<CancelInvoiceResponse> cancelInvoice(@PathVariable("invoiceId") Long invoiceId){
        try{
            CancelInvoiceResponse response = invoiceService.cancelInvoice(invoiceId);
            return  ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }
        catch (RuntimeException e){
            CancelInvoiceResponse error = new CancelInvoiceResponse();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
        catch (Exception e){
            CancelInvoiceResponse error = new CancelInvoiceResponse();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    //Tra cứu theo Customer, Staff
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<InvoiceResponse>> getInvoiceByCustomerId(@PathVariable("customerId") Long customerId){
        try{
            List<InvoiceResponse> response = invoiceService.getByCustomerId(customerId);
            return  ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }
        catch (RuntimeException e){
            List<InvoiceResponse> error = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
        catch (Exception e){
            List<InvoiceResponse> error = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/add-service/{invoiceId}")
    public ResponseEntity<AddServiceToInvoiceResponse> addServiceToInvoice(@PathVariable Long invoiceId, @RequestBody AddServiceToInvoiceRequest request){
        try{
            AddServiceToInvoiceResponse response = invoiceService.addServiceToInvoice(invoiceId, request);
            return  ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            AddServiceToInvoiceResponse error = new AddServiceToInvoiceResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
        catch (Exception e){
            AddServiceToInvoiceResponse error = new AddServiceToInvoiceResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

        }
    }

    @PostMapping("/add-service-package/{invoiceId}")
    public ResponseEntity<AddServicePackageToInvoiceResponse> addServicePackageToInvoice(@PathVariable Long invoiceId, @RequestBody AddServicePackageToInvoiceRequest request){
        try{
            AddServicePackageToInvoiceResponse response = invoiceService.addServicePackageToInvoice(invoiceId, request);
            return  ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            AddServicePackageToInvoiceResponse error = new AddServicePackageToInvoiceResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
        catch (Exception e){
            AddServicePackageToInvoiceResponse error = new AddServicePackageToInvoiceResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

        }
    }

    @PostMapping("/add-product/{invoiceId}")
    public ResponseEntity<AddProductToInvoiceResponse> addProductToInvoice(@PathVariable Long invoiceId, @RequestBody AddProductToInvoiceRequest request){
        try{
            AddProductToInvoiceResponse response = invoiceService.addProductToInvoice(invoiceId, request);
            return  ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            AddProductToInvoiceResponse error = new AddProductToInvoiceResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
        catch (Exception e){
            AddProductToInvoiceResponse error = new AddProductToInvoiceResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

        }
    }

    @PostMapping("/add-vaccine/{invoiceId}")
    public ResponseEntity<AddVaccineToInvoiceResponse> addVaccineToInvoice(@PathVariable Long invoiceId, @RequestBody AddVaccineToInvoiceRequest request){
        try{
            AddVaccineToInvoiceResponse response = invoiceService.addVaccineToInvoice(invoiceId, request);
            return  ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            AddVaccineToInvoiceResponse error = new AddVaccineToInvoiceResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
        catch (Exception e){
            AddVaccineToInvoiceResponse error = new AddVaccineToInvoiceResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

        }
    }

    @PostMapping("/add-prescription/{invoiceId}")
    public ResponseEntity<AddPrescriptionToInvoiceResponse> addVaccineToInvoice(@PathVariable Long invoiceId, @RequestBody AddPrescriptionToInvoiceRequest request){
        try{
            AddPrescriptionToInvoiceResponse response = invoiceService.addPrescriptionToInvoice(invoiceId, request);
            return  ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            AddPrescriptionToInvoiceResponse error = new AddPrescriptionToInvoiceResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
        catch (Exception e){
            AddPrescriptionToInvoiceResponse error = new AddPrescriptionToInvoiceResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

        }
    }


    //Theem Service, ServicePackage, Discount, Product, Prescription, Vaccine V

    //Update STatus: PENDING, PAID, CANCELED, ... Bỏ qua

    //Tinh tien
    @GetMapping("/check-invoice/{invoiceId}")
    public ResponseEntity<InvoiceResponse> checkPriceInvoice(@PathVariable Long invoiceId){
        try{
            InvoiceResponse response = invoiceService.checkPriceInvoice(invoiceId);
            return  ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            InvoiceResponse error = new InvoiceResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
        catch (Exception e){
            InvoiceResponse error = new InvoiceResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

        }
    }

    //them thanh toán Payment: Xác nhận thanh toán, update Status, Hoàn tiền


    //Xử lý Discount: Theem Discount vaof Invoice
    @PostMapping("/add-discount/{invoiceId}")
    public ResponseEntity<InvoiceDiscountResponse> addDiscountToInvoice(@PathVariable Long invoiceId, @RequestBody AddDiscountToInvoiceRequest request){
        try{
            InvoiceDiscountResponse response = invoiceService.addDiscountToInvoice(invoiceId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (RuntimeException e) {
            InvoiceDiscountResponse error = new InvoiceDiscountResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
        catch (Exception e){
            InvoiceDiscountResponse error = new InvoiceDiscountResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

        }
    }

    // Thống kê


}

