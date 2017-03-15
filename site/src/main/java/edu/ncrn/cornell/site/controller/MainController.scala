package edu.ncrn.cornell.site.controller

import org.springframework.http.MediaType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, RequestParam, ResponseBody}
import edu.ncrn.cornell.site.view.{AboutView, AppView}

@Autowired
@Controller
class MainController(val aboutView: AboutView, val appView: AppView) {

  @ResponseBody
  @RequestMapping(
    value = Array("/about"),
    method = Array(RequestMethod.GET),
    produces = Array(MediaType.TEXT_HTML_VALUE)
  )
  def welcome(model: Model): String = aboutView.aboutPage


  /**
    * Single-page application for CED2AR
    *
    * @param model
    * @return
    */
  @ResponseBody
  @RequestMapping(
    method = Array(RequestMethod.GET),
    value = Array("/app"),
    produces = Array(MediaType.TEXT_HTML_VALUE)
  )
  def app(
    model: Model,
    @RequestParam(value = "auth", defaultValue = "false") auth: Boolean
  ): String = appView.appContainer


  @RequestMapping(
    value = Array("/"),
    method = Array(RequestMethod.GET)
  )
  def home(model: Model): String = "redirect:app"

}