package com.example.demo.app.survey;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Survey;
import com.example.demo.service.SurveyService;

/*
 * Add annotations here
 */
@Controller
@RequestMapping("/survey")
public class SurveyController {
	
	private final SurveyService surveyService;
	
	@Autowired
	public SurveyController(SurveyService surveyService){
		this.surveyService = surveyService;
	}
	
	@GetMapping
	public String index(Model model) {
		
		List<Survey> list = surveyService.getAll();
		model.addAttribute("surveyList", list);
		model.addAttribute("title", "Survey Form");
		
		return "survey/index";
	}
	
	@GetMapping("/form")
	public String form(Model model,
			SurveyForm surveyForm,
			@ModelAttribute("complete") String complete) {
		
		model.addAttribute("title", "SurveyForm");
		return "survey/form";
	}
	
	@PostMapping("/form")
	public String form(SurveyForm surveyForm,
			Model model) {
		
		model.addAttribute("title", "SurveyForm");
		return "survey/form";
	}
	
	
	@PostMapping("/confirm")
	public String confirm(Model model,
			@Validated SurveyForm surveyForm,
			BindingResult result) {
		
		if(result.hasErrors()) {
			model.addAttribute("title", "SurveyForm");
			return "survey/form";
		}
		model.addAttribute("title", "ConfirmPage");
		return "survey/confirm";
	}
	
	@PostMapping("/complete")
	public String complete(Model model,
			@Validated SurveyForm surveyForm,
			BindingResult result,
			RedirectAttributes redirectAttributes) {
		if(result.hasErrors()) {
			model.addAttribute("title", "SurveyForm");
			return "survey/form";
		}
		
		Survey survey = new Survey();
		survey.setAge(surveyForm.getAge());
		survey.setSatisfaction(surveyForm.getSatisfaction());
		survey.setComment(surveyForm.getComment());
		survey.setCreated(LocalDateTime.now());
		surveyService.save(survey);
		
		redirectAttributes.addFlashAttribute("complete", "Registered");
		return "redirect:/survey/form";
	}
	
}
