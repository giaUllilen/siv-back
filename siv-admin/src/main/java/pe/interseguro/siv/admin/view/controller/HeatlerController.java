package pe.interseguro.siv.admin.view.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = {"/api/v1/"})
public class HeatlerController extends BaseController {

	@RequestMapping(value = "liveness", method = RequestMethod.GET)
	public boolean checkLiveness() {
		return true;
	}
}
