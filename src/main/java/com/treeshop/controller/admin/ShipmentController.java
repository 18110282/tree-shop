package com.treeshop.controller.admin;

import com.treeshop.entity.StatusOfOrder;
import com.treeshop.service.OrdersService;
import com.treeshop.service.ShipperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "/admin/shipment")
public class ShipmentController {
    private final OrdersService ordersService;
    private final ShipperService shipperService;

    @Autowired
    public ShipmentController(OrdersService ordersService, ShipperService shipperService) {
        this.ordersService = ordersService;
        this.shipperService = shipperService;
    }


    @GetMapping("/list")
    public String showListOrder(Model model){
        model.addAttribute("listOrder", ordersService.findShipmentOrderList());
        return "/views/admin/shipment/list-delivery-order";
    }

    @GetMapping("/assign")
    public String showListConfirmedOrder(Model model){
        model.addAttribute("listShipper", shipperService.findAll());
        model.addAttribute("listOrder", ordersService.findByStatus("Đã xác nhận"));
        return "/views/admin/shipment/assign-shipment";
    }

    @PostMapping("/assign/shipper")
    @ResponseBody
    public void assignShipment(@RequestParam(value = "arrOfOrderId[]", required = false) List<String> listOrderId,
                               @RequestParam(value = "shipperId", required = false)Integer shipperId){
        try{
            ordersService.assignShipperForOrder(shipperId, listOrderId);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            System.out.println("Thành công");
        }

    }

}
