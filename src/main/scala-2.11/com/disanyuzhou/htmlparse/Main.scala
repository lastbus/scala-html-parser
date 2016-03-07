package com.disanyuzhou.htmlparse

import java.net.{HttpURLConnection, URL}

import _root_.com.disanyuzhou.htmlparse.conf.Conf

import scala.collection.mutable.ArrayBuffer

/**
 * Created by make on 3/6/16.
 */
object Main {

  def main(args: Array[String]): Unit = {


    val url = "http://www.cfi.net.cn/"
//    val items = JsoupParse.parse(getWebBytes(url), "utf-8")
//    for (item <- items) println(item.toString)

    val absPath = this.getClass.getResource(".").getPath
    val conf = Conf.loadConf(s"${absPath}urls.xml")
    val parseConf = conf.getOrElse(url, null)

    if(parseConf == null) throw new Exception(s"cannot parse website $url.")

    ParserTest.parse(getWebBytes(url), (url, parseConf._1, parseConf._2))


  }








  def getWebBytes(url: String): Array[Byte] = {
    val u = new URL(url)
    val httpUrlConnection = u.openConnection().asInstanceOf[HttpURLConnection]
    httpUrlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:31.0) Gecko/20100101 Firefox/31.0")
    println(getEncoding(httpUrlConnection.getHeaderField("content-type")))
    val reader = httpUrlConnection.getInputStream
    val byteBuffer = new ArrayBuffer[Byte]
    var oneByte = -1
    if (reader.available() > 0) {
      while ( { oneByte = reader.read; oneByte != -1 }) {
        byteBuffer.append(oneByte.toByte)
      }
    } else {
      throw new Exception(s"$url get none data")
    }
    byteBuffer.toArray
  }

  def getEncoding(contentType: String): String = {
    val i = contentType.indexOf("=")
    contentType.substring(i + 1)

  }
}
