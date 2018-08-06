package car

class Car (
        private val positionX: Int,
        private val positionY: Int,
        private val licensePlate: String
){
    fun getPositionX():Int{
        return this.positionX
    }
    fun getPositionY():Int{
        return this.positionY
    }
    fun getLicensePlate(): String{
        return this.licensePlate
    }

    override fun toString(): String
    {
        return "@"
    }
}