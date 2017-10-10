package edu.ncrn.cornell.site.view.comm

import edu.ncrn.cornell.service.api.VarDetails
import edu.ncrn.cornell.site.view.utils.Utils
import fr.hmil.roshttp.HttpRequest
import fr.hmil.roshttp.response.SimpleHttpResponse
import mhtml.Rx
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._

import scala.collection.breakOut
import scala.collection.generic.CanBuildFrom
import scala.util.{Failure, Success}

import monix.execution.Scheduler.Implicits.global

import edu.ncrn.cornell.service.api._

object Request {
  def requestDecodeIterable[B, I[X] <: Iterable[X]](reqRx:  Rx[HttpRequest])
  (implicit dec: Decoder[I[B]], cbf: CanBuildFrom[I[B], B, I[B]])
  : Rx[I[B]] = {
    def emptyIter: I[B] = cbf().result()
    reqRx.flatMap(req =>
      Utils.fromFuture(req.send()).map {
        case Some(resTry) => resTry match {
          case res: Success[SimpleHttpResponse] =>
            decodeIterable[B, I](res.get.body)
          case err: Failure[SimpleHttpResponse] =>
            println("Error retrieving details: " + err.toString)
            emptyIter
        }
        case None => emptyIter
      }
    )
  }

  def decodeIterable[B, I[X] <: Iterable[X]](body: String)
  (implicit dec: Decoder[I[B]], cbf: CanBuildFrom[I[B], B, I[B]])
  : I[B] = {
    def emptyIter: I[B] = cbf().result()
      decode[I[B]](body) match {
        case Left(detailFailure) =>
          println("Error decoding details: " + detailFailure.toString)
          emptyIter
        case Right(newDetails) => newDetails
      }
    }

}
