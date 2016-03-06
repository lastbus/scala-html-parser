package com.disanyuzhou.htmlparse.com.disanyuzhou.htmlparse.conf

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable
import scala.xml.XML

/**
 * Created by make on 3/6/16.
 */
object Conf {

  def loadConf(path: String): Map[String, (String, Array[Query])] = {
    val xml = XML.load(path)
    val confMap = mutable.HashMap.empty[String, (String, Array[Query])]
    val urls = xml \ "url"
    for (url <- urls) {
      val urlValue = (url \ "@value").text
      val urlDefaultEncoding = (url \ "@encoding").text

      val queries = url \ "query"
      val queryArray = new Array[Query](queries.size)
      var i = 0
      for (q <- queries) {
        val queryString = (q \ "value").text
        val queryCount = (q \ "@count").text
        val map2Class = (q \ "map-to-class").text
        val queryType = (q \ "@type").text
        val query = new Query(queryString, queryCount, map2Class, queryType)

        val extracts = (q \ "extract")
        for (extract <- extracts) {
          val exist = extract.text.toBoolean
          val extractType = (extract \ "@type").text
          val extractField = (extract \ "@field").text
          val extractParams = (extract \ "@params").text
          query.addItem(new Command(extractType, extractField, exist, extractParams))
        }
        queryArray(i) = query
        i += 1
      }

      confMap(urlValue) = (urlDefaultEncoding, queryArray)
    }
    confMap.toMap

  }

}

/**
 *
 * @param queryString css / xpath expression
 * @param queryCount the number of selected lists
 * @param map2Class the class of the result to be saved to
 * @param queryType jsoup / javax.xml.xpath / ...  engine
 */
class Query(val queryString: String, val queryCount: String, val map2Class: String, val queryType: String = "jsoup") {

  val cmdList: ArrayBuffer[Command] = new ArrayBuffer[Command]

  def addItem(cmd: Command) = {
    cmdList += (cmd)
  }

  override def toString(): String = {
    s"Query:\n queryType=${queryType},\n queryCount=${queryCount},\n queryString=${queryString},\n map2Class=${map2Class},\n " +
      cmdList.toArray.mkString(", ")
  }
}

case class Command(val name: String, val filed: String, val mustHave: Boolean = true, val params: String = "")
