package com.disanyuzhou.htmlparse.confTest

import com.disanyuzhou.htmlparse.com.disanyuzhou.htmlparse.conf.Conf
import org.scalatest.FlatSpec
import org.scalatest.matchers.Matcher

/**
 * Created by make on 3/6/16.
 */
object ConfTest extends HtmlParserTest {

  override def test: Unit = {
    val absPath = this.getClass.getResource(".").getPath
    val conf = Conf.loadConf(s"${absPath}urls.xml")
    for((key, value) <- conf){
      println(key)
      println(" " + value._1 )
      value._2.foreach(v => println(" " + v))
    }



  }
}
