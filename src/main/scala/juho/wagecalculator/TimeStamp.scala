package juho.wagecalculator

/** Class for managing points in time. The unit used here is always quarters. Construction is done from strings, which
  * should be in form of 12:00. Values outside 0:00-24:00 will throw error.
  */
case class TimeStamp(timeStr: String) {

  val quarters: Int = {
    val parts: Array[String] = timeStr.split(":")
    val hours: Int = parts(0).toInt
    val minutes: Int = parts(1).toInt
    hours * 4 + minutes / 15
  }

  /** Returns the gap in quarters between two timestamps
    *
    * If timestamps are in wrong order, e.g. end.gap(start), 0 will be returned. This is useful when figuring out how
    * gaps fit certain period, like when calculating evening hours.
    *
    * @param that The second timestamp, should be later than the first.
    * @return
    */
  def gap(that: TimeStamp): Int = {
    math.max(0, that.quarters - quarters)
  }

  /** Whether this timestamp is before the timestamp in parameter
    *
    * @param that
    * @return
    */
  def isBefore(that: TimeStamp): Boolean = {
    quarters < that.quarters
  }
}
