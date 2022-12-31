package com.treeshop.controller.admin;

import com.treeshop.controller.CommonController;
import com.treeshop.entity.CategoryEntity;
import com.treeshop.entity.PostsEntity;
import com.treeshop.entity.ProductsEntity;
import com.treeshop.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(path = "/admin/post")
public class PostController {
    private final PostService postService;
    private final CommonController commonController;

    @Autowired
    public PostController(PostService postService, CommonController commonController) {
        this.postService = postService;
        this.commonController = commonController;
    }

    @GetMapping("/list")
    public String viewListPosts(Model model){
        model.addAttribute("listPost", postService.listAll());
        return "/views/admin/post/list-post";
    }

    @GetMapping("/add-post")
    public String addNewPostView(Model model){
        model.addAttribute("post", new PostsEntity());
        model.addAttribute("titlePage", "Thêm bài viết");
        return "/views/admin/post/manage-post";
    }
    @GetMapping("/edit/{postId}")
    public String showEditPostView(@PathVariable("postId") String postId,
                                      Model model) {
        PostsEntity postsEntity = postService.findByPostsId(postId);
        model.addAttribute("post", postsEntity);
        model.addAttribute("titlePage", "Chỉnh sửa bài viết: " + postId);
        model.addAttribute("edit", "");
        return "/views/admin/post/manage-post";
    }

    @PostMapping("/save")
    public String savePost(@ModelAttribute(name = "post") PostsEntity postsEntity,
                              @RequestParam(value = "fileImg", required = false) MultipartFile multipartFileImg,
                              HttpServletRequest request,
                              RedirectAttributes ra) throws IOException {
        //get previousUrl
        String previousUrl = commonController.getHeaderURL(request);
        String url = previousUrl.substring((request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()).length());
        //upload file
        String postsId = postsEntity.getPostsId();
        if (url.equals("/admin/post/add-post")) {
            if (postService.checkPostsId(postsId)) {
                if (postService.findByPostsId(postsId).isEnabled()) {
                    ra.addFlashAttribute("errorMessage", postsId);
                    return "redirect:/admin/post/add-post";
                }
                else {
                    ra.addFlashAttribute("successMessage", "Thêm bài viết: <strong> " + postsId + "</strong> thành công.");
                }
            } else {
                ra.addFlashAttribute("successMessage", "Thêm bài viết: <strong> " + postsId + "</strong> thành công.");
            }
        } else if (url.contains("/admin/post/edit")) {

            ra.addFlashAttribute("successMessage", "Chỉnh sửa sản phẩm: <strong> " + postsId + "</strong> thành công.");
        }
        postService.savePost(postsEntity, multipartFileImg);
        return "redirect:/admin/post/list";
    }
}
