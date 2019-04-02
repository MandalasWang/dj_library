package com.djcps.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author djsxs
 *
 */
@RestController
@RequestMapping(value = "/")
public class IndexController {

	@RequestMapping(value = "index")
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("index");
		return modelAndView;
	}

	@RequestMapping()
	public ModelAndView index1() {
		ModelAndView modelAndView1 = new ModelAndView();
		modelAndView1.setViewName("index");
		return modelAndView1;
	}
}
