package org.hshabbir.practice.csv_parser

import org.joda.time.DateTime
import picocli.CommandLine
import picocli.CommandLine.{Command, Option}

import java.util.Date
import java.util.concurrent.Callable

@Command(name = "java -jar my_cookie.jar", mixinStandardHelpOptions = true, version = Array("my_cookie 1.0"),
  description = Array("Prints the most active cookie given a date and a CSV file with ids and timestamps."))
class Main extends Callable[Int] {
  @Option(names = Array("-f", "--file"), required = true, description = Array("CSV filename to parse (required)."))
  var file: String = null;

  @Option(names = Array("-d", "--date"), required = true, description = Array("Date to to find the most active cookie (required)."))
  var date: Date = null;

  def call(): Int = {
    Solution.getMostActiveCookies(file, (new DateTime(date)).toString("yyyyMMdd").toInt)
      .iterator
      .foreach(println)
    0
  }
}

object Main {
  def main(args: Array[String]): Unit = {
    (new CommandLine(new Main())).execute(args: _*)
  }
}
