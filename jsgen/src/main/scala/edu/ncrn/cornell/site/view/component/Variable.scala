package edu.ncrn.cornell.site.view.component

import edu.ncrn.cornell.service.api._
import edu.ncrn.cornell.site.view.routing.EndPoints
import edu.ncrn.cornell.site.view.utils.Field.renderField

import fr.hmil.roshttp.HttpRequest
import fr.hmil.roshttp.response.SimpleHttpResponse
import io.circe.parser._
import mhtml._
import mhtml.future.syntax._
import monix.execution.Scheduler.Implicits.global

import scala.util.{Failure, Success}
import scala.xml.Node


object Variable {


  def model(cbHandle: String, varId: VarNameId): Rx[VarDetails] = {
    val request: Rx[HttpRequest] = EndPoints.variable(cbHandle, varId).map(ep =>
      HttpRequest(ep).withHeader("Content-Type", "application/javascript")
    )

    val details: Rx[VarDetails] = request.flatMap(req =>
      req.send().toRx.map {
        case Some(resTry) => resTry match {
          case res: Success[SimpleHttpResponse] =>
            decode[VarDetails](res.get.body) match {
              case Left(detailFailure) =>
                println("Error decoding codebook details: " + detailFailure.toString)
                Nil
              case Right(newDetails) => newDetails
            }
          case err: Failure[SimpleHttpResponse] =>
            println("Error retrieving codebook details: " + err.toString)
            Nil
        }
        case None => Nil
      }
    )
    details
  }

  def view(details: Rx[VarDetails], cbHandle: CodebookId): Node = {
    val collapsibleFields = Set("Values")

    <div>
      <div>
        { details.map(vd => vd.map {
          case (fieldName, fieldValues) => renderField(fieldName, fieldValues, collapsibleFields)
        })}
      </div>
    </div>

  }

  def apply(cbHandle: String, varId: VarNameId): Variable = {
    val details = model(cbHandle, varId)
    TaggedComponent.applyLazy[VarDetails, String](view(details, cbHandle), details, cbHandle)
  }
}
