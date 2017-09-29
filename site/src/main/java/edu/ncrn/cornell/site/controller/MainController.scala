package edu.ncrn.cornell.site.controller

import edu.ncrn.cornell.service.ApiInfoService
import edu.ncrn.cornell.site.controller.util.HandyServletContextAware
import org.springframework.http.MediaType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation._
import edu.ncrn.cornell.site.view.AppView
import org.springframework.web.servlet.View
import org.springframework.web.servlet.view.RedirectView

@Autowired
@Controller
@CrossOrigin //TODO: get allowed origins from config properties
class MainController(
  protected val appView: AppView,
  protected val apiInfoService: ApiInfoService
) extends HandyServletContextAware {

  @ResponseBody
  @RequestMapping(
    value = Array("/about"),
    method = Array(RequestMethod.GET)
  )
  def about(model: Model): View = new RedirectView(s"$servletPath/app#/about")


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


  /**
    * Returns basic information about the API service.
    * @param model
    * @return
    */
  @ResponseBody
  @RequestMapping(
    value = Array("/api"),
    method = Array(RequestMethod.GET),
    produces = Array(MediaType.APPLICATION_JSON_UTF8_VALUE)
  )
  def api(model: Model): String = apiInfoService.apiInfoJson

}