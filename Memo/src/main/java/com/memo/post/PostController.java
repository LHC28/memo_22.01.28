package com.memo.post;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.memo.post.bo.PostBO;
import com.memo.post.model.Post;

@RequestMapping("/post")
@Controller
public class PostController {
	
	@Autowired
	private PostBO postBO;

	@RequestMapping("/post_list_view")
	public String postListView(
			@RequestParam(value="prevId", required = false) Integer prevIdParam
			,@RequestParam(value="nextId", required = false) Integer nextIdParam
			,Model model
			,HttpServletRequest request
			) {
		HttpSession session = request.getSession();
		Integer userId = (Integer)session.getAttribute("userId");
		if(userId==null) {
			return "redirect:/user/sign_in_view";
		}
		
		List<Post> postList = postBO.getPostListByUserId(userId, prevIdParam, nextIdParam);
		int prevId = 0;
		int nextId = 0;
		
		if(postList.isEmpty()==false) {
			prevId = postList.get(0).getId();
			nextId = postList.get(postList.size()-1).getId();
			
			if(postBO.isLastPage(userId, nextId)) {
				nextId = 0;
			}
			
			if(postBO.isFirstPage(userId, prevId)) {
				prevId = 0;
			}
		}
		
		model.addAttribute("postList", postList);
		model.addAttribute("viewName", "post/post_list");
		model.addAttribute("prevId", prevId);
		model.addAttribute("nextId", nextId);
		return "template/layout";
	}
	
	@RequestMapping("/post_create_view")
	public String postCreateView(Model model) {
		model.addAttribute("viewName", "post/post_create");
		return "template/layout";
	}
	
	@RequestMapping("/post_detail_view")
	public String postDetailView(
			@RequestParam("postId") int postId
			,Model model
			,HttpServletRequest request
			) {
		HttpSession session = request.getSession();
		Integer userId = (Integer)session.getAttribute("userId");
		if(userId==null) {
			return "redirect:/user/sign_in_view";
		}
		Post post = postBO.getPostByPostIdAndUserId(postId, userId);
		
		model.addAttribute("post", post);
		model.addAttribute("viewName", "post/post_detail");
		return "template/layout";
	}
}
