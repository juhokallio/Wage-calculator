/**
  * Created by juho on 6/25/16.
  */
case class TimeStamp(timeStr: String) {

  val quarters: Int = {
    val parts: Array[String] = timeStr.split(":")
    val hours: Int = parts(0).toInt
    val minutes: Int = parts(1).toInt
    hours * 4 + minutes / 15
  }

  def gap(that: TimeStamp): Int = {
    math.abs(quarters - that.quarters)
  }
}
