package edu.ncrn.cornell.site.view.comm

import edu.ncrn.cornell.service.api.VarDetails
import edu.ncrn.cornell.site.view.utils.Utils
import fr.hmil.roshttp.HttpRequest
import fr.hmil.roshttp.response.SimpleHttpResponse
import mhtml.Rx
import io.circe.parser._
import scala.collection.breakOut

import scala.util.{Failure, Success}

object Request {
//  def requestDecodeIterable[B, I[X] <: Iterable[X]](reqRx:  Rx[HttpRequest]): I[B] = {
//    def empty: I[B] = (Iterable.empty[B]).map(x => x)(breakOut)
//
//    reqRx.flatMap(req =>
//      Utils.fromFuture(req.send()).map {
//        case Some(resTry) => resTry match {
//          case res: Success[SimpleHttpResponse] =>
//            decode[VarDetails](res.get.body) match {
//              case Left(detailFailure) =>
//                println("Error decoding codebook details: " + detailFailure.toString)
//                Nil.map(x => x)(breakOut)
//              case Right(newDetails) => newDetails
//            }
//          case err: Failure[SimpleHttpResponse] =>
//            println("Error retrieving codebook details: " + err.toString)
//            Nil
//        }
//        case None => Nil
//      }
//    )
//  }

}
