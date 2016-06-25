import org.scalactic.TolerantNumerics
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by juho on 3/23/16.
  */
class WorkTimeSpec extends FlatSpec {

  "new WorkTime" should "have correct total worktime on same day times" in {
    val wt = new WorkTime(0, TimeStamp("1:00"), TimeStamp("2:00"))
    assert(wt.totalQuarters == 4)
  }
}
