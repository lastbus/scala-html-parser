package com.disanyuzhou.htmlparse

import com.disanyuzhou.htmlparse.beans.Item
import com.disanyuzhou.htmlparse.conf.{Command, Query}
import com.disanyuzhou.htmlparse.format.URLFormat
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import scala.reflect.runtime.{universe => ru}
import scala.tools.asm.Type

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
  def parse(bytes: Array[Byte], conf: (String, String, Array[Query])): Unit = {

    val doc = Jsoup.parse(new String(bytes, conf._2))

    for (query <- conf._3) yield {
      val selectedBlock = doc.select(query.queryString)
      val size = selectedBlock.size
      val count = query.queryCount

      if(count.contains("-")){
        val range = count.split("-").map(_.toInt)
        if(size < range(0) || range(1) < size) throw new Exception(s"except ${selectedBlock.size} ")
      } else {
        if(count.toInt != size) throw new Exception("number not equal")
      }

      println(query.queryString)
      val pickedData = new Array[(String, String)](size)

      val cmdList = query.cmdList
      val className = query.map2Class
      val c = className.getClass.newInstance()
      println(c)
      println(s"==${size}==")
      for (i <- 0 until size){

        cmdList.map(cmd => {
          val rawData = getElementText(selectedBlock.get(i), cmd)
          val output = if(cmd.format.equals("url-format")) URLFormat.format(conf._1, rawData)
          else rawData

        })

//        pickedData(i) = (getElementText(selectedBlock.get(i), cmdList(0)),
//          getElementText(selectedBlock.get(i), cmdList(1)))
      }

    }
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
