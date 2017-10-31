import org.nield.kotlinstatistics.averageBy
import org.nield.kotlinstatistics.countBy

//declare Product class
class Product(val id: Int,
              val name: String,
              val category: String,
              val section: Int,
              val defectRate: Double)

// Create list of Products
val products = listOf(Product(1, "Rayzeon", "A", 3, 1.1),
        Product(2, "ZenFire", "B", 4, 0.7),
        Product(3, "HydroFlux", "A", 3, 1.9),
        Product(4, "IceFlyer", "C", 1, 2.4),
        Product(5, "FireCoyote", "B", 4, 3.2),
        Product(6, "LightFiber", "B",2,  5.1),
        Product(7, "PyroKit", "A", 3, 1.4),
        Product(8, "BladeKit", "C", 1, 0.5),
        Product(9, "NightHawk", "C", 1, 3.5),
        Product(10, "NoctoSquirrel", "A", 2, 1.1),
        Product(11, "WolverinePack", "A", 3, 1.2)
)


fun main(args: Array<String>) {

    // Data Class for Grouping
    data class Key(val category: String, val section: Int)

    // Get Count by Category and Section
    val countByCategoryAndSection =
            products.countBy { Key(it.category, it.section) }

    println("Counts by Category and Section")
    countByCategoryAndSection.entries.forEach { println(it) }

    // Get Average Defect Rate by Category and Section
    val averageDefectByCategoryAndSection =
            products.averageBy(
                    keySelector = { Key(it.category, it.section) },
                    doubleSelector = { it.defectRate }
            )

    println("\nAverage Defect Rate by Category and Section")
    averageDefectByCategoryAndSection.entries.forEach { println(it) }
}