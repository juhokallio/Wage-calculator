/**
  * Created by juho on 6/24/16.
  */
object WageCalculator {

  val normalCompensation: Money = Money(3, 75) // divide by 4 or multiply with 0.25
  val eveningCompensation: Money = Money(1, 15)
  val normalDayStart: TimeStamp = TimeStamp("6:00")
  val normalDayEnd: TimeStamp = TimeStamp("18:00")

  def parseCsv(): Map[Long, Seq[WorkTime]] = {
    null
  }

  /** Calculates the total compensation from the time of work
    *
    * @param priorWork
    * @param start
    * @param end
    * @return
    */
  def compensation(priorWork: Int, start: TimeStamp, end: TimeStamp): Money = {
    normalCompensation * normalQuarters(priorWork, start, end)
    //    normalCompensation * normalQuarters * 0.25
    //    + eveningCompensation * eveningCompensation * 0.25
  }

  def totalQuarters(start: TimeStamp, end: TimeStamp): Int = start.gap(end)

  def normalQuarters(priorWork: Int, start: TimeStamp, end: TimeStamp): Int = {
    if (start.isBefore(end))
      math.min(math.max(0, 32 - priorWork), start.gap(end))
    else {
      val dayHoursStart: TimeStamp = if (normalDayStart.isBefore(start)) start else normalDayStart
      val dayHoursEnd: TimeStamp = if (normalDayEnd.isBefore(end)) normalDayEnd else end

      // The normal hours in the beginning and end of the overnight shift
      val startHours = dayHoursStart.gap(normalDayEnd)
      val endHours = normalDayStart.gap(dayHoursEnd)

      math.min(math.max(0, 32 - priorWork), startHours + endHours)
    }
  }

  // TODO: what happens if workday is multiple days long
  def eveningWork(start: TimeStamp, end: TimeStamp): Int = {
    var totalTime = start.gap(end)
    if (start.isBefore(end))
      math.min(totalTime, start.gap(normalDayStart)) + math.min(totalTime, normalDayEnd.gap(end))
    else
      math.min(totalTime, start.gap(normalDayStart))
  }
}
