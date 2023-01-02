package com.treeshop.controller.admin;

import com.treeshop.controller.CommonController;
import com.treeshop.entity.ShipperEntity;
import com.treeshop.entity.UserEntity;
import com.treeshop.service.OrdersService;
import com.treeshop.service.ShipperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(path = "/admin/shipment")
public class ShipmentController {
    private final OrdersService ordersService;
    private final ShipperService shipperService;
    private final CommonController commonController;

    @Autowired
    public ShipmentController(OrdersService ordersService, ShipperService shipperService, CommonController commonController) {
        this.ordersService = ordersService;
        this.shipperService = shipperService;
        this.commonController = commonController;
    }


    @GetMapping("/list")
    public String showListOrder(Model model) {
        model.addAttribute("listOrder", ordersService.findShipmentOrderList());
        return "/views/admin/shipment/list-delivery-order";
    }

    @GetMapping("/assign")
    public String showListConfirmedOrder(Model model) {
        model.addAttribute("listShipper", shipperService.findAll());
        model.addAttribute("listOrder", ordersService.findByStatus("Đã xác nhận"));
        return "/views/admin/shipment/assign-shipment";
    }

    @GetMapping("/shipper/list")
    public String showListShipper(Model model) {
        model.addAttribute("listShipper", shipperService.findAll());
        return "/views/admin/shipment/list-shipper";
    }

    @GetMapping("/shipper/add")
    public String viewAddShipper(Model model) {
        model.addAttribute("shipper", new ShipperEntity());
        model.addAttribute("titlePage", "Thêm shipper");
        return "/views/admin/shipment/manage-shipper";
    }

    @GetMapping("/shipper/edit/{shipperId}")
    public String viewEditShipper(@PathVariable("shipperId") Integer shipperId,
                                  Model model) {
        model.addAttribute("shipper", shipperService.findByShipperId(shipperId));
        model.addAttribute("titlePage", "Chỉnh sửa shipper: " + shipperId.toString());
        model.addAttribute("edit", "");
        return "/views/admin/shipment/manage-shipper";
    }

    @PostMapping("/shipper/save")
    public String saveUser(@ModelAttribute("shipper") ShipperEntity user,
                           RedirectAttributes ra,
                           HttpServletRequest request) {
        String previousUrl = commonController.getHeaderURL(request);
        String url = previousUrl.substring((request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()).length());
        String username = user.getUsername();
        if (url.equals("/admin/shipment/shipper/add")) {
            if (shipperService.checkUsername(username)) {
                ra.addFlashAttribute("errorMessage", username);
                return "redirect:/admin/shipment/shipper/add";
            } else {
                ra.addFlashAttribute("successMessage", "Thêm shipper: <strong> " + username + "</strong> thành công.");
            }
        } else if (url.contains("/shipment/shipper/edit")) {
            ra.addFlashAttribute("successMessage", "Chỉnh sửa shipper: <strong> " + username + "</strong> thành công.");
        }
        shipperService.saveShipper(user);
        return "redirect:/admin/shipment/shipper/list";
    }

    @GetMapping("/shipper/delete/{shipperId}")
    public String deleteUser(@PathVariable("shipperId") Integer shipperId,
                             RedirectAttributes ra) {
        shipperService.deleteShipper(shipperId);
        ra.addFlashAttribute("successMessage", "Xóa shipper: <strong> " + shipperId.toString() + "</strong> thành công.");
        return "redirect:/admin/shipment/shipper/list";
    }

    @PostMapping("/assign/shipper")
    @ResponseBody
    public void assignShipment(@RequestParam(value = "arrOfOrderId[]", required = false) List<String> listOrderId,
                               @RequestParam(value = "shipperId", required = false) Integer shipperId) {
        try {
            ordersService.assignShipperForOrder(shipperId, listOrderId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Thành công");
        }
    }

}
