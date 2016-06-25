/**
  * Created by juho on 6/24/16.
  */
class WorkTime(priorWorkQuarters: Int, start: TimeStamp, end: TimeStamp) {
  val normalCompensation: Money = Money(3, 75)
  val eveningCompensation: Money = Money(1, 15)

  val totalQuarters: Int = start.gap(end)
  val normalQuarters: Int = 0
  val eveningQuarters: Int = 0
  val plus25Quarters: Int = 0
  val plus50Quarters: Int = 0
  val plus100Quarters: Int = 0

  def compensation(): Money = {
    normalCompensation * totalQuarters
//    normalCompensation * normalQuarters * 0.25
//    + eveningCompensation * eveningCompensation * 0.25
  }

}
