package com.disanyuzhou.htmlparse

import com.disanyuzhou.htmlparse.conf.{Command, Query}
import com.disanyuzhou.htmlparse.format.URLFormat
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * Created by make on 3/6/16.
 */
object ParserTest {
  var count = 0

  /**
   *
   * @param bytes html 二进制流
   * @param conf 解析命令的配置文件，（网页的编码， html 块的提取语句）
   */
  def parse(bytes: Array[Byte], conf: (String, String, Array[Query])): Unit ={
    val doc = Jsoup.parse(new String(bytes, conf._2))

    for (query <- conf._3) yield {
      val selectedBlock = doc.select(query.queryString)
      println(query.queryString)
      val pickedData = new Array[(String, String)](selectedBlock.size)
      val cmdList = query.cmdList
      val className = query.map2Class
      val c = className.getClass.newInstance()
      println(c)
      println(s"==${selectedBlock.size}==")
      for (i <- 0 until selectedBlock.size){

        cmdList.map(cmd => {
          val txt = getElementText(selectedBlock.get(i), cmd)
          if(cmd.format.equals("url-format")) println(URLFormat.format(conf._1, txt))
          else println(txt)
        })

//        pickedData(i) = (getElementText(selectedBlock.get(i), cmdList(0)),
//          getElementText(selectedBlock.get(i), cmdList(1)))
      }

    }
    println(count)
  }


  def getElementText(element: Element, command: Command): String = {

    if (command.name.equals("text")) {
      element.text()
    } else if (command.name.equals("attr")) {
      element.attr(command.params) // 如果是提取属性值的话，还需要有一个参数
    } else {
      throw new Exception("wrong command to extract data from html.")
    }
  }


}
