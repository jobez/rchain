package coop.rchain.node

import cats.effect._
import org.http4s.server._
import org.http4s.server.blaze._
import com.typesafe.scalalogging.Logger

import coop.rchain.node.service._

case class HttpServer(port: Int) {

  val logger = Logger("main")

  var server: Option[Server[IO]] = None

  val bld = BlazeBuilder[IO]
    .bindHttp(port, "localhost")
    .mountService(jsonrpc.service, "/")
    .mountService(Lykke.service, "/lykke")
    .start

  def start(): Unit =
    server = Some(bld.unsafeRunSync)

  def stop(): Unit =
    server.foreach(_.shutdown.unsafeRunSync)
}
