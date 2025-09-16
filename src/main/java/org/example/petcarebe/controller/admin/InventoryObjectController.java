package org.example.petcarebe.controller.admin;

import org.example.petcarebe.service.InventoryObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/admin/v1/inventory-object")
public class InventoryObjectController {
    @Autowired
    InventoryObjectService inventoryObjectService;


}
