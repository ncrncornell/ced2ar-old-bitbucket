package edu.ncrn.cornell.site.controller

import edu.ncrn.cornell.service.ApiInfoService
import org.springframework.http.MediaType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation._
import edu.ncrn.cornell.site.view.{AboutView, AppView}

@Autowired
@Controller
@CrossOrigin //TODO: get allowed origins from config properties
class MainController(
  protected val aboutView: AboutView,
  protected val appView: AppView,
  protected val apiInfoService: ApiInfoService
) {

  @ResponseBody
  @RequestMapping(
    value = Array("/about"),
    method = Array(RequestMethod.GET),
    produces = Array(MediaType.TEXT_HTML_VALUE)
  )
  def about(model: Model): String = aboutView.aboutPage


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