package org.allenai.common.webapp

import org.allenai.common.Version

import spray.http.StatusCodes
import spray.routing.Directives._
import spray.routing.Route

/** Class providing a spray route with common information, handling requests to the /info path.
  * Requests to the root of the path return a string with all the info keys separated by newlines,
  * while requests to subpaths return the value of the given key, or a 404 for invalid keys.
  *
  * @param info the info to serve
  */
class InfoRoute(val info: Map[String, String] = Map.empty) {
  def withVersion(version: Version): InfoRoute = {
    new InfoRoute(info + ("gitversion" -> version.git) + ("artifactversion" -> version.artifact))
  }

  def withName(name: String): InfoRoute = new InfoRoute(info + ("name" -> name))

  // format: OFF
  def route: Route = get {
    path("info") {
      complete {
        info.keys mkString "\n"
      }
    } ~
    path("info" / Segment) { key =>
      complete {
        info.get(key) match {
          case Some(key) => key
          case None => (StatusCodes.NotFound, "Could not find info: " + key)
        }
      }
    }
  }
  // format: ON
}