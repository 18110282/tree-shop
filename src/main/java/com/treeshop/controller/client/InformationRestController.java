package com.treeshop.controller.client;

import com.treeshop.service.ProductsService;
import com.treeshop.service.UsersService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/home")
public class InformationRestController {
    private final ProductsService productsService;
    private final UsersService usersService;

    public InformationRestController(ProductsService productsService, UsersService usersService) {
        this.productsService = productsService;
        this.usersService = usersService;
    }

    @PostMapping("/save/{username}/password/{oldPassword}/{newPassword}")
    public boolean savePasswordOfClient(@PathVariable("username") String username,
                                       @PathVariable("oldPassword") String oldPassword,
                                       @PathVariable("newPassword") String newPassword){
        return usersService.savePasswordOfClient(username, oldPassword, newPassword);
    }
}