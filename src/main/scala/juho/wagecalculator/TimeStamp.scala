package juho.wagecalculator

/** Class for managing points in time. The unit used here is always quarters. Construction is done from strings, which
  * should be in form of 12:00. Values outside 0:00-24:00 will throw error.
  */
case class TimeStamp(timeStr: String) {

  /** Time in quarters (15mins)
    *
    * As times from input should always be either xx:00, xx:15, xx:30 or xx:45, it makes sense to have 15min as the
    * basic unit. Quarters are counted after the begin of the day, e.g.
    * 0:15  = 1,
    * 01:00 = 4
    */
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
    * @return     Number of 15mins between the timestamps
    */
  def gap(that: TimeStamp): Int = {
    math.max(0, that.quarters - quarters)
  }

  /** Whether this timestamp is before the timestamp in parameter
    *
    * @param that The second timestamp
    * @return     True if this timestamp is before, false otherwise (including same timestamps)
    */
  def isBefore(that: TimeStamp): Boolean = {
    quarters < that.quarters
  }
}
