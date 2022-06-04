package com.treeshop.controller.admin;

import com.treeshop.controller.CommonController;
import com.treeshop.entity.CategoryEntity;
import com.treeshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping(path = "/admin/category")
public class CategoryController {
    private final CategoryService categoryService;
    private final CommonController commonController;

    @Autowired
    public CategoryController(CategoryService categoryService, CommonController commonController) {
        this.categoryService = categoryService;
        this.commonController = commonController;
    }


    @GetMapping("/list")
    public String showListCategory(Model model) {
        model.addAttribute("listCategory", categoryService.findAll());
        return "/views/admin/category/list-category";
    }

    @GetMapping("/add-category")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new CategoryEntity());
        model.addAttribute("titlePage", "Thêm danh mục");
        return "/views/admin/category/manage-category";
    }

    @GetMapping("/edit/{categoryId}")
    public String showEditCategoryForm(@PathVariable("categoryId") String categoryId,
                                       Model model) {
        CategoryEntity categoryEntity = categoryService.findCategoryById(categoryId);
        model.addAttribute("category", categoryEntity);
        model.addAttribute("titlePage", "Chỉnh sửa danh mục: " + categoryId);
        model.addAttribute("edit", "");
        return "/views/admin/category/manage-category";
    }

    @GetMapping("/delete/{categoryId}")
    public String deleteCategory(@PathVariable("categoryId") String categoryId,
                                 RedirectAttributes ra) {
        categoryService.deleteCategory(categoryId);
        ra.addFlashAttribute("successMessage", "Xóa danh mục: <strong> " + categoryId + "</strong> thành công.");
        return "redirect:/admin/category/list";
    }

    @PostMapping("/save")
    public String saveCategory(@ModelAttribute(name = "category") CategoryEntity categoryEntity,
                               @RequestParam(value = "fileImg", required = false) MultipartFile multipartFile,
                               HttpServletRequest request,
                               RedirectAttributes ra) throws IOException {
        //get previousUrl
        String previousUrl = commonController.getHeaderURL(request);
        String url = previousUrl.substring((request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()).length());
        //upload file
        String categoryId = categoryEntity.getCategoryId();
        if (url.equals("/admin/category/add-category")) {
            if (categoryService.checkCategoryId(categoryId)) {
                ra.addFlashAttribute("errorMessage", categoryId);
                return "redirect:/admin/category/add-category";
            } else {
                ra.addFlashAttribute("successMessage", "Thêm danh mục: <strong> " + categoryId + "</strong> thành công.");
            }
        } else if (url.contains("/admin/category/edit")) {

            ra.addFlashAttribute("successMessage", "Chỉnh sửa danh mục: <strong> " + categoryId + "</strong> thành công.");
        }
        categoryService.saveCategory(categoryEntity, multipartFile);
        return "redirect:/admin/category/list";
    }
}
