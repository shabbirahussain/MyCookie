package org.hshabbir.practice.csv_parser

import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTime, DateTimeZone}

import java.io.IOException
import scala.collection.parallel.CollectionConverters._
import scala.collection.parallel.ParIterable
import scala.util.{Try, Using}

object Solution {
  private val DATE_PARSER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ")
  private val FORMAT_YYYYMMDD = DateTimeFormat.forPattern("yyyyMMdd")

  /** Reads the given CSV and a date finds the most active cookie(s).
   *
   * @param file Name of the CSV file to read.
   * @param date Date to find the most active cookie in YYYYMMDD format.
   * @return An iterable of most active cookies ignoring any parsing exceptions if present.
   */
  @throws(classOf[IOException])
  def getMostActiveCookies(file: String, date: Int): Iterable[String] = {
    Using(io.Source.fromFile(file)) { f=>
      getMostActiveCookies(f.getLines.drop(1), date)
    }.get // Throw exception on IO errors
  }

  /** Given the input lines parses them and finds the most active cookie(s).
   *
   * @param lines An iterable of sting lines in CSV format.
   * @param date  Date to find the most active cookie in YYYYMMDD format.
   * @return An iterable of most active cookies ignoring any parsing exceptions if present.
   */
  def getMostActiveCookies(lines: Iterator[String], date: Int): Iterable[String] = {
    val filteredCookies = parseInputs(lines)
      .takeWhile(_._2 >= date) // We know file is sorted in desc order of timestamp.
      .filter(_._2 == date)    // Search for the date.

    if (filteredCookies.isEmpty) return Iterable.empty

    val groupedCookies = filteredCookies
      .groupBy(_._1)
      .mapValues(_.size) // Count most active cookie
      .toSeq
      .map { case (k, v) => (v, Seq(k)) }
      .reduce((a, b) => {
        if (a._1 > b._1) a // Seq a has the max
        else if (a._1 == b._1) (a._1, a._2 ++ b._2) // Both groups are identical.
        else b
      })

    groupedCookies._2
  }

  /** Parses given lines into a tuple of String cookie_id and int date.
   *
   * @param lines An iterable of lines from the CSV to parse.
   * @return An iterable of cookie_id and date in YYYYMMDD int format.
   */
  private def parseInputs(lines: Iterator[String]): ParIterable[(String, Int)] = {
    lines
      .toSeq.par        // We know collection can fit in memory. TODO: Run some benchmarks.
      .map(_.split(',').map(_.trim))
      .filter(_.length == 2)                             // Perform sanity tests on inputs.
      .map(x => Try(x(0), DateTime.parse(x(1), DATE_PARSER)))
      .collect({ case x if x.isSuccess => x.get })       // Reject all invalid date records
      .map(x => (x._1, x._2.withZone(DateTimeZone.UTC))) // Convert TZ to UTC assuming we want to count TZ aware activity
      .map(x => (x._1, x._2.toString(FORMAT_YYYYMMDD).toInt))
  }
}
