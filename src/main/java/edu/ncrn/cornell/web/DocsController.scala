package edu.ncrn.cornell.web

import edu.ncrn.cornell.web.DocsController._
import edu.ncrn.cornell.model.dao.MappingDao
import edu.ncrn.cornell.view.DocsView
import edu.ncrn.cornell.web.util.HandyServletContextAware
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, ResponseBody}

/**
  * Created by Brandon Barker on 8/9/2016.
  */

@Controller
class DocsController extends HandyServletContextAware {

  @Autowired
  var view: DocsView = _

  @Autowired
  var mappingDao: MappingDao = _


  //GET page
  @ResponseBody
  @RequestMapping(
    method = Array(RequestMethod.GET),
    value = Array(thisPath),
    produces = Array(MediaType.TEXT_HTML_VALUE)
  )
  def docs(): String = {
    val userGuides = List(
      UrlWithText(s"$servletPath/schema-map", "Schema Map")
    )
    val externalUrls = List(
      UrlWithText("https://github.com/ncrncornell", "NCRN GitHub"),
      UrlWithText("http://www.ncrn.cornell.edu/", "Cornell NCRN Site")
    )

    view.showDocs(userGuides = userGuides, externalUrls = externalUrls)
  }

}

object DocsController {

  case class UrlWithText(url: String, text: String)
  final val noUrl = UrlWithText(url="", text="")
  final val thisPath = "/docs"
}