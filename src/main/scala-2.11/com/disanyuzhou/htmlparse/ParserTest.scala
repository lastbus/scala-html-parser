package com.disanyuzhou.htmlparse

import _root_.com.disanyuzhou.htmlparse.com.disanyuzhou.htmlparse.conf.Query
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import com.disanyuzhou.htmlparse.conf.Command

/**
 * Created by make on 3/6/16.
 */
object ParserTest {
  var count = 0

  def parse(bytes: Array[Byte], conf: (String, Array[Query])): Unit ={
    val doc = Jsoup.parse(new String(bytes, conf._1))

    for (query <- conf._2) yield {
      val selectedBlock = doc.select(query.queryString)
      println(query.queryString)
      val pickedData = new Array[(String, String)](selectedBlock.size)
      val cmdList = query.cmdList
      val className = query.map2Class
      println(s"==${selectedBlock.size}==")
      for (i <- 0 until selectedBlock.size){

        cmdList.map(cmd => {
          println(getElementText(selectedBlock.get(i), cmd))
        })

//        pickedData(i) = (getElementText(selectedBlock.get(i), cmdList(0)),
//          getElementText(selectedBlock.get(i), cmdList(1)))
      }

    }
    println(count)
  }


  def getElementText(element: Element, command: com.disanyuzhou.htmlparse.conf.Command): String = {
    count += 1
    if (command.name.equals("text")) {
      element.text()
    } else if (command.name.equals("attr")) {
      element.attr(command.params) // 如果是提取属性值的话，还需要有一个参数
    } else {
      throw new Exception("wrong command to extract data from html.")
    }
  }


}
