import java.time.LocalDate

fun main(args: Array<String>) {
    val day = args.firstOrNull()?.toInt() ?: LocalDate.now().dayOfMonth
    Runner.of(day).run()
}
