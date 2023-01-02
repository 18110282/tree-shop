package com.treeshop.controller.client;

import com.treeshop.controller.CommonController;
import com.treeshop.entity.PostsEntity;
import com.treeshop.entity.ProductsEntity;
import com.treeshop.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/home")
public class PostWebController {
    private final PostService postService;
    private final CommonController commonController;

    @Autowired
    public PostWebController(PostService postService, CommonController commonController) {
        this.postService = postService;
        this.commonController = commonController;
    }

    @GetMapping("/post/list")
    public String viewListPost(Model model, HttpSession session){
       // model.addAttribute("listPost", postService.listAll());

        return viewListPostByPage(model, 1, session);
    }

    @GetMapping("/post/list/{page}")
    public String viewListPostByPage(Model model, @PathVariable("page") Integer currentPage,
                                     HttpSession session){
        Page<PostsEntity> postsEntitiesPage = postService.listAllWeb(currentPage);
        Integer totalPages = postsEntitiesPage.getTotalPages();
        List<PostsEntity> postsEntityList = postsEntitiesPage.getContent();
        List<PostsEntity> topNewestPost = postService.findTop3Newest();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("listPost", postsEntityList);
        model.addAttribute("listTopNewest", topNewestPost);
        return "/views/client/list-web-post";
    }

    @GetMapping("/post/{postId}/detail")
    public String viewPostDetail(@PathVariable("postId") String postId,
                                 Model model){
        model.addAttribute("post", postService.findByPostsId(postId));
        model.addAttribute("listTopNewest", postService.findTop3Newest());
        model.addAttribute("listTopRandom", postService.findTop3Random());
        model.addAttribute("post_detail_flg", 1);
        return "/views/client/post-detail";
    }

}
