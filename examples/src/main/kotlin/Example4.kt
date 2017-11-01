import org.nield.kotlinstatistics.binByComparable
import java.time.LocalDate
import java.time.Month

fun main(args: Array<String>) {

    data class Sale(val accountId: Int, val date: LocalDate, val amount: Double)

    val sales = listOf(
            Sale(1, LocalDate.of(2016,12,3), 180.0),
            Sale(2, LocalDate.of(2016, 7, 4), 140.2),
            Sale(3, LocalDate.of(2016, 6, 3), 111.4),
            Sale(4, LocalDate.of(2016, 1, 5), 192.7),
            Sale(5, LocalDate.of(2016, 5, 4), 137.9),
            Sale(6, LocalDate.of(2016, 3, 6), 125.6),
            Sale(7, LocalDate.of(2016, 12,4), 164.3),
            Sale(8, LocalDate.of(2016, 7,11), 144.2)
    )

    //bin by quarter
    val binnedByQuarter = sales.binByComparable(
            valueSelector = { it.date.month },
            binIncrements = 3,
            incrementer = { it.plus(1) }
    )

    binnedByQuarter.forEach(::println)

    //look up bin for February
    println("\n\nLook up bin for FEBRUARY")
    println(binnedByQuarter[Month.FEBRUARY])
}