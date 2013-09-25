package com.craigstjean.workflow.ui.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.craigstjean.workflow.service.WorkflowService;

@Controller
public class HomeController {
	@Autowired
	private WorkflowService workflow;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(
			@RequestParam(value = "user", required = false, defaultValue = "") String user,
			final HttpSession session,
			final Model model) {
		session.setAttribute("user", user);
		
		List<String> users = new ArrayList<String>();
		users.add("John Customer");
		users.add("Jane Customer");
		users.add("Bobby Clerk");
		users.add("Fred Clerk");
		
		model.addAttribute("user", user);
		model.addAttribute("users", users);
		model.addAttribute("tasks", workflow.getTasksForUser(user));
		return "home";
	}
	
	@RequestMapping(value = "/process/new", method = RequestMethod.POST)
	public String newProcess(final HttpSession session, final RedirectAttributes redirectAttributes) {
		workflow.startProcess();
		
		String user = (String) session.getAttribute("user");
		redirectAttributes.addAttribute("user", user);
		return "redirect:/";
	}
	
	@RequestMapping(value = "/task/{tid}/startAndComplete", method = RequestMethod.POST)
	public String startAndComplete(
			@PathVariable("tid") Long taskId,
			final HttpSession session,
			final RedirectAttributes redirectAttributes) {
		String user = (String) session.getAttribute("user");
		
		workflow.startTask(user, taskId);
		workflow.completeTask(user, taskId);
		
		redirectAttributes.addAttribute("user", user);
		return "redirect:/";
	}
}
