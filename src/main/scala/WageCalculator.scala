/**
  * Created by juho on 6/24/16.
  */
object WageCalculator {

  val normalCompensation: Money = Money.fromCurrency(3, 75).divideBy4()
  val eveningCompensation: Money = normalCompensation + Money.fromCurrency(1, 15).divideBy4()
  val normalDayStart: TimeStamp = TimeStamp("6:00")
  val normalDayEnd: TimeStamp = TimeStamp("18:00")

  def parseCsv(): Map[Long, Seq[Money]] = {
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
    val normalMoney: Money = normalCompensation * normalQuarters(priorWork, start, end)
    val eveningMoney: Money = eveningCompensation * eveningWork(start, end)

    normalMoney + eveningMoney
  }

  def totalQuarters(start: TimeStamp, end: TimeStamp): Int = {
    if (start.isBefore(end))
      start.gap(end)
    else
      start.gap(TimeStamp("24:00")) + TimeStamp("0:00").gap(end)
  }

  def normalTime(start: TimeStamp, end: TimeStamp): Int = {
    val dayHoursStart: TimeStamp = if (normalDayStart.isBefore(start)) start else normalDayStart
    val dayHoursEnd: TimeStamp = if (normalDayEnd.isBefore(end)) normalDayEnd else end

    if (start.isBefore(end))
      dayHoursStart.gap(dayHoursEnd)
    else {
      // The normal hours in the beginning and end of the overnight shift
      val startHours = dayHoursStart.gap(normalDayEnd)
      val endHours = normalDayStart.gap(dayHoursEnd)

      startHours + endHours
    }
  }

  def normalQuarters(priorWork: Int, start: TimeStamp, end: TimeStamp): Int = {
    math.min(math.max(0, 32 - priorWork), normalTime(start, end))
  }

  def eveningWork(start: TimeStamp, end: TimeStamp): Int = {
    totalQuarters(start, end) - normalTime(start, end)
  }

  def plus25work(priorWork: Int, start: TimeStamp, end: TimeStamp): Int = {
    val overtime = math.max(0, priorWork + totalQuarters(start, end) - 32)
    math.min(overtime, 8)
  }

  def plus50work(priorWork: Int, start: TimeStamp, end: TimeStamp): Int = {
    val overtime = math.max(0, priorWork + totalQuarters(start, end) - 40)
    math.min(overtime, 8)
  }

  def plus100work(priorWork: Int, start: TimeStamp, end: TimeStamp): Int = {
    math.max(0, priorWork + totalQuarters(start, end) - 48)
  }
}
