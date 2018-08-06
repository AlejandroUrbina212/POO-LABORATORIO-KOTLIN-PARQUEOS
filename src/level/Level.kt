package level

import car.Car
import parkingSpot.ParkingSpot
import wall.Wall

class Level (
        private val levelId: String,
        private val name: String,
        private val color: String,
        private var cars: ArrayList<Car> = arrayListOf(),
        private var walls: ArrayList<Wall> = arrayListOf(),
        private var parkingSpots: ArrayList<ParkingSpot> = arrayListOf(),
        private var height: Int,
        private var width: Int,
        private var canConstruct: Boolean = true


){
    fun setCanConstruct(){
        this.canConstruct=false
    }
    fun getCanConstruct(): Boolean {
        return this.canConstruct
    }

    fun addWall(wall: Wall){
        if (!this.walls.contains(wall)){
            this.walls.add(wall)
        }
    }
    fun addParkingSpot(parkingSpot: ParkingSpot){
        if (!this.parkingSpots.contains(parkingSpot)){
            this.parkingSpots.add(parkingSpot)
        }
    }
    fun addCar(car: Car){
        this.cars.add(car)
    }
    fun setHeight(height: Int){
        this.height = height
    }
    fun setWidth(width: Int){
        this.width = width
    }

    fun getHeight(): Int{
        return this.height
    }
    fun getWidth(): Int{
        return this.width
    }
    fun getLevelId(): String{
        return this.levelId
    }
    fun getParkingSpotBySymbol(symbol: String) :ParkingSpot?{
        val filteredParkingSpot = parkingSpots.filter { it.getSymbol() == symbol }
        if (filteredParkingSpot.count()>0){
            return filteredParkingSpot[0]
        }
        return null
    }
    fun getParkingSpots():ArrayList<ParkingSpot>{
        return this.parkingSpots
    }
    private fun verifyWallInPosition(positionx: Int, positiony: Int): Boolean{
        val find: Wall? = walls.find { wall -> wall.getPositionX() == positionx && wall.getPositionY() == positiony }
        if (find!=null){
            return true
        }
        return false
    }
    private fun verifyParkingSpotInPosition(positionx: Int, positiony: Int): ParkingSpot?{
        val find: ParkingSpot? = parkingSpots.find { parkingSpot ->
            parkingSpot.getPositionX() == positionx && parkingSpot.getPositionY() == positiony }
        if (find!=null){
            return find
        }
        return null
    }
    private fun verifyCarInPosition(positionx: Int, positiony: Int): Car?{
        val find: Car? = cars.find { car ->
            car.getPositionX() == positionx && car.getPositionY() == positiony }
        if (find!=null){
            return find
        }
        return null
    }
    fun getCars(): ArrayList<Car>{
        return this.cars
    }
    fun findSymbolOfParkingSpotOfcar(car: Car): String?{
        for (parkingSpot in this.parkingSpots){
            if ((parkingSpot.getPositionX() == car.getPositionX()) && (parkingSpot.getPositionY()==car.getPositionY())){
                return parkingSpot.getSymbol()
            }
        }
        return null
    }


    fun getCarParkingSpotSymbolByPlate(licensePlate: String): String? {
        for (car in this.getCars()){
            if (car.getLicensePlate()==licensePlate){
                return (findSymbolOfParkingSpotOfcar(car))
            }
        }
        return null
    }





    override fun toString(): String {
        val level = StringBuilder()
        level.append("Level Name: $name \n")
        level.append("Level ID: $levelId \n")
        level.append("Level Color: $color \n")
        for (y in 0..(this.height-1)){
            for (x in 0..(this.width-1)){
                val car: Car? = this.verifyCarInPosition(x, y)
                val isAWall: Boolean = verifyWallInPosition(x, y)
                val isAParkingSpot: ParkingSpot? = verifyParkingSpotInPosition(x,y)

                when {
                    isAWall -> level.append("*")
                    isAParkingSpot!=null -> {
                        level.append(isAParkingSpot.toString())
                    }
                    car != null -> level.append(car.toString())
                    else -> level.append(" ")
                }
            }
            level.append("\n")
            }
        return level.toString()
    }


}