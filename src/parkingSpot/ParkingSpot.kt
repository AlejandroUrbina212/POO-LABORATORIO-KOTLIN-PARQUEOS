package parkingSpot

class ParkingSpot (
        private val positionX: Int,
        private val positionY: Int,
        private val Symbol: String,
        private var isEnabled: Boolean = true

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
    fun parkingSpotOccupied(){
        if (this.isEnabled){
            this.isEnabled=false
        }
    }
    fun getState(): Boolean {
        return this.isEnabled
    }
    override fun toString(): String {
        return getSymbol()
    }
}