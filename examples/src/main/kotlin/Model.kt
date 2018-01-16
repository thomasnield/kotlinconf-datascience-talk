import org.ojalgo.optimisation.ExpressionsBasedModel
import org.ojalgo.optimisation.Variable
import java.util.concurrent.atomic.AtomicInteger

// declare ojAlgo Model
val model = ExpressionsBasedModel()

// custom DSL for model expression inputs, eliminate naming and adding
val funcId = AtomicInteger(0)
val variableId = AtomicInteger(0)
fun variable() = Variable(variableId.incrementAndGet().toString().let { "Variable$it" }).apply(model::addVariable)
fun ExpressionsBasedModel.addExpression() = funcId.incrementAndGet().let { "Func$it"}.let { addExpression(it) }

// constants
val operatingDayLength = operatingDay.endInclusive - operatingDay.start



// Driver class will put itself into the Model when addToModel() is called
data class Driver(val driverNumber: Int,
                  val rate: Double,
                  val availability: IntRange? = null) {

    val shiftStart = variable().lower(6).upper(22)
    val shiftEnd = variable().lower(6).upper(22)

    fun addToModel() {

        //constrain shift length
        model.addExpression()
                .lower(allowableShiftSize.start)
                .upper(allowableShiftSize.endInclusive)
                .set(shiftEnd, 1)
                .set(shiftStart, -1)

        //add specific driver availability
        availability?.let {
            model.addExpression()
                    .lower(it.start)
                    .upper(it.endInclusive)
                    .set(shiftStart, 1)

            model.addExpression()
                    .lower(it.start)
                    .upper(it.endInclusive)
                    .set(shiftEnd, 1)
        }

        //prevent shift overlap
        drivers.asSequence()
                .filter { it != this }
                .forEach { otherDriver ->

                    val occupied = variable().lower(0).upper(1).integer(true)

                    model.addExpression()
                            .upper(0)
                            .set(otherDriver.shiftEnd, 1)
                            .set(occupied, operatingDayLength * - 1)
                            .set(shiftStart, -1)

                    model.addExpression()
                            .upper(operatingDayLength)
                            .set(shiftEnd, 1)
                            .set(occupied, operatingDayLength)
                            .set(otherDriver.shiftStart, -1)
                }
    }

    companion object {

        fun addToModel() {

            //ensure coverage of entire day
            model.addExpression()
                    .level(operatingDayLength)
                    .apply {
                        drivers.forEach {
                            set(it.shiftEnd, 1)
                            set(it.shiftStart, -1)
                        }
                    }

            // set objective
            model.addExpression().weight(1).apply {
                drivers.forEach {
                    set(it.shiftEnd, it.rate)
                    set(it.shiftStart, -1 * it.rate)
                }
            }

            
            drivers.forEach { it.addToModel() }
        }
    }
}

