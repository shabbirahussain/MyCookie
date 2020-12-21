package org.hshabbir.practice.csv_parser

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}

import java.io.{File, PrintWriter}
import scala.util.Using


class MainIntegrationTest extends AnyFlatSpec with BeforeAndAfterAll with BeforeAndAfterEach {
  private var tempFile:String = null

  override def beforeAll(): Unit = {
    val tf = File.createTempFile("csv_parser_test-",".csv")
    tf.deleteOnExit()

    Using(new PrintWriter(tf)) { out=>
      out.println("cookie,timestamp")
      out.println("b,2018-12-09T14:19:00+00:00")
      out.println("c,2018-12-09T10:13:00+00:00")
      out.println("d,2018-12-09T07:25:00+00:00")
      out.println("b,2018-12-09T06:19:00+00:00")
      out.println("a,2018-12-09T00:30:00+01:00")
      out.println("c,2018-12-08T22:03:00+00:00")
      out.println("a,2018-12-08T21:30:00+00:00")
      out.println("e,2018-12-08T09:30:00+00:00")
      out.println("a,2018-12-07T23:30:00+00:00")
    }
    tempFile = tf.getAbsolutePath
  }

  "Program" should "read the file to print the desired output" in {
    assertResult(Seq("a"))(Solution.getMostActiveCookies(tempFile, 20181208))
  }
}
