package parkingSpot

class ParkingSpot (
        private val positionX: Int,
        private val positionY: Int,
        private val Symbol: String
) {
    fun getPositionX():Int{
        return this.positionX
    }
    fun getPositionY():Int{
        return this.positionY
    }
    fun getSymbol(): String{
        return this.Symbol
    }

    override fun toString(): String {
        return getSymbol()
    }
}