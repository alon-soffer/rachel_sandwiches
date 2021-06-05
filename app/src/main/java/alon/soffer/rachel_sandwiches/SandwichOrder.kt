package alon.soffer.rachel_sandwiches

data class SandwichOrder(
        val id : String? = null,
        var name : String? = null,
        var pickles: Int = 0,
        var hummus: Boolean = false,
        var tahini: Boolean = false,
        var comment : String? = null,
        var status : String = RachelApplication.WAITING
)
