package com.disanyuzhou.htmlparse

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * Created by make on 3/6/16.
 */

case class Command(val name: String, val param: String)
case class NewItem(val text: String, val url: String)

object JsoupParse {

  val textCommand = new Command("text", null)
  val attrCommand = new Command("attr", "href")

  /**
   * 第一步， 提取网页数据
   * @param bytes html bytes streaming
   * @param encode html encoding
   * @return extracted data array
   */
  def parse(bytes: Array[Byte], encode: String): Array[NewItem] ={
    if(bytes.length < 1) throw new Exception("html byte is zero.")
    val doc = Jsoup.parse(new String(bytes, encode))
    val selectedList = doc.select("#pagecontent table.ln_fy a")
    val selectedData = new Array[(String, String)](selectedList.size)
    for(i <- 0 until selectedList.size){
      // 对提取出来的每一个 html 标签进行操作提取数据,
      // 命令应该是注入进来, 全局只有这一个变量, 而不是每次都实例化, 这样太浪费内存.
      selectedData(i) = (getElementText(selectedList.get(i), textCommand),
      getElementText(selectedList.get(i), attrCommand))
    }

    selectedData.map(item => NewItem(item._1, item._2))

  }

  def loadData(array: Array[(String, String)]): Array[NewItem] ={
    for (item <- array) yield new NewItem(item._1, item._2)
  }





  /**
   *
   * @param element
   * @param command
   * @return
   */
  def getElementText(element: Element, command: Command): String ={
    if (command.name.equals("text")) {
      element.text()
    } else if (command.name.equals("attr")) {
      element.attr(command.param) // 如果是提取属性值的话，还需要有一个参数
    } else {
      throw new Exception("wrong command to extract data from html.")
    }
  }

}
